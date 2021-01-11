package player.solver.reachablepoints.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import player.analysis.ActionsRating;
import player.analysis.cutoff.CutOffCalculation;
import player.analysis.success.SuccessCalculation;
import player.solver.reachablepoints.IReachablePoints;
import player.solver.reachablepoints.graph.board.Graph;
import player.solver.reachablepoints.graph.board.Node;
import player.solver.reachablepoints.graph.importance.EdgeImportance;
import utility.game.board.Board;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.IDeadline;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.geometry.FloatMatrix;
import utility.logging.GameLogger;

/**
 * Calculator class calculating success and cut off ratings as
 * {@link ActionsRating} objects and storing the last calculated results.
 */
public class GraphCalculator implements IReachablePoints {
	private static final int MAX_THREAD_COUNT = 1;

	private FloatMatrix enemyProbabilitiesMatrix;
	private FloatMatrix enemyMinStepsMatrix;
	private int calculatedPaths;
	private Graph graph;
	private int[] activeEnemiesIds;

	private SuccessCalculation successCalculation;
	private CutOffCalculation cutOffCalculation;
	private EdgeImportance importanceCalculation;

	public void performCalculation(final GameStep gameStep, final FloatMatrix probabilities,
			final FloatMatrix minSteps) {
		this.enemyProbabilitiesMatrix = probabilities;
		this.enemyMinStepsMatrix = minSteps;

		updateGraph(gameStep);

		final List<RatedPredictiveGraphPlayer> startPlayers = RatedPredictiveGraphPlayer
				.getValidChildren(gameStep.getSelf(), graph, probabilities, minSteps);

		final List<GraphCalculation> calculations = getCalculations(startPlayers, gameStep.getDeadline(), graph);

		calculate(calculations);
		addResults(calculations);
	}

	private void updateGraph(GameStep gameStep) {
		if (this.graph == null) {
			// Initialize the graph with an empty Node-Array
			final Node[][] emptyNodes = new Node[gameStep.getBoard().getHeight()][gameStep.getBoard().getWidth()];
			this.graph = new Graph(emptyNodes);

			// Initialize the alive player
			this.activeEnemiesIds = gameStep.getEnemies().keySet().stream().mapToInt(Integer::intValue).toArray();
		}

		// Merge all players that were active last round into one list
		List<IPlayer> enemies = new ArrayList<>();
		for (int playerId : this.activeEnemiesIds)
			enemies.add(gameStep.getEnemies().get(playerId));

		// Transfer the new occupied cells to the graph
		graph.updateGraph(gameStep.getBoard(), gameStep.getSelf(), enemies);

		// Determine the currently dead players to ignore them in the following round
		// for the graph-update.
		this.activeEnemiesIds = enemies.stream().filter(IPlayer::isActive).mapToInt(IPlayer::getPlayerId).toArray();
	}

	/**
	 * Generates {@link GraphCalculation} for different Threads with a base of
	 * startPlayers
	 * 
	 * @param startPlayers {@link RatedPredictiveGraphPlayer players} to start the
	 *                     calculations with
	 * @param deadline     {@link IDeadline} which must not be exceeded
	 * @param graph        The Graph board to find the edges
	 * @return {@link GraphCalculation} objects
	 */
	private List<GraphCalculation> getCalculations(final List<RatedPredictiveGraphPlayer> startPlayers,
			final IDeadline deadline, final Board<Node> graph) {

		final int width = graph.getWidth();
		final int height = graph.getHeight();
		successCalculation = new SuccessCalculation(width, height);
		cutOffCalculation = new CutOffCalculation(width, height);
		importanceCalculation = new EdgeImportance(width, height, startPlayers);

		int threadCount = Math.min(MAX_THREAD_COUNT, (int) (deadline.getRemainingMilliseconds() / 500));

		// Create a Calculation for each thread
		List<GraphCalculation> calculations = new ArrayList<>();

		if (threadCount <= 1) {
			GraphCalculation calculation = new GraphCalculation(graph, this.enemyProbabilitiesMatrix,
					this.enemyMinStepsMatrix, importanceCalculation.getInitialEdges(), deadline);
			calculations.add(calculation);
			startPlayers.stream().forEach(calculation::addPlayerToQueue);
			return calculations;
		}

		while (calculations.size() < threadCount)
			calculations.add(new GraphCalculation(graph, this.enemyProbabilitiesMatrix, this.enemyMinStepsMatrix,
					importanceCalculation.getInitialEdges(), deadline));

		// Define the number of required start players
		final int threadBase = (graph.getHeight() + graph.getWidth()) * 10;
		final int totalBase = threadBase * threadCount;

		// create a Base of Player states
		GraphCalculation baseCalculation = new GraphCalculation(graph, this.enemyProbabilitiesMatrix,
				this.enemyMinStepsMatrix, importanceCalculation.getInitialEdges(), deadline, totalBase);
		startPlayers.stream().forEach(baseCalculation::addPlayerToQueue);

		while (baseCalculation.queueHasNext() && baseCalculation.queueRemaining() < totalBase) {
			baseCalculation.executeStep();
		}
		addResults(baseCalculation);

		for (int calculationIndex = 0; baseCalculation
				.queueHasNext(); calculationIndex = (calculationIndex + 1) % threadCount) {
			final RatedPredictiveGraphPlayer startPlayer = baseCalculation.queuePoll();
			calculations.get(calculationIndex).addPlayerToQueue(startPlayer);
		}

		return calculations;
	}

