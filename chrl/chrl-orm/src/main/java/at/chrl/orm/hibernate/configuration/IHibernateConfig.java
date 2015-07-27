/**
 * @author bravestone
 * Aug 26, 2014 - 7:24:17 PM
 * bravestone-hibernate
 * com.bravestone.hibernate
 */
package at.chrl.orm.hibernate.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.PropertiesUtils;
import at.chrl.orm.hibernate.HibernateService;

/**
 * 
 * Configuration Interface for Hibernate Database Connections.
 * <br>
 * Configuration for Hibernate Core 4.3.6
 * 
 * 
 * @author leopoldc
 * 
 * @see {@link com.bravestone.hibernate.configuration.HibernateConfig}
 * @see {@link com.bravestone.hibernate.configuration.JPAConfig}
 * 
 * @see {@link org.hibernate.cfg.AvailableSettings}
 * @see {@link org.hibernate.jpa.AvailableSettings}
 */
public interface IHibernateConfig {
	
	/**
	 * Empty collection (Set)
	 */
	static List<Class<?>> NO_ANNOTATED_CLASSES = Collections.emptyList();
	
	/**
	 * Returns classes for {@link Configuration} object to build a 
	 * {@link SessionFactory}.
	 * 
	 * <p>
	 * 
	 * return {@link HibernateConfig#NO_ANNOTATED_CLASSES} if no further classes
	 * should be added.
	 * 
	 * @see {@link org.hibernate.cfg.Configuration}
	 * @see {@link org.hibernate.SessionFactory}
	 * 
	 * @return 
	 * 		collection with classes to add to the configuration or
	 */
	public default List<Class<?>> getAnnotatedClasses(){
		return NO_ANNOTATED_CLASSES; 
	}
	
	/**
	 * Returns if Logging is enabled
	 * 
	 * @return if logging is enabled
	 */
	public default boolean isLoggingEnabled(){
		return false;
	}
	
	/**
	 * Returns if flyway is active
	 * 
	 * @return if flyway is active
	 */
	public default boolean isFlywayActive(){
		return true;
	}
	
	/**
	 * Called by {@link HibernateService}.
	 * 
	 * 
	 * @return {@link SessionFactory} instance
	 */
	public default SessionFactory getSessionFactory(){
		Properties props = PropertiesUtils.filterEmtpyValues(ConfigUtil.getProperties(this.getClass()));

		Configuration hibernateCfg = new Configuration();
		hibernateCfg.setProperties(props);
		
		for (Class<?> ie : this.getAnnotatedClasses())
			hibernateCfg.addAnnotatedClass(ie);
		
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(hibernateCfg.getProperties());
        SessionFactory sf = hibernateCfg.buildSessionFactory(ssrb.build());
        return sf;
	}
	
	public default String getConnectionName(){
		return this.getClass().getSimpleName();
	}
}
