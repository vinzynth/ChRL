package at.chrl.spring.hibernate.config;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.chrl.nutils.CollectionUtils;
import at.chrl.orm.hibernate.configuration.JPAConfig;
import at.chrl.orm.hibernate.configuration.ORMMapping;

/**
 * 
 * @author Christian Richard Leopold - ChRL <br>
 *         Feb 26, 2015 - 4:47:25 PM
 *
 */
@Configuration
public class SpringJpaConfig {

//	@Autowired
//	private Collection<GenericRepository<?>> repositories;
	
	@Autowired
	private Collection<ORMMapping> configs;
	
	@Bean(name = "at-chrl-orm-classes")
	public ORMMapping getMappedClasses() {
		return new ORMMapping(){
			/**
			 * {@inheritDoc}
			 * @see at.chrl.orm.hibernate.configuration.IHibernateConfig#getAnnotatedClasses()
			 */
			@Override
			public List<Class<?>> getAnnotatedClasses() {
				List<Class<?>> list = CollectionUtils.newList();
				for (ORMMapping ormMapping : configs) {
					list.addAll(ormMapping.getAnnotatedClasses());
				}
				return list;
			}
		};
	}
	
	@Bean(name = "at-chrl-spring-orm-config")
	public JPAConfig getJpaConfig(){
		return new SpringJPAConfig();
	}
	
	private static class SpringJPAConfig extends JPAConfig{
		
		@Autowired(required = true)
		@Qualifier("at-chrl-orm-classes")
		private ORMMapping mappedClasses;
		
		/**
		 * {@inheritDoc}
		 * @see at.chrl.orm.hibernate.configuration.IHibernateConfig#getAnnotatedClasses()
		 */
		@Override
		public List<Class<?>> getAnnotatedClasses() {
			return mappedClasses.getAnnotatedClasses();
		}
		
		/**
		 * {@inheritDoc}
		 * @see at.chrl.orm.hibernate.configuration.JPAConfig#toString()
		 */
		@Override
		public String toString() {
			return "[chrl-spring] " + super.toString();
		}
	}
}
