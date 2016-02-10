package at.chrl.git.impl;

import at.chrl.git.GitRepository;
import at.chrl.git.GitRepositoryProvider;

/**
 * Created by ChRL on 10.02.16.
 * Project: ChRL
 * Package: at.chrl.git.impl
 * <p>
 * <br>
 *     Default Implementation
 */
public class GitRepositoryProviderImplementation implements GitRepositoryProvider {

    @Override
    public GitRepository getRepository(String remoteUrl) {
        return new GitRepositoryImplementation();
    }
}
