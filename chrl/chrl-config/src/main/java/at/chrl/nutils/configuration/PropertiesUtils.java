package at.chrl.nutils.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

/**
 * This class is designed to simplify routine job with properties
 * 
 * @author SoulKeeper
 */
public class PropertiesUtils
{
	/**
	 * Loads properties by given file
	 * 
	 * @param file
	 *            filename
	 * @return loaded properties
	 * @throws java.io.IOException
	 *             if can't load file
	 */
	public static Properties load(String file) throws IOException
	{
		return load(new File(file));
	}

	/**
	 * Loads properties by given file
	 * 
	 * @param file
	 *            filename
	 * @return loaded properties
	 * @throws java.io.IOException
	 *             if can't load file
	 */
	public static Properties load(File file) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(file)), 8192 << 4);
		Properties p = new Properties();
		p.load(br);
		br.close();
		return p;
	}

	/**
	 * Loades properties from given files
	 * 
	 * @param files
	 *            list of string that represents files
	 * @return array of loaded properties
	 * @throws IOException
	 *             if was unable to read properties
	 */
	public static Properties[] load(String... files) throws IOException
	{
		Properties[] result = new Properties[files.length];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = load(files[i]);
		}
		return result;
	}

	/**
	 * Loades properties from given files
	 * 
	 * @param files
	 *            list of files
	 * @return array of loaded properties
	 * @throws IOException
	 *             if was unable to read properties
	 */
	public static Properties[] load(File... files) throws IOException
	{
		Properties[] result = new Properties[files.length];
		for(int i = 0; i < result.length; i++)
		{
			result[i] = load(files[i]);
		}
		return result;
	}

	/**
	 * Loads non-recursively all .property files form directory
	 * 
	 * @param dir
	 *            string that represents directory
	 * @return array of loaded properties
	 * @throws IOException
	 *             if was unable to read properties
	 */
	public static Properties[] loadAllFromDirectory(String dir) throws IOException
	{
		return loadAllFromDirectory(new File(dir), false);
	}

	/**
	 * Loads non-recursively all .property files form directory
	 * 
	 * @param dir
	 *            directory
	 * @return array of loaded properties
	 * @throws IOException
	 *             if was unable to read properties
	 */
	public static Properties[] loadAllFromDirectory(File dir) throws IOException
	{
		return loadAllFromDirectory(dir, false);
	}

	/**
	 * Loads all .property files form directory
	 * 
	 * @param dir
	 *            string that represents directory
	 * @param recursive
	 *            parse subdirectories or not
	 * @return array of loaded properties
	 * @throws IOException
	 *             if was unable to read properties
	 */
	public static Properties[] loadAllFromDirectory(String dir, boolean recursive) throws IOException
	{
		return loadAllFromDirectory(new File(dir), recursive);
	}

	/**
	 * Loads all .property files form directory
	 * 
	 * @param dir
	 *            directory
	 * @param recursive
	 *            parse subdirectories or not
	 * @return array of loaded properties
	 * @throws IOException
	 *             if was unable to read properties
	 */
	public static Properties[] loadAllFromDirectory(File dir, boolean recursive) throws IOException
	{
		Collection<File> files = FileUtils.listFiles(dir, new String[] { "properties" }, recursive);
		return load(files.toArray(new File[files.size()]));
	}
	
	public static Properties filterEmtpyValues(final Properties props){
		Properties returnMe = new Properties();
		for (Entry<Object, Object> iterable_element : props.entrySet()) {
			if(!((String)iterable_element.getValue()).isEmpty()){
				returnMe.setProperty((String)iterable_element.getKey(), (String)iterable_element.getValue());
			}
		}
		return returnMe;
	}
}
