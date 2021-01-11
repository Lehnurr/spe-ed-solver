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
	 * 
	 * @param value the new value to set
	 */
	void setCellValue(CellType value);

	/**
	 * Checks if the Cell is not in Use by any Player
	 * 
	 * @return true if the cell can be passed
	 */
	boolean isEmpty();
}
