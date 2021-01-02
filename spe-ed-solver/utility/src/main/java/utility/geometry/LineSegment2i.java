package utility.geometry;

import java.util.Objects;

/**
 * A line segment is a part of a vertical or horizontal line that is bounded by
 * two distinct points
 */
public class LineSegment2i {
    private final Point2i pointA;
    private final Point2i pointB;

    /**
     * Initializes the segment by two given points
     * 
     * @param pointA The first point of the segment
     * @param pointB The second point of the segment
     * @throws IllegalArgumentException if the segment is not vertical or horizontal
     */
    public LineSegment2i(Point2i pointA, Point2i pointB) {
        this.pointA = pointA;
        this.pointB = pointB;

        if (pointA.getX() != pointB.getX() && pointA.getY() != pointB.getY()) {
            throw new IllegalArgumentException("A Line Segment must be vertical or horizontal. " + this + " Isn't");
        }
    }

    public Point2i getPointA() {
        return this.pointA;
    }

    public Point2i getPointB() {
        return this.pointB;
    }

    /**
     * Check for Intersection (Euclidean geometry) with the existing edges
     * 
     * @param other the other segment
     * @return true if the segments intersect, otherwise false
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
     * Checks if the segment contains a point
     * 
     * @param point The point to check
     * @return true if the segment contains the point
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
        } else if (getPointA().getY() != getPointB().getY()) {
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
     * Checks if the segment is veritcal
     * 
     * @return true if the segment is vertical, false if the segment is horizontal
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
