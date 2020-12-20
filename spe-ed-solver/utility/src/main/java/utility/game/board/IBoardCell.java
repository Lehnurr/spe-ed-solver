package utility.game.board;

/**
 * IBoardCell
 */
public interface IBoardCell<CellType> {

    /**
     * @return The Cells value
     */
	CellType getCellValue();

    /**
     * Sets the Cells value
     */
    void setCellValue(CellType value);
}
