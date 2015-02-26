/**
 * @author bravestone Aug 25, 2014 - 11:16:31 AM chrl-config at.chrl.nutils
 */
package at.chrl.nutils;

import java.util.Arrays;

import org.junit.Test;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;

/**
 * @author bravestone
 *
 */
public class AbstractConfigTest {

	@Test
	public void test() {

		SubConfig sc = new SubConfig();
		ConfigUtil.loadAndExport(sc);

		System.out.println(sc.GAME_BIND_ADDRESS);

		Arrays.stream(sc.ARGS).forEach(System.out::println);
	}

	public abstract class BaseConfig {

		@Property(key = "test.flags", defaultValue = "0;1;0;1;1;1;0")
		public boolean[] FLAGS;

		@Property(
				key = "test.args",
				defaultValue = "clean compile package install -U -T 3C -DskipTests")
		public String[] ARGS;

	}

	public class SubConfig extends BaseConfig {

		@Property(key = "loginserver.gameserver.host", defaultValue = "*")
		public String GAME_BIND_ADDRESS;
	}

}
