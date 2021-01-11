package utility.geometry;

import java.util.Objects;

/**
 * A {@link LineSegment2i Line segment} is a part of a vertical or horizontal
 * line that is bounded by two distinct {@link Point2i points}.
 */
public class LineSegment2i {
    private final Point2i pointA;
    private final Point2i pointB;

    /**
     * Initializes the {@link LineSegment2i segment} by two given {@link Point2i
     * points}.
     * 
     * @param pointA the first {@link Point2i point} of the {@link LineSegment2i
     *               segment}
     * @param pointB the second {@link Point2i point} of the {@link LineSegment2i
     *               segment}
     * @throws IllegalArgumentException if the {@link LineSegment2i segment} is not
     *                                  vertical or horizontal
     */
    public LineSegment2i(Point2i pointA, Point2i pointB) {
        this.pointA = pointA;
        this.pointB = pointB;

        if (pointA.getX() != pointB.getX() && pointA.getY() != pointB.getY()) {
            throw new IllegalArgumentException("A Line Segment must be vertical or horizontal. " + this + " Isn't");
        }
    }

    /**
     * The first {@link Point2i} of the {@link LineSegment2i segment}.
     * 
     * @return a {@link Point2i} that limits the {@link LineSegment2i segment}
     */
    public Point2i getPointA() {
        return this.pointA;
    }

    /**
     * The second {@link Point2i} of the {@link LineSegment2i segment}.
     * 
     * @return a {@link Point2i} that limits the {@link LineSegment2i segment}
     */
    public Point2i getPointB() {
        return this.pointB;
    }

    /**
     * Check for Intersection (Euclidean geometry) with another {@link LineSegment2i
     * segment}.
     * 
     * @param other the other {@link LineSegment2i segment}
     * @return true if the {@link LineSegment2i segment segments} intersect,
     *         otherwise false
     */
    public boolean intersect(LineSegment2i other) {
        if (this.isVertical() == other.isVertical()) {
            // both vertical or horizontal
            // intersects if one segment contains the start or endpoint of the other segment
            return this.contains(other.pointA) || this.contains(other.pointB);
        }

        // one vertical, one horizontal
        LineSegment2i vertical;
        LineSegment2i horizontal;
        if (this.isVertical()) {
            vertical = this;
            horizontal = other;
        } else {
            vertical = other;
            horizontal = this;
        }

        boolean horizontalIsLeftFromVertical = horizontal.pointA.getX() <= vertical.pointA.getX()
                || horizontal.pointB.getX() <= vertical.pointA.getX();

        boolean horizontalIsRightFromVertical = horizontal.pointA.getX() >= vertical.pointA.getX()
                || horizontal.pointB.getX() >= vertical.pointA.getX();

        boolean verticalIsTopFromHorizontal = vertical.pointA.getY() >= horizontal.pointA.getY()
                || vertical.pointB.getY() >= horizontal.pointA.getY();

        boolean verticalIsDownFromHoriozontal = vertical.pointA.getY() <= horizontal.pointA.getY()
                || vertical.pointB.getY() <= horizontal.pointA.getY();

        return horizontalIsLeftFromVertical && horizontalIsRightFromVertical && verticalIsTopFromHorizontal
                && verticalIsDownFromHoriozontal;
    }

    /**
     * Checks if the {@link LineSegment2i segment} contains a {@link Point2i}.
     * 
     * @param point the {@link Point2i} to check
     * @return true if the {@link LineSegment2i segment} contains the
     *         {@link Point2i}
     */
    public boolean contains(Point2i point) {
        int aValue;
        int bValue;
        int pValue;
        if (getPointA().getX() == point.getX()) {
            // x value is the same, y value of the point must be between pointA and pointB
            aValue = getPointA().getY();
            bValue = getPointB().getY();
            pValue = point.getY();
        } else if (getPointA().getY() != point.getY()) {
            // y value is the same, x value of the point must be between pointA and pointB
            aValue = getPointA().getX();
            bValue = getPointB().getX();
            pValue = point.getX();
        } else
            return false;

        // check if the value of the point that is different is between pointA and
        // pointB
        return (aValue <= pValue && bValue >= pValue) || (aValue >= pValue && bValue <= pValue);
    }

    /**
     * Checks if the {@link LineSegment2i segment} is veritcal
     * 
     * @return true if the {@link LineSegment2i segment} is vertical, false if the
     *         {@link LineSegment2i segment} is horizontal
     */
    public boolean isVertical() {
        return this.pointA.getY() == this.pointB.getY();
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", pointA, pointB);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        LineSegment2i other = (LineSegment2i) o;
        if (Objects.equals(pointA, other.pointA) && Objects.equals(pointB, other.pointB))
            return true;
        if (Objects.equals(pointA, other.pointB) && Objects.equals(pointB, other.pointA))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointA, pointB);
    }

}
