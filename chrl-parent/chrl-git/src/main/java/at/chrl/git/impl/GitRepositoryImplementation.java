package at.chrl.git.impl;

import at.chrl.git.GitRepository;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.CredentialsProvider;
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

    private static final String FILE_URI_PREFIX = "file:";
    private static final String HTTP_URI_PREFIX = "http";

    private String remoteUrl;
    private File parentDir;
    private Git git;
    private CredentialsProvider credentials;

    public GitRepositoryImplementation(String remoteUrl, String username, String password, File baseDir) throws IOException, GitAPIException {
        this.remoteUrl = remoteUrl;
        this.credentials = new UsernamePasswordCredentialsProvider(username, password);

        if(remoteUrl.startsWith(FILE_URI_PREFIX)) {
            baseDir = new File(remoteUrl.substring(FILE_URI_PREFIX.length()));
            git = Git.open(baseDir);
            parentDir = baseDir;
            return;
        }

        String dirStr = remoteUrl;
        if(dirStr.startsWith(HTTP_URI_PREFIX))
            dirStr = dirStr.substring(dirStr.indexOf("//")+2);

        parentDir = new File(baseDir, dirStr);

        if(!parentDir.exists())
            parentDir.mkdirs();

        git = clone(parentDir, remoteUrl);
    }

    private Git clone(File directory, String remoteUrl) throws GitAPIException {
        return Git.cloneRepository()
                .setDirectory(directory)
                .setURI(remoteUrl)
                .setTimeout(10)
                .setCredentialsProvider(credentials)
                .call();
    }

    @Override
    public void createFile(String file) {
        String f = file;
        if(f.startsWith(parentDir.getPath()))
            f = f.substring(parentDir.getPath().length());

        try {
            git.pull()
                    .setCredentialsProvider(credentials)
                    .setStrategy(MergeStrategy.THEIRS)
                    .call();
            git.add()
                    .addFilepattern(f)
                    .call();
            git.commit()
                    .setOnly(f)
                    .setMessage("JGit: Created File: " + f)
                    .call();
            git.push()
                    .setCredentialsProvider(credentials)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File readFile(String file) {
        try {
            git.pull()
                    .setCredentialsProvider(credentials)
                    .setStrategy(MergeStrategy.THEIRS)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return new File(parentDir, file);
    }

    @Override
    public void updateFile(String file) {
        String f = file;
        if(f.startsWith(parentDir.getPath()))
            f = f.substring(parentDir.getPath().length());

        try {
            git.pull()
                    .setCredentialsProvider(credentials)
                    .setStrategy(MergeStrategy.OURS)
                    .call();
            git.add()
                    .setUpdate(true)
                    .call();
            git.commit()
                    .setOnly(f)
                    .setMessage("JGit: Updated File: " + f)
                    .call();
            git.push()
                    .setCredentialsProvider(credentials)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFile(String file) {
        String f = file;
        if(f.startsWith(parentDir.getPath()))
            f = f.substring(parentDir.getPath().length());

        try {
            git.pull()
                    .setCredentialsProvider(credentials)
                    .setStrategy(MergeStrategy.THEIRS)
                    .call();
            git.rm()
                    .addFilepattern(f)
                    .call();
            git.commit()
                    .setOnly(f)
                    .setMessage("JGit: Deleted File: " + f)
                    .call();
            git.push()
                    .setCredentialsProvider(credentials)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File getParent() {
        return parentDir;
    }

    @Override
    public void listFiles(String dir) {
        FileUtils.listFiles(new File(parentDir, dir), null, true).forEach(System.out::println);
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
