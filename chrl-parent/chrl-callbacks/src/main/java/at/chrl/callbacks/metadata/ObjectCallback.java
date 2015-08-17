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
package at.chrl.callbacks.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import at.chrl.callbacks.Callback;

/**
 * Annotation that is used to mark enhanceable methods or classes.<br>
 * <b>Static, native and abstract methods are not allowed</b>
 *
 *
 * @author SoulKeeper
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface ObjectCallback {

	/**
	 * Returns callback class that will be used as listener
	 *
	 * @return callback class that will be used as listener
	 */
	Class<? extends Callback> value();
}