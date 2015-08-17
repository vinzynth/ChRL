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
package at.chrl.orm.hibernate.datatypes;

import java.util.Objects;

import javax.persistence.Embeddable;

/**
 * @author Vinzynth
 * 01.08.2015 - 21:41:59
 * @param <T>
 *
 */
@Embeddable
public class ObjectMapKey<T> implements Comparable<ObjectMapKey<T>>{

	private Class<?> typeClass;
	private String mapKey;
	
	/**
	 * Hiberante needs me
	 */
	@SuppressWarnings("unused")
	private ObjectMapKey() {
	}
	
	/**
	 * 
	 * @param key
	 * @param type
	 */
	public ObjectMapKey(String key, Class<T> type){
		this.mapKey = key;
		this.typeClass = type;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getType() {
		return (Class<T>) typeClass;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setType(Class<T> type) {
		this.typeClass = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return mapKey;
	}
	
	/**
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.mapKey = key;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"typeClass\": \"" + typeClass.getSimpleName() + "\";  \"mapKey\": \"" + mapKey + "\"}";
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj))
			return false;
		if(!(obj instanceof ObjectMapKey))
			return false;
		
		@SuppressWarnings("rawtypes")
		ObjectMapKey o = (ObjectMapKey) obj;
		
		return this.typeClass.equals(o.typeClass) && this.mapKey.equals(o.mapKey);
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int h = this.typeClass.hashCode() * 31;
		return h + this.mapKey.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(@SuppressWarnings("rawtypes") ObjectMapKey o) {
		int c = this.typeClass.getSimpleName().compareTo(o.typeClass.getSimpleName());
		if(c != 0)
			return c;
		return this.mapKey.compareTo(o.mapKey);
	}
}
