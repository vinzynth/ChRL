/**
 * (C) ChRL 2013 - chrl-utils - at.chrl.utils.patterns - SingletonPattern.java
 * Created: 30.12.2013 - 14:27:11
 */
package at.chrl.utils.patterns;

import java.util.HashMap;

/**
 * @author Vinzynth
 *
 */
@SuppressWarnings({ "unchecked" })
public final class SingletonHolder{
	
	protected SingletonHolder(){}
	
	private static HashMap<Class<?>, Object> instances = new HashMap<>();
	
	public static <T> T getInstance(Class<T> clazz){
		return (T) (instances.containsKey(clazz) ? instances.get(clazz) : createInstance(clazz));
	}
	
	private static <T> T createInstance(Class<T> clazz){
		try {
			instances.put(clazz, clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return (T) instances.get(clazz);
	}
	
}
