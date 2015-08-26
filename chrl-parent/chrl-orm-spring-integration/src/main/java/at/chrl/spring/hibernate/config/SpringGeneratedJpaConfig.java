/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 10:38:56 AM
 * chrl-orm-spring-integration
 * at.chrl.spring.hibernate.config
 */
package at.chrl.spring.hibernate.config;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import at.chrl.orm.hibernate.configuration.JPAConfig;
import at.chrl.orm.hibernate.configuration.ORMMapping;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 10:38:56 AM
 *
 */
public class SpringGeneratedJpaConfig extends JPAConfig{
	
	@Autowired(required = true)
	@Qualifier("at-chrl-orm-classes")
	private ORMMapping mappedClasses;

	
	@Autowired(required = false)
	private Collection<SpringJpaConfigInterceptor> configInterceptors;
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.configuration.JPAConfig#overrideConfig()
	 */
	@Override
	public void overrideConfig() {
		super.overrideConfig();
		
		if(Objects.nonNull(configInterceptors))
			for (SpringJpaConfigInterceptor springJpaConfigInterceptor : configInterceptors) {
				springJpaConfigInterceptor.modify(this);
			}
	}
	
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