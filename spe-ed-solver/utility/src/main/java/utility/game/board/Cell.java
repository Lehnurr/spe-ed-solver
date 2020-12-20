package utility.game.board;

/**
 * Cell
 */
public class Cell implements IBoardCell<CellValue> {

    private CellValue value;

    public Cell(int cellValue) {
        this(CellValue.fromInteger(cellValue));
    }
    
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

}
