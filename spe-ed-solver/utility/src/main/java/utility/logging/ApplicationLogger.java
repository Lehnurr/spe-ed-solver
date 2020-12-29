package utility.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Logger for Console or File output. Logs nothing by Default.
 */
public final class ApplicationLogger {
    private static LoggingLevel consoleLoggingLevel = LoggingLevel.INFO;
    private static String logFilePath = Paths
            .get("log", DateTimeFormatter.ofPattern("'lehnurr_speed_'yyyyMMddHHmm'.log'").format(ZonedDateTime.now()))
            .toAbsolutePath().toString();
    private static boolean debugModeEnabled;

    private ApplicationLogger() {
    }

    /**
     * Determines whether the log entries are output to the console
     * 
     * @param logInConsole True if the messages should be printed to the console.
     *                     Default is false.
     */
    public static void setConsoleLoggingLevel(LoggingLevel loggingLevel) {
        if (!debugModeEnabled)
            ApplicationLogger.consoleLoggingLevel = loggingLevel;
    }

    /**
     * Determines whether debug information (e.g. stacktrace of exceptions) are
     * displayed on the console
     * 
     * @param debugEnabled True, if all available information is to be displayed on
     *                     the console.
     */
    public static void setDebugMode(boolean debugEnabled) {
        setConsoleLoggingLevel(LoggingLevel.WARNING);
        ApplicationLogger.debugModeEnabled = debugEnabled;
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
        try {
            logFilePath = Paths.get(logFileDirectory, dateTimeString).toAbsolutePath().toString();
        } catch (InvalidPathException e) {
            logFilePath = Paths.get(dateTimeString).toAbsolutePath().toString();
        }

        try {
            // Create directory if necessary
            Path logDirectoryPath = Paths.get(logFileDirectory);
            if (!Files.exists(logDirectoryPath))
                Files.createDirectory(logDirectoryPath);

            // crete File if necessary (should be)
            new File(logFilePath).createNewFile();

        } catch (IOException ex) {
            logFilePath = null;
            ApplicationLogger.logException(ex, LoggingLevel.WARNING);
        }
    }

    /**
     * Logs a simple Information-String with the Information-Tag and a Time-Stamp
     * 
     * @param informationMessage The Message to log
     */
    public static void logInformation(String informationMessage) {
        logMessage(LoggingLevel.INFO, informationMessage, LoggingLevel.INFO, informationMessage);
    }

    /**
     * Logs a simple Warning-String with the Warning-Tag and a Time-Stamp
     * 
     * @param warningMessage The Message to log
     */
    public static void logWarning(String warningMessage) {
        if (logFilePath == null && consoleLoggingLevel.getLevel() < LoggingLevel.WARNING.getLevel())
            return;

        logMessage(LoggingLevel.WARNING, warningMessage, LoggingLevel.WARNING, warningMessage);
    }

    /**
     * Logs a simple Error-String with the Error-Tag and a Time-Stamp
     * 
     * @param errorMessage The Message to log
     */
    public static void logError(String errorMessage) {
        logMessage(LoggingLevel.ERROR, errorMessage, LoggingLevel.ERROR, errorMessage);
    }

    /**
     * Logs a Exception with a Time-Stamp. Additionally, a seperate level for the
     * console output can be secified
     * 
     * @param exception         The occurred exception
     * @param exceptionLoglevel The level for the exception
     * @param messageLoglevel   The level for the console output of the exception
     *                          message
     */
    public static void logException(Throwable exception, LoggingLevel exceptionLoglevel, LoggingLevel messageLoglevel) {
        final StringBuilder exceptionOutput = new StringBuilder();

        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            exception.printStackTrace(pw);
            exceptionOutput.append(sw.toString());
        } catch (IOException closeException) {
            // Can't even get the Stacktrace.
            exception.printStackTrace();
            closeException.printStackTrace();
        }

        logMessage(exceptionLoglevel, exceptionOutput.toString(), messageLoglevel, exception.getMessage());
        if (debugModeEnabled) {
            exception.printStackTrace();
        }
    }

    /**
     * Logs a Exception with a Time-Stamp.
     * 
     * @param exception                   The occurred exception
     * @param exceptionAndMessageLoglevel The level for the exception
     */
    public static void logException(Throwable exception, LoggingLevel exceptionAndMessageLoglevel) {
        logException(exception, exceptionAndMessageLoglevel, exceptionAndMessageLoglevel);
    }

    /**
     * Logs and throws a Exception with a Time-Stamp. Additionally, a seperate level
     * for the console output can be secified
     * 
     * @param exception         The occurred exception
     * @param exceptionLoglevel The level for the exception
     * @param messageLoglevel   The level for the console output of the exception
     *                          message
     * @return will be never used because this function throws alwas an exception
     * @throws ExceptionType the passed exception
     */
    public static <ExceptionType extends Throwable> ExceptionType logAndThrowException(ExceptionType exception,
            LoggingLevel exceptionLoglevel, LoggingLevel messageLoglevel) throws ExceptionType {
        logException(exception, exceptionLoglevel, messageLoglevel);
        throw exception;
    }

    /**
     * Logs and throws a Exception with a Time-Stamp. Additionally, a seperate level
     * for the console output can be secified
     * 
     * @param exception                   The occurred exception
     * @param exceptionAndMessageLoglevel The level for the exception
     * @return will be never used because this function throws alwas an exception
     * @throws ExceptionType the passed exception
     */
    public static <ExceptionType extends Throwable> ExceptionType logAndThrowException(ExceptionType exception,
            LoggingLevel exceptionAndMessageLoglevel) throws ExceptionType {
        logException(exception, exceptionAndMessageLoglevel, exceptionAndMessageLoglevel);
        throw exception;
    }

    /**
     * 
     * Outputs a message to the console and saves it to a file (depending on the
     * configuration)
     * 
     * @param logFileLevel   A {@link LoggingLevel LoggingLevel} to Tag the Message
     *                       as Info, Warning, etc. for the LogFile
     * @param logFileMessage The message for the log File
     * @param consoleLevel   A {@link LoggingLevel LoggingLevel} to Tag and filter
     *                       the Message for the console output
     * @param consoleMessage The message for the console output
     */
    static void logMessage(LoggingLevel logFileLevel, String logFileMessage, LoggingLevel consoleLevel,
            String consoleMessage) {
        if (logFilePath == null && consoleLevel.getLevel() > consoleLoggingLevel.getLevel())
            return;

        // A String for the chronological classification of the message
        String timeTag = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String fileLog = String.format("%-13s [%s]: %s", logFileLevel.getTag(), timeTag, logFileMessage);
        String consoleLog = String.format("%-13s [%s]: %s", consoleLevel.getTag(), timeTag, consoleMessage);

        if (consoleLoggingLevel.getLevel() >= consoleLevel.getLevel()) {
            if (consoleLevel == LoggingLevel.ERROR || consoleLevel == LoggingLevel.WARNING) {
                System.err.println(consoleLog);
            } else {
                System.out.println(consoleLog);
            }
        }

        if (ApplicationLogger.logFilePath != null) {
            try (FileWriter logFile = new FileWriter(ApplicationLogger.logFilePath, true)) {
                logFile.append(String.format("%s%n", fileLog));
            } catch (IOException ex) {
                System.err.println("Error while Writing in LOG-File");
                if (consoleLoggingLevel.getLevel() < logFileLevel.getLevel()) {
                    // Output the message to console if this has not already happened
                    System.err.println(fileLog);
                }
                ex.printStackTrace();
            }
        }
    }

}
