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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Vinzynth
 * 24.08.2015 - 01:31:35
 *
 */
@RunWith(Parameterized.class)
public class CollectionUtilChoppedTest {
	
	private static final int TEST_SET_SIZE = 1_000;
	
	private List<String> data;
	private int length;
	
	@Parameters
	public static Collection<Integer> data() {
		Collection<Integer> lengths = new ArrayList<>(TEST_SET_SIZE);
		for (int i = 0; i <= TEST_SET_SIZE; i++) {
			lengths.add(i);
		}
		return lengths;
	}
	
	/**
	 * 
	 */
	public CollectionUtilChoppedTest(int length) {
		this.length = length;
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		data = new ArrayList<>(TEST_SET_SIZE);
		
		for (int i = 0; i < TEST_SET_SIZE; i++) {
			data.add(Rnd.nextString());
		}
	}


	@Test(timeout = 1000)
	public void testChopped() {
		List<String> choppedAndMerged = CollectionUtils.chopped(data, length).stream()
			.flatMap(List::stream).collect(Collectors.toList());
		
		data.removeAll(choppedAndMerged);
		
		Assert.assertTrue(data.isEmpty());
	}

	@Test(timeout = 1000)
	public void testCapacity() {
		int capacity = CollectionUtils.capacity(length);
		
		Assert.assertTrue(capacity > 0);
		Assert.assertTrue(capacity >= length);
	}
}
