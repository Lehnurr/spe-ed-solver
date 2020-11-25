package utility.game.player;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlayerDirectionTest {

	@Test
	public void testDoAction() {

		assertEquals(PlayerDirection.UP, PlayerDirection.LEFT.doAction(PlayerAction.TURN_RIGHT));

		assertEquals(PlayerDirection.LEFT, PlayerDirection.LEFT.doAction(PlayerAction.SPEED_UP));
		assertEquals(PlayerDirection.LEFT, PlayerDirection.LEFT.doAction(PlayerAction.SLOW_DOWN));
		assertEquals(PlayerDirection.LEFT, PlayerDirection.LEFT.doAction(PlayerAction.CHANGE_NOTHING));
		assertEquals(PlayerDirection.DOWN, PlayerDirection.LEFT.doAction(PlayerAction.TURN_LEFT));

		assertEquals(PlayerDirection.LEFT, PlayerDirection.UP.doAction(PlayerAction.TURN_LEFT));

	}

	@Test
	public void testGetInversion() {
		assertEquals(PlayerDirection.DOWN, PlayerDirection.UP.getInversion());
		assertEquals(PlayerDirection.LEFT, PlayerDirection.RIGHT.getInversion());
		assertEquals(PlayerDirection.UP, PlayerDirection.DOWN.getInversion());
		assertEquals(PlayerDirection.RIGHT, PlayerDirection.LEFT.getInversion());
	}

}
