package at.chrl.git;

/**
 * Created by chrl on 10.02.16.
 *
 * Git Service Interface
 */
public interface GitRepository {

    /**
     * CRUD
     */
    void createFile();
    void readFile();
    void updateFile();
    void deleteFile();

}
