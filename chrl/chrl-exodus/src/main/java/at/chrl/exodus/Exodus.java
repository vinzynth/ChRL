/**
 * (C) ChRL 2014 - chrl-exodus - at.chrl.exodus - Exodus.java Created:
 * 03.08.2014 - 14:03:00
 */
package at.chrl.exodus;

import java.io.PrintStream;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;

/**
 * @author Vinzynth
 *
 */
public class Exodus {

	static {
		ConfigUtil.loadAndExport(Exodus.class);
	}

	@Property(
			key = "exodus.output_stream",
			defaultValue = "System.out",
			description = "Output stream for logging purposes. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
	public static PrintStream out;

	@Property(
			key = "exodus.error_stream",
			defaultValue = "System.err",
			description = "Output stream for logging purposes. Handles exception outputs. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
	public static PrintStream err;

}
