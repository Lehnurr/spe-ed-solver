package utility.game.board;

import utility.geometry.Point2i;

/**
 * Board
 */
public class Board<CellType extends IBoardCell> {

    private final CellType[][] cells;
    private final int height;
    private final int width;

    public Board(CellType[][] cells) {
        this.cells = cells;
        this.height = cells.length;
        this.width = cells[0].length;
    }

    public CellType getBoardCellAt(Point2i position) {
        if (isOnBoard(position))
            return cells[position.getY()][position.getX()];
        else
            return null;
    }

    public boolean isOnBoard(Point2i position) {
        return 0 <= position.getX() && position.getX() < this.width && 0 <= position.getY()
                && position.getY() < this.height;
    }

}
