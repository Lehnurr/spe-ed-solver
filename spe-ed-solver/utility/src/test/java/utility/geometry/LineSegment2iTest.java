package utility.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineSegment2iTest {

    @Test
    public void testContains() {
        Point2i pointA = new Point2i(1, 1);
        Point2i pointB = new Point2i(1, 5);
        LineSegment2i segmentA = new LineSegment2i(pointA, pointB);

        Point2i pointC = new Point2i(1, 3);
        Point2i pointD = new Point2i(0, 1);
        Point2i pointE = new Point2i(1, -1);
        Point2i pointF = new Point2i(1, 6);

        assertTrue(segmentA.contains(pointA));
        assertTrue(segmentA.contains(pointB));
        assertTrue(segmentA.contains(pointC));
        assertFalse(segmentA.contains(pointD));
        assertFalse(segmentA.contains(pointE));
        assertFalse(segmentA.contains(pointF));
    }

    @Test
    public void testIsVertical() {
        Point2i pointA = new Point2i(1, 1);
        Point2i pointB = new Point2i(1, 5);
        LineSegment2i segmentA = new LineSegment2i(pointA, pointB);
        assertFalse(segmentA.isVertical());

        Point2i pointC = new Point2i(4, 2);
        Point2i pointD = new Point2i(3, 2);
        LineSegment2i segmentB = new LineSegment2i(pointC, pointD);
        assertTrue(segmentB.isVertical());
    }

    @Test
    public void testEquals() {
        Point2i pointA = new Point2i(1, 1);
        Point2i pointB = new Point2i(1, 5);
        LineSegment2i segmentA = new LineSegment2i(pointA, pointB);
        LineSegment2i segmentB = new LineSegment2i(pointA, pointB);
        LineSegment2i segmentC = new LineSegment2i(pointB, pointA);
        Point2i pointC = new Point2i(3, 1);
        LineSegment2i segmentD = new LineSegment2i(pointA, pointC);

        assertEquals(segmentA, segmentA);
        assertEquals(segmentA, segmentB);
        assertEquals(segmentA, segmentC);
        assertNotEquals(segmentA, segmentD);
        assertNotEquals(segmentC, segmentD);
    }
}
