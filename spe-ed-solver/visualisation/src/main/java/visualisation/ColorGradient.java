package visualisation;

import java.awt.Color;

/**
 * Color Gradient for generating RGB values for float values between 0 and 1.
 */
public enum ColorGradient {

	/**
	 * Red dominant color gradient: black - red - orange - yellow - white.
	 */
	FIRE {
		@Override
		protected int applyUnbounded(final float value) {
			final int rValue = (int) (0xFF * Math.max(0, Math.min(1 / 3f, value)) * 3);
			final int gValue = (int) (0xFF * Math.max(0, Math.min(1 / 3f, value - 0.33)) * 3);
			final int bValue = (int) (0xFF * Math.max(0, Math.min(1 / 3f, value - 0.66)) * 3);
			final int rgbValue = (rValue << 16) | (gValue << 8) | bValue;
			return rgbValue;
		}
	},
	/**
	 * Gradient at the outer edge of the color circle.
	 */
	RAINBOW {
		@Override
		protected int applyUnbounded(final float value) {
			return Color.HSBtoRGB(value, 1, 1);
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
	 * Clipping value to range between 0 and 1 and applying RGB gradient
	 * transformation.
	 * 
	 * @param value the float value to get the RGB value for
	 * @return RGB value as int (0 - 0xFFFFFF)
	 */
	public final int apply(final float value) {
		float boundedValue = Math.max(0, Math.min(1, value));
		return this.applyUnbounded(boundedValue);
	}
}
