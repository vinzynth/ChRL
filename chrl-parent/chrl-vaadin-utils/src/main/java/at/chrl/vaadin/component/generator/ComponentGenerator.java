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
	
	/**
	 * col must not be empty! use {@link ComponentGenerator#generateGrid(Class, Collection)} instead.
	 * @param col
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public default <T> GeneratedAbstractGrid<T> generateGrid(Collection<T> col, boolean readOnly){
		T t = col.stream().findFirst().orElse(null);
		if(Objects.isNull(t))
			return null;
		GeneratedAbstractGrid<T> generatedAbstractGrid = new GeneratedAbstractGrid<T>((Class<T>) t.getClass(), readOnly);
		generatedAbstractGrid.getContainer().addAll(col);
		return generatedAbstractGrid;
	}
	
	public default <T> GeneratedAbstractGrid<T> generateGrid(Class<T> cls, Collection<T> col, boolean readOnly){
		GeneratedAbstractGrid<T> generatedAbstractGrid = new GeneratedAbstractGrid<T>(cls, readOnly);
		generatedAbstractGrid.getContainer().addAll(col);
		return generatedAbstractGrid;
	}
	
	public default <T> GeneratedAbstractGrid<T> generateGrid(Class<T> cls, boolean readOnly){
		return new GeneratedAbstractGrid<>(cls, readOnly);
	}
	
	public default <T> GeneratedAbstractGrid<T> generateGrid(Collection<T> col){
		return generateGrid(col, false);
	}
	
	public default <T> GeneratedAbstractGrid<T> generateGrid(Class<T> cls, Collection<T> col){
		return generateGrid(cls, col, false);
	}
	
	public default <T> GeneratedAbstractGrid<T> generateGrid(Class<T> cls){
		return generateGrid(cls, false);
	}
}
