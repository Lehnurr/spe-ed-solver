package utility.logging;

import java.util.Iterator;

import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;

/**
 * Uses the {@link ApplicationLogger#ApplicationLogger ApplicationLogger} to Log
 * information about the Game.
 */
public final class GameLogger {

	private GameLogger() {
	}

	/**
	 * Logs the Data of a GameStep.
	 * 
	 * @param step the {@link GameStep Step} to log
	 */
	public static void logGameStep(GameStep step) {

		String running;
		if (step.isRunning())
			running = "is running";
		else
			running = "finished";

		// Log the general state
		StringBuilder gameStep = new StringBuilder(
				String.format("%s: {you=%d, round=%d, playerCount=%d, remainingMilliseconds=%s}", running,
						step.getSelf().getPlayerId(), step.getSelf().getRound(), step.getPlayerCount(),
						step.getDeadline().getRemainingMilliseconds()));

		// Log the Player
		IPlayer currentPlayer = step.getSelf();
		final Iterator<IPlayer> iterator = step.getEnemies().values().iterator();
		do {
			String active;
			if (currentPlayer.isActive())
				active = "alive";
			else
				active = "dead";

			String player = String.format("%n\tPlayer %d {%s, %s, %s, %d}", currentPlayer.getPlayerId(), active,
					currentPlayer.getDirection().name(), currentPlayer.getPosition().toString(),
					currentPlayer.getSpeed());

			gameStep.append(player);

			if (iterator.hasNext())
				currentPlayer = iterator.next();
			else
				currentPlayer = null;
		} while (currentPlayer != null);

		final String consoleMessage = gameStep.toString();

		// Log the Board
		gameStep.append(String.format("%nboard=%s", step.getBoard().toString()));
		final String logFileMessage = gameStep.toString();

		ApplicationLogger.logMessage(LoggingLevel.GAME_INFO, logFileMessage, LoggingLevel.GAME_INFO, consoleMessage);
	}

	/**
	 * 
	 * Logs the data of a Player State.
	 * 
	 * @param player           the {@link IPlayer Player} to log
	 * @param action           the players chosen {@link PlayerAction}
	 * @param requiredSeconds  seconds required by the player to calculate the
	 *                         {@link PlayerAction}
	 * @param availableSeconds seconds available to the player to calculate the
	 *                         {@link PlayerAction}
	 * 
	 */
	public static void logPlayerAction(IPlayer player, PlayerAction action, float requiredSeconds,
			float availableSeconds) {

		String playerState = String.format("action {%s} by Player %d {%s, %s, %d} in Round %d (%.3f/%.3f sec)",
				action.getName(), player.getPlayerId(), player.getDirection().name(), player.getPosition().toString(),
				player.getSpeed(), player.getRound(), requiredSeconds, availableSeconds);

		ApplicationLogger.logMessage(LoggingLevel.GAME_INFO, playerState, LoggingLevel.GAME_INFO, playerState);
	}

	public static void logGameInformation(String message) {
		ApplicationLogger.logMessage(LoggingLevel.GAME_INFO, message, LoggingLevel.GAME_INFO, message);
	}
}
