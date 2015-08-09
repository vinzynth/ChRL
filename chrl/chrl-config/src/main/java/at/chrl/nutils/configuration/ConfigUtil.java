/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration - Baseconfig.java
 * Created: 22.07.2014 - 22:41:11
 */
package at.chrl.nutils.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import at.chrl.nutils.configuration.ConfigurableProcessor;
import at.chrl.nutils.configuration.ConfigurationExporter;
import at.chrl.nutils.configuration.IConfigPrinter;
import at.chrl.nutils.configuration.printer.PropertyFileStreamPrinter;

/**
 * @author Vinzynth
 *
 */
public final class ConfigUtil {

	private static Collection<File> propertiesFiles;
	private static final LinkedList<Class<?>> classes = new LinkedList<Class<?>>();
	private static Properties[] props = new Properties[0];
	private static File configDirectory;
	private static Map<Class<?>, File> exportedFiles = new HashMap<>();

	static {
		propertiesFiles = FileUtils.listFiles(new File("."), new String[] { "properties" }, true);
		props = loadPropertiesFiles();
	}

	static final Properties[] loadPropertiesFiles() {
		try {
			return PropertiesUtils.load(propertiesFiles.toArray(new File[propertiesFiles.size()]));
		} catch (IOException e) {
			System.err.println("Error loading Properties");
			e.printStackTrace();
		}
		return props;
	}

	public synchronized static final void loadAndExport(final Class<?> targetClass) {
		load(targetClass);
		export(targetClass);
	}

	public synchronized static final void loadAndExport(final Object obj) {
		load(obj);
		export(obj.getClass());
	}

	public synchronized static final void export(final Class<?> classToExport) {
		File toExport = new File(configDirectory, classToExport.getSimpleName() + ".properties");
		export(classToExport, new PropertyFileStreamPrinter(toExport));
		exportedFiles.put(classToExport, toExport);
	}

	public synchronized static final void export(final Class<?> classToExport, final IConfigPrinter printer) {
		ConfigurationExporter.process(classToExport, printer, getLoadedProperties(classToExport));
	}

	public synchronized static final void export(final Object obj) {
		File toExport = new File(configDirectory, obj.getClass().getSimpleName() + ".properties");
		ConfigurationExporter.process(obj, new PropertyFileStreamPrinter(toExport));
		exportedFiles.put(obj.getClass(), toExport);
	}

	public synchronized static final void load(final Class<?> classToLoad) {
		ConfigurableProcessor.process(classToLoad, getLoadedProperties(classToLoad));
		if (!classes.contains(classToLoad))
			classes.add(classToLoad);
	}

	public synchronized static final void load(final Object obj) {
		Class<?> classToLoad = obj.getClass();
		ConfigurableProcessor.process(obj, getLoadedProperties(classToLoad));
		if (!classes.contains(classToLoad))
			classes.add(classToLoad);
	}

	private static Properties[] getLoadedProperties(final Class<?> classToLoad) {
		for (File file : propertiesFiles) {
			if (file.getName().equals(classToLoad.getSimpleName() + ".properties")) {
				try {
					return new Properties[] { PropertiesUtils.load(file) };
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}

	public static final void reload() {
		props = loadPropertiesFiles();
		for (Class<?> ie : classes) {
			load(ie);
		}
	}

	public static final void reload(final Class<?> classToReload) {
		props = loadPropertiesFiles();
		load(classToReload);
	}

	/**
	 * 
	 */
	private ConfigUtil() {
	}

	public synchronized static File getConfigDirectory() {
		return configDirectory;
	}

	public synchronized static void setConfigDirectory(final File configDirectory) {
		if (Objects.isNull(configDirectory))
			throw new IllegalArgumentException("Configuration folder passed as parameter is null");
		if (!configDirectory.exists())
			throw new IllegalArgumentException("Configuration folder does not exist: " + configDirectory.getAbsolutePath());
		if (!configDirectory.isDirectory())
			throw new IllegalArgumentException("Configuration folder is not a directory: " + configDirectory.getAbsolutePath());
		if (!configDirectory.canWrite())
			throw new IllegalArgumentException("Cannot write to configuration folder: " + configDirectory.getAbsolutePath());
		if (!configDirectory.canRead())
			throw new IllegalArgumentException("Cannot read from configuration folder: " + configDirectory.getAbsolutePath());

		ConfigUtil.configDirectory = configDirectory;

		propertiesFiles = FileUtils.listFiles(configDirectory, new String[] { "properties" }, true);

		reload();
		for (Class<?> class1 : classes) {
			export(class1);
		}
	}

	public static void setConfigDirectory(String configDirectory) {
		if (Objects.isNull(configDirectory))
			throw new IllegalArgumentException("Configuration folder passed as parameter is null");
		if (configDirectory.isEmpty())
			throw new IllegalArgumentException("Configuration folder passed as parameter is a empty String");
		setConfigDirectory(new File(configDirectory));
	}

	public synchronized static Properties getProperties(final Class<?> targetClass) {
		if (exportedFiles.containsKey(targetClass)) {
			try {
				return PropertiesUtils.load(exportedFiles.get(targetClass));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadAndExport(targetClass);
		if (exportedFiles.containsKey(targetClass)) {
			try {
				return PropertiesUtils.load(exportedFiles.get(targetClass));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Properties();
	}
}
