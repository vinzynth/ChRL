package at.chrl.git.impl;

import at.chrl.git.GitRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by ChRL on 10.02.16.
 * Project: ChRL
 * Package: at.chrl.git.impl
 * <p>
 * <br>
 *      Default implementation
 */
public class GitRepositoryImplementation implements GitRepository {

    private static final String DEFAULT_LABEL = "master";
    private static final String FILE_URI_PREFIX = "file:";
    private static final String HTTP_URI_PREFIX = "http";

    private String remoteUrl;
    private String username;
    private String password;
    private File baseDir;
    private Git git;

    public GitRepositoryImplementation(String remoteUrl, String username, String password, File baseDir) throws IOException, GitAPIException {
        this.remoteUrl = remoteUrl;
        this.username = username;
        this.password = password;
        this.baseDir = baseDir;

        if(remoteUrl.startsWith(FILE_URI_PREFIX)) {
            git = Git.open(new File(remoteUrl.substring(FILE_URI_PREFIX.length())));
            return;
        }

        String dirStr = remoteUrl;
        if(dirStr.startsWith(HTTP_URI_PREFIX))
            dirStr = dirStr.substring(dirStr.indexOf("//")+2);

        final File dir = new File(baseDir, dirStr);

        if(!dir.exists())
            dir.mkdirs();

        git = clone(dir, remoteUrl);
    }

    private Git clone(File directory, String remoteUrl) throws GitAPIException {
        return Git.cloneRepository()
                .setDirectory(directory)
                .setURI(remoteUrl)
                .setTimeout(10)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call();
    }

    @Override
    public void createFile() {

    }

    @Override
    public void readFile() {

    }

    @Override
    public void updateFile() {

    }

    @Override
    public void deleteFile() {

    }

    @Override
    public String toString() {
        return "GitRepositoryImplementation{" +
                "remoteUrl='" + remoteUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitRepositoryImplementation that = (GitRepositoryImplementation) o;

        return remoteUrl != null ? remoteUrl.equals(that.remoteUrl) : that.remoteUrl == null;

    }

    @Override
    public int hashCode() {
        return remoteUrl != null ? remoteUrl.hashCode() : 0;
    }
}
