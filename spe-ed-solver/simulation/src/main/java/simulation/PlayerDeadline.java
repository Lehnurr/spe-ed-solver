package simulation;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import utility.game.step.Deadline;

/**
 * A deadline, which depends on the first call of
 * {@link PlayerDeadline#getRemainingMilliseconds() getRemainingMilliseconds}
 */
public class PlayerDeadline implements Deadline {
    private ZonedDateTime deadline;
    private final long deadlineMilliseconds;

    /**
     * Initializes a new Deadline with the given amount of milliseconds.
     * 
     * @param milliseconds the number of milliseconds for the calculation of the
     *                     deadline
     */
    public PlayerDeadline(long milliseconds) {
        this.deadlineMilliseconds = milliseconds;
    }

    @Override
    /**
     * Determines the milliseconds until the deadline. The Deadline is calculated
     * after the first call of this function.
     * 
     * @return The remaining milliseconds until the deadline
     */
    public long getRemainingMilliseconds() {
        if (deadline == null)
            deadline = ZonedDateTime.now().plus(this.deadlineMilliseconds, ChronoUnit.MILLIS);

        final ZonedDateTime now = ZonedDateTime.now();
        final Duration durationLeft = Duration.between(now, deadline);
        return durationLeft.toMillis();
    }

}
