package at.chrl.orm.hibernate;

import gnu.trove.set.hash.THashSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;

import at.chrl.nutils.ArrayUtils;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;

public abstract class SessionTemplate implements AutoCloseable {

	private static Map<Session, String> openSessions = new ConcurrentHashMap<>();

	public static synchronized void shutdown() {
		Map<Session, String> opSes = openSessions;
		openSessions = null;

		for (Session session : opSes.keySet()) {
			try {
				if (session.getTransaction().isActive())
					if (!session.getTransaction().wasCommitted())
						session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				System.err.println("Exception in Shutdown Progress");
			}
		}
	}

	protected abstract IHibernateConfig getHibernateConfig();

	protected final Session session;
	private boolean loggingEnabled;

	/**
	 * Get the pojo object for the given Pojo Class and identifier
	 * 
	 * @param cls
	 *            - the Pojo Class
	 * @param id
	 *            - the identifier for the pojo object
	 * @return the pojo object for the given identifier or null if object
	 *         doesn't exists
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObjectForPK(Class<T> cls, java.io.Serializable id) {
		if (id == null)
			return null;
		
		if(loggingEnabled)
			logQuery(true);
		
		return (T) session.get(cls, id);
	}
	
	@SuppressWarnings("unchecked")
	public <T, K> Collection<T> getObjectsForPK(Class<T> cls, Collection<K> ids) {
		if (ids == null || ids.isEmpty())
			return Collections.emptyList();
		
		if(loggingEnabled)
			logQuery(false);
		
		String idFieldName = session.getSessionFactory().getClassMetadata(cls).getIdentifierPropertyName();
		return session.createCriteria(cls).add(Restrictions.in(idFieldName, ids)).list();
	}
	
	protected SessionTemplate() {
		this.session = newSession();
		this.loggingEnabled = getHibernateConfig().isLoggingEnabled();
	}

	private Session newSession() {
		Session returnMe = HibernateService.getInstance().getSession(this.getHibernateConfig());
		// System.out.println(Thread.currentThread().getStackTrace()[4].toString());
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder(250);
		for (int i = 6; i >= 4; i--) {
			if (stackTrace.length > i) {
				sb.append(stackTrace[i].toString()).append(" -> ");
			}
		}
		sb.append(" - ").append(new Date().toString());
		openSessions.put(returnMe, sb.toString());
		returnMe.beginTransaction();
		return returnMe;
	}

	public StatelessSession createStatelessSession() {
		return HibernateService.getInstance().getStatelessSession(this.getHibernateConfig());
	}

	public static String[][] printOpenSessionInfo() {
		return openSessions.values().stream().reduce(new String[][] {}, (a, s2) -> ArrayUtils.addAll(a, new String[][] { {}, { s2 } }), ArrayUtils::addAll);
	}

	public void save(Object obj) throws Exception {
		try {
			if (!session.getTransaction().isActive())
				session.getTransaction().begin();
			session.persist(obj);
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}

	public void saveOrUpdate(Object obj) throws Exception {
		try {
			if (!session.getTransaction().isActive())
				session.getTransaction().begin();
			session.saveOrUpdate(obj);
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T merge(T obj) throws Exception {
		try {
			if (!session.getTransaction().isActive())
				session.getTransaction().begin();
			return (T) session.merge(obj);
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}

	public void update(Object obj) throws Exception {
		try {
			if (!session.getTransaction().isActive())
				session.getTransaction().begin();
			session.update(obj);
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}

	public void refresh(Object obj) throws Exception {
		try {
			if (!session.getTransaction().isActive())
				session.getTransaction().begin();
			session.refresh(obj);
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}

	public void delete(Object obj) throws Exception {
		try {
			if (!session.getTransaction().isActive())
				session.getTransaction().begin();
			session.delete(obj);
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}

	public SessionFactory getSessionFactory() {
		return session.getSessionFactory();
	}

	/**
	 * Rollback a Transaction
	 */
	protected void rollback() {
		session.getTransaction().rollback();
	}

	/**
	 * Check if the Session is still opened
	 * 
	 * @return
	 */
	public boolean isOpen() {
		return session.isOpen();
	}

	/**
	 * Create query
	 * 
	 * @param stmt
	 *            the statement
	 */
	public org.hibernate.Query createQuery(String stmt) {
		return session.createQuery(stmt).setCacheable(true);
	}

