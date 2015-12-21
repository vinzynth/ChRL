package at.chrl.config.spring.test;

import at.chrl.config.spring.ConfigSourceConfigurer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TestCase for Spring Integration
 *
 * Created by vinzynth on 21.12.15.
 */
@ContextConfiguration(classes = { TestConfig.class, ConfigSourceConfigurer.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringConfigIntegrationTest {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
        return new ConfigSourceConfigurer();
    }

    @Value("${at.chrl.config.ex.val1}")
    private String val1;
    @Value("${at.chrl.config.ex.val2}")
    private String val2;
    @Value("${at.chrl.config.ex.val3}")
    private String val3;
    @Value("${at.chrl.config.ex.val4:default}")
    private String val4;

    @Test
    public void testName() throws Exception {
        System.out.println();
        Assert.assertEquals(val1, "val1");
        Assert.assertEquals(val2, "val2");
        Assert.assertEquals(val3, "val3");
        Assert.assertEquals(val4, "default");
    }
}
