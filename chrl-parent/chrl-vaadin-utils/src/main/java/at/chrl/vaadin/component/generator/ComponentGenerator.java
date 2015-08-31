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
package at.chrl.vaadin.component.generator;

/**
 * @author Vinzynth
 * 29.08.2015 - 02:12:11
 *
 * <p>
 * Default implementation:
 * {@link ComponentGeneratorImpl} 
 * 
 * @see ComponentGeneratorImpl
 *
 */
public interface ComponentGenerator {

	public <T> GeneratedAbstractField<T> generate(Class<T> cls);
	
	@SuppressWarnings("unchecked")
	public default <T> GeneratedAbstractField<T> generate(T o){
		GeneratedAbstractField<T> generate = (GeneratedAbstractField<T>) generate(o.getClass());
		generate.setValue(o);
		return generate;
	}
	
}
