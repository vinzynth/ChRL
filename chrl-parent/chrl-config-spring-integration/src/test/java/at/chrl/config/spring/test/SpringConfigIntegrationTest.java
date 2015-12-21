package at.chrl.config.spring.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by bravestone on 21.12.15.
 */
@ContextConfiguration()
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringConfigIntegrationTest {

    @Test
    public void testName() throws Exception {
        System.out.println("Hello Junit Test - by Spring");
    }
}
