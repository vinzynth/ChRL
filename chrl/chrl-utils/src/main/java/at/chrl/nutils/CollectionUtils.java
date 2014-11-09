/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - CollectionUtils.java Created:
 * 02.08.2014 - 11:48:00
 */
package at.chrl.nutils;

import gnu.trove.map.hash.THashMap;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.google.common.collect.MapMaker;

/**
 * @author Vinzynth
 *
 */
public class CollectionUtils {

	private CollectionUtils() {
	}

	private static final MapMaker mapMaker = new MapMaker();

	/**
	 * Returns a capacity that is sufficient to keep the map from being resized
	 * as long as it grows no larger than expectedSize and the load factor is >=
	 * its default (0.75).
	 */
	public static int capacity(int expectedSize) {
		return (int) ((expectedSize / 0.75) + 1);
	}

	public static <K, V> Supplier<Map<K, V>> getMapSupplier() {
		return THashMap<K, V>::new;
	}

	public static <K, V> Supplier<Map<K, V>> getWeakMapSupplier() {
		return WeakHashMap<K, V>::new;
	}

	public static <K, V> Supplier<Map<K, V>> getConcurrentMapSupplier() {
		return ConcurrentHashMap<K, V>::new;
	}

	public static <K, V> Supplier<Map<K, V>> getConcurrentWeakMapSupplier() {
		return () -> mapMaker.concurrencyLevel(16).weakKeys().<K, V> makeMap();
	}
	
	private static <T> T get(Supplier<T> sup){
		return sup.get();
	}
	
	public static <K,V> Map<K,V> newMap(){
		return get(getMapSupplier());
	}
	
	public static <K,V> Map<K,V> newConcurrentMap(){
		return get(getConcurrentMapSupplier());
	}
}
