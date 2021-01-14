package solver.analysis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utility.game.player.PlayerAction;

public class ActionsRatingTest {

	@Test
	public void testDefaultValue() {
		ActionsRating actionsRating = new ActionsRating();
		assertEquals(0, actionsRating.getRating(PlayerAction.CHANGE_NOTHING), 0.001);
	}

	@Test
	public void testSetValue() {
		ActionsRating actionsRating = new ActionsRating();
		actionsRating.setRating(PlayerAction.CHANGE_NOTHING, 5.5);
		assertEquals(5.5, actionsRating.getRating(PlayerAction.CHANGE_NOTHING), 0.001);
	}

	@Test
	public void testMaxAction() {
		ActionsRating actionsRating = new ActionsRating();
		actionsRating.setRating(PlayerAction.CHANGE_NOTHING, 5.5);
		actionsRating.setRating(PlayerAction.SLOW_DOWN, 3.1);
		assertEquals(PlayerAction.CHANGE_NOTHING, actionsRating.maxAction());
	}

	@Test
	public void testMaxRating() {
		ActionsRating actionsRating = new ActionsRating();
		actionsRating.setRating(PlayerAction.CHANGE_NOTHING, 5.5);
		actionsRating.setRating(PlayerAction.SLOW_DOWN, 3.1);
		assertEquals(5.5, actionsRating.maxRating(), 0.001);
	}

	@Test
	public void testCombine() {
		ActionsRating first = new ActionsRating();
		first.setRating(PlayerAction.CHANGE_NOTHING, 5);
		first.setRating(PlayerAction.SLOW_DOWN, 10);

		ActionsRating second = new ActionsRating();
		second.setRating(PlayerAction.CHANGE_NOTHING, 10);
		second.setRating(PlayerAction.SLOW_DOWN, 5);

		ActionsRating combined = first.combine(second, 0.5);

		assertEquals(10, combined.getRating(PlayerAction.CHANGE_NOTHING), 0.001);
		assertEquals(12.5, combined.getRating(PlayerAction.SLOW_DOWN), 0.001);
	}

}
