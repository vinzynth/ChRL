/**
 * (C) ChRL 2013 - chrl-utils - at.chrl.utils - ClassFinder.java Created:
 * 30.12.2013 - 14:56:02
 */
package at.chrl.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * use {@link at.chrl.nutils.ClassUtils} instead
 * 
 * @author Vinzynth 08.11.2014 - 16:38:14
 *
 */
@Deprecated
public final class ClassFinder {

	private final static char DOT = '.';
	private final static char SLASH = '/';
	private final static String CLASS_SUFFIX = ".class";
	private final static String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the given '%s' package exists?";

	public static List<Class<?>> find(final String scannedPackage) {
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final String scannedPath = scannedPackage.replace(DOT, SLASH);
		final Enumeration<URL> resources;
		try {
			resources = classLoader.getResources(scannedPath);
		} catch (IOException e) {
			throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage), e);
		}
		final List<Class<?>> classes = new LinkedList<Class<?>>();
		while (resources.hasMoreElements()) {
			final File file = new File(resources.nextElement().getFile());
			classes.addAll(find(file, scannedPackage.substring(0, scannedPackage.lastIndexOf(DOT) > 0 ? scannedPackage.lastIndexOf(DOT) : scannedPackage.length())));
		}
		return classes;
	}

	private static List<Class<?>> find(final File file, final String scannedPackage) {
		final List<Class<?>> classes = new LinkedList<Class<?>>();
		final String resource = scannedPackage + DOT + file.getName();
		if (file.isDirectory()) {
			for (File nestedFile : file.listFiles()) {
				classes.addAll(find(nestedFile, resource));
			}
		} else if (resource.endsWith(CLASS_SUFFIX)) {
			final int endIndex = resource.length() - CLASS_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			try {
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException | NoClassDefFoundError ignore) {
			}
		}
		return classes;
	}

	public static <T> List<Class<? extends T>> getClassesExtending(Class<T> c) {
		return getClassesExtending(c, c.getPackage());
	}

	public static <T> List<Class<? extends T>> getClassesExtending(Class<T> c, Type t, Package p) {
		LinkedList<Class<? extends T>> returnMe = new LinkedList<Class<? extends T>>();
		for (Class<? extends T> ie : getClassesExtending(c, p)) {
			if (ie.getGenericSuperclass() == null)
				continue;
			Class<?> cc = ie;
			String typeString = "";
			while (!(typeString = cc.getGenericSuperclass().toString()).contains("<") && cc != Object.class)
				cc = cc.getSuperclass();
			if (cc == Object.class)
				continue;
			final String generic = cc.getGenericSuperclass().toString().substring(typeString.indexOf("<") + 1, typeString.lastIndexOf(">"));
			if (t.toString().contains(generic))
				returnMe.add(ie);
		}
		return returnMe;
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> List<Class<? extends T>> getClassesExtending(Class<T> c, Package p) {
		List<Class<? extends T>> returnMe = new LinkedList<Class<? extends T>>();
		try {
			Field f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);
			List<Class<?>> classes = ClassFinder.find(p.getName());
			for (Class<?> class1 : classes) {
				final Class<?> sClass = class1;
				while ((class1 = class1.getSuperclass()) != Object.class) {
					if (class1 == null)
						break;
					if (c.equals(class1))
						returnMe.add((Class<? extends T>) sClass);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMe;
	}
}
