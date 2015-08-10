/**
 * @author bravestone
 * Aug 22, 2014 - 9:55:32 AM
 * bravestone-hibernate
 * com.bravestone.hibernate
 */
package at.chrl.orm.hibernate.configuration;

import java.util.Objects;

import org.hibernate.cfg.AvailableSettings;

import at.chrl.nutils.configuration.Property;

/**
 * 
 * Configuration Class for Hibernate Database Connections.
 * <br>
 * Configuration for Hibernate Core 4.3.6
 * 
 * @author leopoldc
 * @see {@link org.hibernate.cfg.AvailableSettings}
 */
public abstract class HibernateConfig implements IHibernateConfig{
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return USER+"@"+URL;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj))
			return false;
		if(this == obj)
			return true;
		return this.getClass().equals(obj.getClass());
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.configuration.IHibernateConfig#isFlywayActive()
	 */
	@Override
	public boolean isFlywayActive() {
		return FLYWAY_ACTIVE;
	}

	/**
	 * {@inheritDoc}
	 * @see com.bravestone.hibernate.configuration.IHibernateConfig#isLoggingEnabled()
	 */
	@Override
	public boolean isLoggingEnabled() {
		return LOGG_QUERRIES;
	}
	
	@Property(key = "com.bravestone.hibernate.logQuerries", defaultValue = "false")
	public boolean LOGG_QUERRIES;
	
	@Property(key = AvailableSettings.SESSION_FACTORY_NAME, defaultValue = "", description = " Defines a name for the {@link org.hibernate.SessionFactory}. Useful both to<ul> <li>allow serialization and deserialization to work across different jvms</li> <li>optionally allow the SessionFactory to be bound into JNDI</li> </ul> @see #SESSION_FACTORY_NAME_IS_JNDI / ")
	public String SESSION_FACTORY_NAME;
	@Property(key = AvailableSettings.SESSION_FACTORY_NAME_IS_JNDI, defaultValue = "", description = "Does the value defined by {@link #SESSION_FACTORY_NAME} represent a {@literal JNDI} namespace into which the {@link org.hibernate.SessionFactory} should be bound? / ")
	public String SESSION_FACTORY_NAME_IS_JNDI;
	@Property(key = AvailableSettings.CONNECTION_PROVIDER, defaultValue = "", description = "Names the {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider} to use for obtaining JDBC connections. Can either reference an instance of {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider} or a {@link Class} or {@link String} reference to the {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider} implementation class. / ")
	public String CONNECTION_PROVIDER;
	@Property(key = AvailableSettings.DRIVER, defaultValue = "", description = "Names the {@literal JDBC} driver class / ")
	public String DRIVER;
	@Property(key = AvailableSettings.URL, defaultValue = "", description = "Names the {@literal JDBC} connection url. / ")
	public String URL;
	@Property(key = AvailableSettings.USER, defaultValue = "", description = "Names the connection user. This might mean one of 2 things in out-of-the-box Hibernate {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider}: <ul> <li>The username used to pass along to creating the JDBC connection</li> <li>The username used to obtain a JDBC connection from a data source</li> </ul> / ")
	public String USER;
	@Property(key = AvailableSettings.PASS, defaultValue = "", description = "Names the connection password. See usage discussion on {@link #USER} / ")
	public String PASS;
	@Property(key = AvailableSettings.ISOLATION, defaultValue = "", description = "Names the {@literal JDBC} transaction isolation level / ")
	public String ISOLATION;
	@Property(key = AvailableSettings.AUTOCOMMIT, defaultValue = "", description = "Names the {@literal JDBC} autocommit mode / ")
	public String AUTOCOMMIT;
	@Property(key = AvailableSettings.POOL_SIZE, defaultValue = "", description = "Maximum number of inactive connections for the built-in Hibernate connection pool. / ")
	public String POOL_SIZE;
	@Property(key = AvailableSettings.DATASOURCE, defaultValue = "", description = "Names a {@link javax.sql.DataSource}. Can either reference a {@link javax.sql.DataSource} instance or a {@literal JNDI} name under which to locate the {@link javax.sql.DataSource}. / ")
	public String DATASOURCE;
	@Property(key = AvailableSettings.CONNECTION_PREFIX, defaultValue = "", description = "Names a prefix used to define arbitrary JDBC connection properties. These properties are passed along to the {@literal JDBC} provider when creating a connection. / ")
	public String CONNECTION_PREFIX;
	@Property(key = AvailableSettings.JNDI_CLASS, defaultValue = "", description = "Names the {@literal JNDI} {@link javax.naming.InitialContext} class. @see javax.naming.Context#INITIAL_CONTEXT_FACTORY / ")
	public String JNDI_CLASS;
	@Property(key = AvailableSettings.JNDI_URL, defaultValue = "", description = "Names the {@literal JNDI} provider/connection url @see javax.naming.Context#PROVIDER_URL / ")
	public String JNDI_URL;
	@Property(key = AvailableSettings.JNDI_PREFIX, defaultValue = "", description = "Names a prefix used to define arbitrary {@literal JNDI} {@link javax.naming.InitialContext} properties. These properties are passed along to {@link javax.naming.InitialContext#InitialContext(java.util.Hashtable)} / ")
	public String JNDI_PREFIX;
	@Property(key = AvailableSettings.DIALECT, defaultValue = "", description = "Names the Hibernate {@literal SQL} {@link org.hibernate.dialect.Dialect} class / ")
	public String DIALECT;
	@Property(key = AvailableSettings.DIALECT_RESOLVERS, defaultValue = "", description = "Names any additional {@link org.hibernate.engine.jdbc.dialect.spi.DialectResolver} implementations to register with the standard {@link org.hibernate.engine.jdbc.dialect.spi.DialectFactory}. / ")
	public String DIALECT_RESOLVERS;
	@Property(key = AvailableSettings.DEFAULT_SCHEMA, defaultValue = "", description = "A default database schema (owner) name to use for unqualified tablenames / ")
	public String DEFAULT_SCHEMA;
	@Property(key = AvailableSettings.DEFAULT_CATALOG, defaultValue = "", description = "A default database catalog name to use for unqualified tablenames / ")
	public String DEFAULT_CATALOG;
	@Property(key = AvailableSettings.SHOW_SQL, defaultValue = "true", description = "Enable logging of generated SQL to the console / ")
	public String SHOW_SQL;
	@Property(key = AvailableSettings.FORMAT_SQL, defaultValue = "", description = "Enable formatting of SQL logged to the console / ")
	public String FORMAT_SQL;
	@Property(key = AvailableSettings.USE_SQL_COMMENTS, defaultValue = "", description = "Add comments to the generated SQL / ")
	public String USE_SQL_COMMENTS;
	@Property(key = AvailableSettings.MAX_FETCH_DEPTH, defaultValue = "", description = "Maximum depth of outer join fetching / ")
	public String MAX_FETCH_DEPTH;
	@Property(key = AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, defaultValue = "500", description = "The default batch size for batch fetching / ")
	public String DEFAULT_BATCH_FETCH_SIZE;
	@Property(key = AvailableSettings.USE_STREAMS_FOR_BINARY, defaultValue = "", description = "Use <tt>java.io</tt> streams to read / write binary data from / to JDBC / ")
	public String USE_STREAMS_FOR_BINARY;
	@Property(key = AvailableSettings.USE_SCROLLABLE_RESULTSET, defaultValue = "", description = "Use JDBC scrollable <tt>ResultSet</tt>s. This property is only necessary when there is no <tt>ConnectionProvider</tt>, ie. the user is supplying JDBC connections. / ")
	public String USE_SCROLLABLE_RESULTSET;
	@Property(key = AvailableSettings.USE_GET_GENERATED_KEYS, defaultValue = "", description = "Tells the JDBC driver to attempt to retrieve row Id with the JDBC 3.0 PreparedStatement.getGeneratedKeys() method. In general, performance will be better if this property is set to true and the underlying JDBC driver supports getGeneratedKeys(). / ")
	public String USE_GET_GENERATED_KEYS;
	@Property(key = AvailableSettings.STATEMENT_FETCH_SIZE, defaultValue = "20", description = "Gives the JDBC driver a hint as to the number of rows that should be fetched from the database when more rows are needed. If <tt>0</tt>, JDBC driver default settings will be used. / ")
	public String STATEMENT_FETCH_SIZE;
	@Property(key = AvailableSettings.STATEMENT_BATCH_SIZE, defaultValue = "20", description = "Maximum JDBC batch size. A nonzero value enables batch updates. / ")
	public String STATEMENT_BATCH_SIZE;
	@Property(key = AvailableSettings.BATCH_STRATEGY, defaultValue = "", description = "Select a custom batcher. / ")
	public String BATCH_STRATEGY;
	@Property(key = AvailableSettings.BATCH_VERSIONED_DATA, defaultValue = "", description = "Should versioned data be included in batching? / ")
	public String BATCH_VERSIONED_DATA;
	@Property(key = AvailableSettings.OUTPUT_STYLESHEET, defaultValue = "", description = "An XSLT resource used to generate \"custom\" XML / ")
	public String OUTPUT_STYLESHEET;
	@Property(key = AvailableSettings.C3P0_MAX_SIZE, defaultValue = "5", description = "Maximum size of C3P0 connection pool / ")
	public String C3P0_MAX_SIZE;
	@Property(key = AvailableSettings.C3P0_MIN_SIZE, defaultValue = "2", description = "Minimum size of C3P0 connection pool / ")
	public String C3P0_MIN_SIZE;
	@Property(key = AvailableSettings.C3P0_TIMEOUT, defaultValue = "180", description = "Maximum idle time for C3P0 connection pool / ")
	public String C3P0_TIMEOUT;
	@Property(key = AvailableSettings.C3P0_MAX_STATEMENTS, defaultValue = "50", description = "Maximum size of C3P0 statement cache / ")
	public String C3P0_MAX_STATEMENTS;
	@Property(key = AvailableSettings.C3P0_ACQUIRE_INCREMENT, defaultValue = "", description = "Number of connections acquired when pool is exhausted / ")
	public String C3P0_ACQUIRE_INCREMENT;
	@Property(key = AvailableSettings.C3P0_IDLE_TEST_PERIOD, defaultValue = "150", description = "Idle time before a C3P0 pooled connection is validated / ")
	public String C3P0_IDLE_TEST_PERIOD;
	@Property(key = AvailableSettings.PROXOOL_XML, defaultValue = "", description = "Proxool property to configure the Proxool Provider using an XML (<tt>/path/to/file.xml</tt>) / ")
	public String PROXOOL_XML;
	@Property(key = AvailableSettings.PROXOOL_PROPERTIES, defaultValue = "", description = "Proxool property to configure the Proxool Provider using a properties file (<tt>/path/to/proxool.properties</tt>) / ")
	public String PROXOOL_PROPERTIES;
	@Property(key = AvailableSettings.PROXOOL_EXISTING_POOL, defaultValue = "", description = "Proxool property to configure the Proxool Provider from an already existing pool (<tt>true</tt> / <tt>false</tt>) / ")
	public String PROXOOL_EXISTING_POOL;
	@Property(key = AvailableSettings.PROXOOL_POOL_ALIAS, defaultValue = "", description = "Proxool property with the Proxool pool alias to use (Required for <tt>PROXOOL_EXISTING_POOL</tt>, <tt>PROXOOL_PROPERTIES</tt>, or <tt>PROXOOL_XML</tt>) / ")
	public String PROXOOL_POOL_ALIAS;
	@Property(key = AvailableSettings.AUTO_CLOSE_SESSION, defaultValue = "", description = "Enable automatic session close at end of transaction / ")
	public String AUTO_CLOSE_SESSION;
	@Property(key = AvailableSettings.FLUSH_BEFORE_COMPLETION, defaultValue = "", description = "Enable automatic flush during the JTA <tt>beforeCompletion()</tt> callback / ")
	public String FLUSH_BEFORE_COMPLETION;
	@Property(key = AvailableSettings.RELEASE_CONNECTIONS, defaultValue = "", description = "Specifies how Hibernate should release JDBC connections. / ")
	public String RELEASE_CONNECTIONS;
	@Property(key = AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, defaultValue = "", description = "Context scoping impl for {@link org.hibernate.SessionFactory#getCurrentSession()} processing. / ")
	public String CURRENT_SESSION_CONTEXT_CLASS;
	@Property(key = AvailableSettings.TRANSACTION_STRATEGY, defaultValue = "", description = "Names the implementation of {@link org.hibernate.engine.transaction.spi.TransactionFactory} to use for creating {@link org.hibernate.Transaction} instances / ")
	public String TRANSACTION_STRATEGY;
	@Property(key = AvailableSettings.JTA_PLATFORM, defaultValue = "", description = "Names the {@link org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform} implementation to use for integrating with {@literal JTA} systems. Can reference either a {@link org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform} instance or the name of the {@link org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform} implementation class @since 4.0 / ")
	public String JTA_PLATFORM;
	@Property(key = AvailableSettings.JTA_PLATFORM_RESOLVER, defaultValue = "", description = "Names the {@link org.hibernate.engine.transaction.jta.platform.spi.JtaPlatformResolver} implementation to use. @since 4.3 / ")
	public String JTA_PLATFORM_RESOLVER;
	@Property(key = AvailableSettings.CACHE_REGION_FACTORY, defaultValue = "org.hibernate.cache.ehcache.EhCacheRegionFactory", description = "The {@link org.hibernate.cache.spi.RegionFactory} implementation class / ")
	public String CACHE_REGION_FACTORY;
	@Property(key = AvailableSettings.CACHE_PROVIDER_CONFIG, defaultValue = "", description = "The <tt>CacheProvider</tt> implementation class / ")
	public String CACHE_PROVIDER_CONFIG;
	@Property(key = AvailableSettings.CACHE_NAMESPACE, defaultValue = "", description = "The <tt>CacheProvider</tt> JNDI namespace, if pre-bound to JNDI. / ")
	public String CACHE_NAMESPACE;
	@Property(key = AvailableSettings.USE_QUERY_CACHE, defaultValue = "true", description = "Enable the query cache (disabled by default) / ")
	public String USE_QUERY_CACHE;
	@Property(key = AvailableSettings.QUERY_CACHE_FACTORY, defaultValue = "", description = "The <tt>QueryCacheFactory</tt> implementation class. / ")
	public String QUERY_CACHE_FACTORY;
	@Property(key = AvailableSettings.USE_SECOND_LEVEL_CACHE, defaultValue = "true", description = "Enable the second-level cache (enabled by default) / ")
	public String USE_SECOND_LEVEL_CACHE;
	@Property(key = AvailableSettings.USE_MINIMAL_PUTS, defaultValue = "", description = "Optimize the cache for minimal puts instead of minimal gets / ")
	public String USE_MINIMAL_PUTS;
	@Property(key = AvailableSettings.CACHE_REGION_PREFIX, defaultValue = "", description = "The <tt>CacheProvider</tt> region name prefix / ")
	public String CACHE_REGION_PREFIX;
	@Property(key = AvailableSettings.USE_STRUCTURED_CACHE, defaultValue = "", description = "Enable use of structured second-level cache entries / ")
	public String USE_STRUCTURED_CACHE;
	@Property(key = AvailableSettings.AUTO_EVICT_COLLECTION_CACHE, defaultValue = "", description = "Enables the automatic eviction of a bi-directional association's collection cache when an element in the ManyToOne collection is added/updated/removed without properly managing the change on the OneToMany side. / ")
	public String AUTO_EVICT_COLLECTION_CACHE;
	@Property(key = AvailableSettings.GENERATE_STATISTICS, defaultValue = "true", description = "Enable statistics collection / ")
	public String GENERATE_STATISTICS;
	@Property(key = AvailableSettings.USE_IDENTIFIER_ROLLBACK, defaultValue = "", description = "")
	public String USE_IDENTIFIER_ROLLBACK;
	@Property(key = AvailableSettings.USE_REFLECTION_OPTIMIZER, defaultValue = "true", description = "Use bytecode libraries optimized property access / ")
	public String USE_REFLECTION_OPTIMIZER;
	@Property(key = AvailableSettings.QUERY_TRANSLATOR, defaultValue = "", description = "The classname of the HQL query parser factory / ")
	public String QUERY_TRANSLATOR;
	@Property(key = AvailableSettings.QUERY_SUBSTITUTIONS, defaultValue = "", description = "A comma-separated list of token substitutions to use when translating a Hibernate query to SQL / ")
	public String QUERY_SUBSTITUTIONS;
	@Property(key = AvailableSettings.QUERY_STARTUP_CHECKING, defaultValue = "", description = "Should named queries be checked during startup (the default is enabled). <p/> Mainly intended for test environments. / ")
	public String QUERY_STARTUP_CHECKING;
	@Property(key = AvailableSettings.HBM2DDL_AUTO, defaultValue = "update", description = "Auto export/update schema using hbm2ddl tool. Valid values are <tt>update</tt>, <tt>create</tt>, <tt>create-drop</tt> and <tt>validate</tt>. / ")
	public String HBM2DDL_AUTO;
	@Property(key = AvailableSettings.HBM2DDL_IMPORT_FILES, defaultValue = "", description = "Comma-separated names of the optional files containing SQL DML statements executed during the SessionFactory creation. File order matters, the statements of a give file are executed before the statements of the following files. These statements are only executed if the schema is created ie if <tt>hibernate.hbm2ddl.auto</tt> is set to <tt>create</tt> or <tt>create-drop</tt>. The default value is <tt>/import.sql</tt> / ")
	public String HBM2DDL_IMPORT_FILES;
	@Property(key = AvailableSettings.HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR, defaultValue = "", description = "{@link String} reference to {@link org.hibernate.tool.hbm2ddl.ImportSqlCommandExtractor} implementation class. Referenced implementation is required to provide non-argument constructor. The default value is <tt>org.hibernate.tool.hbm2ddl.SingleLineSqlCommandExtractor</tt>. / ")
	public String HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR;
	@Property(key = AvailableSettings.SQL_EXCEPTION_CONVERTER, defaultValue = "", description = "The {@link org.hibernate.exception.spi.SQLExceptionConverter} to use for converting SQLExceptions to Hibernate's JDBCException hierarchy. The default is to use the configured {@link org.hibernate.dialect.Dialect}'s preferred SQLExceptionConverter. / ")
	public String SQL_EXCEPTION_CONVERTER;
	@Property(key = AvailableSettings.WRAP_RESULT_SETS, defaultValue = "", description = "Enable wrapping of JDBC result sets in order to speed up column name lookups for broken JDBC drivers / ")
	public String WRAP_RESULT_SETS;
	@Property(key = AvailableSettings.ORDER_UPDATES, defaultValue = "", description = "Enable ordering of update statements by primary key value / ")
	public String ORDER_UPDATES;
	@Property(key = AvailableSettings.ORDER_INSERTS, defaultValue = "", description = "Enable ordering of insert statements for the purpose of more efficient JDBC batching. / ")
	public String ORDER_INSERTS;
	@Property(key = AvailableSettings.DEFAULT_NULL_ORDERING, defaultValue = "", description = "Default precedence of null values in {@code ORDER BY} clause. Supported options: {@code none} (default), {@code first}, {@code last}. / ")
	public String DEFAULT_NULL_ORDERING;
	@Property(key = AvailableSettings.DEFAULT_ENTITY_MODE, defaultValue = "", description = "The EntityMode in which set the Session opened from the SessionFactory. / ")
	public String DEFAULT_ENTITY_MODE;
	@Property(key = AvailableSettings.GLOBALLY_QUOTED_IDENTIFIERS, defaultValue = "", description = "Should all database identifiers be quoted. / ")
	public String GLOBALLY_QUOTED_IDENTIFIERS;
	@Property(key = AvailableSettings.CHECK_NULLABILITY, defaultValue = "", description = "Enable nullability checking. Raises an exception if a property marked as not-null is null. Default to false if Bean Validation is present in the classpath and Hibernate Annotations is used, true otherwise. / ")
	public String CHECK_NULLABILITY;
	@Property(key = AvailableSettings.BYTECODE_PROVIDER, defaultValue = "", description = "")
	public String BYTECODE_PROVIDER;
	@Property(key = AvailableSettings.JPAQL_STRICT_COMPLIANCE, defaultValue = "", description = "")
	public String JPAQL_STRICT_COMPLIANCE;
	@Property(key = AvailableSettings.PREFER_POOLED_VALUES_LO, defaultValue = "", description = "When using pooled {@link org.hibernate.id.enhanced.Optimizer optimizers}, prefer interpreting the database value as the lower (lo) boundary. The default is to interpret it as the high boundary. / ")
	public String PREFER_POOLED_VALUES_LO;
	@Property(key = AvailableSettings.QUERY_PLAN_CACHE_MAX_SIZE, defaultValue = "", description = "The maximum number of entries including: <ul> <li>{@link org.hibernate.engine.query.spi.HQLQueryPlan}</li> <li>{@link org.hibernate.engine.query.spi.FilterQueryPlan}</li> <li>{@link org.hibernate.engine.query.spi.NativeSQLQueryPlan}</li> </ul> maintained by {@link org.hibernate.engine.query.spi.QueryPlanCache}. Default is 2048. / ")
	public String QUERY_PLAN_CACHE_MAX_SIZE;
	@Property(key = AvailableSettings.QUERY_PLAN_CACHE_PARAMETER_METADATA_MAX_SIZE, defaultValue = "", description = "The maximum number of {@link org.hibernate.engine.query.spi.ParameterMetadata} maintained by {@link org.hibernate.engine.query.spi.QueryPlanCache}. Default is 128. / ")
	public String QUERY_PLAN_CACHE_PARAMETER_METADATA_MAX_SIZE;
	@Property(key = AvailableSettings.NON_CONTEXTUAL_LOB_CREATION, defaultValue = "", description = "Should we not use contextual LOB creation (aka based on {@link java.sql.Connection#createBlob()} et al). / ")
	public String NON_CONTEXTUAL_LOB_CREATION;
	@Property(key = AvailableSettings.CLASSLOADERS, defaultValue = "", description = "Used to define a {@link java.util.Collection} of the {@link ClassLoader} instances Hibernate should use for class-loading and resource-lookups. @since 5.0 / ")
	public String CLASSLOADERS;
	@Property(key = AvailableSettings.C3P0_CONFIG_PREFIX, defaultValue = "", description = "")
	public String C3P0_CONFIG_PREFIX;
	@Property(key = AvailableSettings.PROXOOL_CONFIG_PREFIX, defaultValue = "", description = "")
	public String PROXOOL_CONFIG_PREFIX;
	@Property(key = AvailableSettings.JMX_ENABLED, defaultValue = "", description = "")
	public String JMX_ENABLED;
	@Property(key = AvailableSettings.JMX_PLATFORM_SERVER, defaultValue = "", description = "")
	public String JMX_PLATFORM_SERVER;
	@Property(key = AvailableSettings.JMX_AGENT_ID, defaultValue = "", description = "")
	public String JMX_AGENT_ID;
	@Property(key = AvailableSettings.JMX_DOMAIN_NAME, defaultValue = "", description = "")
	public String JMX_DOMAIN_NAME;
	@Property(key = AvailableSettings.JMX_SF_NAME, defaultValue = "", description = "")
	public String JMX_SF_NAME;
	@Property(key = AvailableSettings.JMX_DEFAULT_OBJ_NAME_DOMAIN, defaultValue = "", description = "")
	public String JMX_DEFAULT_OBJ_NAME_DOMAIN;
	@Property(key = AvailableSettings.JTA_CACHE_TM, defaultValue = "", description = "A configuration value key used to indicate that it is safe to cache {@link javax.transaction.TransactionManager} references. @since 4.0 / ")
	public String JTA_CACHE_TM;
	@Property(key = AvailableSettings.JTA_CACHE_UT, defaultValue = "", description = "A configuration value key used to indicate that it is safe to cache {@link javax.transaction.UserTransaction} references. @since 4.0 / ")
	public String JTA_CACHE_UT;
	@Property(key = AvailableSettings.DEFAULT_CACHE_CONCURRENCY_STRATEGY, defaultValue = "", description = "Setting used to give the name of the default {@link org.hibernate.annotations.CacheConcurrencyStrategy} to use when either {@link javax.persistence.Cacheable @Cacheable} or {@link org.hibernate.annotations.Cache @Cache} is used. {@link org.hibernate.annotations.Cache @Cache(strategy=\"..\")} is used to override. / ")
	public String DEFAULT_CACHE_CONCURRENCY_STRATEGY;
	@Property(key = AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, defaultValue = "", description = "Setting which indicates whether or not the new {@link org.hibernate.id.IdentifierGenerator} are used for AUTO, TABLE and SEQUENCE. Default to false to keep backward compatibility. / ")
	public String USE_NEW_ID_GENERATOR_MAPPINGS;
	@Property(key = AvailableSettings.CUSTOM_ENTITY_DIRTINESS_STRATEGY, defaultValue = "", description = "Setting to identify a {@link org.hibernate.CustomEntityDirtinessStrategy} to use. May point to either a class name or instance. / ")
	public String CUSTOM_ENTITY_DIRTINESS_STRATEGY;
	@Property(key = AvailableSettings.MULTI_TENANT, defaultValue = "", description = "Strategy for multi-tenancy. @see org.hibernate.MultiTenancyStrategy @since 4.0 / ")
	public String MULTI_TENANT;
	@Property(key = AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, defaultValue = "", description = "Names a {@link org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider} implementation to use. As MultiTenantConnectionProvider is also a service, can be configured directly through the {@link org.hibernate.boot.registry.StandardServiceRegistryBuilder} @since 4.1 / ")
	public String MULTI_TENANT_CONNECTION_PROVIDER;
	@Property(key = AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, defaultValue = "", description = "Names a {@link org.hibernate.context.spi.CurrentTenantIdentifierResolver} implementation to use. <p/> Can be<ul> <li>CurrentTenantIdentifierResolver instance</li> <li>CurrentTenantIdentifierResolver implementation {@link Class} reference</li> <li>CurrentTenantIdentifierResolver implementation class name</li> </ul> @since 4.1 / ")
	public String MULTI_TENANT_IDENTIFIER_RESOLVER;
	@Property(key = AvailableSettings.FORCE_DISCRIMINATOR_IN_SELECTS_BY_DEFAULT, defaultValue = "", description = "")
	public String FORCE_DISCRIMINATOR_IN_SELECTS_BY_DEFAULT;
	@Property(key = AvailableSettings.IMPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS, defaultValue = "", description = "The legacy behavior of Hibernate is to not use discriminators for joined inheritance (Hibernate does not need the discriminator...). However, some JPA providers do need the discriminator for handling joined inheritance. In the interest of portability this capability has been added to Hibernate too. <p/> However, we want to make sure that legacy applications continue to work as well. Which puts us in a bind in terms of how to handle \"implicit\" discriminator mappings. The solution is to assume that the absence of discriminator metadata means to follow the legacy behavior unless* this setting is enabled. With this setting enabled, Hibernate will interpret the absence of discriminator metadata as an indication to use the JPA defined defaults for these absent annotations. <p/> See Hibernate Jira issue HHH-6911 for additional background info. @see #IGNORE_EXPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS / ")
	public String IMPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS;
	@Property(key = AvailableSettings.IGNORE_EXPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS, defaultValue = "", description = "The legacy behavior of Hibernate is to not use discriminators for joined inheritance (Hibernate does not need the discriminator...). However, some JPA providers do need the discriminator for handling joined inheritance. In the interest of portability this capability has been added to Hibernate too. <p/> Existing applications rely (implicitly or explicitly) on Hibernate ignoring any DiscriminatorColumn declarations on joined inheritance hierarchies. This setting allows these applications to maintain the legacy behavior of DiscriminatorColumn annotations being ignored when paired with joined inheritance. <p/> See Hibernate Jira issue HHH-6911 for additional background info. @see #IMPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS / ")
	public String IGNORE_EXPLICIT_DISCRIMINATOR_COLUMNS_FOR_JOINED_SUBCLASS;
	@Property(key = AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, defaultValue = "", description = "")
	public String ENABLE_LAZY_LOAD_NO_TRANS;
	@Property(key = AvailableSettings.HQL_BULK_ID_STRATEGY, defaultValue = "", description = "")
	public String HQL_BULK_ID_STRATEGY;
	@Property(key = AvailableSettings.BATCH_FETCH_STYLE, defaultValue = "", description = "Names the {@link org.hibernate.loader.BatchFetchStyle} to use. Can specify either the {@link org.hibernate.loader.BatchFetchStyle} name (insensitively), or a {@link org.hibernate.loader.BatchFetchStyle} instance. / ")
	public String BATCH_FETCH_STYLE;
	@Property(key = AvailableSettings.USE_DIRECT_REFERENCE_CACHE_ENTRIES, defaultValue = "", description = "Enable direct storage of entity references into the second level cache when applicable (immutable data, etc). Default is to not store direct references. / ")
	public String USE_DIRECT_REFERENCE_CACHE_ENTRIES;
	@Property(key = AvailableSettings.USE_NATIONALIZED_CHARACTER_DATA, defaultValue = "", description = "Enable nationalized character support on all string / clob based attribute ( string, char, clob, text etc ). Default is <clode>false</clode>. / ")
	public String USE_NATIONALIZED_CHARACTER_DATA;
	@Property(key = AvailableSettings.JTA_TRACK_BY_THREAD, defaultValue = "", description = "A transaction can be rolled back by another thread (\"tracking by thread\") -- not the original application. Examples of this include a JTA transaction timeout handled by a background reaper thread. The ability to handle this situation requires checking the Thread ID every time Session is called. This can certainly have performance considerations. Default is <code>true</code> (enabled). / ")
	public String JTA_TRACK_BY_THREAD;
	@Property(key = AvailableSettings.JACC_CONTEXT_ID, defaultValue = "", description = "")
	public String JACC_CONTEXT_ID;
	@Property(key = AvailableSettings.JACC_PREFIX, defaultValue = "", description = "")
	public String JACC_PREFIX;
	@Property(key = AvailableSettings.JACC_ENABLED, defaultValue = "", description = "")
	public String JACC_ENABLED;
	@Property(key = AvailableSettings.ENABLE_SYNONYMS, defaultValue = "", description = "If enabled, allows {@link org.hibernate.tool.hbm2ddl.DatabaseMetadata} to support synonyms during schema update and validations. Due to the possibility that this would return duplicate tables (especially in Oracle), this is disabled by default. / ")
	public String ENABLE_SYNONYMS;
	@Property(key = AvailableSettings.UNIQUE_CONSTRAINT_SCHEMA_UPDATE_STRATEGY, defaultValue = "", description = "Unique columns and unique keys both use unique constraints in most dialects. SchemaUpdate needs to create these constraints, but DB's support for finding existing constraints is extremely inconsistent. Further, non-explicitly-named unique constraints use randomly generated characters. Therefore, select from these strategies. {@link org.hibernate.tool.hbm2ddl.UniqueConstraintSchemaUpdateStrategy#DROP_RECREATE_QUIETLY} (DEFAULT): Attempt to drop, then (re-)create each unique constraint. Ignore any exceptions thrown. {@link org.hibernate.tool.hbm2ddl.UniqueConstraintSchemaUpdateStrategy#RECREATE_QUIETLY}: attempt to (re-)create unique constraints, ignoring exceptions thrown if the constraint already existed {@link org.hibernate.tool.hbm2ddl.UniqueConstraintSchemaUpdateStrategy#SKIP}: do not attempt to create unique constraints on a schema update / ")
	public String UNIQUE_CONSTRAINT_SCHEMA_UPDATE_STRATEGY;
	@Property(key = AvailableSettings.LOG_SESSION_METRICS, defaultValue = "", description = "A setting to control whether to {@link org.hibernate.engine.internal.StatisticalLoggingSessionEventListener} is enabled on all Sessions (unless explicitly disabled for a given Session). The default value of this setting is determined by the value for {@link #GENERATE_STATISTICS}, meaning that if collection of statistics is enabled logging of Session metrics is enabled by default too. / ")
	public String LOG_SESSION_METRICS;
	@Property(key = AvailableSettings.AUTO_SESSION_EVENTS_LISTENER, defaultValue = "", description = "")
	public String AUTO_SESSION_EVENTS_LISTENER;
	
	/**
	 * Infinispan
	 */
	@Property(key = "hibernate.cache.infinispan.cachemanager", defaultValue = "java:CacheManager")
	public String INFINISPAN_CACHE_MANAGER;
	

	@Property(key = "hibernate.search.default.directory_provider", defaultValue = "filesystem", examples = {"ram", "filesystem", "filesystem-master", "filesystem-slave"})
	public String HIBERNATE_SEARCH_DIRECTORY_PROVIDER;
	@Property(key = "hibernate.search.default.locking_strategy", defaultValue = "none", examples = {"simple", "native", "single", "none"}, description = "Simple: Creates a marker file | Native: Native OS-File Locking | Single: Java Object in memory")
	public String HIBERNATE_SEARCH_LOCKING_STRATEGY;
	@Property(key = "hibernate.search.default.filesyste_access_type", defaultValue = "auto", examples = {"auto", "simple", "nio", "mmap"})
	public String HIBERNATE_SEARCH_FILESYSTEM_ACCESS_TYPE;
	@Property(key = "hibernate.search.default.indexBase", defaultValue = "indexes")
	public String HIBERNATE_SEARCH_INDEX_BASE;
	@Property(key = "hibernate.search.default.exclusive_index_use", defaultValue = "false", examples = {"true", "false"})
	public String HIBERNATE_SEARCH_EXCLUSIVE_INDEX_USER;
	@Property(key = "hibernate.search.autoregister_listeners", defaultValue = "true", examples = {"true", "false"}, description = "disable hibernate search")
	public String HIBERNATE_SEARCH_AUTOREGISTER_LISTENERS;
//    configProperties.put("hibernate.search.default.directory_provider", MISProperties.getProperty(MISConstants.LUCENE_PROVIDER));
	// hibernate.search.default.exclusive_index_user=false
//    configProperties.put("hibernate.search.default.indexBase", MISProperties.getProperty(MISConstants.LUCENE_INDEXBASE));
	
	

	/**
	 * Flyway
	 */
	@Property(key = "chrl.orm.flyway.active", defaultValue = "true", description = "Defines if Flyway is active or not")
	public boolean FLYWAY_ACTIVE;
	
}
