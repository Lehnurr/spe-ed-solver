package simulation;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SimulationDeadlineTest {

    @Test
    public void testResetDeadLine() throws InterruptedException {

        final SimulationDeadline deadline = new SimulationDeadline();
        TimeUnit.MILLISECONDS.sleep(300);
        final long milliseconds = deadline.getRemainingMilliseconds();

        deadline.resetDeadLine();
        final long resetMilliseconds = deadline.getRemainingMilliseconds();
        assertNotEquals(resetMilliseconds, milliseconds);
    }

    @Test
    public void testSetTimeLimit() {

        SimulationDeadline.setLowerTimeLimit(1);
        SimulationDeadline.setUpperTimeLimit(1);
        final SimulationDeadline oneSecondDeadline = new SimulationDeadline();
        assertTrue("Deadline upper limit must be the maximum", oneSecondDeadline.getRemainingMilliseconds() <= 1000);

        // test with inverted maximum and minimum
        SimulationDeadline.setLowerTimeLimit(5);
        SimulationDeadline.setUpperTimeLimit(1);
        final long remainingSeconds = new SimulationDeadline().getRemainingMilliseconds();
        assertTrue("Deadline upper limit must be the maximum", remainingSeconds <= 5000);

        // reset the limits
        SimulationDeadline.setLowerTimeLimit(2);
        SimulationDeadline.setUpperTimeLimit(15);
    }
}
