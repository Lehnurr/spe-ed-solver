package solver.analysis.slowdown;

import solver.analysis.ActionsRating;
import solver.analysis.PredictivePlayer;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;

/**
 * Class to calculate a {@link ActionsRating} which prioritizes
 * {@link PlayerAction PlayerActions} with a lower speed.
 */
public final class SlowDown {

	private SlowDown() {
	}

	/**
	 * Calculates the {@link ActionsRating} for the given {@link IPlayer} on the
	 * given {@link Board}. The {@link ActionsRating} is favoring
	 * {@link PlayerAction actions} slower speeds.
	 * 
	 * @param self  {@link IPlayer} to test for
	 * @param board {@link Board} to check for collisions
	 * @return result as {@link ActionsRating}
	 */
	public static ActionsRating getActionsRating(final IPlayer self, final Board<Cell> board) {

		final PredictivePlayer startPlayer = new PredictivePlayer(self);
		final ActionsRating result = new ActionsRating();

		for (final PlayerAction action : PlayerAction.values()) {
			final PredictivePlayer nextPlayer = new PredictivePlayer(startPlayer, action, board);
			final float ratingValue = ratingValue(startPlayer, nextPlayer);
			result.setRating(action, ratingValue);
		}
		return result;
	}

	/**
	 * Calculates the single rating value for two {@link IPlayer players}. One has
	 * to be the parent, one the child.<br>
	 * Calculation: 0 (speed up), 0.5 (keeping speed), 1 (slow down)
	 * 
	 * @param parent {@link IPlayer} parent
	 * @param child  {@link IPlayer} child
	 * @return rating value result as float
	 */
	private static float ratingValue(final IPlayer parent, final IPlayer child) {
		if (child.isActive())
			return (parent.getSpeed() - child.getSpeed()) / 2f + 0.5f;
		else
			return 0;
	}

}
