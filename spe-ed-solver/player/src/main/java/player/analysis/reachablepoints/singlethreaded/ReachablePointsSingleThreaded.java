package player.analysis.reachablepoints.singlethreaded;

import java.util.EnumMap;
import java.util.Map;

import player.analysis.ActionsRating;
import player.analysis.RatedPredictivePlayer;
import player.analysis.reachablepoints.IReachablePoints;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.Deadline;
import utility.geometry.FloatMatrix;
import utility.logging.GameLogger;

/**
 * Class implementing the {@link IReachablePoints} interface. The reachable
 * points calculation is performed on a single thread (main thread). The
 * calculation is distributed to multiple
 * {@link GradualReachablePointsCalculation} objects which are repeatedly
 * alternated to distribute the resources equally.
 */
public class ReachablePointsSingleThreaded implements IReachablePoints {

	private static final int DEADLINE_MILLISECOND_BUFFER = 500;

	private Map<PlayerAction, GradualReachablePointsCalculation> calculations;

	private ActionsRating successRating;
	private ActionsRating cutOffRating;

	@Override
	public void performCalculation(final IPlayer self, final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps, final Deadline deadline) {
		reset();
		initCalculations(self, board, probabilities, minSteps);
		executeCalculationLoop(deadline);
		updateActionsRatings();
	}

	/**
	 * Resets the {@link GradualReachablePointsCalculation calculations} and
	 * {@link ActionsRating ratings}.
	 */
	private void reset() {
		calculations = new EnumMap<>(PlayerAction.class);
		successRating = new ActionsRating();
		cutOffRating = new ActionsRating();
	}

	/**
	 * Initializes the {@link GradualReachablePointsCalculation calculations} with
	 * the given start information.
	 * 
	 * @param self          {@link IPlayer} to test for further movement
	 * @param board         {@link Board} to check for collisions
	 * @param probabilities probabilities of enemies as {@link FloatMatrix}
	 * @param minSteps      minimum steps of enemies as {@link FloatMatrix}
	 */
	private void initCalculations(final IPlayer self, final Board<Cell> board, final FloatMatrix probabilities,
			final FloatMatrix minSteps) {

		final RatedPredictivePlayer startPlayer = new RatedPredictivePlayer(self);
		for (final PlayerAction action : PlayerAction.values()) {
			final RatedPredictivePlayer nextPlayer = new RatedPredictivePlayer(startPlayer, action, board,
					probabilities, minSteps);
			final GradualReachablePointsCalculation calculation = new GradualReachablePointsCalculation(board,
					probabilities, minSteps, nextPlayer);
			calculations.put(action, calculation);
		}
	}

	/**
	 * Executes the main calculation loop. Thereby multiple
	 * {@link GradualReachablePointsCalculation calculations} are repeatedly
	 * alternated until a {@link Deadline} is reached.
	 * 
	 * @param deadline {@link Deadline} for the
	 *                 {@link GradualReachablePointsCalculation calculations}
	 */
	private void executeCalculationLoop(final Deadline deadline) {

		boolean finished = false;

		while (deadline.getRemainingMilliseconds() > DEADLINE_MILLISECOND_BUFFER && !finished) {
			finished = true;
			for (final PlayerAction action : PlayerAction.values()) {
				final GradualReachablePointsCalculation calculation = calculations.get(action);
				if (!calculation.isFinished()) {
					calculation.performSingleStep();
					finished = false;
				}
			}
		}

		int calculatedPaths = 0;
		for (final GradualReachablePointsCalculation calculation : calculations.values())
			calculatedPaths += calculation.getCalculatedPathsCount();
		GameLogger.logGameInformation(String.format("Calculated %d reachable points paths!", calculatedPaths));
	}

	/**
	 * Updates the success and cut off {@link ActionsRating ratings} with the
	 * calculated success and cut off {@link FloatMatrix matrices}.
	 */
	private void updateActionsRatings() {
		for (final PlayerAction action : PlayerAction.values()) {
			final float successValue = calculations.get(action).getSuccessMatrixResult().sum();
			successRating.addRating(action, successValue);
			final float cutOffValue = calculations.get(action).getCutOffMatrixResult().max();
			cutOffRating.addRating(action, cutOffValue);
		}
		successRating.normalize();
	}

	@Override
	public ActionsRating getSuccessRatingsResult() {
		return successRating;
	}

	@Override
	public ActionsRating getCutOffRatingsResult() {
		return cutOffRating;
	}

	@Override
	public Map<PlayerAction, FloatMatrix> getSuccessMatrixResult() {
		final Map<PlayerAction, FloatMatrix> result = new EnumMap<>(PlayerAction.class);
		for (final PlayerAction action : PlayerAction.values()) {
			result.put(action, calculations.get(action).getSuccessMatrixResult());
		}
		return result;
	}

	@Override
	public Map<PlayerAction, FloatMatrix> getCutOffMatrixResult() {
		final Map<PlayerAction, FloatMatrix> result = new EnumMap<>(PlayerAction.class);
		for (final PlayerAction action : PlayerAction.values()) {
			result.put(action, calculations.get(action).getCutOffMatrixResult());
		}
		return result;
	}

}
