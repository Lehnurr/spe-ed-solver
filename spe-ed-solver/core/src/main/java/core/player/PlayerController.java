package core.player;

import java.util.ArrayList;
import java.util.List;

import player.ISpeedSolverPlayer;
import player.PlayerType;
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

	private final ISpeedSolverPlayer player;

	private final IViewer viewer;

	public PlayerController(final boolean viewerEnabled, final PlayerType playerType) {
		this.player = playerType.newInstance();

		if (viewerEnabled) {
			this.viewer = new Viewer();
		} else {
			this.viewer = new InactiveViewer();
		}

	}

	/**
	 * Sends the new {@link GameStep} to the {@link SpeedSolverPlayer} and returns
	 * the chosen {@link PlayerAction}.
	 * 
	 * @param gameStep the current {@link GameStep}
	 * @return the {@link PlayerAction} chosen by the player
	 */
	public PlayerAction calculateAction(GameStep gameStep) {

		final long availableMilliseconds = gameStep.getDeadline().getRemainingMilliseconds();
		final float availableSeconds = availableMilliseconds / 1000f;

		final List<ContextualFloatMatrix> boardRatings = new ArrayList<>();

		final PlayerAction action = player.calculateAction(gameStep, boardRatings::add);

		final long requiredMilliseconds = availableMilliseconds - gameStep.getDeadline().getRemainingMilliseconds();
		final float requiredSeconds = requiredMilliseconds / 1000f;

		viewer.commitRound(availableSeconds, action, requiredSeconds, gameStep.getBoard(), boardRatings);

		return action;
	}
}
