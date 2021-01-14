package utility.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FloatMatrixTest {

	private final static double TEST_DOUBLE_DELTA = 0.00001;

	@Test
	public void testFloatMatrixDimensions() {
		FloatMatrix matrix = new FloatMatrix(10, 15);
		assertEquals(10, matrix.getWidth());
		assertEquals(15, matrix.getHeight());
	}

	@Test
	public void testFloatMatrixDefaultValue() {
		FloatMatrix matrix = new FloatMatrix(2, 2, 10);
		assertEquals(10f, matrix.getValue(0, 0), TEST_DOUBLE_DELTA);
	}

	@Test
	public void testFloatMatrixMinMax() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 0);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0);

		m1.setValue(6, 3, 5);
		m1.setValue(2, 2, 5);
		m2.setValue(6, 3, 7);
		m2.setValue(2, 2, -1);

		FloatMatrix m3 = m1.min(m2);
		assertEquals(5, m3.getValue(6, 3), TEST_DOUBLE_DELTA);
		assertEquals(-1, m3.getValue(2, 2), TEST_DOUBLE_DELTA);

		FloatMatrix m4 = m1.max(m2);
		assertEquals(7, m4.getValue(6, 3), TEST_DOUBLE_DELTA);
		assertEquals(5, m4.getValue(2, 2), TEST_DOUBLE_DELTA);
	}

	@Test
	public void testFloatMatrixMultiply() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 0);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0);

		m1.setValue(6, 3, 5);
		m2.setValue(6, 3, 7);

		FloatMatrix m3 = m1.mul(m2);
		assertEquals(35, m3.getValue(6, 3), TEST_DOUBLE_DELTA);
	}

	@Test
	public void testFloatMatrixAdd() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 0);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0);

		m1.setValue(6, 3, 5);
		m2.setValue(6, 3, 7);

		FloatMatrix m3 = m1.sum(m2);
		assertEquals(12, m3.getValue(6, 3), TEST_DOUBLE_DELTA);
	}

	@Test
	public void testFloatMatrixAddPoint() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 0);

		m1.add(new Point2i(0, 1), 5);

		assertEquals(5, m1.getValue(0, 1), TEST_DOUBLE_DELTA);
	}

	@Test
	public void testFloatMatrixMinPoint() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 10);

		m1.min(new Point2i(0, 1), 5);
		m1.min(new Point2i(1, 1), 15);

		assertEquals(5, m1.getValue(0, 1), TEST_DOUBLE_DELTA);
		assertEquals(10, m1.getValue(1, 1), TEST_DOUBLE_DELTA);
	}

	@Test
	public void testFloatMatrixMaxPoint() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 10);

		m1.max(new Point2i(0, 1), 5);
		m1.max(new Point2i(1, 1), 15);

		assertEquals(10, m1.getValue(0, 1), TEST_DOUBLE_DELTA);
		assertEquals(15, m1.getValue(1, 1), TEST_DOUBLE_DELTA);
	}

	@Test
	public void testFloatMatrixSum() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 10);
		assertEquals(500, m1.sum(), TEST_DOUBLE_DELTA);
	}

}
