/**
 * @author Christian Richard Leopold - ChRL
 * Aug 21, 2015 - 5:11:51 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.hibernate.config
 */
package at.chrl.spring.hibernate.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.chrl.spring.generics.repositories.GenericRepository;
import at.chrl.spring.hibernate.config.impl.RepositoryHolderImplementation;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 21, 2015 - 5:11:51 PM
 *
 */
@Configuration
public class RepositoryConfig {


	@Autowired(required = false)
	private Collection<GenericRepository<?>> repositories;
	
	@Autowired(required = true)
	private SpringJpaConfig jpaConfig;
	
	@Bean
	public RepositoryHolder getRepositoryHolder(){
		return new RepositoryHolderImplementation(jpaConfig, repositories);
	}
}
