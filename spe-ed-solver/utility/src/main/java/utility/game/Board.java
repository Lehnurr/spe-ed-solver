package utility.game;

/**
 * Board
 */
public class Board<CellType extends IBoardCell> {

    private CellType[][] cells;

    public Board(CellType[][] cells) {
        this.cells = cells;
    }

    public CellType getBoardCellAt(BoardPosition position) {
        return cells[position.getY()][position.getX()];
    }

}
