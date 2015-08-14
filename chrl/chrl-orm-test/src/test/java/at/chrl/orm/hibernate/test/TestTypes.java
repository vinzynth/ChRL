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

import at.chrl.orm.hibernate.datatypes.ObjectMapKey;

/**
 * @author Vinzynth
 * 02.08.2015 - 00:11:57
 *
 */
public final class TestTypes {

	public static final ObjectMapKey<TestClass> TEST_2_CLASS_1 = new ObjectMapKey<>("testclass1", TestClass.class);
	public static final ObjectMapKey<TestClass> TEST_2_CLASS_2 = new ObjectMapKey<>("testclass2", TestClass.class);
	
}
