package player;

import java.util.function.Consumer;

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
	 * @param gameStep              The new gameStep
	 * @param doPlayerActionActionA A Function-Reference for the Player to send the
	 *                              next Action
	 */
	public PlayerAction calculateAction(GameStep gameStep, Consumer<ContextualFloatMatrix> boardRatingConsumer) {

		// Send the Calculated Action
		return PlayerAction.CHANGE_NOTHING;
	}

	public int getPlayerId() {
		return this.playerId;
	}
}