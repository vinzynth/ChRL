package at.chrl.git.impl;

import at.chrl.git.GitRepository;

/**
 * Created by ChRL on 10.02.16.
 * Project: ChRL
 * Package: at.chrl.git.impl
 * <p>
 * <br>
 *      Default implementation
 */
public class GitRepositoryImplementation implements GitRepository {

    private String remoteUrl;

    public GitRepositoryImplementation(String remoteUrl) {
        this.remoteUrl = remoteUrl;
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