	/**
	 * Calculates the first given calculation in this Thread and all other
	 * calculation in seperate Threads
	 * 
	 * @param calculations {@link GraphCalculation} objects to execute the
	 *                     calculation for
	 */
	private static void calculate(final List<GraphCalculation> calculations) {
		if (calculations.isEmpty())
			return;

		final List<Thread> threads = new ArrayList<>();

		for (int i = 1; i < calculations.size(); i++) {
			final GraphCalculation calculation = calculations.get(i);
			final Thread thread = new Thread(calculation::executeDeadline);
			threads.add(thread);
			thread.start();
		}

		calculations.get(0).executeDeadline();

		// To be on the safe side interrupt all other threads
		threads.stream().forEach(Thread::interrupt);
	}

	/**
	 * Updates all the locally stored results by collecting all result of the
	 * {@link GraphCalculation} objects.
	 * 
	 * @param calculations {@link GraphCalculation} objects mapped to the taken
	 *                     {@link PlayerAction}
	 */
	private void addResults(final List<GraphCalculation> calculations) {
		calculations.stream().forEach(this::addResults);

		GameLogger.logGameInformation(String.format("Calculated %d reachable points paths!", calculatedPaths));
		calculatedPaths = 0;
	}

	private void addResults(GraphCalculation calculation) {
		this.successCalculation.add(calculation.getSuccessCalculation());
		this.cutOffCalculation.add(calculation.getCutOffCalculation());
		this.importanceCalculation.add(calculation.getEdgeImportance());
		calculatedPaths += calculation.getCalculatedPathsCount();
	}

	public void logGameInformation(ActionsRating combinedActionsRating) {
		GameLogger.logGameInformation(String.format("success-rating:\t%s", successCalculation.getRatingResult()));
		GameLogger.logGameInformation(String.format("cut-off-rating:\t%s", cutOffCalculation.getRatingResult()));
		GameLogger.logGameInformation(
				String.format("inverted-importance-rating:\t%s", importanceCalculation.getInvertedRatingResult()));
		GameLogger.logGameInformation(String.format("combined-rating:\t%s", combinedActionsRating));
	}

	public ActionsRating combineActionsRating(float aggressiveWeight, float defensiveWeight) {
		return successCalculation.getRatingResult().combine(cutOffCalculation.getRatingResult(), aggressiveWeight)
				.combine(importanceCalculation.getInvertedRatingResult(), defensiveWeight);
	}

	@Override
	public Collection<ContextualFloatMatrix> getContextualFloatMatrices(PlayerAction action) {
		final ArrayList<ContextualFloatMatrix> matrices = new ArrayList<>();

		matrices.add(new ContextualFloatMatrix("success", successCalculation.getMatrixResult(action), 0, 1));
		matrices.add(new ContextualFloatMatrix("cut off", cutOffCalculation.getMatrixResult(action), 0, 1));
		matrices.add(new ContextualFloatMatrix("inverted importance", importanceCalculation.getInvertedMatrixResult(),
				0, 1));
		matrices.add(new ContextualFloatMatrix("probability", enemyProbabilitiesMatrix, 0, 1));
		matrices.add(new ContextualFloatMatrix("min steps", enemyMinStepsMatrix));

		return matrices;
	}
}
