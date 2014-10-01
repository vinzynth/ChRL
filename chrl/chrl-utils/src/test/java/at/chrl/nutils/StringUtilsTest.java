/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - StringUtilsTest.java
 * Created: 23.07.2014 - 23:27:56
 */
package at.chrl.nutils;

import static at.chrl.utils.StringUtils.*;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vinzynth
 *
 */
public class StringUtilsTest {

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
	public void testTrim() {
		String test = "  test     ";
		assertTrue(test.trim().equals(trim(' ', test)));
	}
	
	@Test
	public void testTrim2() {
		String test = "//test/////";
		assertTrue(trim('/', test).equals("test"));
		assertTrue(trim(test, '/').equals("test"));
	}
	
	@Test
	public void testTrim3() {
		String test = "/...//./test/..//....//";
		assertTrue(trim(test, '/', '.').equals("test"));
	}

	@Test
	public void testTrim4() {
		String test = "testtesttest";
		assertTrue(trimAmount(test, 4).equals("test"));
	}
		
	@Test
	public void testTrimAround() {
		String test = "/...//./test/..//....//";
		assertTrue(trimAround(test, "\\.", '/').equals("test"));
		assertTrue(trimAround(test, "test", '/').equals("...//...//...."));
	}

	@Test
	public void testLineBreaks() {
		String test = "aiglhiohgoierhgüh24tuzüßa0fgh2outzeügärhebft3r32prgweuizftr3wrg";
		int lsLength = System.lineSeparator().length();
		assertTrue((test.length() + test.length()/3*lsLength-lsLength) == insertLineBreaks(test, 3).length());
		assertTrue((test.length() + test.length()/4*lsLength) == insertLineBreaks(test, 4).length());
		assertTrue((test.length() + test.length()/5*lsLength) == insertLineBreaks(test, 5).length());
		assertTrue((test.length() + test.length()/10*lsLength) == insertLineBreaks(test, 10).length());
		assertTrue((test.length() == insertLineBreaks(test, test.length()).length()));
		assertTrue((test.length() == insertLineBreaks(test, test.length() + 3).length()));
	}
	
	@Test
	public void testMatchCount(){
		assertTrue(countMatches("192.168.0.1", '.') == 3);
		assertTrue(countMatches("192.168.0.1", '1') == 3);
		assertTrue(countMatches("192.168.0.1", '0') == 1);
		assertTrue(countMatches("192.168.0.1", 'x') == 0);
		assertTrue(countMatches("192.168.0.1", "192") == 1);
		assertTrue(countMatches("192.168.0.1", "1922") == 0);
	}

}
