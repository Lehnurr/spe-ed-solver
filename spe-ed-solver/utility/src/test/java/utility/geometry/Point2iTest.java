package utility.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Point2iTest {

	@Test
	public void manhattenDistanceTest() {
		Point2i p1 = new Point2i(0, 10);
		Point2i p2 = new Point2i(5, -1);
		
		int distanceP1P2 = p1.manhattanDistance(p2);
		int distanceP2P1 = p2.manhattanDistance(p1);
		
		assertEquals(distanceP1P2, 16);
		assertEquals(distanceP2P1, 16);
	}
	
	@Test
	public void translateTest() {
		Point2i start = new Point2i(1, 7);
		Point2i endpoint = start.translate(new Vector2i(8, -5));
		assertEquals(endpoint, new Point2i(9, 2));
	}

}
