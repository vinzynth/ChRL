package at.chrl.git;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by chrl on 10.02.16.
 *
 * Git Service Testclass
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = DefaultTestConfiguration.class)
public class GitRepositoryTest {

//    @Autowired
    GitRepositoryProvider gitRepositoryProvider;

//    @Test
    public void testCreateFile() throws Exception {
        final GitRepository repository = gitRepositoryProvider.getRepository("file:test");

        final File file = new File(repository.getParent(), "test.txt");
        final FileWriter fileWriter = new FileWriter(file);
        IOUtils.write("test", fileWriter);
        fileWriter.close();

        repository.createFile("test.txt");

        Assert.assertTrue(file.exists());

        repository.deleteFile("test.txt");

        Assert.assertTrue(!file.exists());
    }

//    @Test
    public void testListFiles() throws Exception {
        final GitRepository repository = gitRepositoryProvider.getRepository("file:test");

        final FileWriter fileWriter = new FileWriter(new File(repository.getParent(), "test.txt"));
        IOUtils.write("test", fileWriter);
        fileWriter.close();

        repository.createFile("test.txt");

        int size = repository.listFiles("").size();
        //repository.listFiles("").forEach(System.out::println);
        //System.out.println("Found " + size + " Files!");

        Assert.assertTrue(size > 0);

        repository.deleteFile("test.txt");

        //repository.listFiles("").forEach(System.out::println);
        //System.out.println("Found " + repository.listFiles("").size() + " Files!");

        Assert.assertTrue(size+1 > repository.listFiles("").size());
    }
}
