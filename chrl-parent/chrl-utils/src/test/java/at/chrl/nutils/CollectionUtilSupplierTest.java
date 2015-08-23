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
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Vinzynth
 * 24.08.2015 - 01:22:26
 *
 */
@RunWith(Parameterized.class)
public class CollectionUtilSupplierTest {

	@Parameters
	public static Collection<Supplier<?>> data(){
		Collection<Supplier<?>> returnMe = new ArrayList<>();
		returnMe.add(CollectionUtils.getConcurrentMapSupplier());
		returnMe.add(CollectionUtils.getDequeSupplier());
		returnMe.add(CollectionUtils.getListSupplier());
		returnMe.add(CollectionUtils.getListSupplier(10));
		returnMe.add(CollectionUtils.getMapSupplier());
		returnMe.add(CollectionUtils.getMapSupplier(10));
		returnMe.add(CollectionUtils.getQueueSupplier());
		returnMe.add(CollectionUtils.getSetSupplier());
		returnMe.add(CollectionUtils.getSetSupplier(10));
		returnMe.add(CollectionUtils.getWeakMapSupplier());
		return returnMe;
	}
	

	private Supplier<?> sup;

	/**
	 * 
	 */
	public CollectionUtilSupplierTest(Supplier<?> sup) {
		this.sup = sup;
	}
	
	@Test
	public void testSupplier() {
		Assert.assertNotNull(sup.get());
	}
}
