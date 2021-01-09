package core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import player.PlayerType;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.logging.ApplicationLogger;

/**
 * GameController for one or multiple players
 */
public class GameController {

	private final List<PlayerType> playerTypes;

	private final Map<Integer, PlayerController> playerControllers;

	private final boolean viewerEnabled;

	/**
	 * A Controller to Control multiple {@link PlayerController PlayerControllers}.
	 * 
	 * @param viewerEnabled true if the viewer should be enabled for the players
	 * @param playerTypes   {@link List} of {@link PlayerType} of the players
	 *                      participating
	 */
	public GameController(final boolean viewerEnabled, final List<PlayerType> playerTypes) {
		this.playerTypes = playerTypes;
		this.playerControllers = new HashMap<>();
		this.viewerEnabled = viewerEnabled;
	}

	/**
	 * Sends the new {@link GameStep} to the {@link IPlayer} self of the
	 * {@link GameStep}.
	 * 
	 * @param gameStep the new {@link GameStep}
	 * @return {@link PlayerAction} to send back to the server
	 */
	public PlayerAction handleGameStep(final GameStep gameStep) {
		final int playerId = gameStep.getSelf().getPlayerId();

		playerControllers.computeIfAbsent(playerId, key -> {
			final PlayerType playerType = playerTypes.remove(0);
			ApplicationLogger
					.logInformation(String.format("Registered player of type %s and id %d.", playerType.name(), key));
			return new PlayerController(viewerEnabled, playerType);
		});

		return this.playerControllers.get(playerId).calculateAction(gameStep);
	}
}
