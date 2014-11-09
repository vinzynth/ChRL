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

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import at.chrl.callbacks.Callback;
import at.chrl.callbacks.CallbackResult;
import at.chrl.callbacks.util.GlobalCallbackHelper;

/**
 * 
 * @author Vinzynth 08.11.2014 - 16:51:03
 *
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class MemoizeCallback implements Callback<Object> {	
	
	private static final Object STATIC;
	
	static{
		GlobalCallbackHelper.addCallback(SingletonHolder.instance);
		STATIC = new Object();
	}
	
	private final Map<String, Map<Object, Map<Object, Object>>> cache;
	private final Supplier<Map> mapSupplier;
	private final Supplier<Map> weakMapSupplier;

	private long hits;
	private long misses;
	
	private MemoizeCallback(final Supplier<Map> mapSupplier, final Supplier<Map> weakMapSupplier){
		this.cache = mapSupplier.get();
		this.mapSupplier = mapSupplier;
		this.weakMapSupplier = weakMapSupplier;
		this.hits = 0;
		this.misses = 0;
	}
	
	private static String getCallingMethod() {
		String string = Thread.currentThread().getStackTrace()[4].toString();
		string = string.substring(string.lastIndexOf('$') + 1, string.lastIndexOf('('));
		return string;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.callbacks.Callback#beforeCall(java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public CallbackResult beforeCall(Object obj, Object[] args) {
		String callingMethod = getCallingMethod();
		if (!cache.containsKey(callingMethod))
			cache.putIfAbsent(callingMethod, mapSupplier.get());
		
		Map<Object, Map<Object, Object>> cache2 = cache.get(callingMethod);
		if (!cache2.containsKey(obj))
			if (Objects.nonNull(obj))
				cache2.putIfAbsent(obj, weakMapSupplier.get());
			else
				cache2.putIfAbsent(STATIC, mapSupplier.get());

		int hash = 0;
		for (Object object : args)
			hash = hash * 31 + object.hashCode();

		Object object = null;
		if (Objects.nonNull(obj))
			object = cache2.get(obj).get(hash);
		else
			object = cache2.get(STATIC).get(hash);

		if (Objects.nonNull(object)){
			++hits;
			return CallbackResult.newFullBlocker(object);
		}
		++misses;
		return CallbackResult.newContinue();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param obj
	 * @param args
	 * @param methodResult
	 * @return
	 */
	public CallbackResult afterCall(Object obj, Object[] args, Object methodResult){
		String callingMethod = getCallingMethod();
		
		int hash = 0;
		for (Object object : args)
			hash = hash * 31 + object.hashCode();
		
		if(Objects.nonNull(obj))
			cache.get(callingMethod).get(obj).put(hash, methodResult);
		else
			cache.get(callingMethod).get(STATIC).put(hash, methodResult);
		
		return CallbackResult.newContinue();
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Memoize Cache | Hits: " + hits + " | Misses: " + misses + " | Hit Ratio: " + ((float)hits/(hits+misses)) + " | " +super.toString();
	}
	
	public static String printString(){
		return SingletonHolder.instance.toString();
	}

	private static final class SingletonHolder {
		private static final MemoizeCallback instance = new MemoizeCallback(
				CollectionSupplier.rawSupplier(CollectionSupplier.getMapSupplier()),
				CollectionSupplier.rawSupplier(CollectionSupplier.getWeakMapSupplier())
		);
	}
}
