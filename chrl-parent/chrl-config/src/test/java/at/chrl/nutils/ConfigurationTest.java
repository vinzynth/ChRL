/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - ConfigurationTest.java Created:
 * 19.07.2014 - 18:44:54
 */
package at.chrl.nutils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import at.chrl.nutils.configuration.ConfigurableProcessor;
import at.chrl.nutils.configuration.PropertiesUtils;
import at.chrl.nutils.configuration.Property;

/**
 * @author Vinzynth
 *
 */
public class ConfigurationTest {

	@Test
	public void testLoad() throws IOException {
		Properties[] props;
		String file = "config" + File.separator;
//		System.out.println(file);
		props = PropertiesUtils.loadAllFromDirectory(file);
		ConfigurableProcessor.process(ExampleConfig.class, props);
		assertTrue(props.length > 0);
	}

	@Test
	public void testLoadDefault() throws IOException {
		ConfigurableProcessor.process(ExampleConfig.class, PropertiesUtils.load("config" + File.separator + "example2.properties"));
//		System.out.println(ExampleConfig.LOGIN_TRY_BEFORE_BAN);
		assertTrue(ExampleConfig.LOGIN_TRY_BEFORE_BAN == 5);
	}

	public static class ExampleConfig {

		/**
		 * Login Server port
		 */
		@Property(key = "loginserver.client.port", defaultValue = "2106")
		public static int LOGIN_PORT;

		/**
		 * Login Server bind ip
		 */
		@Property(key = "loginserver.client.host", defaultValue = "*")
		public static String LOGIN_BIND_ADDRESS;

		/**
		 * Login Server port
		 */
		@Property(key = "loginserver.gameserver.port", defaultValue = "9014")
		public static int GAME_PORT;

		/**
		 * Login Server bind ip
		 */
		@Property(key = "loginserver.gameserver.host", defaultValue = "*")
		public static String GAME_BIND_ADDRESS;

		/**
		 * Number of trys of login before ban
		 */
		@Property(key = "loginserver.client.logintrybeforeban",
				defaultValue = "5")
		public static int LOGIN_TRY_BEFORE_BAN;

		/**
		 * Ban time in minutes
		 */
		@Property(key = "loginserver.client.bantimeforbruteforcing",
				defaultValue = "15")
		public static int WRONG_LOGIN_BAN_TIME;

		/**
		 * Number of Threads that will handle io read (>= 0)
		 */
		@Property(key = "loginserver.nio.threads.read", defaultValue = "0")
		public static int NIO_READ_THREADS;

		/**
		 * Number of Threads that will handle io write (>= 0)
		 */
		@Property(key = "loginserver.nio.threads.write", defaultValue = "0")
		public static int NIO_WRITE_THREADS;

		/**
		 * Should server automaticly create accounts for users or not?
		 */
		@Property(key = "loginserver.accounts.autocreate",
				defaultValue = "true")
		public static boolean ACCOUNT_AUTO_CREATION;
	}

}
