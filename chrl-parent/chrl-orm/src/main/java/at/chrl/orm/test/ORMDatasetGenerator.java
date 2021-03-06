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
package at.chrl.orm.test;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

import at.chrl.nutils.DatasetGenerator;
import at.chrl.orm.hibernate.datatypes.MultiMap;
import at.chrl.orm.hibernate.datatypes.MultiMapEntry;
import at.chrl.orm.hibernate.datatypes.ObjectMap;
import at.chrl.orm.hibernate.datatypes.ObjectMapKey;

/**
 * @author Vinzynth
 * 09.08.2015 - 17:47:22
 *
 */
public final class ORMDatasetGenerator extends DatasetGenerator {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ORMDatasetGenerator() {
		addExclusion(GeneratedValue.class);
		addExclusion(Embeddable.class);
		addExclusion(MultiMapEntry.class);
		addExclusion(MultiMap.class);
		addExclusion(ObjectMap.class);
		addExclusion(ObjectMapKey.class);
	}
}
