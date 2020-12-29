package utility.logging;

import utility.game.player.IMovablePlayer;
import utility.game.step.GameStep;

/**
 * Uses the {@link ApplicationLogger#ApplicationLogger ApplicationLogger} to Log
 * information about the Game
 */
public final class GameLogger {

    private GameLogger() {
    }

    /**
     * Logs the Data of a GameStep
     * 
     * @param step The {@link GameStep Step} to log
     */
    public static void logGameStep(GameStep step) {

        String running;
        if (step.isRunning())
            running = "is running";
        else
            running = "finished";

        // Log the general state
        StringBuilder gameStep = new StringBuilder(String.format(
                "%s: {you=%d, playerCount=%d, remainingMilliseconds=%s}%n", running, step.getSelf().getPlayerId(),
                step.getPlayerCount(), step.getDeadline().getRemainingMilliseconds()));

        // Log the Board
        gameStep.append(String.format("board=%s%n", step.getBoard().toString()));

        // Log the Player
        var currentPlayer = step.getSelf();
        var iterator = step.getEnemies().values().iterator();
        do {
            String active;
            if (currentPlayer.isActive())
                active = "alive";
            else
                active = "dead";

            String player = String.format("Player %d {%s, %s, %s, %d} in Round %d%n", currentPlayer.getPlayerId(),
                    active, currentPlayer.getDirection().name(), currentPlayer.getPosition().toString(),
                    currentPlayer.getSpeed(), currentPlayer.getRound());

            gameStep.append(player);

            if (iterator.hasNext())
                currentPlayer = iterator.next();
            else
                currentPlayer = null;
        } while (currentPlayer != null);

        ApplicationLogger.logMessage(LoggingLevel.GAME_INFO, gameStep.toString());
    }

    /**
     * Logs the data of a Player State
     * 
     * @param player The {@link IMovablePlayer Player} to log
     */
    public static void logPlayerAction(IMovablePlayer player) {

        String action;
        if (player.getNextAction() != null)
            action = player.getNextAction().getName();
        else
            action = "NO ACTION";

        String playerState = String.format("action {%s} by Player %d {%s, %s, %d} in Round %d", action,
                player.getPlayerId(), player.getDirection().name(), player.getPosition().toString(), player.getSpeed(),
                player.getRound());

        ApplicationLogger.logMessage(LoggingLevel.GAME_INFO, playerState);
    }

    public static void logGameInformation(String message) {
        ApplicationLogger.logMessage(LoggingLevel.GAME_INFO, message);
    }
}
