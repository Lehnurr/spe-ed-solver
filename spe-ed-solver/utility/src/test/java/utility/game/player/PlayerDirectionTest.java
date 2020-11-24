package utility.game.player;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlayerDirectionTest {

	@Test
	public void turnTest() {

		assertEquals(PlayerDirection.UP, PlayerDirection.LEFT.doAction(PlayerAction.TURN_RIGHT));

		assertEquals(PlayerDirection.LEFT, PlayerDirection.LEFT.doAction(PlayerAction.SPEED_UP));
		assertEquals(PlayerDirection.LEFT, PlayerDirection.LEFT.doAction(PlayerAction.SLOW_DOWN));
		assertEquals(PlayerDirection.LEFT, PlayerDirection.LEFT.doAction(PlayerAction.CHANGE_NOTHING));
		assertEquals(PlayerDirection.DOWN, PlayerDirection.LEFT.doAction(PlayerAction.TURN_LEFT));

		assertEquals(PlayerDirection.LEFT, PlayerDirection.UP.doAction(PlayerAction.TURN_LEFT));

	}

}
