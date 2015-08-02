/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - CollectionUtils.java Created:
 * 02.08.2014 - 11:48:00
 */
package at.chrl.nutils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.google.common.collect.MapMaker;

/**
 * @author Vinzynth
 *
 */
public final class CollectionUtils {

	private CollectionUtils() {
	}

	private static final MapMaker MAP_MAKER = new MapMaker();

	/**
	 * Returns a capacity that is sufficient to keep the map from being resized
	 * as long as it grows no larger than expectedSize and the load factor is >=
	 * its default (0.75).
	 */
	public static int capacity(int expectedSize) {
		return (int) ((expectedSize / 0.75) + 1);
	}

	public static <K, V> Supplier<Map<K, V>> getMapSupplier() {
		return Object2ObjectArrayMap<K, V>::new;
	}

	public static <T> Supplier<Set<T>> getSetSupplier() {
		return ObjectOpenHashSet<T>::new;
	}

	public static <T> Supplier<List<T>> getListSupplier() {
		return ObjectArrayList<T>::new;
	}
	
	public static <T> Supplier<Queue<T>> getQueueSupplier() {
		return ArrayDeque<T>::new;
	}

	public static <T> Supplier<Deque<T>> getDequeSupplier() {
		return ArrayDeque<T>::new;
	}
	
	public static <K, V> Supplier<Map<K, V>> getWeakMapSupplier() {
		return WeakHashMap<K, V>::new;
	}

	public static <K, V> Supplier<Map<K, V>> getConcurrentMapSupplier() {
		return ConcurrentHashMap<K, V>::new;
	}
	
	public static <K, V> Supplier<Map<K, V>> getMapSupplier(final int size) {
		return () -> new Object2ObjectArrayMap<>(size);
	}
	
	public static <T> Supplier<Set<T>> getSetSupplier(final int size) {
		return () -> new ObjectOpenHashSet<>(size);
	}

	public static <T> Supplier<List<T>> getListSupplier(final int size) {
		return () -> new ObjectArrayList<>(size);
	}

	public static <K, V> Supplier<Map<K, V>> getConcurrentWeakMapSupplier() {
		return () -> MAP_MAKER.concurrencyLevel(16).weakKeys().<K, V> makeMap();
	}

	private static <T> T get(Supplier<T> sup) {
		return sup.get();
	}

	public static <K, V> Map<K, V> newMap() {
		return get(getMapSupplier());
	}

	public static <K> Set<K> newSet() {
		return get(getSetSupplier());
	}

	public static <K> List<K> newList() {
		return get(getListSupplier());
	}
	
	public static <K, V> Map<K, V> newMap(final int size) {
		return get(getMapSupplier(size));
	}

	public static <K> Set<K> newSet(final int size) {
		return get(getSetSupplier(size));
	}

	public static <K> List<K> newList(final int size) {
		return get(getListSupplier(size));
	}

	public static <K, V> Map<K, V> newConcurrentMap() {
		return get(getConcurrentMapSupplier());
	}
	

	public static <T> List<List<T>> chopped(List<T> list, final int length) {
		List<List<T>> parts = CollectionUtils.<List<T>>newList();
		final int n = list.size();
		for (int i = 0; i < n; i += length) {
			parts.add(new ObjectArrayList<T>(list.subList(i, Math.min(n, i + length))));
		}
		return parts;
	}
}
