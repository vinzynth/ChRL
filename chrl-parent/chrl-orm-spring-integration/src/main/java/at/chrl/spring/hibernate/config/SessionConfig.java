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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import at.chrl.orm.hibernate.HibernateService;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;
import at.chrl.orm.hibernate.configuration.JPAConfig;
import at.chrl.spring.generics.repositories.utils.RepositoryThreadPool;
import at.chrl.spring.generics.repositories.utils.impl.RepositoryThreadPoolImplementation;

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
	public RepositoryThreadPool getRepositoryThreadPool(){
		return new RepositoryThreadPoolImplementation();
	}
	
	@Bean
	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(SpringGeneratedJpaConfig jpaConfig){
		ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
		int maxPoolSize = 100;
		try {
			maxPoolSize = Integer.parseInt(jpaConfig.HIBERNATE_HIKARI_MAXIMUMPOOLSIZE);			
		} catch (Exception e) {
			System.err.println("Error on parsing Hikari maximum pool size: " + e.getMessage());
		}
		ex.setMaxPoolSize(maxPoolSize);
		return ex;
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
		return new SessionTemplateFactory() {
			
			@Override
			public SessionTemplate createTemplate() {
				return new SessionTemplateImplementation();
			}
		};
	}
	
	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new JpaTransactionManager(getEntityManagerFactory());
	}
	
	private static class SessionTemplateImplementation extends SessionTemplate{
		
		@Autowired(required = true)
		private JPAConfig jpaConfig;
		
		/**
		 * {@inheritDoc}
		 * @see at.chrl.orm.hibernate.SessionTemplate#getHibernateConfig()
		 */
		@Override
		protected IHibernateConfig getHibernateConfig() {
			return jpaConfig;
		}
	}
}
