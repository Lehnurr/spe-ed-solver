package utility.geometry;

/**
 * Wrapper for {@link FloatMatrix} containing name describing its contents.
 */
public class ContextualFloatMatrix {

	private final String name;

	private final FloatMatrix matrix;

	private final double rangeMin;
	private final double rangeMax;

	/**
	 * Generates a {@link ContextualFloatMatrix} only with its name. Range values
	 * are determined by the range of the input {@link FloatMatrix}.
	 * 
	 * @param name   the describing name of the matrix
	 * @param matrix the represented {@link FloatMatrix}
	 */
	public ContextualFloatMatrix(final String name, final FloatMatrix matrix) {
		this(name, matrix, matrix.min(), matrix.max());
	}

	/**
	 * Generates a {@link ContextualFloatMatrix} with name and range values.
	 * 
	 * @param name     the describing name of the matrix
	 * @param matrix   the represented {@link FloatMatrix}
	 * @param rangeMin the minimum meaningful value
	 * @param rangeMax the maximum meaningful value
	 */
	public ContextualFloatMatrix(final String name, final FloatMatrix matrix, final double rangeMin,
			final double rangeMax) {
		this.name = name;
		this.matrix = new FloatMatrix(matrix);
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
	}

	/**
	 * Responsible for delivering the name of the matrix.
	 * 
	 * @return initiated name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value of the underlying matrix at the given position.
	 * 
	 * @param point the {@link Point2i position} to get the value from
	 * @return double value at the given coordinates
	 */
	public double getValue(Point2i point) {
		return matrix.getValue(point);
	}

	/**
	 * Returns the value of the underlying matrix at the given position.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return double value at the given coordinates
	 */
	public double getValue(int x, int y) {
		return matrix.getValue(x, y);
	}

	/**
	 * Returns the width of the underlying {@link FloatMatrix}.
	 * 
	 * @return width of the matrix
	 */
	public int getWidth() {
		return matrix.getWidth();
	}

	/**
	 * Returns the height of the underlying {@link FloatMatrix}.
	 * 
	 * @return height of the matrix
	 */
	public int getHeight() {
		return matrix.getHeight();
	}

	/**
	 * Returns the maximum valid value for the matrix.
	 * 
	 * @return max valid value for the matrix
	 */
	public double getRangeMax() {
		return rangeMax;
	}

	/**
	 * Returns the minimum valid value for the matrix.
	 * 
	 * @return min valid value for the matrix
	 */
	public double getRangeMin() {
		return rangeMin;
	}

}
