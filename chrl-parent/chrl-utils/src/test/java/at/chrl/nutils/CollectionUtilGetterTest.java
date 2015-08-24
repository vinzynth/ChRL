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

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Vinzynth
 * 24.08.2015 - 01:28:57
 *
 */
@RunWith(Parameterized.class)
public class CollectionUtilGetterTest {
	
	private Object col;

	@Parameters()
	public static Collection<Object> data(){
		Collection<Object> returnMe = new ArrayList<>();
		returnMe.add(CollectionUtils.newList());
		returnMe.add(CollectionUtils.newList(10));
		returnMe.add(CollectionUtils.newSet());
		returnMe.add(CollectionUtils.newSet(10));
		returnMe.add(CollectionUtils.newConcurrentMap());
		returnMe.add(CollectionUtils.newMap());
		returnMe.add(CollectionUtils.newMap(10));
		return returnMe;
	}
	
	/**
	 * 
	 */
	public CollectionUtilGetterTest(Object col) {
		this.col = col;
	}
	
	@Test
	public void test() {
		assertNotNull(col);
	}
	
}
