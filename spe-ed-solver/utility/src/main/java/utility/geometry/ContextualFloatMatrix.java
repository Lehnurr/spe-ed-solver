package utility.geometry;

/**
 * Wrapper for {@link FloatMatrix} containing name describing its contents.
 */
public class ContextualFloatMatrix {

	// name describing the matrix content
	private final String name;

	// copy of the actual matrix with its content
	private final FloatMatrix matrix;

	// range in which the element values reside
	private final float rangeMin;
	private final float rangeMax;

	/**
	 * Generates a {@link ContextualFloatMatrix} only with its name. Range values
	 * are determined by the range of the input {@link FloatMatrix}.
	 * 
	 * @param name
	 * @param matrix
	 */
	public ContextualFloatMatrix(final String name, final FloatMatrix matrix) {
		this(name, matrix, matrix.min(), matrix.max());
	}

	/**
	 * Generates a {@link ContextualFloatMatrix} with name and range values.
	 * 
	 * @param name
	 * @param matrix
	 * @param rangeMin
	 * @param rangeMax
	 */
	public ContextualFloatMatrix(final String name, final FloatMatrix matrix, final float rangeMin,
			final float rangeMax) {
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
	 * @param point
	 * @return float value at the given coordinates
	 */
	public float getValue(Point2i point) {
		return matrix.getValue(point);
	}

	/**
	 * Returns the maximum valid value for the matrix.
	 * 
	 * @return max valid value for the matrix
	 */
	public float getRangeMax() {
		return rangeMax;
	}

	/**
	 * Returns the minimum valid value for the matrix.
	 * 
	 * @return min valid value for the matrix
	 */
	public float getRangeMin() {
		return rangeMin;
	}

}
