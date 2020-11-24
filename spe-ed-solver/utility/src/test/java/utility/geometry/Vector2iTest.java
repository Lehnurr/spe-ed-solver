package utility.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector2iTest {

	@Test
	public void multiplyTest() {
		Vector2i baseVector = new Vector2i(2, 7);
		Vector2i result = baseVector.multiply(3);

		assertEquals(6, result.getX());
		assertEquals(21, result.getY());
	}

}
