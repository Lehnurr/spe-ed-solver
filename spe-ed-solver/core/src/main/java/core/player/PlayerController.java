package core.player;

import java.util.ArrayList;
import java.util.List;

import player.SpeedSolverPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;
import visualisation.IViewer;
import visualisation.InactiveViewer;
import visualisation.Viewer;

/**
 * PlayerController
 */
public class PlayerController {

	private final SpeedSolverPlayer player;

	private final IViewer viewer;

	public PlayerController(int playerId, boolean viewerEnabled) {
		this.player = new SpeedSolverPlayer(playerId);

		if (viewerEnabled) {
			this.viewer = new Viewer();
		} else {
			this.viewer = new InactiveViewer();
		}

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

		final PlayerAction action = player.calculateAction(gameStep, boardRatings::add);

		final long requiredMilliseconds = availableMilliseconds - gameStep.getDeadline().getRemainingMilliseconds();

		viewer.commitRound(availableMilliseconds / 1000., action, requiredMilliseconds / 1000., boardRatings);

		return action;
	}
}
