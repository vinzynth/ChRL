package at.chrl.config.spring.test;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;
import org.springframework.context.annotation.Configuration;

/**
 * Example TestConfig for Unit Tests
 * Created by vinzynth on 21.12.15.
 */
@Configuration
public class TestConfig {

    static { ConfigUtil.load(TestConfig.class); }

    @Property(key = "at.chrl.config.ex.val1", defaultValue = "val1")
    public static String VAL_1;

    @Property(key = "at.chrl.config.ex.val2", defaultValue = "val2")
    public static String VAL_2;

    @Property(key = "at.chrl.config.ex.val3", defaultValue = "val3")
    public static String VAL_3;

}
