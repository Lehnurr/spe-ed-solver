package utility.game.board;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import utility.geometry.Point2i;

public class BoardTest {

    private Cell[][] cells;
    private Board<Cell> board;

    @Before
    public void initialize() {
        // Test Data: Cells
        this.cells = new Cell[][] { { new Cell(0), new Cell(0), new Cell(0) },
                { new Cell(0), new Cell(0), new Cell(0) }, { new Cell(0), new Cell(0), new Cell(0) },
                { new Cell(0), new Cell(0), new Cell(0) } };
        // Test Data: Board
        this.board = new Board<Cell>(cells);
    }

    @Test
    public void testGetCell() {

        // Check for same object Reference
        assertSame(cells[1][2], board.getBoardCellAt(new Point2i(2, 1)));

        // check for not found Cell
        assertNull(board.getBoardCellAt(new Point2i(4, 2)));
    }

    @Test
    public void testIsOnBoard() {

        // Check Border-Points
        assertTrue(board.isOnBoard(new Point2i(1, 0)));
        assertTrue(board.isOnBoard(new Point2i(2, 3)));
        assertTrue(board.isOnBoard(new Point2i(0, 2)));
        assertTrue(board.isOnBoard(new Point2i(2, 1)));

        // Check Inner-Field Points
        assertTrue(board.isOnBoard(new Point2i(1, 2)));

        // Check outer rim Points
        assertFalse(board.isOnBoard(new Point2i(-1, 0)));
        assertFalse(board.isOnBoard(new Point2i(0, -1)));
        assertFalse(board.isOnBoard(new Point2i(5, 0)));
        assertFalse(board.isOnBoard(new Point2i(0, 10)));
    }
}
