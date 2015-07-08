/**
 * @author Christian Richard Leopold - ChRL
 * Mar 13, 2015 - 9:01:13 PM
 * chrl-utils
 * at.chrl.nutils
 */
package at.chrl.nutils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Mar 13, 2015 - 9:01:13 PM
 *
 */
public final class StreamUtils {

	private StreamUtils() {}
	
	/**
	 * identity function
	 * @return identity function
	 */
	public static <T> Function<T, T> identity(){
		return t -> t;
	}
	
	/**
	 * to null function
	 * @return to null function
	 */
	public static <T> Function<T, T> toNull(){
		return t -> null;
	}
	
	/**
	 * binary keep first function
	 * @return keep first function
	 */
	public static <T> BinaryOperator<T> keepFirst(){
		return (t, t2) -> t;
	}
	
	/**
	 * binary keep last function
	 * @return keep last function
	 */
	public static <T> BinaryOperator<T> keepLast(){
		return (t1, t) -> t;
	}
	
	/**
	 * {@link Collectors#toMap(Function, Function, BinaryOperator, java.util.function.Supplier)} override
	 * returns toMap function with given key Mapper.
	 * Keeps first if duplicates
	 *  
	 * @param keyMapper
	 * @return mapper function
	 */
	public static <T, K> Collector<T, ?, Map<K, T>> toMap(Function<T, K> keyMapper){
		return Collectors.toMap(keyMapper, StreamUtils.identity(), StreamUtils.keepFirst());
	}
	
	/**
	 * {@link Collectors#toMap(Function, Function, BinaryOperator, java.util.function.Supplier)} override
	 * returns toMap function with given key Mapper.
	 * Keeps last if duplicates
	 *  
	 * @param keyMapper
	 * @return mapper function
	 */
	public static <T, K> Collector<T, ?, Map<K, T>> toMapKeepLast(Function<T, K> keyMapper){
		return Collectors.toMap(keyMapper, StreamUtils.identity(), StreamUtils.keepLast());
	}
	
	/**
	 *  creates a range stream of given type T.
	 *  <p>
	 *  checks if value after applying the increment function equals the end value.
	 *  <p>
	 *  <b>
	 *  BEWARE: INFITE LOOP IF END VALUE IS NEVER REACHED BY INCREMENT FUNCTION
	 *  </b>
	 *  
	 * @param start - start value of stream
	 * @param end - end value of stream
	 * @param incrementFunction - increment function
	 * @return stream
	 */
	public static final <T> Stream<T> rangeStream(T start, T end, Function<T, T> incrementFunction){
		List<T> range = new ArrayList<T>();
		range.add(start);
		T i = start;
		do {
			i = incrementFunction.apply(i);
			range.add(i);
		} while (!end.equals(i));
		return range.stream();
	}
	
	/**
	 * returns comparator for gicen getter function
	 * 
	 * @param getter
	 * @param reverse for reverse rodering
	 * @return
	 */
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(Function<T, R> getter, boolean reverse){
		if(reverse)
			return (e1, e2) -> getter.apply(e2).compareTo(getter.apply(e1));
		return (e1, e2) -> getter.apply(e1).compareTo(getter.apply(e2));
	}
	
	/**
	 * returns comparator by getter functions
	 * @param getters
	 * @return comparator
	 */
	@SafeVarargs
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(Function<T, R>... getters){
		return getComparator(false, getters);
	}

	/**
	 * returns comparator by getter functions
	 * @param reverse - for reverse ordering
	 * @param getters
	 * @return comparator
	 */
	@SafeVarargs
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(boolean reverse, Function<T, R>... getters){
		return (e1, e2) -> {
			int v = 0;
			for (Function<T, R> getter : getters) {
				v = getter.apply(e1).compareTo(getter.apply(e2));
				if(v != 0)
					return reverse ? -v : v;
			}
			return v;
		};
	}
	
