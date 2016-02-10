package at.chrl.git;

import at.chrl.git.config.DefaultTestConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * Created by ChRL on 10.02.16.
 * Project: ChRL
 * Package: at.chrl.git
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultTestConfiguration.class)
public class GitRepositoryProviderTest {

    @Autowired
    GitRepositoryProvider gitRepositoryProvider;

    @Test
    public void testGetRepository() throws Exception {
        Assert.assertEquals(gitRepositoryProvider.getRepository("file:test" + File.separator + ".git"), gitRepositoryProvider.getRepository("file:test" + File.separator + ".git"));
        Assert.assertNotEquals(gitRepositoryProvider.getRepository("file:test1" + File.separator + ".git"), gitRepositoryProvider.getRepository("file:test2" + File.separator + ".git"));
    }


}
