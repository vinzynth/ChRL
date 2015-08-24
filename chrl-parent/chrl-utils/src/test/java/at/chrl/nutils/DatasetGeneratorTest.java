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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Vinzynth
 * 24.08.2015 - 01:54:28
 *
 */
@RunWith(Parameterized.class)
public class DatasetGeneratorTest {

	private static final int TEST_SET_SIZE = 10;
	
	@Parameters(name = "Class: {0}")
	public static Collection<Class<?>> data(){
		return new DatasetGenerator().getSupportedTypes();
	}
	
	private DatasetGenerator gen;
	private Class<?> cls;

	/**
	 * 
	 */
	public DatasetGeneratorTest(Class<?> cls) {
		this.cls = cls;
	}
	
	@Before
	public void setUp(){
		gen = new DatasetGenerator();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGenerateIterator() throws Exception {
		List instances = CollectionUtils.newList(); 
		gen.generateIterable(cls, TEST_SET_SIZE).forEachRemaining(instances::add);
		assertTrue(instances.size() == TEST_SET_SIZE);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGenerateIteratorAndExclude() throws Exception {
		List instances = CollectionUtils.newList(); 
		gen.generateIterable(cls, TEST_SET_SIZE).forEachRemaining(instances::add);
		assertTrue(instances.size() == TEST_SET_SIZE);

		gen.addExclusion(cls);
		
		instances.clear();
		gen.generateIterable(cls, TEST_SET_SIZE).forEachRemaining(instances::add);
		assertTrue(instances.size() == 0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGenerateIteratorAndExcludeAndClearExclude() throws Exception {
		List instances = CollectionUtils.newList(); 
		gen.generateIterable(cls, TEST_SET_SIZE).forEachRemaining(instances::add);
		assertTrue(instances.size() == TEST_SET_SIZE);

		gen.addExclusion(cls);
		
		instances.clear();
		gen.generateIterable(cls, TEST_SET_SIZE).forEachRemaining(instances::add);
		assertTrue(instances.size() == 0);
		
		instances.clear();
		gen.clearExclusions();
		gen.generateIterable(cls, TEST_SET_SIZE).forEachRemaining(instances::add);
		assertTrue(instances.size() == TEST_SET_SIZE);		
	}
	
	@Test
	public void testGenerateSingleton() throws Exception {
		assertNotNull(gen.generate(cls));
	}
	
	@Test
	public void testGenerateSingletonAndExclude() throws Exception {
		assertNotNull(gen.generate(cls));
		gen.addExclusion(cls);
		assertNull(gen.generate(cls));
	}
	
	@Test
	public void testGenerateSingletonAndExcludeAndClearExcludes() throws Exception {
		assertNotNull(gen.generate(cls));
		gen.addExclusion(cls);
		assertNull(gen.generate(cls));
		gen.clearExclusions();
		assertNotNull(gen.generate(cls));
	}
	
	@Test
	public void testGenerate() throws Exception {
		assertTrue(gen.generate(cls, TEST_SET_SIZE).toArray().length == TEST_SET_SIZE);
	}
	
	@Test
	public void testGenerateAndExclude() throws Exception {
		assertTrue(gen.generate(cls, TEST_SET_SIZE).toArray().length == TEST_SET_SIZE);
		gen.addExclusion(cls);
		assertTrue(gen.generate(cls, TEST_SET_SIZE).toArray().length == 0);
	}
	
	@Test
	public void testGenerateAndExcludeAndClearExcludes() throws Exception {
		assertTrue(gen.generate(cls, TEST_SET_SIZE).toArray().length == TEST_SET_SIZE);
		gen.addExclusion(cls);
		assertTrue(gen.generate(cls, TEST_SET_SIZE).toArray().length == 0);
		gen.clearExclusions();
		assertTrue(gen.generate(cls, TEST_SET_SIZE).toArray().length == TEST_SET_SIZE);
	}
}
