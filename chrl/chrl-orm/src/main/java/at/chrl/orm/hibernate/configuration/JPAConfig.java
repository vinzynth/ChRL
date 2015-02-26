package at.chrl.orm.hibernate.configuration;

import org.hibernate.jpa.AvailableSettings;

import at.chrl.nutils.configuration.Property;

/**
 * 
 * Configuration Class for Hibernate(JPA) Database Connections.
 * <br>
 * Configuration for Hibernate Core 4.3.6
 * 
 * @author leopoldc
 * @see {@link org.hibernate.jpa.AvailableSettings}
 */
public abstract class JPAConfig extends HibernateConfig {
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JDBC_USER+"@"+JDBC_URL;
	}
	
	@Property(key = AvailableSettings.PROVIDER, defaultValue = "org.hibernate.ejb.HibernatePersistence")
	public String PROVIDER;
	@Property(key = AvailableSettings.TRANSACTION_TYPE, defaultValue = "")
	public String TRANSACTION_TYPE;
	@Property(key = AvailableSettings.JTA_DATASOURCE, defaultValue = "")
	public String JTA_DATASOURCE;
	@Property(key = AvailableSettings.NON_JTA_DATASOURCE, defaultValue = "")
	public String NON_JTA_DATASOURCE;
	@Property(key = AvailableSettings.JDBC_DRIVER, defaultValue = "org.h2.Driver")
	public String JDBC_DRIVER;
	@Property(key = AvailableSettings.JDBC_URL, defaultValue = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MVCC=TRUE")
	public String JDBC_URL;
	@Property(key = AvailableSettings.JDBC_USER, defaultValue = "sa")
	public String JDBC_USER;
	@Property(key = AvailableSettings.JDBC_PASSWORD, defaultValue = "")
	public String JDBC_PASSWORD;
	@Property(key = AvailableSettings.SHARED_CACHE_MODE, defaultValue = "")
	public String SHARED_CACHE_MODE;
	@Property(key = AvailableSettings.SHARED_CACHE_RETRIEVE_MODE, defaultValue = "")
	public String SHARED_CACHE_RETRIEVE_MODE;
	@Property(key = AvailableSettings.SHARED_CACHE_STORE_MODE, defaultValue = "")
	public String SHARED_CACHE_STORE_MODE;
	@Property(key = AvailableSettings.VALIDATION_MODE, defaultValue = "")
	public String VALIDATION_MODE;
	@Property(key = AvailableSettings.VALIDATION_FACTORY, defaultValue = "")
	public String VALIDATION_FACTORY;
	@Property(key = AvailableSettings.LOCK_SCOPE, defaultValue = "")
	public String LOCK_SCOPE;
	@Property(key = AvailableSettings.LOCK_TIMEOUT, defaultValue = "")
	public String LOCK_TIMEOUT;
	@Property(key = AvailableSettings.PERSIST_VALIDATION_GROUP, defaultValue = "")
	public String PERSIST_VALIDATION_GROUP;
	@Property(key = AvailableSettings.UPDATE_VALIDATION_GROUP, defaultValue = "")
	public String UPDATE_VALIDATION_GROUP;
	@Property(key = AvailableSettings.REMOVE_VALIDATION_GROUP, defaultValue = "")
	public String REMOVE_VALIDATION_GROUP;
	@Property(key = AvailableSettings.CDI_BEAN_MANAGER, defaultValue = "")
	public String CDI_BEAN_MANAGER;
	@Property(key = AvailableSettings.SCHEMA_GEN_CREATE_SOURCE, defaultValue = "")
	public String SCHEMA_GEN_CREATE_SOURCE;
	@Property(key = AvailableSettings.SCHEMA_GEN_DROP_SOURCE, defaultValue = "")
	public String SCHEMA_GEN_DROP_SOURCE;
	@Property(key = AvailableSettings.SCHEMA_GEN_CREATE_SCRIPT_SOURCE, defaultValue = "")
	public String SCHEMA_GEN_CREATE_SCRIPT_SOURCE;
	@Property(key = AvailableSettings.SCHEMA_GEN_DROP_SCRIPT_SOURCE, defaultValue = "")
	public String SCHEMA_GEN_DROP_SCRIPT_SOURCE;
	@Property(key = AvailableSettings.SCHEMA_GEN_DATABASE_ACTION, defaultValue = "")
	public String SCHEMA_GEN_DATABASE_ACTION;
	@Property(key = AvailableSettings.SCHEMA_GEN_SCRIPTS_ACTION, defaultValue = "")
	public String SCHEMA_GEN_SCRIPTS_ACTION;
	@Property(key = AvailableSettings.SCHEMA_GEN_SCRIPTS_CREATE_TARGET, defaultValue = "")
	public String SCHEMA_GEN_SCRIPTS_CREATE_TARGET;
	@Property(key = AvailableSettings.SCHEMA_GEN_SCRIPTS_DROP_TARGET, defaultValue = "")
	public String SCHEMA_GEN_SCRIPTS_DROP_TARGET;
	@Property(key = AvailableSettings.SCHEMA_GEN_CREATE_SCHEMAS, defaultValue = "")
	public String SCHEMA_GEN_CREATE_SCHEMAS;
	@Property(key = AvailableSettings.SCHEMA_GEN_CONNECTION, defaultValue = "")
	public String SCHEMA_GEN_CONNECTION;
	@Property(key = AvailableSettings.SCHEMA_GEN_DB_NAME, defaultValue = "")
	public String SCHEMA_GEN_DB_NAME;
	@Property(key = AvailableSettings.SCHEMA_GEN_DB_MAJOR_VERSION, defaultValue = "")
	public String SCHEMA_GEN_DB_MAJOR_VERSION;
	@Property(key = AvailableSettings.SCHEMA_GEN_DB_MINOR_VERSION, defaultValue = "")
	public String SCHEMA_GEN_DB_MINOR_VERSION;
	@Property(key = AvailableSettings.SCHEMA_GEN_LOAD_SCRIPT_SOURCE, defaultValue = "")
	public String SCHEMA_GEN_LOAD_SCRIPT_SOURCE;
	@Property(key = AvailableSettings.ALIAS_SPECIFIC_LOCK_MODE, defaultValue = "")
	public String ALIAS_SPECIFIC_LOCK_MODE;
	@Property(key = AvailableSettings.AUTODETECTION, defaultValue = "")
	public String AUTODETECTION;
	@Property(key = AvailableSettings.CFG_FILE, defaultValue = "")
	public String CFG_FILE;
	@Property(key = AvailableSettings.CLASS_CACHE_PREFIX, defaultValue = "")
	public String CLASS_CACHE_PREFIX;
	@Property(key = AvailableSettings.COLLECTION_CACHE_PREFIX, defaultValue = "")
	public String COLLECTION_CACHE_PREFIX;
	@Property(key = AvailableSettings.INTERCEPTOR, defaultValue = "")
	public String INTERCEPTOR;
	@Property(key = AvailableSettings.SESSION_INTERCEPTOR, defaultValue = "")
	public String SESSION_INTERCEPTOR;
	@Property(key = AvailableSettings.SESSION_FACTORY_OBSERVER, defaultValue = "")
	public String SESSION_FACTORY_OBSERVER;
	@Property(key = AvailableSettings.NAMING_STRATEGY, defaultValue = "")
	public String NAMING_STRATEGY;
	@Property(key = AvailableSettings.EVENT_LISTENER_PREFIX, defaultValue = "")
	public String EVENT_LISTENER_PREFIX;
	@Property(key = AvailableSettings.USE_CLASS_ENHANCER, defaultValue = "")
	public String USE_CLASS_ENHANCER;
	@Property(key = AvailableSettings.DISCARD_PC_ON_CLOSE, defaultValue = "")
	public String DISCARD_PC_ON_CLOSE;
	@Property(key = AvailableSettings.FLUSH_MODE, defaultValue = "")
	public String FLUSH_MODE;
	@Property(key = AvailableSettings.SCANNER, defaultValue = "")
	public String SCANNER;
	@Property(key = AvailableSettings.ENTITY_MANAGER_FACTORY_NAME, defaultValue = "")
	public String ENTITY_MANAGER_FACTORY_NAME;
	@Property(key = AvailableSettings.JPA_METAMODEL_POPULATION, defaultValue = "")
	public String JPA_METAMODEL_POPULATION;
	@Property(key = AvailableSettings.XML_FILE_NAMES, defaultValue = "")
	public String XML_FILE_NAMES;
	@Property(key = AvailableSettings.HBXML_FILES, defaultValue = "")
	public String HBXML_FILES;
//	passed by Hibernate Service
//	@Property(key = AvailableSettings.LOADED_CLASSES, defaultValue = "")
//	public String LOADED_CLASSES;
	@Property(key = AvailableSettings.PERSISTENCE_UNIT_NAME, defaultValue = "default")
	public String PERSISTENCE_UNIT_NAME;
}
