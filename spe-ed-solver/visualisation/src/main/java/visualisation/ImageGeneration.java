package visualisation;

import java.awt.image.BufferedImage;

import utility.geometry.ContextualFloatMatrix;

/**
 * Class for generating {@link NamedImage NamedImages} from
 * {@link ContextualFloatMatrix Matrices} with a {@link ColorGradient}.
 */
public class ImageGeneration {

	/**
	 * Generates a {@link BufferedImage} from a given {@link ContextualFloatMatrix}
	 * 
	 * @param matrix
	 * @param colorGradient
	 * @return
	 */
	public static NamedImage generateImageFromMatrix(final ContextualFloatMatrix matrix,
			final ColorGradient colorGradient) {

		final int height = matrix.getHeight();
		final int width = matrix.getWidth();

		final float rangeMax = matrix.getRangeMax();
		final float rangeMin = matrix.getRangeMin();
		final float stretchFactor = 1 / (rangeMax - rangeMin);

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float value = (matrix.getValue(x, y) - rangeMin) * stretchFactor;
				bufferedImage.setRGB(x, y, colorGradient.apply(value));
			}
		}

		NamedImage result = new NamedImage(matrix.getName(), bufferedImage);
		return result;
	}

}
