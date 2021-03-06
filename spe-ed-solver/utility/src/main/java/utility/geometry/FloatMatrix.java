package utility.geometry;

/**
 * A Matrix with double values.
 */
public class FloatMatrix {

	private final double[][] values;

	/**
	 * Generates a new 2d matrix with the given dimensions.
	 * 
	 * @param width  the width of the {@link FloatMatrix}
	 * @param height the height of the {@link FloatMatrix}
	 */
	public FloatMatrix(int width, int height) {
		this.values = new double[height][width];
	}

	/**
	 * Generates a new 2d matrix with the given dimensions and a default value for
	 * each element.
	 * 
	 * @param width        the width of the {@link FloatMatrix}
	 * @param height       the height of the {@link FloatMatrix}
	 * @param initialValue the initial value for all elements
	 */
	public FloatMatrix(int width, int height, double initialValue) {
		this(width, height);

		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				values[y][x] = initialValue;
			}
		}
	}

	/**
	 * Copy Constructor for duplicating the {@link FloatMatrix} by duplicating each
	 * value.
	 * 
	 * @param sourceMatrix the {@link FloatMatrix} to get the initial values from
	 */
	public FloatMatrix(final FloatMatrix sourceMatrix) {
		this(sourceMatrix.getWidth(), sourceMatrix.getHeight());
		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				values[y][x] = sourceMatrix.getValue(x, y);
			}
		}
	}

	/**
	 * Returns the width of the 2d matrix.
	 * 
	 * @return width of the matrix
	 */
	public int getWidth() {
		return values[0].length;
	}

	/**
	 * Returns the height of the 2d matrix.
	 * 
	 * @return height of the matrix
	 */
	public int getHeight() {
		return values.length;
	}

	/**
	 * Returns the double value stored at the given position.
	 * 
	 * @param point {@link Point2i} of the position
	 * @return double value at the given position
	 */
	public double getValue(Point2i point) {
		return getValue(point.getX(), point.getY());
	}

	/**
	 * Returns the double value stored at the given coordinates.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return double value at the given position
	 */
	public double getValue(int x, int y) {
		return values[y][x];
	}

	/**
	 * Changes the value at a given {@link Point2i}.
	 * 
	 * @param point the {@link Point2i position} to set the value for
	 * @param value the new value to set
	 */
	public void setValue(Point2i point, double value) {
		setValue(point.getX(), point.getY(), value);
	}

	/**
	 * Changes the value at the given coordinates.
	 * 
	 * @param x     the x coordinate
	 * @param y     thy y coordinates
	 * @param value the new value to set
	 */
	public void setValue(int x, int y, double value) {
		values[y][x] = value;
	}

	/**
	 * Compares to {@link FloatMatrix} and decides if they have the same size.
	 * 
	 * @param other the {@link FloatMatrix} to compare
	 * @return true if height and width of both matrices are matching.
	 */
	public boolean isSameSize(FloatMatrix other) {
		return this.getHeight() == other.getHeight() && this.getWidth() == other.getWidth();
	}

	/**
	 * Returns the highest value of the matrix.
	 * 
	 * @return highest double value in matrix
	 */
	public double max() {
		double maxValue = Float.MIN_VALUE;
		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				maxValue = Math.max(values[y][x], maxValue);
			}
		}
		return maxValue;
	}

	/**
	 * Builds a new {@link FloatMatrix} with the max value for each element.
	 * 
	 * @param other the other {@link FloatMatrix} to find the maximum value
	 * @return {@link FloatMatrix} of max values
	 */
	public FloatMatrix max(FloatMatrix other) {

		if (!isSameSize(other)) {
			throw new IllegalArgumentException("Both matrices need to have the same size");
		}

		FloatMatrix resultMatrix = new FloatMatrix(getWidth(), getHeight());

		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				resultMatrix.setValue(x, y, Math.max(getValue(x, y), other.getValue(x, y)));
			}
		}

		return resultMatrix;
	}

	/**
	 * Returns the lowest value of the matrix.
	 * 
	 * @return lowest double value in matrix
	 */
	public double min() {
		double minValue = Float.MAX_VALUE;
		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				minValue = Math.min(values[y][x], minValue);
			}
		}
		return minValue;
	}

	/**
	 * Builds a new {@link FloatMatrix} with the min value for each element.
	 * 
	 * @param other the other {@link FloatMatrix} to find the minimum value
	 * @return {@link FloatMatrix} of min values
	 */
	public FloatMatrix min(FloatMatrix other) {

		if (!isSameSize(other)) {
			throw new IllegalArgumentException("Both matrices need to have the same size");
		}

		FloatMatrix resultMatrix = new FloatMatrix(getWidth(), getHeight());

		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				resultMatrix.setValue(x, y, Math.min(getValue(x, y), other.getValue(x, y)));
			}
		}

		return resultMatrix;
	}

	/**
	 * Builds a new {@link FloatMatrix} with the multiplied value for each element.
	 * 
	 * @param other the other {@link FloatMatrix} to multiply
	 * @return {@link FloatMatrix} of products
	 */
	public FloatMatrix mul(FloatMatrix other) {

		if (!isSameSize(other)) {
			throw new IllegalArgumentException("Both matrices need to have the same size");
		}

		FloatMatrix resultMatrix = new FloatMatrix(getWidth(), getHeight());

		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				resultMatrix.setValue(x, y, getValue(x, y) * other.getValue(x, y));
			}
		}

		return resultMatrix;
	}

	/**
	 * Builds a new {@link FloatMatrix} with the summed up value for each element.
	 * 
	 * @param other the other {@link FloatMatrix} to add
	 * @return {@link FloatMatrix} of sums
	 */
	public FloatMatrix sum(FloatMatrix other) {

		if (!isSameSize(other)) {
			throw new IllegalArgumentException("Both matrices need to have the same size");
		}

		FloatMatrix resultMatrix = new FloatMatrix(getWidth(), getHeight());

		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				resultMatrix.setValue(x, y, getValue(x, y) + other.getValue(x, y));
			}
		}

		return resultMatrix;
	}

	/**
	 * Returns the sum of each element.
	 * 
	 * @return sum of each element
	 */
	public double sum() {
		double sum = 0;
		for (int y = 0; y < values.length; y++) {
			for (int x = 0; x < values[0].length; x++) {
				sum += getValue(x, y);
			}
		}
		return sum;
	}

	/**
	 * Adds a value to the matrix at a given position.
	 * 
	 * @param position {@link Point2i} of the position
	 * @param value    double value to add
	 */
	public void add(final Point2i position, final double value) {
		setValue(position, getValue(position) + value);
	}

	/**
	 * Replaces the existing value at a given position with the new one if the new
	 * value is smaller.
	 * 
	 * @param position {@link Point2i} of the position
	 * @param value    double value to compare and possibly set
	 */
	public void min(final Point2i position, final double value) {
		if (value < getValue(position)) {
			setValue(position, value);
		}
	}

	/**
	 * Replaces the existing value at a given position with the new one if the new
	 * value is bigger.
	 * 
	 * @param position {@link Point2i} of the position
	 * @param value    double value to compare and possibly set
	 */
	public void max(final Point2i position, final double value) {
		if (value > getValue(position)) {
			setValue(position, value);
		}
	}

	/**
	 * Builds a new normalized Matrix by dividing all values by the maximum value
	 * 
	 * @return The Matrix with normalized values
	 */
	public FloatMatrix normalize() {
		final double maxValue = max();

		final double normalizeFactor = 1 / maxValue;
		if (Double.isNaN(normalizeFactor))
			return this;

		final FloatMatrix factorMatrix = new FloatMatrix(this.getWidth(), this.getHeight(), normalizeFactor);

		return this.mul(factorMatrix);
	}

}
