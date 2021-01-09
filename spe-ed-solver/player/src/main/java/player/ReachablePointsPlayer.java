package player;

import java.util.function.Consumer;

import player.analysis.ActionsRating;
import player.analysis.enemyprobability.EnemyProbabilityCalculator;
import player.analysis.slowdown.SlowDown;
import player.solver.reachablepoints.IReachablePoints;
import player.solver.reachablepoints.ReachablePointsType;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import utility.logging.GameLogger;

/**
 * Player implementing {@link ISpeedSolverPlayer} used for multiple
 * {@link PlayerType} values. For each possible {@link PlayerAction} the
 * reachable points are determined. For each reachable point is rated for its
 * success and for the cut off potential. The {@link ActionsRating} is used to
 * give each analysis its weight. The overall highest rated {@link PlayerAction}
 * gets chosen for each {@link GameStep}.
 */
public class ReachablePointsPlayer implements ISpeedSolverPlayer {

	private final EnemyProbabilityCalculator enemyProbabilityCalculator;
	private final IReachablePoints reachablePointsCalculator;
	private final SlowDown slowDown;

	private final int enemySearchDepth;
	private final float cutOffWeight;
	private final float slowDownWeight;

	/**
	 * Creates a new {@link ReachablePointsPlayer} with the given configuration
	 * values.
	 * 
	 * @param enemySearchDepth recursive search depth to search for enemy actions
	 * @param cutOffWeight     relative weight for the cut off {@link ActionsRating}
	 * @param slowDownWeight   relative weight for the slow down
	 *                         {@link ActionsRating}
	 * @param type             the {@link ReachablePointsType} of the calculation
	 */
	public ReachablePointsPlayer(final int enemySearchDepth, final float cutOffWeight, final float slowDownWeight,
			final ReachablePointsType type) {
		this.enemyProbabilityCalculator = new EnemyProbabilityCalculator();
		this.reachablePointsCalculator = type.newInstance();
		this.slowDown = new SlowDown();
		this.enemySearchDepth = enemySearchDepth;
		this.cutOffWeight = cutOffWeight;
		this.slowDownWeight = slowDownWeight;
	}

	@Override
	public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {

		enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard(),
				enemySearchDepth);

		reachablePointsCalculator.performCalculation(gameStep.getSelf(), gameStep.getBoard(),
				enemyProbabilityCalculator.getProbabilitiesMatrix(), enemyProbabilityCalculator.getMinStepsMatrix(),
				gameStep.getDeadline());

		final ActionsRating successActionsRating = reachablePointsCalculator.getSuccessRatingsResult();
		final ActionsRating cutOffActionsRating = reachablePointsCalculator.getCutOffRatingsResult();
		final ActionsRating slowDownActionsRating = slowDown.getActionsRating(gameStep.getSelf(), gameStep.getBoard());
		final ActionsRating combinedActionsRating = successActionsRating.combine(cutOffActionsRating, cutOffWeight)
				.combine(slowDownActionsRating, slowDownWeight);

		final PlayerAction actionToTake = combinedActionsRating.maxAction();

		GameLogger.logGameInformation(String.format("success-rating:\t%s", successActionsRating));
		GameLogger.logGameInformation(String.format("cut-off-rating:\t%s", cutOffActionsRating));
		GameLogger.logGameInformation(String.format("slow-down-rating:\t%s", slowDownActionsRating));
		GameLogger.logGameInformation(String.format("combined-rating:\t%s", combinedActionsRating));

		var probabilitiesNamedMatrix = new ContextualFloatMatrix("probability",
				enemyProbabilityCalculator.getProbabilitiesMatrix(), 0, 1);
		boardRatingConsumer.accept(probabilitiesNamedMatrix);

		var minStepsNamedMatrix = new ContextualFloatMatrix("min steps",
				enemyProbabilityCalculator.getMinStepsMatrix());
		boardRatingConsumer.accept(minStepsNamedMatrix);

		var successNamedMatrix = new ContextualFloatMatrix("success",
				reachablePointsCalculator.getSuccessMatrixResult(actionToTake), 0, 1);
		boardRatingConsumer.accept(successNamedMatrix);

		var cutOffNamedMatrix = new ContextualFloatMatrix("cut off",
				reachablePointsCalculator.getCutOffMatrixResult(actionToTake), 0, 1);
		boardRatingConsumer.accept(cutOffNamedMatrix);

		// Send the Calculated Action
		return actionToTake;
	}
}