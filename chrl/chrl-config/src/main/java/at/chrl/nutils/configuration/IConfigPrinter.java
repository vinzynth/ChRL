/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils.configuration - IConfigPrinter.java
 * Created: 03.08.2014 - 16:46:08
 */
package at.chrl.nutils.configuration;

/**
 * @author Vinzynth
 *
 */
public interface IConfigPrinter {
	
	/**
	 * Gets called from {@link ConfigurationExporter} during export iteration over fileds from property classes
	 * @param property
	 * 				property to print
	 * @param currentValue
	 * 				current value in application
	 */
	public <T> void printConfigField(Property property, String currentValue, Class<T> annotatedType);
}
