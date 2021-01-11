package utility.game.board;

/**
 * Base implementation of a {@link IBoardCell}
 */
public class Cell implements IBoardCell<CellValue> {

	private CellValue value;

	/**
	 * Initializes a new {@link Cell} with the given value.
	 * 
	 * @param cellValue the initial value of the {@link Cell}
	 */
	public Cell(int cellValue) {
		this(CellValue.fromInteger(cellValue));
	}

	/**
	 * Initializes a new {@link Cell} with the given value.
	 * 
	 * @param value the initial value of the {@link Cell}
	 */
	public Cell(CellValue value) {
		this.value = value;
	}

	@Override
	public CellValue getCellValue() {
		return this.value;
	}

	@Override
	public void setCellValue(CellValue value) {
		this.value = value;
	}

	/**
	 * Sets the value of the {@link Cell}
	 * 
	 * @param value the new value to set
	 */
	public void setCellValue(int value) {
		this.value = CellValue.fromInteger(value);
	}

	@Override
	public boolean isEmpty() {
		return value == CellValue.EMPTY_CELL;
	}

	@Override
	public String toString() {
		return Integer.toString(getCellValue().getIntegerValue());
	}

}
