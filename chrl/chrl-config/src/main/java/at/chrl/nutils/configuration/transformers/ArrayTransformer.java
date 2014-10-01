/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration.transformers - ArrayTransformer.java
 * Created: 10.08.2014 - 11:34:16
 */
package at.chrl.nutils.configuration.transformers;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import at.chrl.nutils.configuration.PropertyTransformer;
import at.chrl.nutils.configuration.PropertyTransformerFactory;
import at.chrl.nutils.configuration.TransformationException;
import at.chrl.utils.StringUtils;

/**
 * @author Vinzynth
 *
 */
public class ArrayTransformer<T> implements PropertyTransformer<T[]> {

	public static final ArrayTransformer<Object> SHARED_INSTANCE = new ArrayTransformer<>();
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.nutils.configuration.PropertyTransformer#transform(java.lang.String, java.lang.reflect.Field)
	 */
	@Override
	public T[] transform(String value, Field field) throws TransformationException {
		return transformInner(value, field.getType(), field);
	}
	

	
	@SuppressWarnings("unchecked")
	private T[] transformInner(String value, Class<?> type, Field field){
		try {			
			if(type.isArray() && type.getComponentType().isArray())
			{
				validBrackets(value);
				ArrayTransformer<?> pt = (ArrayTransformer<?>) PropertyTransformerFactory.newTransformer(type.getComponentType(), ArrayTransformer.class);
				
				return (T[]) Arrays.stream(parse(value)).map(p ->  pt.transformInner(p, type.getComponentType(), field)).toArray();
			}
				
			String spliter = File.pathSeparator;
			value = StringUtils.trim(value, '{', '}');
			if(!value.contains(spliter))
				spliter = ";";
			if(!value.contains(spliter))
				spliter = ",";
			if(!value.contains(spliter))
				spliter = " ";
			
			return	(T[]) Arrays.stream(value.split(spliter)).
					map(s -> transformElement(s, type, field)).
					toArray();
		} catch (Exception e) {
			throw new TransformationException(e);
		}
	}
	
	private T transformElement(String value, Class<?> clazz, Field field){
		@SuppressWarnings("unchecked")
		PropertyTransformer<T> pt = PropertyTransformerFactory.newTransformer(clazz.getComponentType(), PropertyTransformer.class);
		return pt.transform(value, field);
	}
	
	public static final String[] parse(final String value){
		String[] returnMe = new String[StringUtils.countMatches(value, '}')];
		int j = 0;
		int i = 0;
		while(value.indexOf("{", i) < value.indexOf("}", i)){
			returnMe[j++] = value.substring(value.indexOf("{", i) + 1, value.indexOf("}", i));
			i = value.indexOf("}", i)+1;
			if(value.indexOf("}", i) < 0)
				return returnMe;
		}
		return returnMe;
	}
	
	
	/**
	 * Checks if String v is parsable by {@link ArrayTransformer}
	 * 
	 * @throws {@link IllegalArgumentException} of v is not parsable
	 * 
	 * @param v
	 * 			String to check
	 * @return
	 * 			whether string is parsable or not
	 */
	public static final boolean validBrackets(final String v){
		
		String value = StringUtils.trim(v, ' ');
		
		if(!value.startsWith("{") || !value.endsWith("}"))
			throw new IllegalArgumentException("Syntax Erro: Property Value doesn't start with '{' or ends with '}'");
		
		if(StringUtils.countMatches(value, '{') != StringUtils.countMatches(value, '}'))
			throw new IllegalArgumentException("Syntax Error: Brackets not valid");
		
		if(StringUtils.countMatches(value, '{') > 1)
			if(!(value.contains("}, {") || value.contains("} ,{") || value.contains("},{") || value.contains("} , {") || value.contains("}{") || value.contains("} {")))
				throw new IllegalArgumentException("Syntax Error: Multiple Array Syntax not valid | try: \" ...}, {....\"");
		
		int i = 0;
		while(value.indexOf("{", i) < value.indexOf("}", i)){
			i = value.indexOf("}", i)+1;
			if(value.indexOf("}", i) < 0)
				return true;
		}
		throw new IllegalArgumentException("Syntax Error: Only 2D Arrays supportet now");
	}
	
}
