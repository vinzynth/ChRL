package at.chrl.git;

/**
 * Created by chrl on 10.02.16.
 *
 * Git Repository Provider Class
 */
public interface GitRepositoryProvider {

    void setUsername(String username);
    void setPassword(String password);

    default GitRepository getRepository(String remoteUrl){
        return getRepository(remoteUrl, "master");
    }
    GitRepository getRepository(String remoteUrl, String branch);

}
