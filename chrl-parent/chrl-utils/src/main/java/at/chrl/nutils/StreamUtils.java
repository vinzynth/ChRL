/**
 * @author Christian Richard Leopold - ChRL
 * Mar 13, 2015 - 9:01:13 PM
 * chrl-utils
 * at.chrl.nutils
 */
package at.chrl.nutils;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Christian Richard Leopold - ChRL <br>
 *         Mar 13, 2015 - 9:01:13 PM
 *
 */
public final class StreamUtils {

	private StreamUtils() {
	}

	/**
	 * identity function
	 * @see {@link UnaryOperator#identity()}
	 * 
	 * @return identity function
	 */
	public static <T> Function<T, T> identity() {
		return UnaryOperator.identity();
	}

	/**
	 * identity function and print on System.out
	 * 
	 * @return identity function with print
	 */
	public static <T> Function<T, T> printOut() {
		return t -> {
			System.out.println(t);
			return t;
		};
	}

	/**
	 * identity function with print on given stream
	 * 
	 * @param out
	 *            - given outputStream
	 * @return
	 */
	public static <T> Function<T, T> printOut(final PrintStream out) {
		return t -> {
			out.println(t);
			return t;
		};
	}

	/**
	 * to null function
	 * 
	 * @return to null function
	 */
	public static <T> Function<T, T> toNull() {
		return t -> null;
	}

	/**
	 * binary keep first function
	 * 
	 * @return keep first function
	 */
	public static <T> BinaryOperator<T> keepFirst() {
		return (t, t2) -> t;
	}

	/**
	 * binary keep last function
	 * 
	 * @return keep last function
	 */
	public static <T> BinaryOperator<T> keepLast() {
		return (t1, t) -> t;
	}

	/**
	 * {@link Collectors#toMap(Function, Function, BinaryOperator, java.util.function.Supplier)}
	 * override returns toMap function with given key Mapper. Keeps first if
	 * duplicates
	 * 
	 * @param keyMapper
	 * @return mapper function
	 */
	public static <T, K> Collector<T, ?, Map<K, T>> toMap(
			Function<T, K> keyMapper) {
		return Collectors.toMap(keyMapper, StreamUtils.identity(),
				StreamUtils.keepFirst());
	}

	/**
	 * {@link Collectors#toMap(Function, Function, BinaryOperator, java.util.function.Supplier)}
	 * override returns toMap function with given key Mapper. Keeps last if
	 * duplicates
	 * 
	 * @param keyMapper
	 * @return mapper function
	 */
	public static <T, K> Collector<T, ?, Map<K, T>> toMapKeepLast(
			Function<T, K> keyMapper) {
		return Collectors.toMap(keyMapper, StreamUtils.identity(),
				StreamUtils.keepLast());
	}
	
	/**
	 * Collector to collect values into multimaps 
	 * 
	 * @param keyMapper
	 * @return collector
	 */
	public static <T, K> Collector<T, ? , Map<K, Collection<T>>> toMultimap(
			Function<T, K> keyMapper){
		return Collectors.toMap(keyMapper, v -> {
			List<T> list = CollectionUtils.<T>newList();
			list.add(v);
			return list;
		}, new BinaryOperator<Collection<T>>() {

			@Override
			public Collection<T> apply(Collection<T> t, Collection<T> u) {
				u.addAll(t);
				return u;
			}
		});
	}

	/**
	 * creates a range stream of given type T.
	 * <p>
	 * checks if value after applying the increment function equals the end
	 * value.
	 * <p>
	 * <b> BEWARE: INFITE LOOP IF END VALUE IS NEVER REACHED BY INCREMENT
	 * FUNCTION </b>
	 * 
	 * @param start
	 *            - start value of stream
	 * @param end
	 *            - end value of stream
	 * @param incrementFunction
	 *            - increment function
	 * @return stream
	 */
	public static final <T> Stream<T> rangeStream(final T start, final T end,
			final UnaryOperator<T> incrementFunction) {
		
		final Iterator<T> iterator = new Iterator<T>() {
			T t = (T) start;

			@Override
			public boolean hasNext() {
				return !t.equals(end);
			}

			@Override
			public T next() {
				return (t = incrementFunction.apply(t));
			}
		};
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
	}

	/**
	 * returns comparator for gicen getter function
	 * 
	 * @param getter
	 * @param reverse
	 *            for reverse rodering
	 * @return
	 */
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(
			Function<T, R> getter, boolean reverse) {
		if (reverse)
			return (e1, e2) -> getter.apply(e2).compareTo(getter.apply(e1));
		return (e1, e2) -> getter.apply(e1).compareTo(getter.apply(e2));
	}

	/**
	 * returns comparator by getter functions
	 * 
	 * @param getters
	 * @return comparator
	 */
	@SafeVarargs
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(
			Function<T, R>... getters) {
		return getComparator(false, getters);
	}

