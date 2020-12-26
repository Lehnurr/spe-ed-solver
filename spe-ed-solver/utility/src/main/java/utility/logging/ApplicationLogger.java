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
    private static boolean logInConsole = false;
    private static String logFilePath = null;

    private ApplicationLogger() {
    }

    static boolean isLoggingEnabled() {
        return logInConsole || ApplicationLogger.logFilePath != null;
    }

    /**
     * Determines whether the log entries are output to the console
     * 
     * @param logInConsole True if the messages should be printed to the console.
     *                     Default is false.
     */
    public static void setLogInConsole(boolean logInConsole) {
        ApplicationLogger.logInConsole = logInConsole;
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
    public static void setLogFilePath(String logFileDirectory, ZonedDateTime applicationStartTime) throws IOException {

        String dateTimeString = DateTimeFormatter.ofPattern("'lehnurr_speed_'yyyyMMddHHmm'.log'")
                .format(applicationStartTime);
        logFilePath = Paths.get(logFileDirectory, dateTimeString).toAbsolutePath().toString();

        // Create directory if necessary
        Path logDirectoryPath = Paths.get(logFileDirectory);
        if (!Files.exists(logDirectoryPath))
            Files.createDirectory(logDirectoryPath);

        // crete File if necessary (should be)
        new File(logFilePath).createNewFile();
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
     * Logs a Exception with the Fatal-Error-Tag and a Time-Stamp
     * 
     * @param exception The occurred exception
     */
    public static void logException(Throwable exception) {
        if (!isLoggingEnabled())
            return;

        final StringBuilder exceptionOutput = new StringBuilder(exception.getMessage() + ": ");

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
     * Logs a Exception with the Fatal-Error-Tag and a Time-Stamp and throws the
     * Exception
     * 
     * @param exception The occurred exception
     */
    public static <ExceptionType extends Throwable> void logAndThrowException(ExceptionType exception)
            throws ExceptionType {
        logException(exception);
        throw exception;
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
    static void logMessage(LoggingTag typeTag, String logMessage) {
        if (!isLoggingEnabled())
            return;

        // A String for the chronological classification of the message
        String timeTag = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String outputMessage = String.format("%-13s [%s]: %s", typeTag.getTag(), timeTag, logMessage);

        if (logInConsole) {
            System.out.println(outputMessage);
        }

        if (ApplicationLogger.logFilePath != null) {
            try (FileWriter logFile = new FileWriter(ApplicationLogger.logFilePath, true)) {
                logFile.append(String.format("%s%n", outputMessage));
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
