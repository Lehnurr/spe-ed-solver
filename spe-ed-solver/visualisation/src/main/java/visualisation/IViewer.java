package visualisation;

import java.util.List;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.player.PlayerAction;
import utility.geometry.ContextualFloatMatrix;

public interface IViewer {

	/**
	 * Stores round specific information by submitting them to the {@link IViewer}.
	 * 
	 * @param availableMilliseconds available time in milliseconds
	 * @param performedAction       {@link PlayerAction} performed in the round
	 * @param requiredMilliseconds  required time in milliseconds
	 * @param board                 the board for the current {@link GameStep}
	 * @param boardRatings          {@link ContextualFloatMatrix}s of board ratings
	 */
	public void commitRound(double availableTime, PlayerAction performedAction, double requiredTime, Board<Cell> board,
			List<ContextualFloatMatrix> boardRatings);

}
