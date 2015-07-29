/**
 * @author Christian Richard Leopold - ChRL
 * Jul 29, 2015 - 12:37:41 PM
 * chrl-orm
 * at.chrl.orm.hibernate.generator
 */
package at.chrl.orm.hibernate.generator;

import gnu.trove.map.hash.THashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.persistence.GeneratedValue;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import at.chrl.nutils.ClassUtils;
import at.chrl.nutils.CollectionUtils;
import at.chrl.nutils.Rnd;

/**
 * Dataset generator
 * NOT SUPPORTED: arrays of primtives | multimaps
 * 
 * @author Christian Richard Leopold - ChRL <br>
 * Jul 29, 2015 - 12:37:41 PM
 *
 */
public final class DatasetGenerator {

	private static final Map<Class<?>, Supplier<?>> SUPPORTED_TYPES;
	private static final int COLLECTION_POPULATION = 5;
	
	static{
		SUPPORTED_TYPES = new THashMap<>();
		
		SUPPORTED_TYPES.put(String.class, Rnd::nextString);
		SUPPORTED_TYPES.put(Boolean.class, Rnd::nextBoolean);
		SUPPORTED_TYPES.put(Byte.class, Rnd::nextByte);
		SUPPORTED_TYPES.put(Short.class, Rnd::nextShort);
		SUPPORTED_TYPES.put(Integer.class, Rnd::nextInt);
		SUPPORTED_TYPES.put(Long.class, Rnd::nextLong);
		SUPPORTED_TYPES.put(Float.class, Rnd::nextFloat);
		SUPPORTED_TYPES.put(Double.class, Rnd::nextDouble);
		SUPPORTED_TYPES.put(Character.class, Rnd::nextChar);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Boolean.class), Rnd::nextBoolean);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Byte.class), Rnd::nextByte);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Short.class), Rnd::nextShort);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Integer.class), Rnd::nextInt);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Long.class), Rnd::nextLong);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Float.class), Rnd::nextFloat);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Double.class), Rnd::nextDouble);
		SUPPORTED_TYPES.put(ClassUtils.getPrimitiveClass(Character.class), Rnd::nextChar);
		SUPPORTED_TYPES.put(Set.class, CollectionUtils.getSetSupplier());
		SUPPORTED_TYPES.put(List.class, CollectionUtils.getListSupplier());
		SUPPORTED_TYPES.put(Collection.class, CollectionUtils.getListSupplier());
		SUPPORTED_TYPES.put(Queue.class, CollectionUtils.getQueueSupplier());
		SUPPORTED_TYPES.put(Deque.class, CollectionUtils.getDequeSupplier());
		SUPPORTED_TYPES.put(Map.class, CollectionUtils.getMapSupplier());
		SUPPORTED_TYPES.put(Date.class, Date::new);
		SUPPORTED_TYPES.put(Calendar.class, Calendar::getInstance);
		SUPPORTED_TYPES.put(AtomicInteger.class, () -> new AtomicInteger(Rnd.nextInt()));
		SUPPORTED_TYPES.put(AtomicLong.class, () -> new AtomicLong(Rnd.nextLong()));
	}
	
	private final Objenesis objenesis;
	private final Map<Class<?>, ObjectInstantiator<?>> instantiators;
	private final Collection<Class<?>> currentlyGenerating;
	
	public <T> Stream<T> generate(final Class<T> cls, final int count){
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(generateIterable(cls, count), Spliterator.ORDERED), false).filter(Objects::nonNull);
	}
	
	public <T> Iterator<T> generateIterable(final Class<T> cls, final int count){
		return new Iterator<T>() {

			int i = 0;
			
			@Override
			public boolean hasNext() {
				return ++i <= count;
			}

			@Override
			public T next() {
				return DatasetGenerator.getInstance().generate(cls);
			}
		};
	}
	
	public <T> T generate(final Class<T> cls){
		final T instance = newInstance(cls);
		
		if(SUPPORTED_TYPES.containsKey(cls) || cls.isEnum())
			return instance;
		
		// Dont populate recursive datasets with data
		if(this.currentlyGenerating.contains(cls))
			return instance;
		
		this.currentlyGenerating.add(cls);
		Arrays.stream(cls.getDeclaredFields()).filter(this::filterField).forEach(f -> fillField(instance, f));
		this.currentlyGenerating.remove(cls);
		
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T newInstance(Class<T> cls){
		if(SUPPORTED_TYPES.containsKey(cls))
			return (T) SUPPORTED_TYPES.get(cls).get();
		
		if(cls.isArray())
			return (T) Array.newInstance(cls.getComponentType(), cls.getComponentType().isPrimitive() ? 0 : COLLECTION_POPULATION);
		
		if(cls.isEnum()){
			T[] enumConstants = cls.getEnumConstants();
			return enumConstants[Rnd.get(enumConstants.length)];
		}
		
		instantiators.putIfAbsent(cls, objenesis.getInstantiatorOf(cls));
		
		return (T) instantiators.get(cls).newInstance();
	}
	
	private boolean filterField(final Field f){
		if(f.isSynthetic())
			return false;
		
		int mods = f.getModifiers();
		if(Modifier.isStatic(mods) || Modifier.isNative(mods) || Modifier.isAbstract(mods) || Modifier.isInterface(mods))
			return false;
		
		for (Annotation annotation : f.getAnnotations()) {
			if(annotation.annotationType().equals(GeneratedValue.class))
				return false;
		}
		
		if(!f.isAccessible())
			f.setAccessible(true);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private <T, C, K, V> void fillField(final T object, final Field field){
		try {
			Class<C> c = (Class<C>) field.getType(); // Class
			Class<K> k = null;						 // Key
			Class<V> v = null;						 // Value
			
			
			if(Objects.nonNull(field.getGenericType()) && field.getGenericType() instanceof ParameterizedType){
				ParameterizedType p = (ParameterizedType) field.getGenericType();
				if(p.getActualTypeArguments().length > 0)
					k = (Class<K>) p.getActualTypeArguments()[0];
				if(p.getActualTypeArguments().length > 1)
					v = (Class<V>) p.getActualTypeArguments()[1];
			}
			
			if(c.isArray()){
				k = (Class<K>) c.getComponentType();
			}
			
			field.set(object, generate(c));
			
			if(c.isArray() && !k.isPrimitive()){
				K[] ks = (K[]) field.get(object);
				for (int i = 0; i < COLLECTION_POPULATION; i++)
					ks[i] = generate(k);
			}
			else if(Objects.nonNull(k)){
				if(Objects.nonNull(v) && Map.class.isAssignableFrom(c)){
					Map<K,V> map = (Map<K,V>)field.get(object);
					for (int i = 0; i < COLLECTION_POPULATION; i++) {
						map.put(generate(k),generate(v));						
					}
				}
				else if(Objects.isNull(v) && Collection.class.isAssignableFrom(c)){
					Collection<K> col = (Collection<K>)field.get(object);
					generate(k,COLLECTION_POPULATION).forEach(col::add);
				}
			}
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	
	private DatasetGenerator() {
		this.objenesis = new ObjenesisStd();
		this.instantiators = new THashMap<>();
		this.currentlyGenerating = new Vector<>();
	}

	/**
	 * Singleton Holder class for type: DatasetGenerator
	 */
	private static final class SingletonHolder {
		private static final DatasetGenerator instance = new DatasetGenerator();
	}

	/**
	 * Returns single instance for this type
	 *
	 * @returns
	 *		Singleton instance for type DatasetGenerator
	 */
	public static final DatasetGenerator getInstance() {
		return SingletonHolder.instance;
	}
}
