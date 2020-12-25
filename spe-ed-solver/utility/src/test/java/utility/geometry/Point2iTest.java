package utility.geometry;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class Point2iTest {

	@Test
	public void testManhattenDistance() {
		Point2i p1 = new Point2i(0, 10);
		Point2i p2 = new Point2i(5, -1);

		int distanceP1P2 = p1.manhattanDistance(p2);
		int distanceP2P1 = p2.manhattanDistance(p1);

		assertEquals(16, distanceP1P2);
		assertEquals(16, distanceP2P1);
	}

	@Test
	public void testTranslate() {
		Point2i start = new Point2i(1, 7);
		Point2i endpoint = start.translate(new Vector2i(8, -5));
		assertEquals(new Point2i(9, 2), endpoint);
	}

	@Test
	public void testVonNeumannNeighborhood() {

		Point2i center = new Point2i(3, 2);
		List<Point2i> neighbors = center.vonNeumannNeighborhood();
		assertEquals("Von-Neumann-Neighborhood have to return 4 neighbors", 4, neighbors.size());
		assertTrue("west neighbor required", neighbors.contains(new Point2i(2, 2)));
		assertTrue("east neighbor required", neighbors.contains(new Point2i(4, 2)));
		assertTrue("norh neighbor required", neighbors.contains(new Point2i(3, 1)));
		assertTrue("south neighbor required", neighbors.contains(new Point2i(3, 3)));
	}

	@Test
	public void testMooreNeighborhood() {

		Point2i center = new Point2i(7, 0);
		List<Point2i> neighbors = center.mooreNeighborhood();
		assertEquals("Moore-Neighborhood have to return 8 neighbors", 8, neighbors.size());

		assertTrue("north-west neighbor required", neighbors.contains(new Point2i(6, -1)));
		assertTrue("nort neighbor required", neighbors.contains(new Point2i(7, -1)));
		assertTrue("north-east neighbor required", neighbors.contains(new Point2i(8, -1)));

		assertTrue("south-west neighbor required", neighbors.contains(new Point2i(6, 1)));
		assertTrue("south neighbor required", neighbors.contains(new Point2i(7, 1)));
		assertTrue("south-east neighbor required", neighbors.contains(new Point2i(8, 1)));

		assertTrue("west neighbor required", neighbors.contains(new Point2i(6, 0)));
		assertTrue("east neighbor required", neighbors.contains(new Point2i(8, 0)));
	}

	@Test
	public void testPointsInRectangle() {
		Point2i pointA = new Point2i(2, 3);
		Point2i pointB = new Point2i(3, 4);
		List<Point2i> containedPoints = pointA.pointsInRectangle(pointB);

		assertTrue(containedPoints.contains(new Point2i(2, 3)));
		assertTrue(containedPoints.contains(new Point2i(2, 4)));
		assertTrue(containedPoints.contains(new Point2i(3, 3)));
		assertTrue(containedPoints.contains(new Point2i(3, 4)));

	}

}
