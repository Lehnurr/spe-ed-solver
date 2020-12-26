package utility.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import utility.game.player.IMovablePlayer;
import utility.game.step.GameStep;

/**
 * A Logger for Console or File output. Logs nothing by Default.
 */
public final class LehnurrLogger {
    private static boolean logInConsole = false;
    private static FileWriter logFileWriter = null;

    private LehnurrLogger() {
    }

    private static boolean isLoggingEnabled() {
        return logInConsole || LehnurrLogger.logFileWriter != null;
    }

    /**
     * Determines whether the log entries are output to the console
     * 
     * @param logInConsole True if the messages should be printed to the console.
     *                     Default is false.
     */
    public static void setLogInConsole(boolean logInConsole) {
        LehnurrLogger.logInConsole = logInConsole;
    }

    /**
     * Specifies whether and where to store the log file(s).
     * 
     * @param logFileDirectory     A Path (Empty string for the execution path) to a
     *                             Directory or null if no file should be created.
     *                             Default is null.
     * @param applicationStartTime The start time of the application to assign a
     *                             unique name to the log file.
     * @throws IOException
     */
    public static void setLogFilePath(String logFileDirectory, LocalDateTime applicationStartTime) throws IOException {
        if (logFileDirectory == null || applicationStartTime == null) {
            // Closing a previously closed stream has no effect.
            if (logFileWriter != null)
                logFileWriter.close();
            logFileWriter = null;
        } else {
            String dateTimeString = applicationStartTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String logFilePath = Paths.get(logFileDirectory, dateTimeString, ".log").toAbsolutePath().toString();

            if (logFileWriter != null)
                logFileWriter.close();
            logFileWriter = new FileWriter(logFilePath, false);
        }
    }

    /**
     * Logs the Data of a GameStep
     * 
     * @param step The {@link GameStep#GameStep Steps} to log
     */
    public static void logGameStep(GameStep step) {
        if (!isLoggingEnabled())
            return;

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

        logMessage(LoggingTag.GAME_INFO, gameStep.toString());
    }

    /**
     * Logs the data of a Player State
     * 
     * @param player The {@link IMovablePlayer#IMovablePlayer Player} to log
     */
    public static void logPlayerAction(IMovablePlayer player) {
        if (!isLoggingEnabled())
            return;

        String action;
        if (player.getNextAction() != null)
            action = player.getNextAction().getName();
        else
            action = "NO ACTION";

        String playerState = String.format("action {%s} by Player %d {%s, %s, %d} in Round %d", action,
                player.getPlayerId(), player.getDirection().name(), player.getPosition().toString(), player.getSpeed(),
                player.getRound());

        logMessage(LoggingTag.GAME_INFO, playerState);
    }

    /**
     * Logs a simple Information-String with the Information-Tag and a Time-Stamp
     * 
     * @param informationMessage The Message to log
     */
    public static void logInformation(String informationMessage) {
        if (!isLoggingEnabled())
            return;

        logMessage(LoggingTag.INFO, informationMessage);
    }

    /**
     * Logs a simple Warning-String with the Warning-Tag and a Time-Stamp
     * 
     * @param warningMessage The Message to log
     */
    public static void logWarning(String warningMessage) {
        if (!isLoggingEnabled())
            return;

        logMessage(LoggingTag.WARNING, warningMessage);
    }

    /**
     * Logs a simple Error-String with the Error-Tag and a Time-Stamp
     * 
     * @param errorMessage The Message to log
     */
    public static void logError(String errorMessage) {
        if (!isLoggingEnabled())
            return;

        logMessage(LoggingTag.ERROR, errorMessage);
    }

    /**
     * Logs a Error-String with the Fatal-Error-Tag and a Time-Stamp
     * 
     * @param exception The occurred exception
     */
    public static void logException(Throwable exception) {
        if (!isLoggingEnabled())
            return;

        final StringBuilder exceptionOutput = new StringBuilder(exception.getMessage() + ":");

        // Performance can be disregarded, as we have a much worse problem here with an
        // exception
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            exception.printStackTrace(pw);
            exceptionOutput.append(sw.toString());
        } catch (IOException closeException) {
            // Can't even get the Stacktrace.
            exception.printStackTrace();
            closeException.printStackTrace();
        }

        logMessage(LoggingTag.FATAL_ERROR, exceptionOutput.toString());
    }

    /**
     * 
     * Outputs a message without any modifications to the console and/or saves it to
     * a file (depending on the configuration)
     * 
     * @param typeTag    A {@link LoggingTag#LoggingTag Tag} to mark the Message as
     *                   Info, Warning, etc.
     * @param logMessage The message to be output
     */
    private static void logMessage(LoggingTag typeTag, String logMessage) {
        if (!isLoggingEnabled())
            return;

        // A String for the chronological classification of the message
        String timeTag = LocalDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String outputMessage = String.format("%-13s [%s]: %s", typeTag.getTag(), timeTag, logMessage);

        if (logInConsole) {
            System.out.println(outputMessage);
        }

        if (LehnurrLogger.logFileWriter != null) {
            try {
                logFileWriter.append(String.format("%s%n", outputMessage));
            } catch (IOException ex) {
                if (!logInConsole) {
                    // Output the message to console if this has not already happened
                    System.out.println(outputMessage);
                }
                System.out.println("Error while Writing in LOG-File");
                ex.printStackTrace();
            }
        }
    }

}
