/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration - ConfigurationExporter.java
 * Created: 03.08.2014 - 14:21:20
 */
package at.chrl.nutils.configuration;

import static at.chrl.nutils.Constants.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

/**
 * @author Vinzynth
 *
 */
public class ConfigurationExporter {

	@SuppressWarnings("rawtypes")
	public static void process(Object object, IConfigPrinter os, Properties... props)
	{
		Class clazz;

		if(object instanceof Class)
		{
			clazz = (Class) object;
			object = null;
		}
		else
		{
			clazz = object.getClass();
		}

		process(clazz, object, os, props);
	}
	
	@SuppressWarnings("rawtypes")
	protected static void process(Class clazz, Object obj, IConfigPrinter os, Properties... props) {
		processFields(clazz, obj, os, props);
	
		// Interfaces can't have any object fields, only static
		// So there is no need to parse interfaces for instances of objects
		// Only classes (static fields) can be located in interfaces
		if(obj == null)
		{
			for(Class itf : clazz.getInterfaces())
			{
				process(itf, obj, os, props);
			}
		}
	
		Class superClass = clazz.getSuperclass();
		if(superClass != null && superClass != Object.class)
		{
			process(superClass, obj, os, props);
		}
	}

	@SuppressWarnings("rawtypes")
	static void processFields(Class clazz, Object obj, IConfigPrinter os, Properties... props)
	{
		for(Field f : clazz.getDeclaredFields())
		{
//			// Static fields should not be modified when processing object
//			if(Modifier.isStatic(f.getModifiers()) && obj != null)
//			{
//				continue;
//			}
//
//			// Not static field should not be processed when parsing class
//			if(!Modifier.isStatic(f.getModifiers()) && obj == null)
//			{
//				continue;
//			}

			if(f.isAnnotationPresent(Property.class))
			{
				// Final fields should not be processed
				if(Modifier.isFinal(f.getModifiers()))
				{
					RuntimeException re = new RuntimeException("Attempt to proceed final field " + f.getName()
						+ " of class " + clazz.getName());
					log.error(re.toString());
					throw re;
				}
				else
				{
					processField(f, obj, os, props);
				}
			}
		}
	}

	private static void processField(Field f, Object obj, IConfigPrinter os, Properties... props)
	{
		boolean oldAccessible = f.isAccessible();
		f.setAccessible(true);
		try
		{
			Property property = f.getAnnotation(Property.class);
			if(ConfigurableProcessor.isKeyPresent(property.key(), props))
				os.printConfigField(property, ConfigurableProcessor.findPropertyByKey(property.key(), props));
			else
				os.printConfigField(property, property.defaultValue());
		}
		catch(Exception e)
		{
			RuntimeException re = new RuntimeException("Can't print field " + f.getName() + " of class "
				+ f.getDeclaringClass(), e);
			log.error(re.toString());
			throw re;
		}
		f.setAccessible(oldAccessible);
	}
}
