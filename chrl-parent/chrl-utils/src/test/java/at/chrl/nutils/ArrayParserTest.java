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
 * 24.08.2015 - 00:43:50
 *
 */
@RunWith(Parameterized.class)
public class ArrayParserTest {
	
	public static final int TEST_SET_SIZE = 500;

	@Parameters
	public static Collection<Short[]> data() {
		
		Collection<Short[]> returnMe = CollectionUtils.newList();
		for (int j = 0; j < TEST_SET_SIZE; j++) {
			Short[] testData = new Short[5];
			
			for (int i = 0; i < 5; i++) {
				testData[i] = Rnd.nextShort();
			}
			
			returnMe.add(testData);
		}
		return returnMe;
	}
	
	Short[] sh;
	
	/**
	 * 
	 */
	public ArrayParserTest(Short s1, Short s2, Short s3, Short s4, Short s5) {
		sh = new Short[5];
		sh[0] = s1;
		sh[1] = s2;
		sh[2] = s3;
		sh[3] = s4;
		sh[4] = s5;
	}
	

	@Test
	public void testToString() {
		String[] str = ArrayParser.parseToStringArray(sh);
		short[] shs = ArrayParser.parseToShortArray(str);
		str = ArrayParser.parseToStringArray(shs);
		Short[] sh2 = ArrayParser.parseToShortObjectArray(str);
		
		Assert.assertArrayEquals(sh, sh2);
	}

	
	@Test
	public void testToLong() {
		String[] str = ArrayParser.parseToStringArray(sh);
		long[] shs = ArrayParser.parseToLongArray(str);
		str = ArrayParser.parseToStringArray(shs);
		Long[] sho = ArrayParser.parseToLongObjectArray(str);
		str = ArrayParser.parseToStringArray(sho);
		Short[] sh2 = ArrayParser.parseToShortObjectArray(str);
		
		Assert.assertArrayEquals(sh, sh2);
	}
	
	@Test
	public void testToInteger() {
		String[] str = ArrayParser.parseToStringArray(sh);
		int[] shs = ArrayParser.parseToIntArray(str);
		str = ArrayParser.parseToStringArray(shs);
		Integer[] sho = ArrayParser.parseToIntegerArray(str);
		str = ArrayParser.parseToStringArray(sho);
		Short[] sh2 = ArrayParser.parseToShortObjectArray(str);
		
		Assert.assertArrayEquals(sh, sh2);
	}
}
