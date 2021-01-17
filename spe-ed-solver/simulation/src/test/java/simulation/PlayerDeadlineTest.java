package simulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class PlayerDeadlineTest {

    @Test
    public void testGetRemainingMilliseconds() throws InterruptedException {

        final PlayerDeadline deadlineA = new PlayerDeadline(100);
        final PlayerDeadline deadlineB = new PlayerDeadline(1000);

        TimeUnit.MILLISECONDS.sleep(2000);

        assertTrue(deadlineA.getRemainingMilliseconds() > 0);
        assertTrue(deadlineB.getRemainingMilliseconds() > 0);

        TimeUnit.MILLISECONDS.sleep(200);
        assertTrue(deadlineA.getRemainingMilliseconds() < 0);
    }
}