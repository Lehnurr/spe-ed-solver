package simulation;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Random;

import utility.game.step.IDeadline;

/**
 * A {@link IDeadline } implementation with the possibility to reset the
 * deadline. The lower and upper limit for the generated Deadline can be defined
 * via cli-parameters.
 */
public final class SimulationDeadline implements IDeadline {

    private ZonedDateTime deadline;
    private final Random random;
    private final int minSeconds;
    private final int maxSeconds;

    private static int lowerTimeLimit = 2;
    private static int upperTimeLimit = 15;

    /**
     * Creates a new {@link SimulationDeadline} and calls
     * {@link SimulationDeadline#resetDeadLine()}.
     */
    public SimulationDeadline() {
        // Determine the min- and max-seconds, since the input via the command line does
        // not guarantee that the lower limit is below the upper limit
        minSeconds = Math.min(lowerTimeLimit, upperTimeLimit);
        maxSeconds = Math.max(upperTimeLimit, lowerTimeLimit);

        random = new Random();
        resetDeadLine();
    }

    /**
     * Resets the deadline by a random number of seconds between
     * {@link SimulationDeadline#lowerTimeLimit} and
     * {@link SimulationDeadline#upperTimeLimit}.
     */
    public void resetDeadLine() {
        final int deadLineSeconds = random.nextInt(maxSeconds - minSeconds + 1) + minSeconds;
        this.deadline = ZonedDateTime.now().plusSeconds(deadLineSeconds);
    }

    @Override
    public long getRemainingMilliseconds() {
        final ZonedDateTime now = ZonedDateTime.now();
        final Duration durationLeft = Duration.between(now, deadline);
        return durationLeft.toMillis();
    }

    /**
     * Changes the global {@link SimulationDeadline#lowerTimeLimit} for all
     * instances of {@link SimulationDeadline}.
     * 
     * @param lowerTimeLimit the minimum number of seconds for a random generated
     *                       deadline
     */
    public static void setLowerTimeLimit(int lowerTimeLimit) {
        SimulationDeadline.lowerTimeLimit = lowerTimeLimit;
    }

    /**
     * Changes the global {@link SimulationDeadline#upperTimeLimit} for all
     * instances of {@link SimulationDeadline}.
     * 
     * @param upperTimeLimit the maximum number of seconds for a random generated
     *                       deadline
     */
    public static void setUpperTimeLimit(int upperTimeLimit) {
        SimulationDeadline.upperTimeLimit = upperTimeLimit;
    }

}
