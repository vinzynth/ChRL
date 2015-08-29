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
package at.chrl.vaadin.component.generator.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.data.Validatable;
import com.vaadin.data.Validator;

/**
 * Reqiures the given ComponentType instance of {@link Validatable}
 * 
 * @author Vinzynth
 * 29.08.2015 - 02:19:05
 *
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentValidator {
	Class<? extends Validator> value();
}
