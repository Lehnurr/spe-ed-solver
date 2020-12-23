package player;

import java.util.function.Consumer;

import player.analysis.enemyprobability.EnemyForwardPrediction;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * SpeedSolverPlayer
 */
public class SpeedSolverPlayer {

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

		var test = new EnemyForwardPrediction(gameStep.getBoard(), gameStep.getSelf());
		test.doCalculation(5);
		var matrix = test.getProbabilityMatrix();
		var namedMatrix = new ContextualFloatMatrix("probability", matrix, 0, 1);
		boardRatingConsumer.accept(namedMatrix);

		// Send the Calculated Action
		return PlayerAction.CHANGE_NOTHING;
	}

	public int getPlayerId() {
		return this.playerId;
	}
}