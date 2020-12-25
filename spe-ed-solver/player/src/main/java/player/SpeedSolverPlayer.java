package player;

import java.util.function.Consumer;

import player.analysis.enemyprobability.EnemyProbabilityCalculator;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * SpeedSolverPlayer
 */
public class SpeedSolverPlayer {

	private static final int ENEMY_PROBABILITY_SEARCH_DEPTH = 5;

	private final EnemyProbabilityCalculator enemyProbabilityCalculator = new EnemyProbabilityCalculator();

	private final int playerId;

	public SpeedSolverPlayer(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * Starts the Player to calculate a Action for the given GameStep
	 * 
	 * @param gameStep            The new gameStep
	 * @param boardRatingConsumer {@link Consumer} which consumes
	 *                            {@link ContextualFloatMatrix} for documentation of
	 *                            the decision made
	 */
	public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {

		enemyProbabilityCalculator.performCalculation(gameStep.getEnemies().values(), gameStep.getBoard(),
				ENEMY_PROBABILITY_SEARCH_DEPTH);

		var probabilitiesNamedMatrix = new ContextualFloatMatrix("probability", enemyProbabilityCalculator.getProbabilitiesMatrix(), 0, 1);
		boardRatingConsumer.accept(probabilitiesNamedMatrix);
		
		var minStepsNamedMatrix = new ContextualFloatMatrix("min steps", enemyProbabilityCalculator.getMinStepsMatrix());
		boardRatingConsumer.accept(minStepsNamedMatrix);

		// Send the Calculated Action
		return PlayerAction.CHANGE_NOTHING;
	}

	public int getPlayerId() {
		return this.playerId;
	}
}