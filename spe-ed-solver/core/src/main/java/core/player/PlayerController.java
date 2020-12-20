package core.player;

import java.util.ArrayList;
import java.util.List;

import player.SpeedSolverPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import visualisation.Viewer;

/**
 * PlayerController
 */
public class PlayerController {

	private final SpeedSolverPlayer player;

	private final Viewer viewer;

	public PlayerController(int playerId) {
		this.player = new SpeedSolverPlayer(playerId);

		this.viewer = new Viewer();
	}

	/**
	 * Sends the new GameStep to the Player and returns the chosen Action
	 * 
	 * @param gameStep The current Game Step
	 * @return The action chosen by the player
	 */
	public PlayerAction calculateAction(GameStep gameStep) {

		final long availableMilliseconds = gameStep.getDeadline().getRemainingMilliseconds();

		final List<ContextualFloatMatrix> boardRatings = new ArrayList<>();
		boardRatings.add(gameStep.getBoardAsMatrix("Board"));

		final PlayerAction action = player.calculateAction(gameStep);

		final long requiredMilliseconds = availableMilliseconds - gameStep.getDeadline().getRemainingMilliseconds();

		viewer.commitRound(availableMilliseconds, action, requiredMilliseconds, boardRatings);

		return action;
	}
}
