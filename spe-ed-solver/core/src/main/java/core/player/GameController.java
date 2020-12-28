package core.player;

import java.util.HashMap;
import java.util.Map;

import utility.game.player.PlayerAction;
import utility.game.step.GameStep;

/**
 * GameController for multiple Player
 */
public class GameController {

	private final Map<Integer, PlayerController> playerControllers;

	private final boolean viewerEnabled;

	/**
	 * A Controller to Control multiple {@link PlayerController PlayerControllers}.
	 * 
	 */
	public GameController(final boolean viewerEnabled) {
		this.playerControllers = new HashMap<>();
		this.viewerEnabled = viewerEnabled;
	}

	/**
	 * Sends the new {@link GameStep} to the {@link IPlayer} self of the
	 * {@link GameStep}.
	 * 
	 * @param gameStep The new GameStep
	 */
	public PlayerAction sendGameStep(GameStep gameStep) {
		final int playerId = gameStep.getSelf().getPlayerId();

		playerControllers.computeIfAbsent(playerId, key -> new PlayerController(key, viewerEnabled));

		return this.playerControllers.get(playerId).calculateAction(gameStep);
	}
}
