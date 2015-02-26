package at.chrl.orm.hibernate.configuration;

import java.io.PrintStream;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;

/**
 * 
 * @author Vinzynth 26.02.2015 - 22:46:11
 *
 */
public class HibernateServiceConfig {

	static {
		ConfigUtil.load(HibernateServiceConfig.class);
	}

	@Property(
			key = "hibernate.service.output_stream",
			defaultValue = "System.out",
			description = "Output stream for logging purposes. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
	public static PrintStream out;

	@Property(
			key = "hibernate.service.error_stream",
			defaultValue = "System.err",
			description = "Output stream for logging purposes. Handles exception outputs. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
	public static PrintStream err;

}
