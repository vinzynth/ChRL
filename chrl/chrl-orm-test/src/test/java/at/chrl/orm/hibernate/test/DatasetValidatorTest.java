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
package at.chrl.orm.hibernate.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import at.chrl.nutils.CollectionUtils;
import at.chrl.orm.hibernate.datatypes.MultiMapEntry;
import at.chrl.orm.test.validator.DatamodelValidator;

/**
 * @author Vinzynth
 * 09.08.2015 - 19:30:10
 *
 */
public class DatasetValidatorTest {

	
	private DatamodelValidator validator;
	
	@Before
	public void initValidator(){
		List<Class<?>> testClasses = CollectionUtils.newList();
		
		testClasses.add(TestClass.class);
		testClasses.add(Test2Class.class);
		testClasses.add(TestEnum.class);
		testClasses.add(MultiMapEntry.class);
		
		this.validator = new DatamodelValidator(testClasses);
	}
	
	@Test
	public void testConnection() throws Exception {
		validator.testConnection();
	}
	
	@Test
	public void testSessionPersist() throws Exception {
		validator.testSessionPersist();
	}
	
	@Test
	public void testPersist() throws Exception {
		validator.testPersist();
	}
	
	
}
