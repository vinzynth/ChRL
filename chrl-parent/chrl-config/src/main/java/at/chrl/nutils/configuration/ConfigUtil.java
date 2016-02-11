/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration - Baseconfig.java
 * Created: 22.07.2014 - 22:41:11
 */
package at.chrl.nutils.configuration;

import at.chrl.nutils.configuration.listener.ConfigEventListener;
import at.chrl.nutils.configuration.printer.PropertyFileStreamPrinter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Vinzynth
 *
 */
public final class ConfigUtil {

	private Collection<File> propertiesFiles;
	private final LinkedList<Class<?>> classes = new LinkedList<>();
	private Properties[] props = new Properties[0];
	private File configDirectory;
	private Map<Class<?>, File> exportedFiles = new HashMap<>();
    private Collection<ConfigEventListener> configEventListeners = new ArrayList<>();

	public ConfigUtil(){
		propertiesFiles = FileUtils.listFiles(new File("."), new String[] { "properties" }, true);
		props = loadPropertiesFiles();
	}

    final Properties[] loadPropertiesFiles() {
		try {
			return PropertiesUtils.load(propertiesFiles.toArray(new File[propertiesFiles.size()]));
		} catch (IOException e) {
			System.err.println("Error loading Properties");
			e.printStackTrace();
		}
		return props;
	}

	public synchronized final void loadAndExport(final Class<?> targetClass) {
		load(targetClass);
		export(targetClass);
	}

	public synchronized final void loadAndExport(final Object obj) {
		load(obj);
		export(obj.getClass());
	}

	public synchronized final void export(final Class<?> classToExport) {
		File toExport = new File(configDirectory, classToExport.getSimpleName() + ".properties");
		export(classToExport, new PropertyFileStreamPrinter(toExport));
		exportedFiles.put(classToExport, toExport);
        for (ConfigEventListener cel : configEventListeners) {
            try {
                cel.onExportedConfigClass(classToExport);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
	}

	public synchronized final void export(final Class<?> classToExport, final IConfigPrinter printer) {
		ConfigurationExporter.process(classToExport, printer, getLoadedProperties(classToExport));
	}

	public synchronized final void export(final Object obj) {
		File toExport = new File(configDirectory, obj.getClass().getSimpleName() + ".properties");
		ConfigurationExporter.process(obj, new PropertyFileStreamPrinter(toExport));
		exportedFiles.put(obj.getClass(), toExport);
        for (ConfigEventListener cel : configEventListeners) {
            try {
                cel.onExportedConfigObject(obj);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
	}

	public synchronized final void load(final Class<?> classToLoad) {
        for (ConfigEventListener cel : configEventListeners) {
            try {
                cel.beforeOnLoadedConfigClass(classToLoad);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
		ConfigurableProcessor.process(classToLoad, getLoadedProperties(classToLoad));
		if (!classes.contains(classToLoad))
			classes.add(classToLoad);
        for (ConfigEventListener cel : configEventListeners) {
            try {
                cel.onLoadedConfigClass(classToLoad);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

	public synchronized final void load(final Object obj) {
        for (ConfigEventListener cel : configEventListeners) {
            try {
                cel.beforeOnLoadedConfigObject(obj);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
		Class<?> classToLoad = obj.getClass();
		ConfigurableProcessor.process(obj, getLoadedProperties(classToLoad));
		if (!classes.contains(classToLoad))
			classes.add(classToLoad);
        for (ConfigEventListener cel : configEventListeners) {
            try {
                cel.onLoadedConfigObject(obj);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
	}

	private Properties[] getLoadedProperties(final Class<?> classToLoad) {
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

	public final void reload() {
		props = loadPropertiesFiles();
        classes.forEach(this::load);
	}

	public final void reload(final Class<?> classToReload) {
		props = loadPropertiesFiles();
		load(classToReload);
	}

	public synchronized File getConfigDirectory() {
		return configDirectory;
	}

	public synchronized void setConfigDirectory(final File configDirectory) {
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

		this.configDirectory = configDirectory;

		propertiesFiles = FileUtils.listFiles(configDirectory, new String[] { "properties" }, true);

		reload();
        classes.forEach(this::export);
	}

	public void setConfigDirectory(String configDirectory) {
		if (Objects.isNull(configDirectory))
			throw new IllegalArgumentException("Configuration folder passed as parameter is null");
		if (configDirectory.isEmpty())
			throw new IllegalArgumentException("Configuration folder passed as parameter is a empty String");
		setConfigDirectory(new File(configDirectory));
	}

	public synchronized Properties getProperties(final Class<?> targetClass) {
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

    public boolean addConfigEventListener(final ConfigEventListener listener){
        return !configEventListeners.contains(listener) && configEventListeners.add(listener);
    }

    public boolean removeConfigEventListener(final ConfigEventListener listener){
        return configEventListeners.contains(listener) && configEventListeners.remove(listener);
    }

    /**
     * Deletes config files afterwards
     */
    public void cleanupConfig(){
        exportedFiles.values().forEach(f -> {
            try {
                f.delete();
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        File f = getConfigDirectory();
        if(f != null && f.isDirectory() && f.list().length <= 0){
            try {
                FileUtils.deleteDirectory(f);
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private static class SingletonHoler{
        private static final ConfigUtil instance = new ConfigUtil();
    }

    public static ConfigUtil getInstance(){
        return SingletonHoler.instance;
    }
}
