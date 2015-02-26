/**
 * This file is part of aion-lightning <aion-lightning.org>.
 * 
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.rebellion.classlistener;

/**
 * This interface implements listener that is called post class load/before
 * class unload.<br>
 * 
 * @author SoulKeeper
 */
public interface ClassListener {

	/**
	 * This method is invoked after classes were loaded.
	 * 
	 * @param classes
	 *            all loaded classes by script context
	 */
	public void postLoad(Class<?>[] classes);

	/**
	 * This method is invoked before class unloading. As argument are passes all
	 * loaded classes
	 * 
	 * @param classes
	 *            all loaded classes (they are going to be unloaded) by script
	 *            context
	 */
	public void preUnload(Class<?>[] classes);
}
