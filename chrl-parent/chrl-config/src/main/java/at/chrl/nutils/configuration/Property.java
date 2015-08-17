package at.chrl.nutils.configuration;

import java.io.PrintStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import at.chrl.nutils.configuration.transformers.*;

/**
 * This annotation is used to mark field that should be processed by
 * {@link commons.configuration.ConfigurableProcessor}<br>
 * <br>
 * 
 * This annotation is Documented, all definitions with it will appear in javadoc
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
	/**
	 * This string shows to {@link commons.configuration.ConfigurableProcessor}
	 * that init value of the object should not be overridden.
	 */
	public static final String DEFAULT_VALUE = "DO_NOT_OVERWRITE_INITIALIAZION_VALUE";

	/**
	 * Default description value
	 */
	public static final String DEFAULT_DESCRIPTION = "No Description available";

	/**
	 * Property name in configuration
	 * 
	 * @return name of the property that will be used
	 */
	public String key();

	/**
	 * PropertyTransformer to use.<br>
	 * List of automatically transformed types:<br>
	 * <ul>
	 * <li>{@link Boolean} and boolean by {@link BooleanTransformer}</li>
	 * <li>{@link Byte} and byte by {@link ByteTransformer}</li>
	 * <li>{@link Character} and char by {@link CharTransformer}</li>
	 * <li>{@link Short} and short by {@link ShortTransformer}</li>
	 * <li>{@link Integer} and int by {@link IntegerTransformer}</li>
	 * <li>{@link Float} and float by {@link FloatTransformer}</li>
	 * <li>{@link Long} and long by {@link LongTransformer}</li>
	 * <li>{@link Double} and double by {@link DoubleTransformer}</li>
	 * <li>{@link String} by {@link StringTransformer}</li>
	 * <li>{@link PrintStream} by {@link PrintStreamTransformer}
	 * <li>{@link Enum} and enum by {@link EnumTransformer}</li>
	 * <li>{@link java.io.File} by {@link FileTransformer}</li>
	 * <li>{@link java.net.InetSocketAddress} by
	 * {@link InetSocketAddressTransformer}</li>
	 * <li>{@link java.util.regex.Pattern} by {@link PatternTransformer}
	 * </ul>
	 * <p/>
	 * If your value is one of this types - just leave this field empty
	 * 
	 * @return returns class that will be used to transform value
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends PropertyTransformer> propertyTransformer() default PropertyTransformer.class;

	/**
	 * Represents default value that will be parsed if key not found. If this
	 * key equals(default) {@link #DEFAULT_VALUE} initial value of the object
	 * won't be overridden
	 * 
	 * @return default value of the property
	 */
	public String defaultValue() default DEFAULT_VALUE;

	/**
	 * Represents default value for property description. Equals
	 * {@link #DEFAULT_DESCRIPTION}
	 * 
	 * @return description the property
	 */
	public String description() default DEFAULT_DESCRIPTION;

	/**
	 * Represents a array with (hopefully) working example configurations. <br>
	 * if your target class is a enumeration, use
	 * 
	 * @return array wiith valid examples
	 */
	public String[] examples() default {};

	/**
	 * Represents method types for Function Transformer
	 * 
	 * @see {@link FunctionTransformer}
	 * 
	 * @return type arrays
	 */
	public Class<?>[] types() default {};

}
