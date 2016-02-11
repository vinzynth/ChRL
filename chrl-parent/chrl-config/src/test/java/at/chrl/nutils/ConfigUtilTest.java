/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - ConfigurationTest.java Created:
 * 19.07.2014 - 18:44:54
 */
package at.chrl.nutils;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;
import at.chrl.nutils.configuration.printer.PrintStreamPrinter;
import at.chrl.nutils.configuration.printer.PropertyFileStreamPrinter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

/**
 * @author Vinzynth
 *
 */
public class ConfigUtilTest {

	@Test
	public void testLoad() throws IOException {
		ConfigUtil.getInstance().reload();
	}

	@Test
	public void testLoadProperty() throws IOException {
//		System.out.println(ExampleConfig.LOGIN_PORT);
		assertTrue(ExampleConfig.LOGIN_PORT == 7777);
	}

	@Test
	public void testLoadDefault() throws IOException {
//		System.out.println(ExampleConfig.GAME_PORT);
		assertTrue(ExampleConfig.GAME_PORT == 9014);
//
//		for (Object b : ExampleConfig.FLAGS) {
//			System.out.println(b);
//			System.out.println(b.getClass());
//		}
//
//		Arrays.stream(ExampleConfig.ARGS).forEach(System.out::println);
//		Arrays.stream(ExampleConfig.ARGS_SINGLE).forEach(System.out::println);
	}

	@Test
	public void testPrint() {
		ConfigUtil.getInstance().export(ExampleConfig.class, new PrintStreamPrinter(System.out));
		ConfigUtil.getInstance().export(ExampleConfig.class, new PropertyFileStreamPrinter(new File(ExampleConfig.class.getSimpleName() + ".properties")));
	}

	@Test
	public void test2DArray() {
//		for (int i = 0; i < ExampleConfig.FLAGS_2.length; i++) {
//			System.out.println("i = " + i + " <");
//			for (int j = 0; j < ExampleConfig.FLAGS_2[i].length; j++) {
//				System.out.println("j = " + j + " -> " + ExampleConfig.FLAGS_2[i][j]);
//			}
//			System.out.println(" >");
//		}
	}

	@Test
	public void testMathLog() {
//		Double d = 5d;
//		System.out.println(ExampleConfig.MATH.apply(d));
//		System.out.println(Math.log(d));
//		System.out.println(ExampleConfig.MATH_2.apply(5d, 3d));
	}

	public static class ExampleConfig {

		static {
			ConfigUtil.getInstance().load(ExampleConfig.class);
		}

		@Property(key = "test.math", defaultValue = "java.lang.Math::sqrt",
				types = { Double.class })
		public static Function<Double, ?> MATH;

		@Property(key = "test.math2", defaultValue = "java.lang.Math::max",
				types = { Double.class, Double.class })
		public static BiFunction<Double, Double, ?> MATH_2;

		@Property(key = "test.flags2", defaultValue = "{0;1;0;1},{1;1;0}")
		public static Double[][] FLAGS_2;

		@Property(key = "test.flags", defaultValue = "0;1;0;1;1;1;0")
		public static boolean[] FLAGS;

		@Property(
				key = "test.args",
				defaultValue = "clean compile package install -U -T 3C -DskipTests")
		public static String[] ARGS;

		@Property(key = "test.argsSingle", defaultValue = "clean")
		public static String[] ARGS_SINGLE;

		/**
		 * Login Server port
		 */
		@Property(
				key = "loginserver.client.port",
				defaultValue = "2106",
				description = "Login port of login server" + "...blalalbairwetgh" + "pauwegfuqwpgewbe" + "puwpügq3bgob329ßtasga" + "h9rehwehweh" + "erherherhwerh" + "werhwerhqwvjkqehrgrweh" + "erhwerljghörlwehwerh" + "erehrwttjtzjurzku")
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
