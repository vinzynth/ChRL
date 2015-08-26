package at.chrl.spring.hibernate.config;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.chrl.nutils.CollectionUtils;
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
	public SpringGeneratedJpaConfig getJpaConfig(){
		return new SpringGeneratedJpaConfig();
	}
}
