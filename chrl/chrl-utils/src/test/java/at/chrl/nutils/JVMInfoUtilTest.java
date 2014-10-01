/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - JVMInfoUtilTest.java
 * Created: 23.07.2014 - 23:08:48
 */
package at.chrl.nutils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Vinzynth
 *
 */
public class JVMInfoUtilTest {

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
		JVMInfoUtil.printAllInfos(System.out);
	}

}
