package player.analysis.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import player.analysis.ActionsRating;
import player.boardevaluation.graph.Graph;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;
import utility.logging.ApplicationLogger;
import utility.logging.GameLogger;
import utility.logging.LoggingLevel;

/**
 * Calculator class calculating success and cut off ratings as
 * {@link ActionsRating} objects and storing the last calculated results.
 */
public class GraphCalculator {
	private static final int THREAD_COUNT = 5;

	private ActionsRating successRatingsResult;
	private ActionsRating cutOffRatingsResult;

	private Map<PlayerAction, FloatMatrix> successMatrixResult;
	private Map<PlayerAction, FloatMatrix> cutOffMatrixResult;

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

		final RatedPredictiveGraphPlayer startPlayer = new RatedPredictiveGraphPlayer(self);

		final List<GraphCalculation> calculations = getCalculations(startPlayer, probabilities, minSteps, deadline,
				graph);

		calculateMultithreaded(calculations);

		updateResults(calculations);
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
	private List<GraphCalculation> getCalculations(final RatedPredictiveGraphPlayer startPlayer,
			final FloatMatrix probabilities, final FloatMatrix minSteps, final Deadline deadline, final Graph graph) {

		final int basePerThread = (graph.getHeight() + graph.getWidth()) * 10;

		var calculations = new ArrayList<GraphCalculation>();
		var baseCalculation = new GraphCalculation(probabilities, minSteps, new ArrayList<>(), deadline, graph);
		calculations.add(baseCalculation);

		// create a Base of Player states
		List<RatedPredictiveGraphPlayer> fullBase = new ArrayList<>();
		fullBase.add(startPlayer);
		int i;
		for (i = 0; i < fullBase.size() && fullBase.size() - i < basePerThread * THREAD_COUNT; i++) {
			var self = fullBase.get(i);
			boolean doJump = (self.getRound() + 1) % 6 == 0 && self.getSpeed() > 2;

			for (var action : PlayerAction.values()) {
				var child = self.calculateChild(action);

				if (child == null)
					continue;

				var edge = graph.getBoardCellAt(self.getPosition()).getEdge(child.getDirection(), doJump,
						child.getSpeed());

				// Check if the move is possible, try to add the Edge and update Ratings
				if (edge == null
						|| !child.addEdgeAndCalculateRating(self.getSuccessRating(), probabilities, minSteps, edge))
					continue;

				baseCalculation.getSuccessMatrixResult(child.getInitialAction()).max(child.getPosition(),
						child.getSuccessRating());
				baseCalculation.getCutOffMatrixResult(child.getInitialAction()).max(child.getPosition(),
						child.getCutOffRating());

				fullBase.add(child);
				baseCalculation.incrementCalculatedPathsCount();
			}
		}

		List<List<RatedPredictiveGraphPlayer>> bases = new ArrayList<>();
		for (int j = 0; j < THREAD_COUNT; j++)
			bases.add(new ArrayList<>());

		// Split the fullBase to the different Bases
		while (i < fullBase.size()) {
			for (var base : bases) {
				if (i < fullBase.size()) {
					base.add(fullBase.get(i));
					i++;
				}
			}
		}

		for (var base : bases)
			calculations.add(new GraphCalculation(probabilities, minSteps, base, deadline, graph));

		return calculations;
	}

	/**
	 * Calculates the second {@link GraphCalculation} in this thread and the other
	 * each in a separate thread (Does not calculate the first calculation, because
	 * this is the base)
	 * 
	 * @param calculations {@link GraphCalculation} objects to execute the
	 *                     calculation for
	 */
	private void calculateMultithreaded(final Collection<GraphCalculation> calculations) {
		// TODO: Make Multithreading more readable and stable
		final List<Thread> threads = new ArrayList<>();

		int i = 0;
		GraphCalculation baseCalculation = null;
		for (final GraphCalculation calculation : calculations) {
			if (i == 0) {
				i++;
				continue;
			}
			if (i == 1) {
				baseCalculation = calculation;
				i++;
				continue;
			}
			final Thread thread = new Thread(calculation::execute);
			threads.add(thread);
			thread.start();
		}

		baseCalculation.execute();

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				ApplicationLogger.logWarning("The reachable points calculation was interrupted!");
				ApplicationLogger.logException(e, LoggingLevel.WARNING);
			}
		}
	}

	/**
	 * Updates all the locally stored results by collecting all result of the
	 * {@link ReachablePointsCalculation} objects.
	 * 
	 * @param calculations {@link ReachablePointsCalculation} objects mapped to the
	 *                     taken {@link PlayerAction}
	 */
	private void updateResults(final List<GraphCalculation> calculations) {

		clearResults();

		int calculatedPaths = 0;
		for (var calculation : calculations) {

			for (final PlayerAction action : PlayerAction.values()) {
				final FloatMatrix successMatrix = calculation.getSuccessMatrixResult(action);
				final FloatMatrix cutOffMatrix = calculation.getCutOffMatrixResult(action);
				successMatrixResult.put(action, new FloatMatrix(1, 1));
				cutOffMatrixResult.put(action, new FloatMatrix(1, 1));

				successMatrixResult.put(action, successMatrix);
				cutOffMatrixResult.put(action, cutOffMatrix);

				successRatingsResult.addRating(action, successMatrix.sum());
				cutOffRatingsResult.addRating(action, cutOffMatrix.sum());
			}
			calculatedPaths += calculation.getCalculatedPathsCount();
		}

		GameLogger.logGameInformation(String.format("Calculated %d reachable points paths!", calculatedPaths));

		successRatingsResult.normalize();
		cutOffRatingsResult.normalize();
	}

	/**
	 * Clears all locally stored results.
	 */
	private void clearResults() {
		successMatrixResult = new EnumMap<>(PlayerAction.class);
		cutOffMatrixResult = new EnumMap<>(PlayerAction.class);
		successRatingsResult = new ActionsRating();
		cutOffRatingsResult = new ActionsRating();
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
	 * Returns a {@link Map} mapping each {@link PlayerAction} to a
	 * {@link FloatMatrix} containing the success ratings for each element.
	 * 
	 * @return success matrices map
	 */
	public Map<PlayerAction, FloatMatrix> getSuccessMatrixResult() {
		return Collections.unmodifiableMap(successMatrixResult);
	}

	/**
	 * Returns a {@link Map} mapping each {@link PlayerAction} to a
	 * {@link FloatMatrix} containing the cut off ratings for each element.
	 * 
	 * @return cut off matrices map
	 */
	public Map<PlayerAction, FloatMatrix> getCutOffMatrixResult() {
		return Collections.unmodifiableMap(cutOffMatrixResult);
	}

}
