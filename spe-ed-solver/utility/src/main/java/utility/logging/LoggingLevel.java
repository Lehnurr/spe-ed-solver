package utility.logging;

/**
 * Specifies a Level for Log-Messages to put their importance in relation
 */
public enum LoggingLevel {
    /**
     * An error that prevents further execution
     */
    ERROR(0),
    /**
     * Information regarding the game spe_ed
     */
    GAME_INFO(1),
    /**
     * Information about the status of the application
     */
    INFO(2),
    /**
     * An error which does not prevent further execution
     */
    WARNING(3);

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
        if (intValue < ERROR.getLevel() || intValue > WARNING.getLevel()) {
            ApplicationLogger.logAndThrowException(
                    new IllegalArgumentException("Logging level " + intValue + " does not exist!"), LoggingLevel.ERROR);
        }

        return LoggingLevel.values()[intValue];
    }
}