	/**
	 * Create SQL query
	 * 
	 * @param stmt
	 *            the SQL statement
	 */
	public org.hibernate.SQLQuery createSQLQuery(String stmt) {
		return (SQLQuery) session.createSQLQuery(stmt).setCacheable(true);
	}

	/**
	 * Flush
	 */
	public void flush() {
		session.flush();
	}

	/**
	 * Evict
	 */
	public void evict(Object obj) {
		session.evict(obj);
	}
	
	protected Session getSession(){
		return session;
	}

	public <T> Collection<T> executeQuery(Query q){
		return executeQuery(q, 0);
	}
	
	public <T> Collection<T> executeQuery(Query q, final int maxResults){
		return executeQuery(q, maxResults, true);
	}
	
	public int executeUpdate(Query q){
		
		if(loggingEnabled)
			logQuery(false);
		
		try {
			return q.executeUpdate();			
		} catch (Exception e) {
			rollback();
			e.printStackTrace();
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Collection<T> executeQuery(Query q, final int maxResults, boolean chacheable){
		if(maxResults > 0)
			q = q.setMaxResults(maxResults);
		q = q.setCacheable(chacheable);
		
		if(loggingEnabled)
			logQuery(false);
		
		try {
			return filterNull(q.list());			
		} catch (Exception e) {
			rollback();
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public <T> Collection<T> getAll(Class<T> entityClass){
		return this.getAll(entityClass, 0);
	}
	
	public <T> Collection<T> getAll(Class<T> entityClass, int maxResults){
		return this.executeQuery(session.createQuery("select e from " + entityClass.getSimpleName() + " e"), maxResults);
	}
	
	public <T> Collection<T> executeHQLQuery(final String query, final int maxResults){
		return executeQuery(session.createQuery(query), maxResults);
	}
	
	public <T> Collection<T> executeSQLQuery(final String query, final int maxResults){
		return executeQuery(session.createSQLQuery(query), maxResults, false);
	}
	
	public <T> Collection<T> executeNamedQuery(final String query, final int maxResults){
		return executeQuery(session.getNamedQuery(query), maxResults);
	}
	
	public <T> Collection<T> executeHQLQuery(final String query){
		return executeHQLQuery(query, 0);
	}
	
	public <T> Collection<T> executeSQLQuery(final String query){
		return executeSQLQuery(query, 0);
	}
	
	public <T> Collection<T> executeNamedQuery(final String query){
		return executeNamedQuery(query, 0);
	}
	
	public <T> T executeQueryUniqueResult(Query q){
		return executeQueryUniqueResult(q, true);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T executeQueryUniqueResult(Query q, boolean chacheable){
		q = q.setMaxResults(1);
		q = q.setCacheable(chacheable);

		if(loggingEnabled)
			logQuery(true);
		Object uniqueResult = q.uniqueResult();
		return (T) uniqueResult;
	}
	
	public <T> T executeHQLQueryUniqueResult(final String query){
		return executeQueryUniqueResult(session.createQuery(query));
	}
	
	public <T> T executeSQLQueryUniqueResult(final String query){
		return executeQueryUniqueResult(session.createSQLQuery(query), false);
	}
	
	public <T> T executeNamedQueryUniqueResult(final String query){
		return executeQueryUniqueResult(session.getNamedQuery(query));
	}
	
	public static <T> Collection<T> filterNull(Collection<T> input){
		if(Objects.isNull(input))
			return Collections.emptyList();
		return input.stream().filter(Objects::nonNull).collect(Collectors.toCollection(THashSet::new));
	}
	
	private static void logQuery(boolean uniqueResult){
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder(250);
		for (int i = 10; i >= 3; i--) {
			if (stackTrace.length > i) {
				sb.append(stackTrace[i].toString()).append(" -> ");
			}
		}
		if(uniqueResult)
			System.out.println("Execute unique result query from: " + sb.toString());
		else
			System.out.println("Execute query from: " + sb.toString());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		if (session.getTransaction().isActive())
			if (!session.getTransaction().wasCommitted())
				session.getTransaction().commit();
		session.close();
		openSessions.remove(session);
		if(loggingEnabled)
			System.out.println("close session " + this);
	}
}
