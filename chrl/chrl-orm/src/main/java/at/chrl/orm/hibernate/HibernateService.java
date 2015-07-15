package at.chrl.orm.hibernate;

import static at.chrl.orm.hibernate.configuration.HibernateServiceConfig.err;
import static at.chrl.orm.hibernate.configuration.HibernateServiceConfig.out;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.AvailableSettings;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import at.chrl.nutils.ArrayUtils;
import at.chrl.nutils.JVMInfoUtil;
import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.PropertiesUtils;
import at.chrl.orm.hibernate.configuration.HibernateConfig;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;
import at.chrl.orm.hibernate.configuration.JPAConfig;

/**
 * @author bravestone
 *
 */
public final class HibernateService implements AutoCloseable {

	private final ConcurrentHashMap<JPAConfig, EntityManagerFactory> jpaDatabaseConnections;
	private final ConcurrentHashMap<HibernateConfig, SessionFactory> databaseConnections;

	// --------------------------------------------------------------------------
	// private Methods
	// --------------------------------------------------------------------------

	private boolean initJPA(final JPAConfig config) {
		if (jpaDatabaseConnections.containsKey(config))
			return false;

		Properties props = PropertiesUtils.filterEmtpyValues(ConfigUtil.getProperties(config.getClass()));

		props.put(AvailableSettings.LOADED_CLASSES, config.getAnnotatedClasses());

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(config.PERSISTENCE_UNIT_NAME, props);
			jpaDatabaseConnections.put(config, emf);
		} catch (PersistenceException pex) {
			throw new PersistenceException("Error initializing JPA-Database Connection", pex);
		}

