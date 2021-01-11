package utility.game.board;

/**
 * Generalization for cells of a {@link Board}
 */
public interface IBoardCell<CellType> {

	/**
	 * @return The value of the {@link IBoardCell}
	 */
	CellType getCellValue();

	/**
	 * Sets the value of the {@link IBoardCell}
	 * 
	 * @param value the new value to set
	 */
	void setCellValue(CellType value);

	/**
	 * Checks if the {@link IBoardCell} is not in Use by any Player
	 * 
	 * @return true if the {@link IBoardCell} can be passed
	 */
	boolean isEmpty();
}
