package player.analysis.reachablepoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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

		final Map<PlayerAction, ReachablePointsCalculation> calculations = new EnumMap<>(PlayerAction.class);
		final List<Thread> threads = new ArrayList<>();

		final RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(self);

		for (final PlayerAction action : PlayerAction.values()) {
			final RatedPredictivePlayer child = new RatedPredictivePlayer(startPlayer, action, board, probabilities,
					minSteps);
			final ReachablePointsCalculation calculation = new ReachablePointsCalculation(board, probabilities,
					minSteps, child, deadline);
			calculations.put(action, calculation);
			final Thread thread = new Thread(calculation::execute);
			threads.add(thread);
		}

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				ApplicationLogger.logWarning("The reachable points calculation was interrupted!");
				ApplicationLogger.logException(e);
			}
		}

		successMatrixResult = new EnumMap<>(PlayerAction.class);
		cutOffMatrixResult = new EnumMap<>(PlayerAction.class);

		successRatingsResult = new ActionsRating();
		cutOffRatingsResult = new ActionsRating();

		for (final PlayerAction action : PlayerAction.values()) {
			final ReachablePointsCalculation calculation = calculations.get(action);
			final FloatMatrix successMatrix = calculation.getSuccessMatrixResult();
			final FloatMatrix cutOffMatrix = calculation.getCutOffMatrixResult();

			successMatrixResult.put(action, successMatrix);
			cutOffMatrixResult.put(action, cutOffMatrix);

			successRatingsResult.setRating(action, successMatrix.sum());
			cutOffRatingsResult.setRating(action, cutOffMatrix.sum());
		}

		successRatingsResult.normalize();
		cutOffRatingsResult.normalize();
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
