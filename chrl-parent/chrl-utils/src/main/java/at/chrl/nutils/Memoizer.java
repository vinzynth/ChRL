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
package at.chrl.nutils;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Vinzynth 08.11.2014 - 16:39:32
 * 
 * @see http://java.dzone.com/articles/java-8-automatic-memoization
 */
public class Memoizer {

	/**
	 * No lambda expressions here, since Javaagent does not support that yet
	 * 
	 * @param function
	 * @return
	 */
	private static <T, U> Function<T, U> doMemoize(final Function<T, U> function, Map<T, U> cache) {
		//Map<T, U> cache = CollectionUtils.newMap();
		return new Function<T, U>() {
			@Override
			public U apply(T t) {
				return cache.computeIfAbsent(t, function::apply);
			}
		};
	}
	
	/**
	 * No lambda expressions here, since Javaagent does not support that yet
	 * 
	 * @param function
	 * @return
	 */
	private static <T> Predicate<T> doMemoize(final Predicate<T> predicate, Map<T, Boolean> cache) {
		//Map<T, Boolean> cache = CollectionUtils.newMap();
		return new Predicate<T>() {

			@Override
			public boolean test(T t) {
				return cache.computeIfAbsent(t, predicate::test);
			}
		};
	}

    public static <T, U> Function<T, U> memoize(final Function<T, U> function) {
        return doMemoize(function, CollectionUtils.newMap());
    }

    public static <T> Predicate<T> memoizePredicate(final Predicate<T> predicate) {
        return doMemoize(predicate, CollectionUtils.newMap());
    }

	public static <T, U> Function<T, U> memoize(final Function<T, U> function, Map<T, U> cache) {
		return doMemoize(function, cache);
	}

	public static <T> Predicate<T> memoizePredicate(final Predicate<T> predicate, Map<T, Boolean> cache) {
		return doMemoize(predicate, cache);
	}
}
