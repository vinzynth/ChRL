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
package at.chrl.nutils.interfaces;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Vinzynth 01.08.2015 - 21:32:09
 *
 */
public interface INestedMap<M extends Map<K, V>, K, V> extends Map<K, V> {

	/**
	 * Returns nestes map object
	 * 
	 * @return nested map object
	 */
	public M getNestedMap();

	public default int size() {
		return getNestedMap().size();
	}

	public default boolean isEmpty() {
		return getNestedMap().isEmpty();
	}

	public default boolean containsKey(Object key) {
		return getNestedMap().containsKey(key);
	}

	public default boolean containsValue(Object value) {
		return getNestedMap().containsValue(value);
	}

	public default V get(Object key) {
		return getNestedMap().get(key);
	}

	public default V put(K key, V value) {
		return getNestedMap().put(key, value);
	}

	public default V remove(Object key) {
		return getNestedMap().remove(key);
	}

	public default void putAll(Map<? extends K, ? extends V> m) {
		getNestedMap().putAll(m);
	}

	public default void clear() {
		getNestedMap().clear();
	}

	public default Set<K> keySet() {
		return getNestedMap().keySet();
	}

	public default Collection<V> values() {
		return getNestedMap().values();
	}

	public default Set<Map.Entry<K, V>> entrySet() {
		return getNestedMap().entrySet();
	}
}
