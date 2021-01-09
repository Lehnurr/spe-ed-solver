package player.solver.reachablepoints.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import player.analysis.ActionsRating;
import player.solver.reachablepoints.IReachablePoints;
import player.solver.reachablepoints.graph.board.ConcreteEdge;
import player.solver.reachablepoints.graph.board.Graph;
import player.solver.reachablepoints.graph.board.Node;
import utility.game.board.Board;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
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

	private ActionsRating successRatingsResult;
	private ActionsRating cutOffRatingsResult;
	private ActionsRating invertedImportanceResult;
	private FloatMatrix enemyProbabilitiesMatrix;
	private FloatMatrix enemyMinStepsMatrix;
	private int calculatedPaths;
	private Graph graph;
	private int[] activeEnemiesIds;

	private Map<PlayerAction, FloatMatrix> successMatrixResult;
	private Map<PlayerAction, FloatMatrix> cutOffMatrixResult;
	private FloatMatrix invertedImportanceMatrixResult;

	public void performCalculation(final GameStep gameStep, final FloatMatrix probabilities,
			final FloatMatrix minSteps) {
		this.enemyProbabilitiesMatrix = probabilities;
		this.enemyMinStepsMatrix = minSteps;
		updateGraph(gameStep);

		final List<RatedPredictiveGraphPlayer> startPlayers = RatedPredictiveGraphPlayer.getValidChildren(
				gameStep.getSelf(), graph, probabilities, minSteps, new ConcreteEdge[0], new HashMap<>());

		final List<GraphCalculation> calculations = getCalculations(startPlayers, gameStep.getDeadline(), graph);

		clearResults();
		calculate(calculations);
		addResults(calculations);
	}

	private void updateGraph(GameStep gameStep) {
		if (this.graph == null) {
			// Initialize the graph with an empty Node-Array
			var emptyNodes = new Node[gameStep.getBoard().getHeight()][gameStep.getBoard().getWidth()];
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
	 * Clears all locally stored results.
	 */
	private void clearResults() {
		successMatrixResult = new EnumMap<>(PlayerAction.class);
		cutOffMatrixResult = new EnumMap<>(PlayerAction.class);
		invertedImportanceMatrixResult = null;
		successRatingsResult = new ActionsRating();
		cutOffRatingsResult = new ActionsRating();
		invertedImportanceResult = new ActionsRating();
		calculatedPaths = 0;
	}

	/**
	 * Generates {@link GraphCalculation} for different Threads with a base of
	 * startPlayers
	 * 
	 * @param startPlayer   {@link RatedPredictiveGraphPlayer} to start the
	 *                      calculations with
	 * @param probabilities {@link FloatMatrix} containing the enemy probability
	 *                      values
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each element
	 * @param deadline      {@link Deadline} which must not be exceeded
	 * @param graph         The Graph board to find the edges
	 * @return {@link GraphCalculation} objects
	 */
	private List<GraphCalculation> getCalculations(final List<RatedPredictiveGraphPlayer> startPlayers,
			final Deadline deadline, final Board<Node> graph) {

		// determine the initial edges based on the startplayers
		Map<PlayerAction, ConcreteEdge> initialEdges = new EnumMap<>(PlayerAction.class);
		for (final var startPlayer : startPlayers) {
			final ConcreteEdge initialEdge = startPlayer.getEdgeTail().get(0);
			initialEdges.put(startPlayer.getInitialAction(), initialEdge);
		}

		int threadCount = Math.min(MAX_THREAD_COUNT, (int) (deadline.getRemainingMilliseconds() / 500));

		// Create a Calculation for each thread
		List<GraphCalculation> calculations = new ArrayList<>();

		if (threadCount <= 1) {
			GraphCalculation calculation = new GraphCalculation(graph, this.enemyProbabilitiesMatrix,
					this.enemyMinStepsMatrix, initialEdges, deadline);
			calculations.add(calculation);
			startPlayers.stream().forEach(calculation::addStartPlayer);
			return calculations;
		}

		while (calculations.size() < threadCount)
			calculations.add(new GraphCalculation(graph, this.enemyProbabilitiesMatrix, this.enemyMinStepsMatrix,
					initialEdges, deadline));

		// Define the number of required start players
		final int threadBase = (graph.getHeight() + graph.getWidth()) * 10;
		final int totalBase = threadBase * threadCount;

		// create a Base of Player states
		GraphCalculation baseCalculation = new GraphCalculation(graph, this.enemyProbabilitiesMatrix,
				this.enemyMinStepsMatrix, initialEdges, deadline, totalBase);
		startPlayers.stream().forEach(baseCalculation::addStartPlayer);

		while (baseCalculation.queueHasNext() && baseCalculation.queueRemaining() < totalBase) {
			baseCalculation.executeStep();
		}
		addResults(baseCalculation);

		for (int calculationIndex = 0; baseCalculation
				.queueHasNext(); calculationIndex = (calculationIndex + 1) % threadCount) {
			final RatedPredictiveGraphPlayer startPlayer = baseCalculation.queuePoll();
			calculations.get(calculationIndex).addStartPlayer(startPlayer);
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
	 * {@link GradualReachablePointsCalculation} objects.
	 * 
	 * @param calculations {@link GradualReachablePointsCalculation} objects mapped
	 *                     to the taken {@link PlayerAction}
	 */
	private void addResults(final List<GraphCalculation> calculations) {
		calculations.stream().forEach(this::addResults);

		GameLogger.logGameInformation(String.format("Calculated %d reachable points paths!", calculatedPaths));

		successRatingsResult.normalize();
		invertedImportanceResult.normalize();
		invertedImportanceMatrixResult = invertedImportanceMatrixResult.normalize();
	}

	private void addResults(GraphCalculation calculation) {
		for (final PlayerAction action : PlayerAction.values()) {
			final FloatMatrix successMatrix = calculation.getSuccessMatrixResult(action);
			final FloatMatrix cutOffMatrix = calculation.getCutOffMatrixResult(action);
			final FloatMatrix invertedImportanceMatrix = calculation.getInvertedImportanceMatrix(action);

			successMatrixResult.compute(action, (k, v) -> v == null ? successMatrix : v.max(successMatrix));
			cutOffMatrixResult.compute(action, (k, v) -> v == null ? cutOffMatrix : v.max(cutOffMatrix));
			if (invertedImportanceMatrixResult == null)
				invertedImportanceMatrixResult = invertedImportanceMatrix;
			else
				invertedImportanceMatrixResult = invertedImportanceMatrixResult.sum(invertedImportanceMatrix);

			successRatingsResult.addRating(action, successMatrix.sum());
			cutOffRatingsResult.addRating(action, cutOffMatrix.max());

			invertedImportanceResult.addRating(action, invertedImportanceMatrix.sum());
		}

		calculatedPaths += calculation.getCalculatedPathsCount();
	}

	public void logGameInformation(ActionsRating combinedActionsRating) {
		GameLogger.logGameInformation(String.format("success-rating:\t%s", successRatingsResult));
		GameLogger.logGameInformation(String.format("cut-off-rating:\t%s", cutOffRatingsResult));
		GameLogger.logGameInformation(String.format("inverted-importance-rating:\t%s", invertedImportanceResult));
		GameLogger.logGameInformation(String.format("combined-rating:\t%s", combinedActionsRating));
	}

	public ActionsRating combineActionsRating(float aggressiveWeight, float defensiveWeight) {
		return successRatingsResult.combine(cutOffRatingsResult, aggressiveWeight).combine(invertedImportanceResult,
				defensiveWeight);
	}

	@Override
	public Collection<ContextualFloatMatrix> getContextualFloatMatrices(PlayerAction action) {
		final ArrayList<ContextualFloatMatrix> matrices = new ArrayList<>();

		matrices.add(new ContextualFloatMatrix("success", successMatrixResult.get(action), 0, 1));
		matrices.add(new ContextualFloatMatrix("cut off", cutOffMatrixResult.get(action), 0, 1));
		matrices.add(new ContextualFloatMatrix("inverted importance", invertedImportanceMatrixResult, 0, 1));
		matrices.add(new ContextualFloatMatrix("probability", enemyProbabilitiesMatrix, 0, 1));
		matrices.add(new ContextualFloatMatrix("min steps", enemyMinStepsMatrix));

		return matrices;
	}
}
