/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.iryna - IrynaConfig.java Created:
 * 29.07.2014 - 22:10:14
 */
package at.chrl.iryna;

import java.io.PrintStream;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;

/**
 * @author Vinzynth
 *
 */
public final class IrynaConfig {

	static {
		ConfigUtil.loadAndExport(IrynaConfig.class);
	}

	@Property(
			key = "iryna.bind_address",
			defaultValue = "127.0.0.1",
			description = "Bind address for this server. Usually localhost oder domain of the hosting machine.")
	public static String BIND_ADDRESS;

	@Property(
			key = "iryna.port",
			defaultValue = "29714",
			description = "Port to listen for connections. Make sure your firewall is configured properly.")
	public static int PORT;

	@Property(key = "iryna.connection_name", defaultValue = "Iryna Connection",
			description = "Connection name which show up in the application")
	public static String CONNECTION_NAME;

	@Property(
			key = "iryna.dispatcher_thread_count",
			defaultValue = "3",
			description = "Thread Count for read and write dispatcher. Raise value properly if server sends tons of packets and lag occurs.")
	public static int DISPATCHER_THREAD_COUNT;

	@Property(
			key = "iryna.packet_processor_min_threads",
			defaultValue = "1",
			description = "Minimum thread count for packet processor. The packet processer handles incoming packets and calls the runImpl() method. Must be less or equal to Maximum thread count.")
	public static int PACKET_PROCESSOR_MIN_THREADS;

	@Property(
			key = "iryna.packet_processor_max_threads",
			defaultValue = "8",
			description = "Maximum thread count for packet processor. The packet processor handles incoming packets nad calls the runImpl() method. Must be greater or equal to Minimum thead count.")
	public static int PACKET_PROCESSOR_MAX_THREADS;

	public static class Iryna {

		static {
			ConfigUtil.loadAndExport(Iryna.class);
		}

		@Property(
				key = "iryna.output_stream",
				defaultValue = "System.out",
				description = "Output stream for logging purposes. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
		public static PrintStream out;

		@Property(
				key = "iryna.error_stream",
				defaultValue = "System.err",
				description = "Output stream for logging purposes. Handles exception outputs. Turn off with \"nop\" or change to a valid filepath for logging into a seperate log file")
		public static PrintStream err;

		@Property(
				key = "iryna.packet_log",
				defaultValue = "System.out",
				description = "Output stream for packet events. Turn off with \"nop\" or change to a valid filepath for logging in a seperate log file")
		public static PrintStream PACKET_LOG;
	}

}
