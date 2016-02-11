package at.chrl.config.git;

import at.chrl.git.GitRepository;
import at.chrl.git.impl.GitRepositoryProviderImplementation;
import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.listener.ConfigEventListener;

/**
 * Created by ChRL on 10.02.16.
 * Project: ChRL
 * Package: at.chrl.config.git
 * <p>
 * <br>
 */
public final class GitConfigUtil {

    public static void setGitConfigRepository(String remoteUrl, String username, String password){
        GitRepositoryProviderImplementation.getInstance().setUsername(username);
        GitRepositoryProviderImplementation.getInstance().setPassword(password);

        GitRepository configRepo = GitRepositoryProviderImplementation.getInstance().getRepository(remoteUrl);

        if(configRepo == null){
            System.out.println("WARNING: Config repository is not available. Using local configuration as fallback!");
            return;
        }

        ConfigUtil.addConfigEventListener(new ConfigEventListener() {

            @Override
            public void beforeOnLoadedConfigClass(Class<?> targetClass) {
                String fileName = targetClass.getSimpleName() + ".properties";
                configRepo.readFile(fileName);
            }

            @Override
            public void beforeOnLoadedConfigObject(Object obj) {
                this.beforeOnLoadedConfigClass(obj.getClass());
            }

            @Override
            public void onExportedConfigClass(Class<?> targetClass) {
                String fileName = targetClass.getSimpleName() + ".properties";
                configRepo.updateFile(fileName);
            }

            @Override
            public void onExportedConfigObject(Object obj) {
                this.onExportedConfigClass(obj.getClass());
            }
        });
        ConfigUtil.setConfigDirectory(configRepo.getParent().getAbsolutePath());
    }
}
