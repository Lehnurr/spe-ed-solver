package utility.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerDirectionTest {
	
	@Test
	public void turnTest() {		
		
		assertEquals(PlayerDirection.LEFT.doTurn(PlayerAction.TURN_RIGHT), PlayerDirection.UP);
		
		assertEquals(PlayerDirection.LEFT.doTurn(PlayerAction.SPEED_UP), PlayerDirection.LEFT);
		assertEquals(PlayerDirection.LEFT.doTurn(PlayerAction.SLOW_DOWN), PlayerDirection.LEFT);
		assertEquals(PlayerDirection.LEFT.doTurn(PlayerAction.CHANGE_NOTHING), PlayerDirection.LEFT);
		assertEquals(PlayerDirection.LEFT.doTurn(PlayerAction.TURN_LEFT), PlayerDirection.DOWN);
		
		assertEquals(PlayerDirection.UP.doTurn(PlayerAction.TURN_LEFT), PlayerDirection.LEFT);
		
	}

}
