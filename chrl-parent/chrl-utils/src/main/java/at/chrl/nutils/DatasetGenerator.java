/**
 * @author Christian Richard Leopold - ChRL
 * Jul 29, 2015 - 12:37:41 PM
 * chrl-orm
 * at.chrl.orm.hibernate.generator
 */
package at.chrl.nutils;

import gnu.trove.map.hash.THashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import at.chrl.nutils.interfaces.INestedCollection;
import at.chrl.nutils.interfaces.INestedMap;

/**
 * Dataset generator
 * NOT SUPPORTED: arrays of primtives | multimaps
 * 
 * @author Christian Richard Leopold - ChRL <br>
 * Jul 29, 2015 - 12:37:41 PM
 *
 */
public class DatasetGenerator {

	private final Objenesis objenesis = new ObjenesisStd();
	private final int COLLECTION_POPULATION = 5;
	private final Collection<Class<?>> EXCLUDED_CLASSES = new CopyOnWriteArraySet<>();
	private final Collection<Class<?>> CURRENTLY_GENERATING = new CopyOnWriteArraySet<>();
	private final Map<Class<?>, ObjectInstantiator<?>> INSTANTIATORS = CollectionUtils.newConcurrentMap();
	
	private static final Map<Class<?>, Supplier<?>> SUPPORTED_TYPES;
	
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
		SUPPORTED_TYPES.put(INestedMap.class, CollectionUtils.getMapSupplier());
		SUPPORTED_TYPES.put(INestedCollection.class, CollectionUtils.getSetSupplier());
		SUPPORTED_TYPES.put(Date.class, Date::new);
		SUPPORTED_TYPES.put(URL.class, () -> {
			try {
				return new URL("http", Rnd.nextString(), Rnd.nextInt(60000), "/"+Rnd.nextString());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
		SUPPORTED_TYPES.put(Calendar.class, Calendar::getInstance);
		SUPPORTED_TYPES.put(AtomicInteger.class, () -> new AtomicInteger(Rnd.nextInt()));
		SUPPORTED_TYPES.put(AtomicLong.class, () -> new AtomicLong(Rnd.nextLong()));
	}
	
	
	public <T> Stream<T> generate(final Class<T> cls, final int count){
		if(EXCLUDED_CLASSES.contains(cls))
			return Stream.empty();
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(generateIterable(cls, count), Spliterator.ORDERED), false).filter(Objects::nonNull);
	}
	
	public <T> Iterator<T> generateIterable(final Class<T> cls, final int count){
		if(EXCLUDED_CLASSES.contains(cls))
			return Collections.emptyIterator();
		
		return new Iterator<T>() {

			int i = 0;
			
			@Override
			public boolean hasNext() {
				if(count < 0)
					return true;
				return ++i <= count;
			}

			@Override
			public T next() {
				return DatasetGenerator.this.generate(cls);
			}
		};
	}
	
	public <T> T generate(final Class<T> cls){
		for (Annotation an : cls.getAnnotations())
			if(EXCLUDED_CLASSES.contains(an.annotationType()))
				return null;
		
		final T instance = newInstance(cls);
		
		if(SUPPORTED_TYPES.containsKey(cls) || cls.isEnum())
			return instance;
		
		// Dont populate recursive datasets with data
		if(CURRENTLY_GENERATING.contains(cls))
			return instance;
		
		CURRENTLY_GENERATING.add(cls);
		Arrays.stream(cls.getDeclaredFields()).filter(FILTER_FUNCTION).forEach(f -> fillField(instance, f));
		CURRENTLY_GENERATING.remove(cls);
		
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
		
		INSTANTIATORS.putIfAbsent(cls, objenesis.getInstantiatorOf(cls));
		
		return (T) INSTANTIATORS.get(cls).newInstance();
	}
	
	/**
	 * Chached in Memoizer
	 */
	private final Predicate<Field> FILTER_FUNCTION = Memoizer.memoizePredicate(this::filterField);
	
	private boolean filterField(final Field f){
		if(f.isSynthetic())
			return false;
		
		int mods = f.getModifiers();
		if(Modifier.isStatic(mods) || Modifier.isNative(mods) || Modifier.isAbstract(mods) || Modifier.isInterface(mods))
			return false;
		
		Collection<Class<?>> annotations = Arrays.stream(f.getAnnotations()).map(Annotation::annotationType).collect(Collectors.toSet());
		annotations.retainAll(EXCLUDED_CLASSES);
		if(!annotations.isEmpty())
			return false;
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private <T, C, K, V> void fillField(final T object, final Field field){
		try {
			if(!field.isAccessible())
				field.setAccessible(true);
			
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
			
			if(c.isArray() && !k.isPrimitive() && !EXCLUDED_CLASSES.contains(k)){
				K[] ks = (K[]) field.get(object);
				for (int i = 0; i < COLLECTION_POPULATION; i++)
					ks[i] = generate(k);
			}
			else if(Objects.nonNull(k) && !EXCLUDED_CLASSES.contains(k)){
				if(Objects.nonNull(v) && !EXCLUDED_CLASSES.contains(v) && Map.class.isAssignableFrom(c)){
					Map<K,V> map = (Map<K,V>)field.get(object);
					for (int i = 0; i < COLLECTION_POPULATION; i++) {
						map.put(generate(k),generate(v));
					}
				}
				else if(Objects.isNull(v) && !EXCLUDED_CLASSES.contains(v) && Collection.class.isAssignableFrom(c)){
					Collection<K> col = (Collection<K>)field.get(object);
					generate(k,COLLECTION_POPULATION).forEach(col::add);
				}
			}
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public boolean addExclusion(final Class<?> cls){
		return EXCLUDED_CLASSES.add(cls);
	}
	
	public void clearExclusions(){
		EXCLUDED_CLASSES.clear();
	}
}
