package player.analysis;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.game.player.IPlayer;
import utility.game.player.PlayerAction;
import utility.game.player.PlayerDirection;
import utility.geometry.Point2i;

public class PredictivePlayerTest {
	
	@Test
	public void testInvalidParent() {
		Board<Cell> board = createEmptyTestBoard();
		board.getBoardCellAt(new Point2i(5, 6)).setCellValue(CellValue.PLAYER_FIVE);
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, false);
		PredictivePlayer startPlayer = new PredictivePlayer(player);

		PredictivePlayer nextPlayer = new PredictivePlayer(startPlayer, PlayerAction.TURN_LEFT, board);

		assertEquals(false, nextPlayer.isActive());
	}
	
	@Test
	public void testSelfCollision() {
		Board<Cell> board = createEmptyTestBoard();
		board.getBoardCellAt(new Point2i(5, 6)).setCellValue(CellValue.PLAYER_FIVE);
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		PredictivePlayer startPlayer = new PredictivePlayer(player);

		PredictivePlayer nextPlayer0 = new PredictivePlayer(startPlayer, PlayerAction.TURN_LEFT, board);
		PredictivePlayer nextPlayer1 = new PredictivePlayer(nextPlayer0, PlayerAction.TURN_LEFT, board);
		PredictivePlayer nextPlayer2 = new PredictivePlayer(nextPlayer1, PlayerAction.TURN_LEFT, board);
		PredictivePlayer nextPlayer3 = new PredictivePlayer(nextPlayer2, PlayerAction.TURN_LEFT, board);
		PredictivePlayer nextPlayer4 = new PredictivePlayer(nextPlayer3, PlayerAction.TURN_LEFT, board);

		assertEquals(false, nextPlayer4.isActive());
	}
	
	@Test
	public void testBoardCollision() {
		Board<Cell> board = createEmptyTestBoard();
		board.getBoardCellAt(new Point2i(5, 4)).setCellValue(CellValue.PLAYER_FIVE);
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		PredictivePlayer startPlayer = new PredictivePlayer(player);

		PredictivePlayer nextPlayer = new PredictivePlayer(startPlayer, PlayerAction.SLOW_DOWN, board);

		assertEquals(false, nextPlayer.isActive());
	}

	@Test
	public void testChildSpeed() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		PredictivePlayer startPlayer = new PredictivePlayer(player);

		PredictivePlayer nextPlayer = new PredictivePlayer(startPlayer, PlayerAction.SLOW_DOWN, board);

		assertEquals(true, nextPlayer.isActive());
		assertEquals(1, nextPlayer.getSpeed());
	}

	@Test
	public void testChildPosition() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		PredictivePlayer startPlayer = new PredictivePlayer(player);

		PredictivePlayer nextPlayer = new PredictivePlayer(startPlayer, PlayerAction.SPEED_UP, board);

		assertEquals(new Point2i(5, 2), nextPlayer.getPosition());
	}

	@Test
	public void testChildShortTail() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 2, new Point2i(5, 5), 0, true);
		PredictivePlayer startPlayer = new PredictivePlayer(player);

		PredictivePlayer nextPlayer = new PredictivePlayer(startPlayer, PlayerAction.SPEED_UP, board);

		assertEquals(new HashSet<Point2i>(Arrays.asList(new Point2i(5, 4), new Point2i(5, 3), new Point2i(5, 2))),
				new HashSet<Point2i>(nextPlayer.getShortTail()));
	}

	@Test
	public void testChildShortTailJump() {
		Board<Cell> board = createEmptyTestBoard();
		IPlayer player = new TestPlayer(0, PlayerDirection.UP, 4, new Point2i(5, 5), 5, true);
		PredictivePlayer startPlayer = new PredictivePlayer(player);

		PredictivePlayer nextPlayer = new PredictivePlayer(startPlayer, PlayerAction.TURN_LEFT, board);

		assertEquals(new HashSet<Point2i>(Arrays.asList(new Point2i(4, 5), new Point2i(1, 5))),
				new HashSet<Point2i>(nextPlayer.getShortTail()));
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
