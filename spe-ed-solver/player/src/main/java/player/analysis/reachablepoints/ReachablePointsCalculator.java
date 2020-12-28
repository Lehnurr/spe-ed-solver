package player.analysis.reachablepoints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import player.analysis.ActionsRating;
import player.analysis.RatedPredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;
import utility.logging.ApplicationLogger;

/**
 * Calculator class calculating success and cut off ratings as
 * {@link ActionsRating} objects and storing the last calculated results.
 */
public class ReachablePointsCalculator {

	private static final int DEADLINE_MILLISECOND_INTERRUPT = 250;

	private ActionsRating successRatingsResult;
	private ActionsRating cutOffRatingsResult;

	private Map<PlayerAction, FloatMatrix> successMatrixResult;
	private Map<PlayerAction, FloatMatrix> cutOffMatrixResult;

	/**
	 * Performs the calculation with the given values and updates the stored
	 * results.
	 * 
	 * @param self          {@link IPlayer} of yourself in the spe_ed game
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities {@link FloatMatrix} containing the enemy probability
	 *                      values
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each element
	 * @param deadline      {@link Deadline} which must not be exceeded
	 */
	public void performCalculation(final IPlayer self, final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final Deadline deadline) {

		final RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(self);

		final Map<PlayerAction, ReachablePointsCalculation> calculations = getCalculations(startPlayer, board,
				probabilities, minSteps, deadline);

		calculateMultithreaded(calculations.values(), deadline);

		updateResults(calculations);
	}

	/**
	 * Generates {@link ReachablePointsCalculation} objects for each possible
	 * {@link PlayerAction} a given {@link RatedPredictivePlayer} can make. The
	 * objects are mapped to the performed {@link PlayerAction} and returned.
	 * 
	 * @param startPlayer   {@link RatedPredictivePlayer} to start the calculations
	 *                      with
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities {@link FloatMatrix} containing the enemy probability
	 *                      values
	 * @param minSteps      {@link FloatMatrix} containing the minimum enemy steps
	 *                      for each element
	 * @param deadline      {@link Deadline} which must not be exceeded
	 * @return {@link ReachablePointsCalculation} objects mapped to the taken child
	 *         {@link PlayerAction}
	 */
	private Map<PlayerAction, ReachablePointsCalculation> getCalculations(final RatedPredictivePlayer startPlayer,
			final Board<Cell> board, final FloatMatrix probabilities, final FloatMatrix minSteps,
			final Deadline deadline) {

		final Map<PlayerAction, ReachablePointsCalculation> result = new EnumMap<>(PlayerAction.class);

		for (final PlayerAction action : PlayerAction.values()) {
			final RatedPredictivePlayer child = new RatedPredictivePlayer(startPlayer, action, board, probabilities,
					minSteps);
			final ReachablePointsCalculation calculation = new ReachablePointsCalculation(board, probabilities,
					minSteps, child, deadline);
			result.put(action, calculation);
		}

		return result;
	}

	/**
	 * Calculates each given {@link ReachablePointsCalculation} in a separate
	 * threads and joins all of them. Additionally a timer is set to interrupt
	 * running threads, which are not finished in time.
	 * 
	 * @param calculations {@link ReachablePointsCalculation} objects to execute the
	 *                     calculation for
	 * @param deadline     {@link Deadline} for the calculations
	 */
	private void calculateMultithreaded(final Collection<ReachablePointsCalculation> calculations,
			final Deadline deadline) {

		final List<Thread> threads = new ArrayList<>();

		for (final ReachablePointsCalculation calculation : calculations) {
			final Thread thread = new Thread(calculation::execute);
			threads.add(thread);
			thread.start();
		}

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (final Thread thread : threads) {
					if (thread.isAlive()) {
						thread.interrupt();
					}
				}
				timer.cancel();
			}
		}, Math.max(1, deadline.getRemainingMilliseconds() - DEADLINE_MILLISECOND_INTERRUPT));

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				ApplicationLogger.logWarning("The reachable points calculation was interrupted!");
				ApplicationLogger.logException(e);
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
	private void updateResults(final Map<PlayerAction, ReachablePointsCalculation> calculations) {

		clearResults();

		for (final PlayerAction action : PlayerAction.values()) {
			final ReachablePointsCalculation calculation = calculations.get(action);
			final FloatMatrix successMatrix = calculation.getSuccessMatrixResult();
			final FloatMatrix cutOffMatrix = calculation.getCutOffMatrixResult();
			successMatrixResult.put(action, new FloatMatrix(1, 1));
			cutOffMatrixResult.put(action, new FloatMatrix(1, 1));

			successMatrixResult.put(action, successMatrix);
			cutOffMatrixResult.put(action, cutOffMatrix);

			successRatingsResult.setRating(action, successMatrix.sum());
			cutOffRatingsResult.setRating(action, cutOffMatrix.sum());
		}

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
