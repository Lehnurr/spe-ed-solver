package utility.logging;

/**
 * Specifies a Level for Log-Messages to put their importance in relation
 */
public enum LoggingLevel {
    GAME_INFO(1), INFO(2), WARNING(3), ERROR(4), FATAL_ERROR(5);

    private int level;

    private LoggingLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public String getTag() {
        return "[" + name() + "]";
    }

    /**
     * Returns the {@link LoggingLevel} for a given integer value. If no
     * {@link LoggingLevel} was found a {@link IllegalArgumentException} is thrown.
     * 
     * @param intValue integer value
     * @return {@link LoggingLevel}
     */
    public static LoggingLevel fromInteger(final int intValue) {
        if (intValue < 1 || intValue > 5) {
            ApplicationLogger.logAndThrowException(
                    new IllegalArgumentException("Logging level " + intValue + " does not exist!"));
        }

        return LoggingLevel.values()[intValue - 1];
    }
}
