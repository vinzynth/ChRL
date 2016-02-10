package at.chrl.git.config;

import at.chrl.git.impl.GitRepositoryProviderImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by ChRL on 10.02.16.
 * Project: ChRL
 * Package: at.chrl.git.config
 * <p>
 * <br>
 */
@ContextConfiguration
public class DefaultTestConfiguration {

    @Bean
    public GitRepositoryProviderImplementation gitRepositoryProviderImplementation(){
        return new GitRepositoryProviderImplementation();
    }
}
