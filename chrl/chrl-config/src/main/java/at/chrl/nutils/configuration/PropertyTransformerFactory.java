package at.chrl.nutils.configuration;

import java.io.File;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import at.chrl.nutils.ClassUtils;
import at.chrl.nutils.configuration.transformers.ArrayTransformer;
import at.chrl.nutils.configuration.transformers.BiFunctionTransformer;
import at.chrl.nutils.configuration.transformers.BooleanTransformer;
import at.chrl.nutils.configuration.transformers.ByteTransformer;
import at.chrl.nutils.configuration.transformers.CharTransformer;
import at.chrl.nutils.configuration.transformers.ClassTransformer;
import at.chrl.nutils.configuration.transformers.DateTransformer;
import at.chrl.nutils.configuration.transformers.DoubleTransformer;
import at.chrl.nutils.configuration.transformers.EnumTransformer;
import at.chrl.nutils.configuration.transformers.FileTransformer;
import at.chrl.nutils.configuration.transformers.FloatTransformer;
import at.chrl.nutils.configuration.transformers.FunctionTransformer;
import at.chrl.nutils.configuration.transformers.InetSocketAddressTransformer;
import at.chrl.nutils.configuration.transformers.IntegerTransformer;
import at.chrl.nutils.configuration.transformers.LongTransformer;
import at.chrl.nutils.configuration.transformers.PatternTransformer;
import at.chrl.nutils.configuration.transformers.PrintStreamTransformer;
import at.chrl.nutils.configuration.transformers.ShortTransformer;
import at.chrl.nutils.configuration.transformers.StringTransformer;

/**
 * This class is responsible for creating property transformers. Each time it
 * creates new instance of custom property transformer, but for build-in it uses
 * shared instances to avoid overhead
 * 
 * @author SoulKeeper
 */
public class PropertyTransformerFactory {
	/**
	 * Returns property transformer or throws
	 * {@link at.chrl.nutils.configuration.TransformationException} if can't
	 * create new one.
	 * 
	 * @param clazzToTransform
	 *            Class that will is going to be transformed
	 * @param tc
	 *            {@link at.chrl.nutils.configuration.PropertyTransformer} class
	 *            that will be instantiated
	 * @return instance of PropertyTransformer
	 * @throws TransformationException
	 *             if can't instantiate
	 *             {@link at.chrl.nutils.configuration.PropertyTransformer}
	 */
	@SuppressWarnings({ "rawtypes" })
	public static PropertyTransformer newTransformer(Class clazzToTransform, Class<? extends PropertyTransformer> tc) throws TransformationException {

		// Just a hack, we can't set null to annotation value
		if (tc == PropertyTransformer.class) {
			tc = null;
		}

		if (tc != null) {
			try {
				return tc.newInstance();
			} catch (Exception e) {
				throw new TransformationException("Can't instantiate property transfromer", e);
			}
		} else {
			if (clazzToTransform == Boolean.class || clazzToTransform == Boolean.TYPE) {
				return BooleanTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Byte.class || clazzToTransform == Byte.TYPE) {
				return ByteTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Character.class || clazzToTransform == Character.TYPE) {
				return CharTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Double.class || clazzToTransform == Double.TYPE) {
				return DoubleTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Float.class || clazzToTransform == Float.TYPE) {
				return FloatTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Integer.class || clazzToTransform == Integer.TYPE) {
				return IntegerTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Long.class || clazzToTransform == Long.TYPE) {
				return LongTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Short.class || clazzToTransform == Short.TYPE) {
				return ShortTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == String.class) {
				return StringTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform.isEnum()) {
				return EnumTransformer.SHARED_INSTANCE;
				// TODO: Implement
				// } else if (ClassUtils.isSubclass(clazzToTransform,
				// Collection.class)) {
				// return new CollectionTransformer();
				// } else if (clazzToTransform.isArray()) {
				// return new ArrayTransformer();
			} else if (clazzToTransform.isArray()) {
				return ArrayTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Function.class || ClassUtils.isSubclass(clazzToTransform, Function.class)) {
				return FunctionTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == BiFunction.class || ClassUtils.isSubclass(clazzToTransform, BiFunction.class)) {
				return BiFunctionTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == File.class) {
				return FileTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Date.class) {
				return DateTransformer.SHARED_INSTANCE;
			} else if (ClassUtils.isSubclass(clazzToTransform, InetSocketAddress.class)) {
				return InetSocketAddressTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Pattern.class) {
				return PatternTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == Class.class) {
				return ClassTransformer.SHARED_INSTANCE;
			} else if (clazzToTransform == PrintStream.class) {
				return PrintStreamTransformer.SHARED_INSTANCE;
			} else {
				throw new TransformationException("Transformer not found for class " + clazzToTransform.getName());
			}
		}
	}
}
