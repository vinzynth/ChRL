package at.chrl.git;

import java.io.File;

/**
 * Created by chrl on 10.02.16.
 *
 * Git Service Interface
 */
public interface GitRepository {

    /**
     * CRUD
     */
    void createFile(String file);
    File readFile(String file);
    void updateFile(String file);
    void deleteFile(String file);
    void listFiles(String dir);

    /**
     * Returns Parent directory
     */
    File getParent();
}
