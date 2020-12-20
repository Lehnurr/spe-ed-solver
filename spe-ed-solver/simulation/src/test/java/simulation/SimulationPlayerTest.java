package simulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utility.game.player.IMovablePlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

public class SimulationPlayerTest {

    @Test
    public void testSetAction() {
        SimulationPlayer player = new SimulationPlayer(1, new Point2i(0, 0), PlayerDirection.UP, 1);

        assertTrue("Players have to be alive initially", player.isActive());
        player.setAction(PlayerAction.SPEED_UP);
        assertTrue(player.isActive());
        player.setAction(PlayerAction.SPEED_UP);
        assertFalse("Player have to die if a second action is set", player.isActive());
    }

    @Test
    public void testDoActionAndMove() {
        SimulationPlayer player = new SimulationPlayer(1, new Point2i(0, 0), PlayerDirection.UP,
                IMovablePlayer.MIN_SPEED);

        player.setAction(PlayerAction.SLOW_DOWN);
        player.doActionAndMove();
        assertEquals(0, player.getSpeed());
        assertFalse("Player with speed < MIN_SPEED have to be dead", player.isActive());

        player = new SimulationPlayer(1, new Point2i(0, 0), PlayerDirection.UP, IMovablePlayer.MAX_SPEED);
        player.setAction(PlayerAction.SPEED_UP);
        player.doActionAndMove();
        assertEquals(11, player.getSpeed());
        assertFalse("Player with speed > MAX_SPEED have to be dead", player.isActive());
        assertEquals("Dead players are not allowed to do moves", new Point2i(0, 0), player.getPosition());

        player = new SimulationPlayer(1, new Point2i(0, 0), PlayerDirection.DOWN, 5);
        player.setAction(PlayerAction.SPEED_UP);
        player.doActionAndMove();
        assertEquals("SPEED_UP have to increase the speed", 6, player.getSpeed());
        assertEquals("The player must move with the set speed", new Point2i(0, 6), player.getPosition());

        player = new SimulationPlayer(1, new Point2i(0, 0), PlayerDirection.UP, 5);
        player.setAction(PlayerAction.SLOW_DOWN);
        player.doActionAndMove();
        assertEquals("SLOW_DOWN have to decrease the speed", 4, player.getSpeed());

        player = new SimulationPlayer(1, new Point2i(0, 0), PlayerDirection.UP, 5);
        player.die();
        player.setAction(PlayerAction.SLOW_DOWN);
        player.doActionAndMove();
        assertEquals("Dead players are not allowed to do actions", 5, player.getSpeed());
    }

}
