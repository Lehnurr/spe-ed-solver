package utility.game.board;

import utility.logging.ApplicationLogger;

/**
 * A possible Cell-Value for the Game (PlayerId, 0 or -1)
 */
public enum CellValue {
	MULTIPLE_PLAYER(-1, 0xFFFFFF), EMPTY_CELL(0, 0x000000), PLAYER_ONE(1, 0xFF0000), PLAYER_TWO(2, 0x00FF00),
	PLAYER_THREE(3, 0x0000FF), PLAYER_FOUR(4, 0xFFFF00), PLAYER_FIVE(5, 0x00FFFF), PLAYER_SIX(6, 0xFF00FF);

	private final int integerValue;
	private final int rgbValue;

	private CellValue(final int integerValue, final int rgbValue) {
		this.integerValue = integerValue;
		this.rgbValue = rgbValue;
	}

	/**
	 * Returns the unique integer value for the {@link CellValue}.
	 * 
	 * @return the integer value of the {@link CellValue}
	 */
	public int getIntegerValue() {
		return this.integerValue;
	}

	/**
	 * Returns a unique RGB integer value for the {@link CellValue}.
	 * 
	 * @return the RGB value of the {@link CellValue}
	 */
	public int getRgbValue() {
		return this.rgbValue;
	}

	/**
	 * Returns the {@link CellValue} for a given integer value. If no
	 * {@link CellValue} was found a {@link IllegalArgumentException} is thrown.
	 * 
	 * @param intValue integer value
	 * @return {@link CellValue}
	 */
	public static CellValue fromInteger(final int intValue) {
		if (intValue > 6 || intValue < -1) {
			ApplicationLogger
					.logAndThrowException(new IllegalArgumentException("Cell value " + intValue + " does not exist!"));
		}
		return CellValue.values()[intValue + 1];
	}
}
