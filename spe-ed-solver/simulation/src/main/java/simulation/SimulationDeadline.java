package simulation;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Random;

import utility.game.step.Deadline;

public final class SimulationDeadline implements Deadline {

    private ZonedDateTime deadline;
    private final Random random;
    private final int minSeconds;
    private final int maxSeconds;

    private static int lowerTimeLimit = 2;
    private static int upperTimeLimit = 15;

    public SimulationDeadline() {
        // Determine the min- and max-seconds, since the input via the command line does
        // not guarantee that the lower limit is below the upper limit
        minSeconds = Math.min(lowerTimeLimit, upperTimeLimit);
        maxSeconds = Math.max(upperTimeLimit, lowerTimeLimit);

        random = new Random();
        resetDeadLine();
    }

    /**
     * Resets the deadline by a random number of seconds between 2 and 15
     */
    public void resetDeadLine() {

        // random number of seconds between min and max
        var deadLineSeconds = random.nextInt(maxSeconds - minSeconds + 1) + minSeconds;
        this.deadline = ZonedDateTime.now().plusSeconds(deadLineSeconds);
    }

    @Override
    public long getRemainingMilliseconds() {
        final ZonedDateTime now = ZonedDateTime.now();
        final Duration durationLeft = Duration.between(now, deadline);
        return durationLeft.toMillis();
    }

    public static void setLowerTimeLimit(int lowerTimeLimit) {
        SimulationDeadline.lowerTimeLimit = lowerTimeLimit;
    }

    public static void setUpperTimeLimit(int upperTimeLimit) {
        SimulationDeadline.upperTimeLimit = upperTimeLimit;
    }

}
