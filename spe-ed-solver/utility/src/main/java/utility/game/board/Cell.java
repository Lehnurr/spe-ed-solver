package utility.game.board;

/**
 * Cell
 */
public class Cell implements IBoardCell {

    private int value;

    public Cell(int cellValue) {
        this.value = cellValue;
    }

    public int getCellValue() {
        return value;
    }

    public void setCellValue(int value) {
        this.value = value;
    }

}
