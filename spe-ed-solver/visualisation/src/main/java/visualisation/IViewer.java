package visualisation;

import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.PlayerAction;
import utility.game.step.GameStep;
import utility.geometry.ContextualFloatMatrix;

/**
 * Generalization to be able to use the {@link Viewer active Viewer} and the
 * {@link InactiveViewer inactive Viewer} equally
 */
public interface IViewer {

	/**
	 * Stores round specific information by submitting them to the {@link IViewer}.
	 * 
	 * @param playerId        the id of the player who owns the window
	 * @param availableTime   available time in milliseconds
	 * @param performedAction {@link PlayerAction} performed in the round
	 * @param requiredTime    required time in milliseconds
	 * @param board           the {@link Board} for the current {@link GameStep}
	 * @param boardRatings    {@link ContextualFloatMatrix}s of board ratings
	 */
	public void commitRound(int playerId, double availableTime, PlayerAction performedAction, double requiredTime,
			Board<Cell> board, List<ContextualFloatMatrix> boardRatings);

}
