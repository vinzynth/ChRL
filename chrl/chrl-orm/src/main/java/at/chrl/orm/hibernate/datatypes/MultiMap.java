/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * ChRL Util Collection. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.orm.hibernate.datatypes;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;

import org.hibernate.Session;

import at.chrl.nutils.CollectionUtils;
import at.chrl.nutils.interfaces.INestedMap;
import at.chrl.orm.hibernate.SessionTemplate;

/**
 * @author Vinzynth 02.08.2015 - 01:46:10
 *
 */
@Embeddable
public class MultiMap<K extends Serializable> implements INestedMap<Map<ObjectMapKey<?>, MultiMapEntry>, ObjectMapKey<?>, MultiMapEntry> {

	public MultiMap() {
		this.multiMapDataset = CollectionUtils.newMap();
	}
	
	/**
	 * Map with given key/primary key pairs
	 */
	@ManyToMany(cascade = {CascadeType.ALL})
	private Map<ObjectMapKey<?>, MultiMapEntry> multiMapDataset;

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.interfaces.INestedMap#getNestedMap()
	 */
	@Override
	public Map<ObjectMapKey<?>, MultiMapEntry> getNestedMap() {
		return multiMapDataset;
	}
	
	public <T extends ObjectMapable<Long>> boolean add(final ObjectMapKey<T> key, T object){
		putIfAbsent(key, new MultiMapEntry());
		return get(key).add(object.getId());
	}
	
	public <T extends ObjectMapable<Long>> boolean remove(final ObjectMapKey<T> key, T object){
		putIfAbsent(key, new MultiMapEntry());
		return get(key).remove(object.getId());
	}
	
	public <T extends ObjectMapable<Long>> void clear(final ObjectMapKey<T> key){
		putIfAbsent(key, new MultiMapEntry());
		get(key).clear();
	}
	
	public <T> Stream<T> get(final EntityManager em, final ObjectMapKey<T> key){
		if(!containsKey(key))
			return Stream.<T>empty();
		return (Stream<T>) get(key).stream().map(l -> em.find(key.getType(), l));
	}
	
	@SuppressWarnings("unchecked")
	public <T> Stream<T> get(final Session session, final ObjectMapKey<T> key){
		if(!containsKey(key))
			return Stream.<T>empty();
		return (Stream<T>) get(key).stream().map(l -> session.get(key.getType(), l));
	}
	
	public <T> Stream<T> get(final SessionTemplate session, final ObjectMapKey<T> key){
		if(!containsKey(key))
			return Stream.<T>empty();
		return (Stream<T>) session.streamObjectsForPK(key.getType(), get(key));
	}
}
