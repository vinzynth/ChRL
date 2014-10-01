package at.chrl.nutils.configuration;

import java.lang.reflect.Field;

/**
 * This insterface represents property transformer, each transformer should
 * implement it.
 * 
 * @author SoulKeeper
 * @param <T>
 *            Type of returned value
 */
public interface PropertyTransformer<T> {

	/**
	 * This method actually transforms value to object instance
	 * 
	 * @param value
	 *            value that will be transformed
	 * @param field
	 *            value will be assigned to this field
	 * @return result of transformation
	 * @throws TransformationException
	 *             if something went wrong
	 */
	public T transform(String value, Field field) throws TransformationException;

	/**
	 * @see {@link PropertyTransformer#transform(String, Field)}
	 * 
	 * @param value
	 *            value that will be transformed
	 * @param field
	 *            value will be assigned to this field
	 * @param types
	 *            additional types for Function Transformer
	 * @return result of transformation
	 * @throws TransformationException
	 *            if something went wrong
	 */
	public default T transform(String value, Field field, Class<?>... types) throws TransformationException {
		return transform(value, field);
	}
}
