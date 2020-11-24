package utility.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FloatMatrixTest {

	private final static double TEST_DOUBLE_DELTA = 0.00001;

	@Test
	public void floatMatrixDimensionsTest() {
		FloatMatrix matrix = new FloatMatrix(10, 15);
		assertEquals(10, matrix.getWidth());
		assertEquals(15, matrix.getHeight());
	}

	@Test
	public void floatMatrixDefaultValueTest() {
		FloatMatrix matrix = new FloatMatrix(2, 2, 10f);
		assertEquals(10f, matrix.getValue(0, 0), TEST_DOUBLE_DELTA);
	}

	@Test
	public void floatMatrixMinMaxTest() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 0f);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0f);

		m1.setValue(6, 3, 5f);
		m1.setValue(2, 2, 5f);
		m2.setValue(6, 3, 7f);
		m2.setValue(2, 2, -1f);

		FloatMatrix m3 = m1.min(m2);
		assertEquals(5f, m3.getValue(6, 3), TEST_DOUBLE_DELTA);
		assertEquals(-1f, m3.getValue(2, 2), TEST_DOUBLE_DELTA);

		FloatMatrix m4 = m1.max(m2);
		assertEquals(7f, m4.getValue(6, 3), TEST_DOUBLE_DELTA);
		assertEquals(5f, m4.getValue(2, 2), TEST_DOUBLE_DELTA);
	}

	@Test
	public void floatMatrixMultiplyTest() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 0f);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0f);

		m1.setValue(6, 3, 5f);
		m2.setValue(6, 3, 7f);

		FloatMatrix m3 = m1.mul(m2);
		assertEquals(35f, m3.getValue(6, 3), TEST_DOUBLE_DELTA);
	}

	@Test
	public void floatMatrixAddTest() {
		FloatMatrix m1 = new FloatMatrix(10, 5, 0f);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0f);

		m1.setValue(6, 3, 5f);
		m2.setValue(6, 3, 7f);

		FloatMatrix m3 = m1.sum(m2);
		assertEquals(12f, m3.getValue(6, 3), TEST_DOUBLE_DELTA);
	}

}