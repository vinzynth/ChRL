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

import java.io.Serializable;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import at.chrl.nutils.CollectionUtils;
import at.chrl.nutils.interfaces.INestedMap;
import at.chrl.orm.hibernate.SessionTemplate;

/**
 * @author Vinzynth
 * 01.08.2015 - 21:31:28
 *
 */
@Embeddable
public class ObjectMap<K extends Serializable> implements INestedMap<Map<ObjectMapKey<?>, K>, ObjectMapKey<?> ,K>{
	
	/**
	 * Map with given key/primary key pairs
	 */
	@ElementCollection
	private Map<ObjectMapKey<?>, K> dataset;
	
	/**
	 * Default constructor
	 */
	public ObjectMap() {
		dataset = CollectionUtils.newMap();
	}
	
	/**
	 * constructor with initialize capacity
	 * @param initCapacity
	 */
	public ObjectMap(int initCapacity) {
		dataset = CollectionUtils.newMap(initCapacity);
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.nutils.interfaces.INestedMap#getNestedMap()
	 */
	@Override
	public Map<ObjectMapKey<?>, K> getNestedMap() {
		return dataset;
	}
	

	public <T extends ObjectMapable<K>> T put(ObjectMapKey<T> key , T value) {
		getNestedMap().put(key, value.getId());
		return value;
	}

	/**
	 * Returns persisted object instance by given {@link EntityManager}
	 * 
	 * @param em
	 * @param key
	 * @return persisted object instance in this map by key
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(EntityManager em, ObjectMapKey<T> key){
		return (T) em.find(key.getClass(), get(key));
	}

	/**
	 * Returns persisted object instance by fiven {@link Session}
	 * 
	 * @param session
	 * @param key
	 * @return persisted object instance in this map by key
	 */
	public <T> T get(Session session, ObjectMapKey<T> key){
		return (T) session.get(key.getType(), get(key));
	}
	
	/**
	 * Returns persisted object instance by given {@link SessionTemplate}
	 * 
	 * @param st
	 * @param key
	 * @return persisted object instance in this map by key
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(SessionTemplate st, ObjectMapKey<T> key){
		return (T) st.getObjectForPK(key.getClass(), get(key));
	}
}
