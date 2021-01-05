package player;

import java.util.function.Consumer;

import player.analysis.ActionsRating;
import player.analysis.enemyprobability.EnemyProbabilityCalculator;
import player.analysis.reachablepoints.IReachablePoints;
import player.analysis.reachablepoints.ReachablePointsType;
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

	private final int enemySearchDepth;
	private final float cutOffWeight;

	/**
	 * Creates a new {@link ReachablePointsPlayer} with the given configuration
	 * values.
	 * 
	 * @param enemySearchDepth recursive search depth to search for enemy actions
	 * @param cutOffWeight     relative weight for the cut off {@link ActionsRating}
	 * @param type             the {@link ReachablePointsType} of the calculation
	 */
	public ReachablePointsPlayer(final int enemySearchDepth, final float cutOffWeight, final ReachablePointsType type) {
		this.enemyProbabilityCalculator = new EnemyProbabilityCalculator();
		this.reachablePointsCalculator = type.newInstance();
		this.enemySearchDepth = enemySearchDepth;
		this.cutOffWeight = cutOffWeight;
	}

	@Override
	public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {

		enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard(),
				enemySearchDepth);

		reachablePointsCalculator.performCalculation(gameStep.getSelf(), gameStep.getBoard(),
				enemyProbabilityCalculator.getProbabilitiesMatrix(), enemyProbabilityCalculator.getMinStepsMatrix(),
				gameStep.getDeadline());

		final ActionsRating successActionsRating = reachablePointsCalculator.getSuccessRatingsResult();
		GameLogger.logGameInformation(String.format("success-rating:\t%s", successActionsRating));

		final ActionsRating cutOffActionsRating = reachablePointsCalculator.getCutOffRatingsResult();
		GameLogger.logGameInformation(String.format("cut-off-rating:\t%s", cutOffActionsRating));

		final ActionsRating combinedActionsRating = successActionsRating.combine(cutOffActionsRating, cutOffWeight);
		GameLogger.logGameInformation(String.format("combined-rating:\t%s", combinedActionsRating));

		final PlayerAction actionToTake = combinedActionsRating.maxAction();

		var probabilitiesNamedMatrix = new ContextualFloatMatrix("probability",
				enemyProbabilityCalculator.getProbabilitiesMatrix(), 0, 1);
		boardRatingConsumer.accept(probabilitiesNamedMatrix);

		var minStepsNamedMatrix = new ContextualFloatMatrix("min steps",
				enemyProbabilityCalculator.getMinStepsMatrix());
		boardRatingConsumer.accept(minStepsNamedMatrix);

		var successNamedMatrix = new ContextualFloatMatrix("success",
				reachablePointsCalculator.getSuccessMatrixResult().get(actionToTake), 0, 1);
		boardRatingConsumer.accept(successNamedMatrix);

		var cutOffNamedMatrix = new ContextualFloatMatrix("cut off",
				reachablePointsCalculator.getCutOffMatrixResult().get(actionToTake), 0, 1);
		boardRatingConsumer.accept(cutOffNamedMatrix);

		// Send the Calculated Action
		return actionToTake;
	}
}