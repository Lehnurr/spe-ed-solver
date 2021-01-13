package utility.logging;

/**
 * Specifies a Level for Log-Messages to put their importance in relation.
 */
public enum LoggingLevel {
    /**
     * An error that prevents further execution.
     */
    ERROR(0),
    /**
     * Information regarding the game spe_ed.
     */
    WARNING(1),
    /**
     * Information about the status of the application.
     */
    GAME_INFO(2),
    /**
     * An error which does not prevent further execution.
     */
    INFO(3),
    /**
     * Information about the status of the application that will never show up on
     * the console.
     */
    FILE_INFO(4);

    private int level;

    private LoggingLevel(int level) {
        this.level = level;
    }

    /**
     * The integer Value of the {@link LoggingLevel}. Must not equals the ordinal
     * value of the enum.
     * 
     * @return the integer-{@link LoggingLevel level} Value
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * A string representation of the {@link LoggingLevel} for the output.
     * 
     * @return the name of the {@link LoggingLevel} with brackets around it
     */
    public String getTag() {
        return "[" + name() + "]";
    }

    /**
     * Returns the {@link LoggingLevel} for a given integer value.
     * 
     * @param intValue the integer value a {@link LoggingLevel} is searched for
     * @return the found {@link LoggingLevel}
     * @throws IllegalArgumentException if no {@link LoggingLevel} was found
     */
    public static LoggingLevel fromInteger(final int intValue) {
        if (intValue < ERROR.getLevel() || intValue > WARNING.getLevel()) {
            throw new IllegalArgumentException("Logging level " + intValue + " does not exist!");
        }

        return LoggingLevel.values()[intValue];
    }
}