	/**
	 * Returns Comparator for quick map entry sorting
	 * Compares by key
	 * 
	 * @param reverse - for reverse sorting
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return key comparator
	 */
	public static final <E extends Entry<K,V>, K extends Comparable<K>, V> Comparator<E> getKeyComparator(boolean reverse){
		if(reverse)
			return (e1, e2) -> e2.getKey().compareTo(e1.getKey());
		return (e1, e2) -> e1.getKey().compareTo(e2.getKey());
	}
	
	/**
	 * Returns Comparator for quick map entry sorting
	 * Compares by key
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return key comparator
	 */
	public static final <E extends Entry<K,V>, K extends Comparable<K>, V> Comparator<E> getKeyComparator(){
		return getKeyComparator(false);
	}
	
	/**
	 * Returns Comparator for quick map entry sorting
	 * Compares by value
	 * 
	 * @param reverse - for reverse sorting
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return value comparator
	 */
	public static final <E extends Entry<K,V>, K, V extends Comparable<V>> Comparator<E> getValueComparator(boolean reverse){
		if(reverse)
			return (e1, e2) -> e2.getValue().compareTo(e1.getValue());
		return (e1, e2) -> e1.getValue().compareTo(e2.getValue());
	}
	
	/**
	 * Returns Comparator for quick map entry sorting
	 * Compares by value
	 * 
	 * @type E - Entry
	 * @type K - Entry Key
	 * @type V - Entry Value
	 * 
	 * @return value comparator
	 */
	public static final <E extends Entry<K,V>, K, V extends Comparable<V>> Comparator<E> getValueComparator(){
		return getValueComparator(false);
	}
	
	public static final <R, T> Function<R, T> parse(final Function<R, T> parseFunction, final T defaultValue){
		return s -> {
			try {
				return parseFunction.apply(s);				
			} catch (Exception e) {
				return defaultValue;
			}
		};
	}
	
	public static final <T> Function<String, T> parseStringToNumeric(final BiFunction<String, Integer, T> parseFunction, final int radix, final T defaultValue){
		return s -> {
			try {
				return parseFunction.apply(s, radix);				
			} catch (Exception e) {
				return defaultValue;
			}
		};
	}
	
	public static final Function<String, Long> parseToLong(){
		return parseToLong(10);
	}
	
	public static final Function<String, Long> parseToLong(final int radix){
		return parseStringToNumeric(Long::parseLong, radix, 0L);
	}
	
	public static final Function<String, Short> parseToShort(){
		return parseToShort(10);
	}
	
	public static final Function<String, Short> parseToShort(final int radix){
		return parseStringToNumeric(Short::parseShort, radix, (short)0);
	}
	
	public static final Function<String, Integer> parseToInt(){
		return parseToInt(10);
	}
	
	public static final Function<String, Integer> parseToInt(final int radix){
		return parseStringToNumeric(Integer::parseInt, radix, 0);
	}
	
	public static final Function<String, Long> parseToULong(){
		return parseToULong(10);
	}
	
	public static final Function<String, Long> parseToULong(final int radix){
		return parseStringToNumeric(Long::parseUnsignedLong, radix, 0L);
	}
	
	public static final Function<String, Byte> parseToByte(){
		return parseToByte(10);
	}
	
	public static final Function<String, Byte> parseToByte(final int radix){
		return parseStringToNumeric(Byte::parseByte, radix, (byte)0);
	}
	
	public static final Function<String, Double> parseToDouble(){
		return parse(Double::parseDouble, 0d);
	}
	
	public static final Function<String, Float> parseToFloat(){
		return parse(Float::parseFloat, 0f);
	}
	
	public static final Function<String, Boolean> parseStringToBoolean(){
		return parse(Boolean::parseBoolean, false);
	}
	
	public static final Function<? extends Number, Boolean> parseNumberToBoolean(){
		return (i) -> i.intValue() > 0;
	}
}
