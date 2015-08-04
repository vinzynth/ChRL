package at.chrl.spring.hibernate.config;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import at.chrl.orm.hibernate.HibernateService;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;
import at.chrl.orm.hibernate.configuration.JPAConfig;

/**
 * 
 * @author Christian Richard Leopold - ChRL <br>
 *         Feb 26, 2015 - 4:47:25 PM
 *
 */
@Configuration
@EnableTransactionManagement
public class SpringJpaConfig implements TransactionManagementConfigurer {

	@Autowired(required = true)
	JPAConfig jpaConfig;

	@Bean
	public EntityManagerFactory getEntityManagerFactory() {
		HibernateService.getInstance().connect(jpaConfig);
		return HibernateService.getInstance().getEntityManagerFactory(jpaConfig);
	}
	
	@Bean
	public SessionFactory getSessionFactory() {
		HibernateService.getInstance().connect(jpaConfig);
		return HibernateService.getInstance().getSessionFactory(jpaConfig);
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
		JPAConfig jpaConfig;
		
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
