/**
 * @author Christian Richard Leopold - ChRL
 * Aug 21, 2015 - 3:18:24 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.hibernate.config
 */
package at.chrl.spring.hibernate.config;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import at.chrl.orm.hibernate.HibernateService;
import at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool;
import at.chrl.spring.generics.repositories.utils.impl.RepositoryTransactionPoolImplementation;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 21, 2015 - 3:18:24 PM
 *
 */
@Configuration
@EnableAsync
@EnableTransactionManagement
public class SessionConfig implements TransactionManagementConfigurer {
	
	@Autowired(required = true)
	private SpringGeneratedJpaConfig jpaConfig;
	
	@Bean(destroyMethod = "close")
	public HibernateService getHibernateService() {
		return HibernateService.getInstance();
	}
	
	@Bean
	public RepositoryTransactionPool getRepositoryThreadPool(){
		return new RepositoryTransactionPoolImplementation();
	}
	
	@Bean(destroyMethod = "")
	public EntityManagerFactory getEntityManagerFactory() {
		HibernateService.getInstance().connect(jpaConfig);
		return getHibernateService().getEntityManagerFactory(jpaConfig);
	}
	
	@Bean(destroyMethod = "")
	public SessionFactory getSessionFactory() {
		HibernateService.getInstance().connect(jpaConfig);
		return getHibernateService().getSessionFactory(jpaConfig);
	}
	
	@Bean
	public SessionTemplateFactory getSessionTemplateFactory(){
		return new SessionTemplateFactoryImpl();
	}
	
	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new JpaTransactionManager(getEntityManagerFactory());
	}
	
	
}
