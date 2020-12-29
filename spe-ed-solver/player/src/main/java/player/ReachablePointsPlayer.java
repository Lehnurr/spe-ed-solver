package player;

import java.util.function.Consumer;

import player.analysis.enemyprobability.EnemyProbabilityCalculator;
import player.analysis.reachablepoints.ReachablePointsCalculator;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * SpeedSolverPlayer
 */
public class ReachablePointsPlayer implements ISpeedSolverPlayer {

	private static final int ENEMY_PROBABILITY_SEARCH_DEPTH = 5;

	private final EnemyProbabilityCalculator enemyProbabilityCalculator = new EnemyProbabilityCalculator();
	private final ReachablePointsCalculator reachablePointsCalculator = new ReachablePointsCalculator();

	/**
	 * Starts the Player to calculate a Action for the given GameStep
	 * 
	 * @param gameStep            The new gameStep
	 * @param boardRatingConsumer {@link Consumer} which consumes
	 *                            {@link ContextualFloatMatrix} for documentation of
	 *                            the decision made
	 */
	@Override
	public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {

		enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard(),
				ENEMY_PROBABILITY_SEARCH_DEPTH);

		reachablePointsCalculator.performCalculation(gameStep.getSelf(), gameStep.getBoard(),
				enemyProbabilityCalculator.getProbabilitiesMatrix(), enemyProbabilityCalculator.getMinStepsMatrix(),
				gameStep.getDeadline());

		final PlayerAction actionToTake = reachablePointsCalculator.getSuccessRatingsResult().maxAction();

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
				reachablePointsCalculator.getCutOffMatrixResult().get(actionToTake));
		boardRatingConsumer.accept(cutOffNamedMatrix);

		// Send the Calculated Action
		return actionToTake;
	}
}