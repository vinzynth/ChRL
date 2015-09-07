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

import java.util.Collection;
import java.util.Objects;

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

	public <T> GeneratedAbstractField<T> generate(Class<T> cls, boolean readOnly);
	
	@SuppressWarnings("unchecked")
	public default <T> GeneratedAbstractField<T> generate(T o, boolean readOnly){
		GeneratedAbstractField<T> generate = (GeneratedAbstractField<T>) generate(o.getClass(), readOnly);
		generate.forceSetValue(o);
		return generate;
	}
	
	public default <T> GeneratedAbstractField<T> generate(Class<T> cls){
		return generate(cls, false);
	}
	
	public default <T> GeneratedAbstractField<T> generate(T o){
		return generate(o, false);
	}
	
	@SuppressWarnings("unchecked")
	public default <T> GeneratedAbstractGrid<T> generateGrid(Collection<T> col){
		T t = col.stream().findFirst().orElse(null);
		if(Objects.isNull(t))
			return null;
		GeneratedAbstractGrid<T> generatedAbstractGrid = new GeneratedAbstractGrid<T>((Class<T>) t.getClass());
		generatedAbstractGrid.getContainer().addAll(col);
		return generatedAbstractGrid;
	}
	
	public default <T> GeneratedAbstractGrid<T> generateGrid(Class<T> cls){
		return new GeneratedAbstractGrid<>(cls);
	}
}
