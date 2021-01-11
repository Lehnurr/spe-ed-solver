package utility.geometry;

/**
 * 2d vector with int values.
 */
public final class Vector2i {

	// size of the vector in each dimension
	private final int x;
	private final int y;

	/**
	 * Generates a new vector.
	 * 
	 * @param x the x element
	 * @param y the y element
	 */
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the size of the vector in the x dimension.
	 * 
	 * @return size of the vector in the x dimension
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the size of the vector in the y dimension.
	 * 
	 * @return size of the vector in the y dimension
	 */
	public int getY() {
		return y;
	}

	/**
	 * Multiplies the sizes of the vector in each dimension by the given factor
	 * 
	 * @param factor factor to multiply with
	 * @return new vector as result
	 */
	public Vector2i multiply(int factor) {
		return new Vector2i(x * factor, y * factor);
	}

	@Override
	/**
	 * Representation as row vector
	 */
	public String toString() {
		return "(" + x + " , " + y + ")";
	}

}
