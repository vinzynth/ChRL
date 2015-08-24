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

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Vinzynth
 * 24.08.2015 - 02:33:12
 *
 */
@RunWith(Parameterized.class)
public class LoremIpsumTest {

	public static final int TEST_SET_SIZE = 1000;
	
	@Parameters(name = "i: {0}")
	public static Collection<Integer> data(){
		return CollectionUtils.range(-TEST_SET_SIZE, TEST_SET_SIZE);
	}
	
	private int length;

	/**
	 * 
	 */
	public LoremIpsumTest(int length) {
		this.length = length;
	}
	

	@Test
	public void testGetRandWithParam() throws Exception {
		Assert.assertNotNull(LoremIpsum.getRand(length));
	}
}
