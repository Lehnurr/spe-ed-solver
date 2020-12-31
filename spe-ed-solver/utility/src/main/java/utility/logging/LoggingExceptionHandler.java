package utility.logging;

import java.lang.Thread.UncaughtExceptionHandler;

public class LoggingExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error has occurred. More details can be seen in the log file.";
        final String THREAD_LOG_MESSAGE = "Uncaught Exception in thread (%s with id %d)";

        // Output the exception synchronized, so that in case of multiple exceptions in
        // different threads the log entries are classifiable.
        synchronized (System.out) {
            LoggingLevel messageLoggingLevel;
            if (t.getId() == 1) {
                // Exception occured in the main thread. This causes the application to be
                // terminated
                messageLoggingLevel = LoggingLevel.ERROR;
                ApplicationLogger.logError(UNEXPECTED_ERROR_MESSAGE);
                ApplicationLogger.logError(String.format(THREAD_LOG_MESSAGE, t.getName(), t.getId()));
            } else {
                // If the exception occurred in a sub-thread, the application can continue
                messageLoggingLevel = LoggingLevel.WARNING;
                ApplicationLogger.logWarning(UNEXPECTED_ERROR_MESSAGE);
                ApplicationLogger.logWarning(String.format(THREAD_LOG_MESSAGE, t.getName(), t.getId()));
            }

            ApplicationLogger.logException(e, LoggingLevel.ERROR, messageLoggingLevel);
        }
    }

}
