package solver.analysis.slowdown;

import static org.junit.Assert.*;

import org.junit.Test;

import solver.MockPlayer;
import solver.analysis.ActionsRating;
import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

public class SlowDownTest {

	@Test
	public void testSlowDownActionsRating() {
		Board<Cell> board = createEmptyTestBoard();
		board.getBoardCellAt(new Point2i(6, 0)).setCellValue(CellValue.MULTIPLE_PLAYER);
		IPlayer player = new MockPlayer(0, PlayerDirection.DOWN, 2, new Point2i(5, 0), 0, true);

		ActionsRating result = SlowDown.getActionsRating(player, board);

		assertEquals(0f, result.getRating(PlayerAction.TURN_LEFT), 0.0001f);
		assertEquals(0.5f, result.getRating(PlayerAction.CHANGE_NOTHING), 0.0001f);
		assertEquals(1f, result.getRating(PlayerAction.SLOW_DOWN), 0.0001f);
		assertEquals(0f, result.getRating(PlayerAction.SPEED_UP), 0.0001f);
		assertEquals(0.5f, result.getRating(PlayerAction.TURN_RIGHT), 0.0001f);
	}

	private Board<Cell> createEmptyTestBoard() {
		Cell[][] cells = new Cell[10][10];
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[0].length; x++) {
				cells[y][x] = new Cell(0);
			}
		}
		return new Board<Cell>(cells);
	}

}
