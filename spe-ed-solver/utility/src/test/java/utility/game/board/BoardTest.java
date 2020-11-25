package utility.game.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utility.geometry.Point2i;

public class BoardTest {

    @Test
    public void boardTest() {
        // Test Data: Cells
        Cell[][] cells = { { new Cell(0), new Cell(0), new Cell(0) }, { new Cell(0), new Cell(0), new Cell(0) },
                { new Cell(0), new Cell(0), new Cell(0) }, { new Cell(0), new Cell(0), new Cell(0) } };
        // Test Data: Board
        Board<Cell> board = new Board<Cell>(cells);

        // Check for same object Reference
        assertSame(cells[1][2], board.getBoardCellAt(new Point2i(2, 1)));

        // check for not fount Cell
        assertEquals(null, board.getBoardCellAt(new Point2i(4, 2)));

        // Check Border-Points
        assertTrue(null, board.isOnBoard(new Point2i(1, 0)));
        assertTrue(null, board.isOnBoard(new Point2i(2, 3)));
        assertTrue(null, board.isOnBoard(new Point2i(0, 2)));
        assertTrue(null, board.isOnBoard(new Point2i(2, 1)));

        // Check Inner-Field Points
        assertTrue(null, board.isOnBoard(new Point2i(1, 2)));

        // Check outer rim Points
        assertFalse(null, board.isOnBoard(new Point2i(-1, 0)));
        assertFalse(null, board.isOnBoard(new Point2i(0, -1)));
        assertFalse(null, board.isOnBoard(new Point2i(5, 0)));
        assertFalse(null, board.isOnBoard(new Point2i(0, 10)));
    }

}
