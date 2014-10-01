/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - ArrayTransformerTest.java
 * Created: 10.08.2014 - 16:34:56
 */
package at.chrl.nutils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.chrl.nutils.configuration.transformers.ArrayTransformer;

/**
 * @author Vinzynth
 *
 */
public class ArrayTransformerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String test = "{2,4,5}";
		assertTrue(ArrayTransformer.validBrackets(test));

		test = "{2,4,5}{2,5}";
		assertTrue(ArrayTransformer.validBrackets(test));

		test = "{2,4,5}, {5.5,3}";
		assertTrue(ArrayTransformer.validBrackets(test));
		
		test = "   {2,4,5}, {5.5,3}   ";
		assertTrue(ArrayTransformer.validBrackets(test));
		
		test = "{2,4,5}, {5.5,3} , {324,325261436,79673335436588956789456u2fc46e4t43646}";
		assertTrue(ArrayTransformer.validBrackets(test));
		

		test = "2,4,5}, {5.5,3}";
		try {
			ArrayTransformer.validBrackets(test);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		
		test = "2,4,5 {5.5,3}";
		try {
			ArrayTransformer.validBrackets(test);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		test = "{2,4,5 {5.5,3}";
		try {
			ArrayTransformer.validBrackets(test);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		test = "{2,4,5{ {5.5,3}";
		try {
			ArrayTransformer.validBrackets(test);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		test = "{2,4,5,{ },5.5,3}";
		try {
			ArrayTransformer.validBrackets(test);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

}
