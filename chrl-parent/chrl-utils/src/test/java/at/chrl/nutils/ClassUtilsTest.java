/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or
 * modify  it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ChRL Util Collection.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.nutils;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Vinzynth
 * 26.08.2015 - 21:21:52
 *
 */
@RunWith(Parameterized.class)
public class ClassUtilsTest {
	
	public static final int TEST_SET_SIZE = 10;
	private Integer i;
	
	@Parameters(name = "i: {0}")
	public static Collection<Integer> data(){
		return CollectionUtils.range(-TEST_SET_SIZE, TEST_SET_SIZE);
	}
	
	/**
	 * 
	 */
	public ClassUtilsTest(Integer i) {
		this.i = i;
	}
	
	@Test
	public void test() throws Exception {
		TestClass a = new TestClass(new Long(i));
		
		assertTrue(a.equals(a));
		assertTrue(a.hashCode() == a.hashCode());
		
		CollectionUtils.range(-TEST_SET_SIZE, TEST_SET_SIZE).forEach(j -> {
			
			TestClass b = new TestClass(new Long(j));
			
			assertTrue(b.equals(b));
			assertTrue(b.hashCode() == b.hashCode());
			
			assertTrue("i: " + i + " | j: " + j + " | i.equals(j): " + Boolean.toString(i.equals(j)) + " | a: " + a + " | b: " + b + " | a.equals(b): " + Boolean.toString(a.equals(b)),
					(i.equals(j)) == (a.equals(b)));
			assertTrue("i: " + i + " | j: " + j + " | i.equals(j): " + Boolean.toString(i.equals(j)) + " | a: " + a + " | b: " + b + " | b.equals(a): " + Boolean.toString(b.equals(a)),
					(i.equals(j)) == (b.equals(a)));
			assertTrue("i: " + i + " | j: " + j + " | i.equals(j): " + Boolean.toString(j.equals(i)) + " | a: " + a + " | b: " + b + " | a.equals(b): " + Boolean.toString(a.equals(b)),
					(j.equals(i)) == (a.equals(b)));
			assertTrue("i: " + i + " | j: " + j + " | i.equals(j): " + Boolean.toString(j.equals(i)) + " | a: " + a + " | b: " + b + " | b.equals(a): " + Boolean.toString(b.equals(a)),
					(j.equals(i)) == (b.equals(a)));
			assertTrue("i: " + i + " | j: " + j + " | a: " + a + " | b: " + b + " | i hash: " + i.hashCode() + " | j hash: " + j.hashCode() + " | a hash: " + a.hashCode() + " | b hash: " + b.hashCode() + " | a.hashCode == b.hashCode: " + Boolean.toString(a.hashCode() == b.hashCode()),
					(i.equals(j)) == (a.hashCode() == b.hashCode()));
		});
	}
	
	
	
	public static class TestClass {
		
		/**
		 * 
		 */
		public TestClass(Long id) {
			this.id = id;
		}
		
		private Long id;
		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * {@inheritDoc}
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			return ClassUtils.equals(this, obj, TestClass::getId);
		}
		
		/**
		 * {@inheritDoc}
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return ClassUtils.hashCode(this, TestClass::getId);
		}
		
		
		/**
		 * {@inheritDoc}
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return ClassUtils.getString(this, TestClass::getId);
		}
	}
}
