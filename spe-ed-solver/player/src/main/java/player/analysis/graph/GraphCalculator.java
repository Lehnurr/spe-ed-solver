package player.analysis.graph;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import player.analysis.ActionsRating;
import player.boardevaluation.graph.ConcreteEdge;
import player.boardevaluation.graph.Graph;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;
import utility.logging.GameLogger;

/**
 * Calculator class calculating success and cut off ratings as
 * {@link ActionsRating} objects and storing the last calculated results.
 */
public class GraphCalculator {
	private static final int MAX_THREAD_COUNT = 1;

	private ActionsRating successRatingsResult;
	private ActionsRating cutOffRatingsResult;
	private ActionsRating invertedImportanceResult;
	private int calculatedPaths;

	private Map<PlayerAction, FloatMatrix> successMatrixResult;
	private Map<PlayerAction, FloatMatrix> cutOffMatrixResult;
	private FloatMatrix invertedImportanceMatrixResult;

	/**
	 * Performs the calculation with the given values and updates the stored
	 * results.
	 * 
	 * @param self          {@link IPlayer} of yourself in the spe_ed game
	 * @param probabilities {@link FloatMatrix} containing the enemy probability
	 *                      values
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each element
	 * @param deadline      {@link Deadline} which must not be exceeded
	 * @param graph         The Graph board to find the edges
	 */
	public void performCalculation(final IPlayer self, final FloatMatrix probabilities, final FloatMatrix minSteps,
			final Deadline deadline, final Graph graph) {

		final List<RatedPredictiveGraphPlayer> startPlayers = RatedPredictiveGraphPlayer.getValidChildren(self, graph,
				probabilities, minSteps, new ConcreteEdge[0], new HashMap<>());

		clearResults();

		final List<GraphCalculation> calculations = getCalculations(startPlayers, probabilities, minSteps, deadline,
				graph);

		calculate(calculations);
		addResults(calculations);
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
			final FloatMatrix probabilities, final FloatMatrix minSteps, final Deadline deadline, final Graph graph) {

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
			GraphCalculation calculation = new GraphCalculation(graph, probabilities, minSteps, initialEdges, deadline);
			calculations.add(calculation);
			startPlayers.stream().forEach(calculation::addStartPlayer);
			return calculations;
		}

		while (calculations.size() < threadCount)
			calculations.add(new GraphCalculation(graph, probabilities, minSteps, initialEdges, deadline));

		// Define the number of required start players
		final int threadBase = (graph.getHeight() + graph.getWidth()) * 10;
		final int totalBase = threadBase * threadCount;

		// create a Base of Player states
		GraphCalculation baseCalculation = new GraphCalculation(graph, probabilities, minSteps, initialEdges, deadline,
				totalBase);
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

	/**
	 * Returns the {@link ActionsRating} for the success ratings for each
	 * {@link PlayerAction}.
	 * 
	 * @return success ratings
	 */
	public ActionsRating getSuccessRatingsResult() {
		return successRatingsResult;
	}

	/**
	 * Returns the {@link ActionsRating} for the cut off ratings for each
	 * {@link PlayerAction}.
	 * 
	 * @return cut off ratings
	 */
	public ActionsRating getCutOffRatingsResult() {
		return cutOffRatingsResult;
	}

	/**
	 * Returns the {@link ActionsRating} for inverted importance ratings for each
	 * {@link PlayerAction}.
	 * 
	 * @return inverted importance ratings
	 */
	public ActionsRating getInvertedImportanceResult() {
		return invertedImportanceResult;
	}

	/**
	 * Returns a {@link Map} mapping each {@link PlayerAction} to a
	 * {@link FloatMatrix} containing the success ratings for each element.
	 * 
	 * @return success matrices map
	 */
	public FloatMatrix getSuccessMatrixResult(PlayerAction action) {
		return successMatrixResult.get(action);
	}

	/**
	 * Returns a {@link Map} mapping each {@link PlayerAction} to a
	 * {@link FloatMatrix} containing the cut off ratings for each element.
	 * 
	 * @return cut off matrices map
	 */
	public FloatMatrix getCutOffMatrixResult(PlayerAction action) {
		return cutOffMatrixResult.get(action);
	}

	public FloatMatrix getNormalizedInvertedImportanceMatrixResult() {
		return this.invertedImportanceMatrixResult.normalize();
	}

}
