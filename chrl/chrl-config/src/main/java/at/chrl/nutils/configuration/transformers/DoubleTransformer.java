package at.chrl.nutils.configuration.transformers;

import java.lang.reflect.Field;

import at.chrl.nutils.configuration.PropertyTransformer;
import at.chrl.nutils.configuration.TransformationException;


/**
 * Transformes decimal that is represented as string to double
 * 
 * @author SoulKeeper
 */
public class DoubleTransformer implements PropertyTransformer<Double>
{
	/**
	 * Shared instance of this transformer. It's thread-safe so no need of multiple instances
	 */
	public static final DoubleTransformer	SHARED_INSTANCE	= new DoubleTransformer();

	/**
	 * Transforms string to required double
	 * 
	 * @param value
	 *            value that will be transformed
	 * @param field
	 *            value will be assigned to this field
	 * @return Double that represents transformed string
	 * @throws TransformationException
	 *             if something went wrong
	 */
	@Override
	public Double transform(String value, Field field) throws TransformationException
	{
		try
		{
			Double d = Double.parseDouble(value); 
			return d.doubleValue();
		}
		catch(Exception e)
		{
			throw new TransformationException(e);
		}
	}
}
