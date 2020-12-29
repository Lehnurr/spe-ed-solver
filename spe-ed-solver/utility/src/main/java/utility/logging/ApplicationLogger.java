package utility.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Logger for Console or File output. Logs nothing by Default.
 */
public final class ApplicationLogger {
    private static LoggingLevel consoleLoggingLevel = LoggingLevel.INFO;
    private static String logFilePath = null;

    private ApplicationLogger() {
    }

    /**
     * Checks for a specific {@link LoggingLevel}, if a message with this level will
     * be logged
     * 
     * @param level The level to check for logging
     * @return true if the message will be logged in the console or in the log file
     */
    static boolean isLoggingEnabled(LoggingLevel level) {
        return level.getLevel() <= consoleLoggingLevel.getLevel() || ApplicationLogger.logFilePath != null;
    }

    /**
     * Determines whether the log entries are output to the console
     * 
     * @param logInConsole True if the messages should be printed to the console.
     *                     Default is false.
     */
    public static void setConsoleLoggingLevel(LoggingLevel loggingLevel) {
        ApplicationLogger.consoleLoggingLevel = loggingLevel;
    }

    /**
     * Specifies whether and where to store the log file(s).
     * 
     * @param logFileDirectory A Path (Empty string for the execution path) to a
     *                         Directory or null if no file should be created.
     *                         Default is null.
     */
    public static void setLogFilePath(String logFileDirectory) {

        if (logFileDirectory == null) {
            // Disable File Logging
            logFilePath = null;
            return;
        }

        // The start time of the application to assign a unique name to the log file.
        final ZonedDateTime applicationStartTime = ZonedDateTime.now();

        // Get Log-File path
        String dateTimeString = DateTimeFormatter.ofPattern("'lehnurr_speed_'yyyyMMddHHmm'.log'")
                .format(applicationStartTime);
        logFilePath = Paths.get(logFileDirectory, dateTimeString).toAbsolutePath().toString();

        try {
            // Create directory if necessary
            Path logDirectoryPath = Paths.get(logFileDirectory);
            if (!Files.exists(logDirectoryPath))
                Files.createDirectory(logDirectoryPath);

            // crete File if necessary (should be)
            new File(logFilePath).createNewFile();

        } catch (IOException ex) {
            logFilePath = null;
            ApplicationLogger.logError("Writing a log file is not possible");
            ex.printStackTrace();
        }
    }

    /**
     * Logs a simple Information-String with the Information-Tag and a Time-Stamp
     * 
     * @param informationMessage The Message to log
     */
    public static void logInformation(String informationMessage) {
        if (!isLoggingEnabled(LoggingLevel.INFO))
            return;

        logMessage(LoggingLevel.INFO, informationMessage);
    }

    /**
     * Logs a simple Warning-String with the Warning-Tag and a Time-Stamp
     * 
     * @param warningMessage The Message to log
     */
    public static void logWarning(String warningMessage) {
        if (!isLoggingEnabled(LoggingLevel.WARNING))
            return;

        logMessage(LoggingLevel.WARNING, warningMessage);
    }

    /**
     * Logs a simple Error-String with the Error-Tag and a Time-Stamp
     * 
     * @param errorMessage The Message to log
     */
    public static void logError(String errorMessage) {
        if (!isLoggingEnabled(LoggingLevel.ERROR))
            return;

        logMessage(LoggingLevel.ERROR, errorMessage);
    }

    /**
     * Logs a Exception with the Fatal-Error-Tag and a Time-Stamp
     * 
     * @param exception The occurred exception
     */
    public static void logException(Throwable exception) {
        if (!isLoggingEnabled(LoggingLevel.FATAL_ERROR))
            return;

        final StringBuilder exceptionOutput = new StringBuilder();

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

        logMessage(LoggingLevel.FATAL_ERROR, exceptionOutput.toString());
    }

    /**
     * Logs a Exception with the Fatal-Error-Tag and a Time-Stamp and throws the
     * Exception
     * 
     * @param <ExceptionType> The type of the occurred exception
     * @param exception       The occurred exception
     * @return an exception to make it possible to set the keyword {@code throw}
     *         before the call of this method, so that the compiler knows, after the
     *         call of this method nothing is executed anymore. Since this method
     *         itself always throws an exception, nothing is ever returned.
     * @throws ExceptionType Throws always the passed exception after passing it
     */
    public static <ExceptionType extends Throwable> ExceptionType logAndThrowException(ExceptionType exception)
            throws ExceptionType {
        logException(exception);
        // verhindern dass fatal error auf konsole ausgegeben wird.
        throw exception;
    }

    /**
     * 
     * Outputs a message without any modifications to the console and/or saves it to
     * a file (depending on the configuration)
     * 
     * @param level      A {@link LoggingLevel LoggingLevel} to Tag the Message as
     *                   Info, Warning, etc.
     * @param logMessage The message to be output
     */
    static void logMessage(LoggingLevel level, String logMessage) {
        if (!isLoggingEnabled(level))
            return;

        // A String for the chronological classification of the message
        String timeTag = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String outputMessage = String.format("%-13s [%s]: %s", level.getTag(), timeTag, logMessage);

        if (consoleLoggingLevel.getLevel() >= level.getLevel()) {
            System.out.println(outputMessage);
        } else if (level == LoggingLevel.FATAL_ERROR) {
            String fatalErrorMessage = String.format("%-13s [%s]: %s", level.getTag(), timeTag,
                    "A fatal error has occurred, please check the log file");
            System.out.println(fatalErrorMessage);
        }
        if (ApplicationLogger.logFilePath != null) {
            try (FileWriter logFile = new FileWriter(ApplicationLogger.logFilePath, true)) {
                logFile.append(String.format("%s%n", outputMessage));
            } catch (IOException ex) {
                if (consoleLoggingLevel.getLevel() < level.getLevel()) {
                    // Output the message to console if this has not already happened
                    System.out.println(outputMessage);
                }
                System.out.println("Error while Writing in LOG-File");
                ex.printStackTrace();
            }
        }
    }

}
