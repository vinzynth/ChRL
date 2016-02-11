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

    public GitConfigUtil(ConfigUtil configUtil){
        GitRepositoryProviderImplementation.getInstance().setUsername(JGitConfig.USERNAME);
        GitRepositoryProviderImplementation.getInstance().setPassword(JGitConfig.PASSWORD);

        GitRepository configRepo = GitRepositoryProviderImplementation.getInstance().getRepository(JGitConfig.REPO);

        if(configRepo == null){
            System.out.println("WARNING: Config repository is not available. Using local configuration as fallback!");
            return;
        }

        configRepo.checkoutBranch(JGitConfig.HOSTNAME);

        //System.out.println("Add Config Listener");
        ConfigUtil.getInstance().addConfigEventListener(new ConfigEventListener() {

            @Override
            public void beforeOnLoadedConfigClass(Class<?> targetClass) {
                String fileName = targetClass.getSimpleName() + ".properties";
                //System.out.println("BeforeOnLoadConfigClass: " + fileName);
                configRepo.readFile(fileName);
            }

            @Override
            public void beforeOnLoadedConfigObject(Object obj) {
                this.beforeOnLoadedConfigClass(obj.getClass());
            }

            @Override
            public void onExportedConfigClass(Class<?> targetClass) {
                //if(targetClass.equals(JGitConfig.class))
                //    return;
                String fileName = targetClass.getSimpleName() + ".properties";
                //System.out.println("onExportedLoadConfigClass: " + fileName);
                configRepo.updateFile(fileName);
            }

            @Override
            public void onExportedConfigObject(Object obj) {
                this.onExportedConfigClass(obj.getClass());
            }
        });

        //System.out.println("Set Config Directory");
        configUtil.export(JGitConfig.class);
        configUtil.setConfigDirectory(configRepo.getParent().getAbsolutePath());
    }

    private static final class SingletonHolder{
        private static final GitConfigUtil instance = new GitConfigUtil(ConfigUtil.getInstance());
    }

    public static final GitConfigUtil getInstance(){
        return SingletonHolder.instance;
    }
}