	/**
	 * returns comparator by getter functions
	 * 
	 * @param reverse
	 *            - for reverse ordering
	 * @param getters
	 * @return comparator
	 */
	@SafeVarargs
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(
			boolean reverse, Function<T, R>... getters) {
		return (e1, e2) -> {
			int v = 0;
			for (Function<T, R> getter : getters) {
				v = getter.apply(e1).compareTo(getter.apply(e2));
				if (v != 0)
					return reverse ? -v : v;
			}
			return v;
		};
	}

	/**
	 * Returns Comparator for quick map entry sorting Compares by key
	 * 
	 * @param reverse
	 *            - for reverse sorting
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return key comparator
	 */
	public static final <E extends Entry<K, V>, K extends Comparable<K>, V> Comparator<E> getKeyComparator(
			boolean reverse) {
		if (reverse)
			return (e1, e2) -> e2.getKey().compareTo(e1.getKey());
		return (e1, e2) -> e1.getKey().compareTo(e2.getKey());
	}

	/**
	 * Returns Comparator for quick map entry sorting Compares by key
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return key comparator
	 */
	public static final <E extends Entry<K, V>, K extends Comparable<K>, V> Comparator<E> getKeyComparator() {
		return getKeyComparator(false);
	}

	/**
	 * Returns Comparator for quick map entry sorting Compares by value
	 * 
	 * @param reverse
	 *            - for reverse sorting
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return value comparator
	 */
	public static final <E extends Entry<K, V>, K, V extends Comparable<V>> Comparator<E> getValueComparator(
			boolean reverse) {
		if (reverse)
			return (e1, e2) -> e2.getValue().compareTo(e1.getValue());
		return (e1, e2) -> e1.getValue().compareTo(e2.getValue());
	}

	/**
	 * Returns Comparator for quick map entry sorting Compares by value
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return value comparator
	 */
	public static final <E extends Entry<K, V>, K, V extends Comparable<V>> Comparator<E> getValueComparator() {
		return getValueComparator(false);
	}

	public static final <R, T> Function<R, T> parse(
			final Function<R, T> parseFunction, final T defaultValue) {
		return s -> {
			try {
				return parseFunction.apply(s);
			} catch (Exception e) {
				return defaultValue;
			}
		};
	}

	public static final <T> Function<String, T> parseStringToNumeric(
			final BiFunction<String, Integer, T> parseFunction,
			final int radix, final T defaultValue) {
		return s -> {
			try {
				return parseFunction.apply(s, radix);
			} catch (Exception e) {
				return defaultValue;
			}
		};
	}

	public static final Function<String, Long> parseToLong() {
		return parseToLong(10);
	}

	public static final Function<String, Long> parseToLong(final int radix) {
		return parseStringToNumeric(Long::parseLong, radix, 0L);
	}

	public static final Function<String, Short> parseToShort() {
		return parseToShort(10);
	}

	public static final Function<String, Short> parseToShort(final int radix) {
		return parseStringToNumeric(Short::parseShort, radix, (short) 0);
	}

	public static final Function<String, Integer> parseToInt() {
		return parseToInt(10);
	}

	public static final Function<String, Integer> parseToInt(final int radix) {
		return parseStringToNumeric(Integer::parseInt, radix, 0);
	}

	public static final Function<String, Long> parseToULong() {
		return parseToULong(10);
	}

	public static final Function<String, Long> parseToULong(final int radix) {
		return parseStringToNumeric(Long::parseUnsignedLong, radix, 0L);
	}

	public static final Function<String, Byte> parseToByte() {
		return parseToByte(10);
	}

	public static final Function<String, Byte> parseToByte(final int radix) {
		return parseStringToNumeric(Byte::parseByte, radix, (byte) 0);
	}

	public static final Function<String, Double> parseToDouble() {
		return parse(Double::parseDouble, 0d);
	}

	public static final Function<String, Float> parseToFloat() {
		return parse(Float::parseFloat, 0f);
	}

	public static final Function<String, Boolean> parseStringToBoolean() {
		return parse(Boolean::parseBoolean, false);
	}

	public static final Function<? extends Number, Boolean> parseNumberToBoolean() {
		return (i) -> i.intValue() > 0;
	}

	public static final <T> Function<T, T> waitFunction(long millis) {
		return t -> {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return t;
		};
	}
	
	public static <T, R> Function<T, R> catchException(Function<T, R> func){
		return t -> {
			try {
				return func.apply(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		};
	}
	
	public static boolean isBlankOrNull(String s) {
		return s == null || s.isEmpty();
	}

	public static boolean isBlankOrNull(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean isBlankOrNull(Map<?, ?> m) {
		return m == null || m.isEmpty();
	}

	public static boolean isBlankOrNull(Number n) {
		return n == null || n.intValue() == 0;
	}

	public static boolean isBlankOrNull(Object[] a) {
		return a == null || a.length == 0;
	}
}
