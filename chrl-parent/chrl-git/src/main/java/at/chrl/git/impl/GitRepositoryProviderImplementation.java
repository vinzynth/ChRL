package at.chrl.git.impl;

import at.chrl.git.GitRepository;
import at.chrl.git.GitRepositoryProvider;
import at.chrl.nutils.CollectionUtils;
import at.chrl.nutils.Memoizer;
import org.eclipse.jgit.errors.RepositoryNotFoundException;

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

    private Map<String, GitRepository> cache = CollectionUtils.newMap();
    private Function<String, GitRepository> REPO_GETTER = Memoizer.memoize(s -> {
        try {
            return new GitRepositoryImplementation(s, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }, cache);

    @Override
    public GitRepository getRepository(String remoteUrl) {
        REPO_GETTER.apply(remoteUrl);

        if(Objects.isNull(cache.get(remoteUrl)))
            cache.remove(remoteUrl);

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
}
