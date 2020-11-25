package utility.geometry;

public class Point2i {

	private final int x;
	private final int y;

	/**
	 * Generates a 2d Point.
	 * 
	 * @param x
	 * @param y
	 */
	public Point2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x value of the Point.
	 * 
	 * @return x value of the point
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y value of the Point.
	 * 
	 * @return x value of the point
	 */
	public int getY() {
		return y;
	}

	/**
	 * Calculates the manhattan distance between self and other {@link Point}
	 * 
	 * @param other
	 * @return value of manhattan distance
	 */
	public int manhattanDistance(Point2i other) {
		return Math.abs(x - other.getX()) + Math.abs(y - other.getY());
	}

	/**
	 * Calculates the new {@link Point2i} when translated with a {@link Vector2i}.
	 * 
	 * @param offsetVector
	 * @return new {@link Point2i} with adapted position
	 */
	public Point2i translate(Vector2i offsetVector) {
		return new Point2i(getX() + offsetVector.getX(), getY() + offsetVector.getY());
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + x;
		result = PRIME * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point2i other = (Point2i) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + " | " + y + ")";
	}
}
