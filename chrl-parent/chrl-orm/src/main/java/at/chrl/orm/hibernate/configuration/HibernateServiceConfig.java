/**
 * @author bravestone
 * Aug 28, 2014 - 4:00:36 PM
 * bravestone-hibernate
 * com.bravestone.hibernate.configuration
 */
package at.chrl.orm.hibernate.configuration;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;

import java.io.PrintStream;

/**
 * @author bravestone
 *
 */
public class HibernateServiceConfig {
	
	static{ ConfigUtil.getInstance().load(HibernateServiceConfig.class); }
	
	@Property(key = "hibernate.service.output_stream", defaultValue = "System.out", description = "Output stream for logging purposes. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
	public static PrintStream out;
	
	@Property(key = "hibernate.service.error_stream", defaultValue = "System.err", description = "Output stream for logging purposes. Handles exception outputs. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
	public static PrintStream err;
	
}
