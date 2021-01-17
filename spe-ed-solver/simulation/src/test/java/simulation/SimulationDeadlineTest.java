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

        SimulationDeadline.setLowerTimeLimit(5);
        SimulationDeadline.setUpperTimeLimit(1);
        final SimulationDeadline swappedDeadline = new SimulationDeadline();
        assertTrue("Deadline upper limit must be the maximum", swappedDeadline.getRemainingMilliseconds() >= 1000);
        assertTrue("Deadline lower limit must be the minimum", swappedDeadline.getRemainingMilliseconds() <= 5000);

        // reset the limits
        SimulationDeadline.setLowerTimeLimit(2);
        SimulationDeadline.setUpperTimeLimit(15);
    }
}
