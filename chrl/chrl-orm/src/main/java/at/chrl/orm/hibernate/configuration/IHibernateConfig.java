package at.chrl.orm.hibernate.configuration;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 
 * Configuration Interface for Hibernate Database Connections.
 * <br>
 * Configuration for Hibernate Core 4.3.6
 * 
 * 
 * @author leopoldc
 * 
 * @see {@link at.chrl.orm.hibernate.configuration.HibernateConfig}
 * @see {@link at.chrl.orm.hibernate.configuration.JPAConfig}
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
	
	public default String getConnectionName(){
		return this.getClass().getSimpleName();
	}
}
