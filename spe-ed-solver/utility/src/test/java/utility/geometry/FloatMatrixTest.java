package utility.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FloatMatrixTest {
	
	
	private final static double TEST_DOUBLE_DELTA = 0.00001;
	
	
	@Test
	public void floatMatrixDimensionsTest() {
		FloatMatrix matrix = new FloatMatrix(10, 15);
		assertEquals(matrix.getWidth(), 10);
		assertEquals(matrix.getHeight(), 15);
	}

	@Test
	public void floatMatrixDefaultValueTest() {
		FloatMatrix matrix = new FloatMatrix(2, 2, 10f);
		assertEquals(matrix.getValue(0, 0), 10f, TEST_DOUBLE_DELTA);
	}
	
	@Test
	public void floatMatrixMinMaxTest(){
		FloatMatrix m1 = new FloatMatrix(10, 5, 0f);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0f);
		
		m1.setValue(6, 3, 5f);
		m1.setValue(2, 2, 5f);
		m2.setValue(6, 3, 7f);
		m2.setValue(2, 2, -1f);
		
		FloatMatrix m3 = m1.min(m2);
		assertEquals(m3.getValue(6, 3), 5f, TEST_DOUBLE_DELTA);
		assertEquals(m3.getValue(2, 2), -1f, TEST_DOUBLE_DELTA);
		
		FloatMatrix m4 = m1.max(m2);
		assertEquals(m4.getValue(6, 3), 7f, TEST_DOUBLE_DELTA);
		assertEquals(m4.getValue(2, 2), 5f, TEST_DOUBLE_DELTA);
	}
	
	@Test
	public void floatMatrixMultiplyTest(){
		FloatMatrix m1 = new FloatMatrix(10, 5, 0f);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0f);
		
		m1.setValue(6, 3, 5f);
		m2.setValue(6, 3, 7f);
		
		FloatMatrix m3 = m1.mul(m2);
		assertEquals(m3.getValue(6, 3), 35f, TEST_DOUBLE_DELTA);
	}
	
	@Test
	public void floatMatrixAddTest(){
		FloatMatrix m1 = new FloatMatrix(10, 5, 0f);
		FloatMatrix m2 = new FloatMatrix(10, 5, 0f);
		
		m1.setValue(6, 3, 5f);
		m2.setValue(6, 3, 7f);
		
		FloatMatrix m3 = m1.sum(m2);
		assertEquals(m3.getValue(6, 3), 12f, TEST_DOUBLE_DELTA);
	}

}
