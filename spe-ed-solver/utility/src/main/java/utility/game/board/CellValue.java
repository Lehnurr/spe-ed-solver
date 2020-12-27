package utility.game.board;

import utility.logging.ApplicationLogger;

/**
 * A possible Cell-Value for the Game (PlayerId, 0 or -1)
 */
public enum CellValue {
	MULTIPLE_PLAYER(-1), EMPTY_CELL(0), PLAYER_ONE(1), PLAYER_TWO(2), PLAYER_THREE(3), PLAYER_FOUR(4), PLAYER_FIVE(5),
	PLAYER_SIX(6);

	private final int value;

	private CellValue(int value) {
		this.value = value;
	}

	/**
	 * Returns the Cells value
	 */
	public int getIntegerValue() {
		return this.value;
	}

	public static CellValue fromInteger(int intValue) {
		if (intValue > 6 || intValue < -1) {
			ApplicationLogger
					.logAndThrowException(new IllegalArgumentException("Cell value " + intValue + " does not exist!"));
		}
		return CellValue.values()[intValue + 1];
	}
}