		out.println("[Hibernate Service] Created Entity Manager Factory for Persistence Unit: " + config.PERSISTENCE_UNIT_NAME + " | " + config.toString());
		return true;
	}

	private boolean initHibernate(final HibernateConfig config) {
		if (databaseConnections.containsKey(config))
			return false;

		Properties props = PropertiesUtils.filterEmtpyValues(ConfigUtil.getProperties(config.getClass()));

		Configuration hibernateCfg = new Configuration();
		hibernateCfg.setProperties(props);

		for (Class<?> ie : config.getAnnotatedClasses())
			hibernateCfg.addAnnotatedClass(ie);

		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(hibernateCfg.getProperties());
		SessionFactory sf = hibernateCfg.buildSessionFactory(ssrb.build());

		databaseConnections.put(config, sf);

		out.println("[Hibernate Service] Created Hibernate Session Factory for: " + config.toString());
		return true;
	}

	private static Session persistSubList(final List<?> sublist, final HibernateConfig conf, Session session) {
		if (session.isOpen() && !session.getTransaction().isActive())
			session.beginTransaction();
		sublist.forEach(session::saveOrUpdate);
		try {
			session.flush();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			session.close();
			session = HibernateService.getInstance().getSession(conf);
			session.setFlushMode(FlushMode.MANUAL);
			final int listSize = sublist.size();
			final int batchSize = listSize >> 1;
			if (batchSize > 0) {
				int i = 0;
				while (i + batchSize < listSize) {
					List<?> sublist2 = sublist.subList(i, i + batchSize);
					session = persistSubList(sublist2, conf, session);
					i += batchSize;
				}
			} else {
				err.println(e.getMessage());
			}
		}
		session.flush();
		if (session.isOpen() && session.getTransaction().isActive())
			session.getTransaction().commit();
		session.clear();
		return session;
	}

	// --------------------------------------------------------------------------
	// Constructor - Private for Singleton Holder
	// --------------------------------------------------------------------------

	private HibernateService() {
		this.databaseConnections = new ConcurrentHashMap<>(5);
		this.jpaDatabaseConnections = new ConcurrentHashMap<>(5);
	}

	// --------------------------------------------------------------------------
	// Methods
	// --------------------------------------------------------------------------

	/**
	 * Tries to set up a factory for given configuration
	 * 
	 * @param hibernateConfig
	 *            configuration with connection properties
	 * @return true if connection was correctly initialized
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 * @throws HibernateException
	 *             if Session Factory could not be created (if parameter is
	 *             instance of {@link HibernateConfig})
	 * @throws PersistenceException
	 *             if Entity Manager could not be created (if parameter is
	 *             instance of {@link JPAConfig})
	 * 
	 * @see {@link SessionFactory}
	 */
	public synchronized boolean connect(final IHibernateConfig hibernateConfig) {
		if (isNull(hibernateConfig))
			throw new IllegalArgumentException("Parameter hibernateConfig of HibernateService.connect is null");

		ConfigUtil.loadAndExport(hibernateConfig);

		if (hibernateConfig instanceof JPAConfig)
			return initJPA((JPAConfig) hibernateConfig);

		if (hibernateConfig instanceof HibernateConfig)
			return initHibernate((HibernateConfig) hibernateConfig);

		return false;
	}

	/**
	 * Tries to disconnect and close factory
	 * 
	 * @param hibernateConfig
	 *            configuration with connection properties
	 * @return true if factory was properly closed <br>
	 *         false if no factory was closed (or was never connected)
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 */
	public synchronized boolean disconnect(final IHibernateConfig hibernateConfig) {
		if (isNull(hibernateConfig))
			throw new IllegalArgumentException("Parameter hibernateConfig of HibernateService.disconnect is null");

		EntityManagerFactory emf = jpaDatabaseConnections.remove(hibernateConfig);
		if (nonNull(emf) && emf.isOpen()) {
			emf.close();
			out.println("[Hibernate Service] Disconnected from " + hibernateConfig.toString() + " - Entity Manager Factory closed");
			return true;
		}

		SessionFactory sf = databaseConnections.remove(hibernateConfig);
		if (nonNull(sf) && !sf.isClosed()) {
			sf.close();
			out.println("[Hibernate Service] Disconnected from " + hibernateConfig.toString() + " - Session Factory closed");
			return true;
		}

		return false;
	}

	/**
	 * Tries to reload given connection
	 * 
	 * @param hibernateConfig
	 *            configuration to reload
	 * @return false if this configuration has no cached connection, or reload
	 *         was not successful <br>
	 *         true otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 * @throws HibernateException
	 *             if Session Factory could not be created
	 * @throws PersistenceException
	 *             if Entity Manager could not be created
	 * 
	 * @see {@link HibernateService#connect(IHibernateConfig)}
	 * @see {@link HibernateService#disconnect(IHibernateConfig)}
	 */
	public synchronized boolean reloadConfiguration(final IHibernateConfig hibernateConfig) {
		if (isNull(hibernateConfig))
			throw new IllegalArgumentException("Parameter hibernateConfig of HibernateService.reloadConfiguration is null");
		if (!jpaDatabaseConnections.containsKey(hibernateConfig) && !databaseConnections.containsKey(hibernateConfig))
			return false;

		disconnect(hibernateConfig);
		connect(hibernateConfig);

		return true;
	}

	/**
	 * Returns a Session Factory Cached by this Service <br>
	 * Returns like specified in {@link ConcurrentHashMap#get(Object)}
	 * <p>
	 * 
	 * if given parameter is a instance of {@link JPAConfig} this method will
	 * cast a given {@link HibernateEntityManagerFactory} to a
	 * {@link SessionFactory} if necessery.
	 * 
	 * @param hconfig
	 *            configuration to look for
	 * @return Cached Session Factory or null
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 */
	public SessionFactory getSessionFactory(final IHibernateConfig hconfig) {
		if (isNull(hconfig))
			throw new IllegalArgumentException("Parameter hconfig of HibernateService.getSessionFactory is null");
		SessionFactory sf = databaseConnections.get(hconfig);
		if (isNull(sf)) {
			if (hconfig instanceof JPAConfig) {
				sf = ((HibernateEntityManagerFactory) jpaDatabaseConnections.get(hconfig)).getSessionFactory();
			}
		}
		return sf;
	}

	/**
	 * Returns a Entity Manager Factory Cached by this Service <br>
	 * Returns like specified in {@link ConcurrentHashMap#get(Object)}
	 * 
	 * @param config
	 *            configuration to look for
	 * @return Cached Manager Factory or null
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 */
	public EntityManagerFactory getEntityManagerFactory(final JPAConfig config) {
		if (isNull(config))
			throw new IllegalArgumentException("Parameter config of HibernateService.getEntityManagerFactory is null");
		return jpaDatabaseConnections.get(config);
	}

	/**
	 * Returns a new open and valid {@link Session}. Calls
	 * {@link HibernateService#connect(IHibernateConfig)} if needed.
	 * 
	 * @param config
	 *            configuration
	 * @return a new open Hibernate Session
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 * @throws HibernateException
	 *             if Session Factory could not be created (if parameter is
	 *             instance of {@link HibernateConfig}) -
	 *             {@link HibernateService#connect(IHibernateConfig)}
	 * @throws PersistenceException
	 *             if Entity Manager could not be created (if parameter is
	 *             instance of {@link JPAConfig}) -
	 *             {@link HibernateService#connect(IHibernateConfig)}
	 * @throws NullPointerException
	 *             if no {@link SessionFactory} could be obtained, when
	 *             {@link HibernateService#connect(IHibernateConfig)} was called
	 * @throws NullPointerException
	 *             if config is no instance of {@link HibernateConfig} or
	 *             {@link JPAConfig}
	 * 
	 * @see {@link HibernateService#connect(IHibernateConfig)}
	 * @see {@link HibernateService#disconnect(IHibernateConfig)}
	 */
	public Session getSession(final IHibernateConfig config) {
		if (isNull(config))
			throw new IllegalArgumentException("Parameter config of HibernateService.getSession is null");

		if (config instanceof JPAConfig) {
			EntityManagerFactory emf = getEntityManagerFactory((JPAConfig) config);
			if (isNull(emf)) {
				connect(config);
				emf = getEntityManagerFactory((JPAConfig) config);
			}
			HibernateEntityManager hem = emf.createEntityManager().unwrap(HibernateEntityManager.class);
			return hem.getSession();
		}

		if (config instanceof HibernateConfig) {
			SessionFactory sf = getSessionFactory((HibernateConfig) config);
			if (isNull(sf)) {
				connect(config);
				sf = getSessionFactory((HibernateConfig) config);
			}
			return sf.openSession();
		}
		throw new NullPointerException("No Session could be created: config parameter is no subtype of HibernateConfig oder JPAConfig");
	}

	/**
	 * Returns a new open and valid {@link StatelessSession}. Calls
	 * {@link HibernateService#connect(IHibernateConfig)} if needed.
	 * 
	 * @param config
	 *            configuration
	 * @return a new open Hibernate Stateless Session
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 * @throws HibernateException
	 *             if Session Factory could not be created (if parameter is
	 *             instance of {@link HibernateConfig}) -
	 *             {@link HibernateService#connect(IHibernateConfig)}
	 * @throws PersistenceException
	 *             if Entity Manager could not be created (if parameter is
	 *             instance of {@link JPAConfig}) -
	 *             {@link HibernateService#connect(IHibernateConfig)}
	 * @throws NullPointerException
	 *             if no {@link SessionFactory} could be obtained, when
	 *             {@link HibernateService#connect(IHibernateConfig)} was called
	 * @throws NullPointerException
	 *             if config is no instance of {@link HibernateConfig} or
	 *             {@link JPAConfig}
	 * 
	 * @see {@link HibernateService#connect(IHibernateConfig)}
	 * @see {@link HibernateService#disconnect(IHibernateConfig)}
	 */
	public StatelessSession getStatelessSession(final IHibernateConfig config) {
		if (isNull(config))
			throw new IllegalArgumentException("Parameter config of HibernateService.getStatelessSession is null");

		if (config instanceof JPAConfig) {
			EntityManagerFactory emf = getEntityManagerFactory((JPAConfig) config);
			if (isNull(emf)) {
				connect(config);
				emf = getEntityManagerFactory((JPAConfig) config);
			}
			HibernateEntityManager hem = emf.createEntityManager().unwrap(HibernateEntityManager.class);
			return hem.getSession().getSessionFactory().openStatelessSession();

		}

		if (config instanceof HibernateConfig) {
			SessionFactory sf = getSessionFactory((HibernateConfig) config);
			if (isNull(sf)) {
				connect(config);
				sf = getSessionFactory((HibernateConfig) config);
			}
			return sf.openStatelessSession();
		}
		throw new NullPointerException("No Stateless Session could be created: config parameter is no subtype of HibernateConfig oder JPAConfig");
	}

	/**
	 * Returns a new {@link EntityManager}. Calls
	 * {@link HibernateService#connect(IHibernateConfig)} if needed.
	 * 
	 * @param config
	 *            configuration
	 * @return a new Entity Manager
	 * 
	 * @throws IllegalArgumentException
	 *             if parameter is null
	 * @throws PersistenceException
	 *             if Entity Manager could not be created -
	 *             {@link HibernateService#connect(IHibernateConfig)}
	 * @throws NullPointerException
	 *             if no {@link SessionFactory} could be obtained, when
	 *             {@link HibernateService#connect(IHibernateConfig)} was called
	 * 
	 * @see {@link HibernateService#connect(IHibernateConfig)}
	 * @see {@link HibernateService#disconnect(IHibernateConfig)}
	 */
	public EntityManager getEntityManager(final JPAConfig config) {
		if (isNull(config))
			throw new IllegalArgumentException("Parameter config of HibernateService.getEntityManager is null");

		EntityManagerFactory emf = getEntityManagerFactory((JPAConfig) config);
		if (isNull(emf)) {
			connect(config);
			emf = getEntityManagerFactory((JPAConfig) config);
		}

		return emf.createEntityManager();
	}

	/**
	 * Creates Schema of given Config in given File. Also prints output to
	 * console.
	 * 
	 * @param config
	 *            HibernateConfig config. Must be {@link HibernateConfig}.
	 * @param exportFile
	 *            Export File Directory
	 * 
	 * @throws HibernateException
	 *             of config is no {@link HibernateConfig}
	 * 
	 */
	public void exportSchema(final HibernateConfig config, final String exportFile) {
		Properties props = PropertiesUtils.filterEmtpyValues(ConfigUtil.getProperties(config.getClass()));

		Configuration hibernateCfg = new Configuration();
		hibernateCfg.setProperties(props);

		for (Class<?> ie : config.getAnnotatedClasses())
			hibernateCfg.addAnnotatedClass(ie);

		SchemaExport ex = new SchemaExport(hibernateCfg);

		ex.setOutputFile(exportFile);
		ex.create(true, false);
	}

	/**
	 * Returns statistics for every database connections. <br>
	 * make sure statistic generation is set to true in hibernate config
	 * 
	 * @see HibernateConfig#GENERATE_STATISTICS
	 * 
	 * @return statistics
	 */
	public String[] getFullStatistics() {
		String[] returnMe = new String[] {};

		returnMe = databaseConnections.keySet().stream().reduce(returnMe, (a, o) -> ArrayUtils.addAll(a, HibernateStatisticService.getFullStatistics(o)), ArrayUtils::addAll);
		returnMe = jpaDatabaseConnections.keySet().stream().reduce(returnMe, (a, o) -> ArrayUtils.addAll(a, HibernateStatisticService.getFullStatistics(o)), ArrayUtils::addAll);

		return returnMe;
	}

	/**
	 * Calls {@link #persistList(List, HibernateConfig)} concurrently. <br>
	 * Creates {@link HibernateConfig#C3P0_MAX_SIZE} * laodFactor Threads. <br>
	 * If single threaded processing is needed (loadFacoter == 0) use
	 * {@link #persistList(List, HibernateConfig)} instead
	 * 
	 * @see #persistList(List, HibernateConfig)
	 * 
	 * @throws IllegalArgumentException
	 *             if loadFactor 0s > 1d || <= 0d
	 * @throws Exception
	 *             as specified in {@link #persistList(List, HibernateConfig)}
	 * 
	 * @param list
	 *            object list to persist
	 * @param conf
	 *            database connection configuration
	 * @param loadFactor
	 *            % of used Sessions. <br>
	 *            1 equals {@link HibernateConfig#C3P0_MAX_SIZE} used sessions.
	 *            0 equals 1 used session. Use
	 *            {@link #persistList(List, HibernateConfig)} in this case.
	 */
	public static void persistListParallel(final List<?> list, final HibernateConfig conf, final double loadFactor) {
		if (loadFactor > 1d || loadFactor <= 0d)
			throw new IllegalArgumentException("LoadFactor must be <= 1d and > 0d");
		final int poolSize = (int) Math.ceil(Integer.valueOf(conf.C3P0_MAX_SIZE) * loadFactor);
		Collection<?> values = chopped(list, poolSize);
		ThreadPoolExecutor exec = new ThreadPoolExecutor(poolSize, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

		values.forEach(l -> exec.execute(() -> persistList((List<?>) l, conf)));
	}

	private static <T> List<List<T>> chopped(List<T> list, final int L) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += L) {
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}

	/**
	 * Calls {@link Session#saveOrUpdate(Object)} on every object in given list. <br>
	 * if a exception occurs, given object wont be persisted. <br>
	 * calls {@link #getSession(IHibernateConfig)} <br>
	 * uses {@link HibernateConfig#STATEMENT_BATCH_SIZE} for batch processing
	 * 
	 * @see HibernateService#getSession(IHibernateConfig)
	 * @see HibernateConfig#STATEMENT_BATCH_SIZE
	 * 
	 * @throws Exception
	 *             as specified in {@link #getSession(IHibernateConfig)}
	 * @throws Exception
	 *             as specified in {@link Session#saveOrUpdate(Object)}
	 * @throws Exception
	 *             as specified in {@link Session#flush(Object)}
	 * @throws Exception
	 *             as specified in {@link Session#close(Object)}
	 * @throws Exception
	 *             as specified in {@link Session#getTransaction()}
	 * @throws Exception
	 *             as specified in {@link Transaction#begin()}
	 * @throws Exception
	 *             as specified in {@link Transaction#rollback()}
	 * 
	 * @param list
	 *            object list to persist
	 * @param conf
	 *            database connection configuration
	 */
	public static void persistList(final List<?> list, final HibernateConfig conf) {
		final int batchSize = Integer.valueOf(conf.STATEMENT_BATCH_SIZE);
		final int listSize = list.size();
		Session session = HibernateService.getInstance().getSession(conf);
		session.setFlushMode(FlushMode.MANUAL);
		int i = 0;
		while (i + batchSize < listSize) {
			List<?> sublist = list.subList(i, i + batchSize);
			session = persistSubList(sublist, conf, session);
			i += batchSize;
		}
		persistSubList(list.subList(i, listSize - 1), conf, session);
		try {
			session.close();
		} catch (SessionException e) {
			// ignore
		}
	}

	// --------------------------------------------------------------------------
	// Implementations
	// --------------------------------------------------------------------------

	/**
	 * Closes all Cached Factories
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		for (final SessionFactory ie : databaseConnections.values()) {
			try {
				ie.close();
			} catch (Exception e) {
				err.println("Error closing factory: " + e.getMessage());
				e.printStackTrace(err);
			}
		}
		for (final EntityManagerFactory ie : jpaDatabaseConnections.values()) {
			try {
				ie.close();
			} catch (Exception e) {
				err.println("Error closing factory: " + e.getMessage());
				e.printStackTrace(err);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder((databaseConnections.size() + jpaDatabaseConnections.size()) * 100);

		sb.append(JVMInfoUtil.printSection(super.toString()));
		sb.append(System.lineSeparator());
		if (databaseConnections.size() > 0) {
			sb.append(JVMInfoUtil.printSection("Hibernate Connections"));
			sb.append(System.lineSeparator());
			for (HibernateConfig iterable_element : databaseConnections.keySet()) {
				sb.append(iterable_element).append(System.lineSeparator());
			}
		}

		if (jpaDatabaseConnections.size() > 0) {
			sb.append(JVMInfoUtil.printSection("JPA Connections"));
			sb.append(System.lineSeparator());
			for (JPAConfig iterable_element : jpaDatabaseConnections.keySet()) {
				sb.append(iterable_element).append(System.lineSeparator());
			}
		}

		return sb.toString();
	}

	// --------------------------------------------------------------------------
	// Singleton Holder
	// --------------------------------------------------------------------------

	private static class SingletonHolder {
		private static final HibernateService instance = new HibernateService();
	}

	public static HibernateService getInstance() {
		return SingletonHolder.instance;
	}
}
