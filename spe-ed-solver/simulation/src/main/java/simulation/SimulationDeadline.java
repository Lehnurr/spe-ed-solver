package simulation;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Random;

import utility.game.step.Deadline;

public final class SimulationDeadline implements Deadline {

    private ZonedDateTime deadline;
    private final Random random;

    public SimulationDeadline() {
        random = new Random();
        resetDeadLine();
    }

    /**
     * Resets the deadline by a random number of seconds between 2 and 15
     */
    public void resetDeadLine() {
        // random number of seconds between 2 (inclusive) and 15 (inclusive)
        final int MIN = 2;
        final int MAX = 15;

        var deadLineSeconds = random.nextInt(MAX - MIN + 1) + MIN;
        this.deadline = ZonedDateTime.now().plusSeconds(deadLineSeconds);
    }

    @Override
    public long getRemainingMilliseconds() {
        final ZonedDateTime now = ZonedDateTime.now();
        final Duration durationLeft = Duration.between(now, deadline);
        return durationLeft.toMillis();

    }
}
