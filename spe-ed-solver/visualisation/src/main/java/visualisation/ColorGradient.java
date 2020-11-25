package visualisation;

/**
 * Color Gradient for generating RGB values for float values between 0 and 1.
 */
public enum ColorGradient {

	/**
	 * Red dominant color gradient: black - red - orange - yellow - white
	 */
	FIRE {
		@Override
		protected int applyUnbounded(float value) {
			return (int) (0xFFFFFF * value);
		}
	};

	/**
	 * Internal abstract function for applying colors to normalized values between 0
	 * and 1.
	 * 
	 * @param value must be between 0 and 1
	 * @return RGB value as int (0 - 0xFFFFFF)
	 */
	protected abstract int applyUnbounded(final float value);
	
	/**
	 * Clipping value to range between 0 and 1 and applying RGB gradient transformation.
	 * 
	 * @param value
	 * @return RGB value as int (0 - 0xFFFFFF)
	 */
	public final int apply(final float value) {
		float boundedValue = Math.max(0, Math.min(1, value));
		return this.applyUnbounded(boundedValue);
	}
}
