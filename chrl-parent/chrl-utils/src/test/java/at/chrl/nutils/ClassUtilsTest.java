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
import java.util.function.Function;

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
	
	public static final int TEST_SET_SIZE = 500;
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
	public void testContract() throws Exception {
		TestClass a = new TestClass(new Long(i));
		
		assertTrue(a.equals(a));
		assertTrue(a.hashCode() == a.hashCode());
		
		CollectionUtils.range(-TEST_SET_SIZE, TEST_SET_SIZE).forEach(j -> {
			
			TestClass b = new TestClass(new Long(j));
			
			assertTrue(b.equals(b));
			assertTrue(b.hashCode() == b.hashCode());
			
			assertTrue(error(i,j,a,b), (i.equals(j)) == (a.equals(b)));
			assertTrue(error(i,j,a,b), (i.equals(j)) == (b.equals(a)));
			assertTrue(error(i,j,a,b), (j.equals(i)) == (a.equals(b)));
			assertTrue(error(i,j,a,b), (j.equals(i)) == (b.equals(a)));
			assertTrue(error(i,j,a,b), (i.hashCode() == j.hashCode()) == (a.hashCode() == b.hashCode()));
			assertTrue(error(i,j,a,b), (i.equals(j)) == (a.hashCode() == b.hashCode()));
			assertTrue(error(i,j,a,b), (j.equals(i)) == (a.hashCode() == b.hashCode()));
			assertTrue(error(i,j,a,b), (i.hashCode() == j.hashCode()) == (a.equals(b)));
			assertTrue(error(i,j,a,b), (i.hashCode() == j.hashCode()) == (b.equals(a)));
		});
	}
	
	private static String error(Number i, Number j, TestClass a, TestClass b){
		StringBuilder sb = new StringBuilder();
		
		Function<TestClass, Long> idGetter = TestClass::getId;
		
		sb.append(System.lineSeparator())
			.append("i: ").append(i)
			.append(System.lineSeparator())
			.append("j: ").append(j)
			.append(System.lineSeparator())
			.append("a: ").append(a)
			.append(System.lineSeparator())
			.append("b: ").append(b)
			.append(System.lineSeparator())
			.append(System.lineSeparator())
			.append("i hash: ").append(i.hashCode())
			.append(System.lineSeparator())
			.append("j hash: ").append(j.hashCode())
			.append(System.lineSeparator())
			.append("a hash: ").append(a.hashCode())
			.append(System.lineSeparator())
			.append("b hash: ").append(b.hashCode())
			.append(System.lineSeparator())
			.append(System.lineSeparator())
			.append("i equals j: ").append(i.equals(j))
			.append(System.lineSeparator())
			.append("j equals i: ").append(j.equals(i))
			.append(System.lineSeparator())
			.append("a equals b: ").append(a.equals(b))
			.append(System.lineSeparator())
			.append("b equals a: ").append(b.equals(a))
			.append(System.lineSeparator())
			.append(System.lineSeparator())
			.append("a.getId: ").append(a.getId())
			.append(System.lineSeparator())
			.append("b.getId: ").append(b.getId())
			.append(System.lineSeparator())
			.append(System.lineSeparator())
			.append("TestClass::getId.apply(a): ").append(idGetter.apply(a))
			.append(System.lineSeparator())
			.append("TestClass::getId.apply(b): ").append(idGetter.apply(b))
			.append(System.lineSeparator())
			.append(System.lineSeparator());
		
		return sb.toString();
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
