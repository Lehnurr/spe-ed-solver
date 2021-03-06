package visualisation;

import java.awt.image.BufferedImage;

import utility.game.board.Board;
import utility.game.board.Cell;
import utility.game.board.CellValue;
import utility.geometry.ContextualFloatMatrix;
import utility.geometry.Point2i;

/**
 * Class for generating {@link NamedImage NamedImages} from
 * {@link ContextualFloatMatrix Matrices} with a {@link ColorGradient}.
 */
public final class ImageGeneration {

	private static final String BOARD_IMAGE_NAME = "Board";

	private ImageGeneration() {
	}

	/**
	 * Generates a {@link BufferedImage} from a given {@link ContextualFloatMatrix}
	 * 
	 * @param matrix        {@link ContextualFloatMatrix} with the values
	 * @param colorGradient {@link ColorGradient} to transform matrix values into
	 *                      colors
	 * @return {@link NamedImage} as result
	 */
	public static NamedImage generateImageFromMatrix(final ContextualFloatMatrix matrix,
			final ColorGradient colorGradient) {

		final int height = matrix.getHeight();
		final int width = matrix.getWidth();

		final double rangeMax = matrix.getRangeMax();
		final double rangeMin = matrix.getRangeMin();
		final double stretchFactor = 1 / (rangeMax - rangeMin);

		final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final double value = (matrix.getValue(x, y) - rangeMin) * stretchFactor;
				bufferedImage.setRGB(x, y, colorGradient.apply(value));
			}
		}

		return new NamedImage(matrix.getName(), bufferedImage);
	}

	/**
	 * Generates a {@link BufferedImage} from a given {@link Board}.
	 * 
	 * @param board {@link Board} to generate the {@link BufferedImage} for
	 * @return {@link NamedImage} as result
	 */
	public static NamedImage generateImageFromBoard(final Board<Cell> board) {

		final int height = board.getHeight();
		final int width = board.getWidth();

		final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				final Point2i position = new Point2i(x, y);
				final CellValue cellValue = board.getBoardCellAt(position).getCellValue();
				final int rgbValue = cellValue.getRgbValue();
				bufferedImage.setRGB(x, y, rgbValue);
			}
		}

		return new NamedImage(BOARD_IMAGE_NAME, bufferedImage);
	}

}
