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

	private void reset() {
		calculations = new EnumMap<>(PlayerAction.class);
		successRating = new ActionsRating();
		cutOffRating = new ActionsRating();
	}

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
