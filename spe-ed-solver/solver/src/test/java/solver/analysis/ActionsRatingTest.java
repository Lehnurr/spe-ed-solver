package solver.analysis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utility.game.player.PlayerAction;

public class ActionsRatingTest {

	@Test
	public void testDefaultValue() {
		ActionsRating actionsRating = new ActionsRating();
		assertEquals(0f, actionsRating.getRating(PlayerAction.CHANGE_NOTHING), 0.001f);
	}

	@Test
	public void testSetValue() {
		ActionsRating actionsRating = new ActionsRating();
		actionsRating.setRating(PlayerAction.CHANGE_NOTHING, 5.5f);
		assertEquals(5.5f, actionsRating.getRating(PlayerAction.CHANGE_NOTHING), 0.001f);
	}

	@Test
	public void testMaxAction() {
		ActionsRating actionsRating = new ActionsRating();
		actionsRating.setRating(PlayerAction.CHANGE_NOTHING, 5.5f);
		actionsRating.setRating(PlayerAction.SLOW_DOWN, 3.1f);
		assertEquals(PlayerAction.CHANGE_NOTHING, actionsRating.maxAction());
	}

	@Test
	public void testMaxRating() {
		ActionsRating actionsRating = new ActionsRating();
		actionsRating.setRating(PlayerAction.CHANGE_NOTHING, 5.5f);
		actionsRating.setRating(PlayerAction.SLOW_DOWN, 3.1f);
		assertEquals(5.5f, actionsRating.maxRating(), 0.001f);
	}

	@Test
	public void testCombine() {
		ActionsRating first = new ActionsRating();
		first.setRating(PlayerAction.CHANGE_NOTHING, 5f);
		first.setRating(PlayerAction.SLOW_DOWN, 10f);

		ActionsRating second = new ActionsRating();
		second.setRating(PlayerAction.CHANGE_NOTHING, 10f);
		second.setRating(PlayerAction.SLOW_DOWN, 5f);

		ActionsRating combined = first.combine(second, 0.5f);

		assertEquals(10f, combined.getRating(PlayerAction.CHANGE_NOTHING), 0.001f);
		assertEquals(12.5f, combined.getRating(PlayerAction.SLOW_DOWN), 0.001f);
	}

}
