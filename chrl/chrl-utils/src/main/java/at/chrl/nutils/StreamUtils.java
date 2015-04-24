/**
 * @author Christian Richard Leopold - ChRL
 * Mar 13, 2015 - 9:01:13 PM
 * chrl-utils
 * at.chrl.nutils
 */
package at.chrl.nutils;

import java.util.Comparator;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Mar 13, 2015 - 9:01:13 PM
 *
 */
public final class StreamUtils {

	private StreamUtils() {}
	
	public static <T> Function<T, T> identity(){
		return t -> t;
	}
	
	public static <T> BinaryOperator<T> keepFirst(){
		return (t, t2) -> t;
	}
	
	public static <T> BinaryOperator<T> keepLast(){
		return (t1, t) -> t;
	}
	
	public static <T, K> Collector<T, ?, Map<K, T>> toMap(Function<T, K> keyMapper){
		return Collectors.toMap(keyMapper, StreamUtils.identity(), StreamUtils.keepFirst());
	}
	
	public static <T, K> Collector<T, ?, Map<K, T>> toMapKeepLast(Function<T, K> keyMapper){
		return Collectors.toMap(keyMapper, StreamUtils.identity(), StreamUtils.keepLast());
	}
	
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(Function<T, R> getter){
		return getComparator(getter, false);
	}
	
	public static final <T, R extends Comparable<R>> Comparator<T> getComparator(Function<T, R> getter, boolean reverse){
		if(reverse)
			return (e1, e2) -> getter.apply(e2).compareTo(getter.apply(e1));
		return (e1, e2) -> getter.apply(e1).compareTo(getter.apply(e2));
	}
	
}
