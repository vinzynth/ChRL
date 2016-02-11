package at.chrl.git.impl;

import at.chrl.git.GitRepository;
import at.chrl.git.GitRepositoryProvider;
import at.chrl.nutils.CollectionUtils;
import at.chrl.nutils.Memoizer;
import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by ChRL on 10.02.16.
 * Project: ChRL
 * Package: at.chrl.git.impl
 * <p>
 * <br>
 *     Default Implementation
 */
public class GitRepositoryProviderImplementation implements GitRepositoryProvider {

    private String password;
    private String username;

    private File baseDir = new File("git_repositories");

    private Map<String, GitRepository> cache = CollectionUtils.newMap();
    private Function<String, GitRepository> REPO_GETTER = Memoizer.memoize(s -> {
        try {
            return new GitRepositoryImplementation(s, username, password, baseDir);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }, cache);

    public GitRepositoryProviderImplementation() {
        this(null);
    }

    public GitRepositoryProviderImplementation(File baseDir) {
        if(baseDir != null)
            this.baseDir = baseDir;


        try {
            FileUtils.delete(this.baseDir, FileUtils.RECURSIVE);
        } catch (IOException ignored) {
        }


        this.baseDir.mkdirs();
    }

    @Override
    public GitRepository getRepository(String remoteUrl, String branch) {
        REPO_GETTER.apply(remoteUrl);

        if(Objects.isNull(cache.get(remoteUrl)))
            cache.remove(remoteUrl);

        if(Objects.nonNull(branch) && cache.containsKey(remoteUrl))
            cache.get(remoteUrl).checkoutBranch(branch);

        return cache.getOrDefault(remoteUrl, null);
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    private static class SingletonHolder{
        private static final GitRepositoryProviderImplementation instance = new GitRepositoryProviderImplementation();
    }

    public static GitRepositoryProvider getInstance(){
        return SingletonHolder.instance;
    }
}
