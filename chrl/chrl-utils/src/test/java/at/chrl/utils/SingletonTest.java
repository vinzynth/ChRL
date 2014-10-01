/**
 * (C) ChRL 2013 - chrl-utils - at.chrl.utils - SingletonTest.java
 * Created: 30.12.2013 - 15:43:22
 */
package at.chrl.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import at.chrl.utils.patterns.SingletonHolder;


/**
 * @author Vinzynth
 *
 */
public class SingletonTest {

	@Test
	public void test() {
		assertTrue(SingletonHolder.getInstance(Singleton.class).i == SingletonHolder.getInstance(Singleton.class).i);
	}
}
