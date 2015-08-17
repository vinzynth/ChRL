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
package at.chrl.cbutils.memoizer;

import gnu.trove.map.hash.THashMap;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.google.common.collect.MapMaker;

import at.chrl.nutils.CollectionUtils;

/**
 * @deprecated Since Javaagent does not support lambdas. Use
 *             {@link CollectionUtils} instead Only used at
 *             {@link MemoizeCallback}.
 * 
 * @author Vinzynth 09.11.2014 - 02:08:42
 *
 */
@Deprecated
public class CollectionSupplier {

	private CollectionSupplier() {
	}

	private static MapMaker mapMaker = new MapMaker();

	public static <K, V> Supplier<Map<K, V>> getMapSupplier() {
		return new Supplier<Map<K, V>>() {

			@Override
			public Map<K, V> get() {
				return new THashMap<>();
			}
		};
	}

	public static <K, V> Supplier<Map<K, V>> getWeakMapSupplier() {
		return new Supplier<Map<K, V>>() {

			@Override
			public Map<K, V> get() {
				return new WeakHashMap<>();
			}
		};
	}

	public static <K, V> Supplier<Map<K, V>> getConcurrentMapSupplier() {
		return new Supplier<Map<K, V>>() {

			@Override
			public Map<K, V> get() {
				return new ConcurrentHashMap<>();
			}
		};
	}

	public static <K, V> Supplier<Map<K, V>> getConcurrentWeakMapSupplier() {
		return new Supplier<Map<K, V>>() {

			@Override
			public Map<K, V> get() {
				return mapMaker.concurrencyLevel(16).weakKeys().<K, V> makeMap();
			}
		};
	}

	@SuppressWarnings("rawtypes")
	public static <T> Supplier rawSupplier(Supplier<T> supplier) {
		return supplier;
	}
}
