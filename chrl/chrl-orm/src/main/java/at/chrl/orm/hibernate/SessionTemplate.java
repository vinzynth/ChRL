package at.chrl.orm.hibernate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;

import at.chrl.nutils.ArrayUtils;
import at.chrl.orm.hibernate.configuration.HibernateConfig;
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
	private int fetchSize = 0;

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

		if (loggingEnabled)
			logQuery(true);

		return (T) session.get(cls, id);
	}

	/**
	 * get Objects for primary key
	 * 
	 * @param cls
	 *            - given persistence class
	 * @param ids
	 *            - identifier
	 * 
	 * @return Collection with given Objects (persistent)
	 */
	public <T, K> Collection<T> getObjectsForPK(Class<T> cls, Collection<K> ids) {
		if (ids == null || ids.isEmpty())
			return Collections.emptyList();

		String idFieldName = session.getSessionFactory().getClassMetadata(cls)
				.getIdentifierPropertyName();
		return executeQuery(createCriteria(cls)
				.add(Restrictions.in(idFieldName, ids)));
	}

	protected SessionTemplate() {
		this.session = newSession();
		this.loggingEnabled = getHibernateConfig().isLoggingEnabled();
	}

	/**
	 * Creates and opens a new Session
	 * 
	 * @return new session
	 */
	private Session newSession() {
		Session returnMe = HibernateService.getInstance().getSession(
				this.getHibernateConfig());
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

	/**
	 * Creates a new {@link StatelessSession} instance
	 * 
	 * @return new {@link StatelessSession}
	 */
	public StatelessSession createStatelessSession() {
		return HibernateService.getInstance().getStatelessSession(
				this.getHibernateConfig());
	}

	/**
	 * Returns information for open sessions
	 * 
	 * @return
	 */
	public static String[][] printOpenSessionInfo() {
		return openSessions
				.values()
				.stream()
				.reduce(new String[][] {},
						(a, s2) -> ArrayUtils.addAll(a, new String[][] { {},
								{ s2 } }), ArrayUtils::addAll);
	}

	/**
	 * Calls {@link Session#save(Object)}
	 * 
	 * @param obj
	 * @throws Exception
	 */
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

	/**
	 * Calls {@link Session#saveOrUpdate(Object)}
	 * 
	 * @param obj
	 * @throws Exception
	 */
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

	/**
	 * Calls {@link Session#merge(Object)}
	 * 
	 * @param obj
	 * @throws Exception
	 */
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

	/**
	 * Calls {@link Session#update(Object)}
	 * 
	 * @param obj
	 * @throws Exception
	 */
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

	/**
	 * Calls {@link Session#refresh(Object)}
	 * 
	 * @param obj
	 * @throws Exception
	 */
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

	/**
	 * Calls {@link Session#delete(Object)}
	 * 
	 * @param obj
	 * @throws Exception
	 */
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

	/**
	 * Returns active Session Factory
	 * 
	 * @return session Factory
	 */
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

	/**
	 * returns low level Hibernate {@link Session}
	 * 
	 * @return associated session
	 */
	protected Session getSession() {
		return session;
	}

	/**
	 * Returns criteria associated with this session for given class
	 * 
	 * @param persistentClass
	 *            - given class
	 * @return empty criteria query
	 */
	public <T> Criteria createCriteria(Class<T> persistentClass) {
		return session.createCriteria(persistentClass);
	}

	/**
	 * Executes given {@link Criteria}
	 * 
	 * @param crit
	 *            - criteria to execute
	 * @return result set as collection
	 */
	public <T> Collection<T> executeQuery(Criteria crit) {
		return executeQuery(crit, 0);
	}

	/**
	 * Executes given {@link Criteria}
	 * 
	 * @param crit
	 *            - criteria to execute
	 * @param maxResults
	 *            - maximal results
	 * @return result set as collection
	 */
	public <T> Collection<T> executeQuery(Criteria crit, final int maxResults) {
		return executeQuery(crit, maxResults, true);
	}

	/**
	 * Executes given {@link Query}
	 * 
	 * @param q
	 *            - query to execute
	 * @return result set as collection
	 */
	public <T> Collection<T> executeQuery(Query q) {
		return executeQuery(q, 0);
	}

	/**
	 * Executes given {@link Query}
	 * 
	 * @param q
	 *            - query to execute
	 * @param maxResults
	 *            - maximal results
	 * @return result set as collection
	 */
	public <T> Collection<T> executeQuery(Query q, final int maxResults) {
		return executeQuery(q, maxResults, true);
	}

	/**
	 * Executes Update Statement
	 * 
	 * @param q
	 *            - given query
	 * @return affected row count
	 */
	public int executeUpdate(Query q) {

		if (loggingEnabled)
			logQuery(false);

		try {
			return q.executeUpdate();
		} catch (Exception e) {
			rollback();
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Executes given Query
	 * 
	 * @param q
	 *            - given query
	 * @param maxResults
	 *            - maximal results
	 * @param chacheable
	 *            - cachable
	 * 
	 * @return Collection with results or empty collection(No null elements)
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> executeQuery(Query q, final int maxResults,
			boolean chacheable) {
		if (maxResults > 0)
			q = q.setMaxResults(maxResults);
		q = q.setCacheable(chacheable);

		if (loggingEnabled)
			logQuery(false);

		try {
			return filterNull(q.list());
		} catch (Exception e) {
			rollback();
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * Executes given Criteria
	 * 
	 * @param crit
	 *            - given criteria
	 * @param maxResults
	 *            - maxResults
	 * @param chacheable
	 *            - cacheable
	 * 
	 * @return Collection with results or empty collection(No null elements)
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> executeQuery(Criteria crit, final int maxResults,
			boolean chacheable) {
		if (maxResults > 0)
			crit = crit.setMaxResults(maxResults);
		crit = crit.setCacheable(chacheable);

		if (loggingEnabled)
			logQuery(false);

		try {
			return filterNull(crit.list());
		} catch (Exception e) {
			rollback();
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * Scroll over the query results
	 * 
	 * @param q
	 *            - given query
	 * 
	 * @return {@link Iterator} instance
	 */
	public <T> Iterable<T> scroll(Query q) {
		return scroll(q, false);
	}

	/**
	 * Scroll over the query results
	 * 
	 * @param q
	 *            - given query
	 * @param stateless
	 *            - stateless flag
	 * 
	 * @return {@link Iterable} instance
	 */
	public <T> Iterable<T> scroll(Query q, boolean stateless) {
		return new QueryIterable<T>(new QueryIterator<T>(q, this, stateless));
	}
	
	/**
	 * Scroll over the query results with a iterator
	 * 
	 * @see {@link SessionTemplate#scroll(Query)}
	 * 
	 * @param q
	 *            - given query
	 * @param stateless
	 *            - stateless flag
	 * 
	 * @return {@link Iterable} instance
	 */
	public <T> Iterator<T> iterator(Query q, boolean stateless) {
		return new QueryIterator<T>(q, this, stateless);
	}
	
	/**
	 * Scroll over the query results
	 * 
	 * @param cri
	 *            - given criteria
	 * 
	 * @return {@link Iterator} instance
	 */
	public <T> Iterable<T> scroll(Criteria crit) {
		return scroll(crit, false);
	}

	/**
	 * Scroll over the query results
	 * 
	 * @param crit
	 *            - given criteria
	 * @param stateless
	 *            - stateless flag
	 * 
	 * @return {@link Iterable} instance
	 */
	public <T> Iterable<T> scroll(Criteria crit, boolean stateless) {
		return new QueryIterable<T>(new QueryIterator<T>(crit, this, stateless));
	}
	
	/**
	 * Scroll over the query results with a iterator
	 * 
	 * @see {@link SessionTemplate#scroll(Criteria)}
	 * 
	 * @param crit
	 *            - given criteria
	 * @param stateless
	 *            - stateless flag
	 * 
	 * @return {@link Iterator} instance
	 */
	public <T> Iterator<T> iterator(Criteria crit, boolean stateless) {
		return new QueryIterator<T>(crit, this, stateless);
	}

	/**
	 * Creates {@link Stream} with given query Result
	 * 
	 * @param q
	 *            - given Query q. q is marked as ReadOnly with
	 *            {@link Query#setReadOnly(true)}
	 * @return new {@link Stream} with given ResultSet
	 */
	public <T> Stream<T> streamReadOnly(Query q) {
		return stream(q.setReadOnly(true));
	}

	/**
	 * crates a Stream with given {@link Query} q
	 * 
	 * @param q
	 *            - given Query
	 * @return new {@link Stream} with given ResultSet
	 */
	public <T> Stream<T> stream(Query q) {
		if (!session.getTransaction().isActive())
			session.beginTransaction();

		if (loggingEnabled)
			logQuery(false);
		
		return StreamSupport.<T> stream(Spliterators.spliteratorUnknownSize(
				new QueryIterator<T>(q.setCacheMode(CacheMode.IGNORE)
						.setFlushMode(FlushMode.MANUAL), this, false),
				Spliterator.ORDERED | Spliterator.DISTINCT), false);
	}

	/**
	 * creates a {@link Stream} with given query String in a
	 * {@link StatelessSession}
	 * 
	 * @param query
	 *            - given query String
	 * @return new {@link Stream} with given ResultSet
	 */
	public <T> Stream<T> streamStateless(String query) {
		StatelessSession ses = createStatelessSession();
		Query q = ses.createQuery(query);

		if (loggingEnabled)
			logQuery(false);
		
		return StreamSupport.<T> stream(Spliterators.spliteratorUnknownSize(
				new QueryIterator<T>(q, this, true), Spliterator.ORDERED
						| Spliterator.DISTINCT), false);
	}

	/**
	 * Creates {@link Stream} with given criteria Result
	 * 
	 * @param crit
	 *            - given Critera crit. crit is marked as ReadOnly with
	 *            {@link Query#setReadOnly(true)}
	 * @return new {@link Stream} with given ResultSet
	 */
	public <T> Stream<T> streamReadOnly(Criteria crit) {
		return stream(crit.setReadOnly(true));
	}

	/**
	 * crates a Stream with given {@link Criteria} crit
	 * 
	 * @param crit
	 *            - given Criteria
	 * @return new {@link Stream} with given ResultSet
	 */
	public <T> Stream<T> stream(Criteria crit) {
		if (!session.getTransaction().isActive())
			session.beginTransaction();

		if (loggingEnabled)
			logQuery(false);
		
		return StreamSupport.<T> stream(Spliterators.spliteratorUnknownSize(
				new QueryIterator<T>(crit.setCacheMode(CacheMode.IGNORE)
						.setFlushMode(FlushMode.MANUAL), this, false),
				Spliterator.ORDERED | Spliterator.DISTINCT), false);
	}

	/**
	 * creates a {@link Stream} with given query String in a
	 * {@link StatelessSession}
	 * 
	 * @param persistentClass
	 *            - class parameter for
	 *            {@link StatelessSession#createCriteria(Class)} - given query
	 *            String
	 * @param criteriaFilter
	 *            - manipulationFunction for created Criteria. Called before
	 *            executing
	 * 
	 * @return new {@link Stream} with given ResultSet
	 */
	public <T> Stream<T> streamStateless(Class<?> persistentClass,
			Function<Criteria, Criteria> criteriaFilter) {
		StatelessSession ses = createStatelessSession();
		Criteria crit = criteriaFilter.apply(ses
				.createCriteria(persistentClass));

		if (loggingEnabled)
			logQuery(false);
		
		return StreamSupport.<T> stream(Spliterators.spliteratorUnknownSize(
				new QueryIterator<T>(crit, this, true), Spliterator.ORDERED
						| Spliterator.DISTINCT), false);
	}

	/**
	 * Intern iterator class for result scrolling
	 * 
	 * @author vinzynth Jul 15, 2015 - 1:25:03 PM
	 *
	 * @param <T>
	 *            - Class elements
	 */
	private static class QueryIterator<T> implements Iterator<T> {
		private ScrollableResults scroll;
		private Queue<T> queue;
		private boolean stateless;
		private SessionTemplate session;

		/**
		 * Constructor with {@link Query}
		 * 
		 * @param q
		 *            - given query
		 * @param session
		 *            - sessiontemplate
		 * @param stateless
		 *            - stateless flag
		 */
		@SuppressWarnings("unchecked")
		public QueryIterator(Query q, SessionTemplate session, boolean stateless) {
			this.session = session;
			this.stateless = stateless;
			this.queue = new ArrayDeque<>(session.getFetchSize());

			if (session.loggingEnabled)
				logQuery(false);

			final int fs = session.getFetchSize();
			scroll = q.setFetchSize(fs).scroll(ScrollMode.FORWARD_ONLY);
			for (int i = 0; i < fs; i++)
				if (!scroll.next())
					break;
				else
					queue.add((T) scroll.get(0));
		}

		/**
		 * Constructor with {@link Criteria}
		 * 
		 * @param crit
		 *            - given query
		 * @param session
		 *            - sessiontemplate
		 * @param stateless
		 *            - stateless flag
		 */
		@SuppressWarnings("unchecked")
		public QueryIterator(Criteria crit, SessionTemplate session,
				boolean stateless) {
			this.session = session;
			this.stateless = stateless;
			this.queue = new ArrayDeque<>(session.getFetchSize());

			if (session.loggingEnabled)
				logQuery(false);

			final int fs = session.getFetchSize();
			scroll = crit.setFetchSize(fs).scroll(ScrollMode.FORWARD_ONLY);
			for (int i = 0; i < fs; i++)
				if (!scroll.next())
					break;
				else
					queue.add((T) scroll.get(0));
		}

		/**
		 * checks if queue iterator is empty
		 * 
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			boolean empty = queue.isEmpty();
			if (!stateless && empty)
				session.getSession().flush();
			return !empty;
		}

		/**
		 * gets and returns next element. loads further elements if queue would
		 * be empty
		 * 
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next() {
			T next = queue.poll();
			T peek = queue.peek();
			if (Objects.isNull(peek))
				nextBuffer();
			return next;
		}

		/**
		 * loads next batch of elements in the queue
		 */
		@SuppressWarnings("unchecked")
		private void nextBuffer() {
			if (!stateless) {
				session.getSession().flush();
				session.getSession().clear();
			}
			final int fs = session.getFetchSize();
			for (int i = 0; i < fs; i++)
				if (!scroll.next())
					break;
				else
					queue.add((T) scroll.get(0));
		}
	}
	
	/**
	 * Intern Iterable implementation for {@link QueryIterator}
	 * 
	 * @author Christian Richard Leopold - ChRL <br>
	 * Jul 24, 2015 - 6:41:26 PM
	 *
	 * @param <T>
	 */
	private static final class QueryIterable<T> implements Iterable<T>{

		/**
		 * {@link QueryIterator} reference
		 */
		private QueryIterator<T> iterator;

		/**
		 * Constructor
		 */
		public QueryIterable(QueryIterator<T> iterator) {
			this.iterator = iterator;
			
		}
		
		/**
		 * {@inheritDoc}
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<T> iterator() {
			return iterator;
		}
	}

	/**
	 * Returns batch size
	 * 
	 * @return 1500 as default or {@link HibernateConfig#STATEMENT_BATCH_SIZE}
	 */
	private int getFetchSize() {
		if (fetchSize > 0)
			return fetchSize;

		fetchSize = 1500;
		if (getHibernateConfig() instanceof HibernateConfig)
			try {
				fetchSize = Integer
						.valueOf(((HibernateConfig) getHibernateConfig()).STATEMENT_BATCH_SIZE);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		return fetchSize;
	}

	/**
	 * Returns all instances of given class.
	 * <p>
	 * <b>USE SCROLLING IF RESULT SET MAY BE LARGE</b>
	 * 
	 * <p>
	 * <b>use {@link SessionTemplate#scrollAll(Class<T> entityClass)} instead</b>
	 * 
	 * @param entityClass - given entityClass
	 * @return Collection with all instances in database
	 */
	public <T> Collection<T> getAll(Class<T> entityClass) {
		return this.getAll(entityClass, 0);
	}

	/**
	 * Returns maxResults instances of given class
	 * 
	 * @param entityClass - given entityClass
	 * @param maxResults - maximalResultCount
	 * @return Collection with instances
	 */
	public <T> Collection<T> getAll(Class<T> entityClass, int maxResults) {
		return this.executeQuery(
				session.createQuery("select e from "
						+ entityClass.getSimpleName() + " e"), maxResults);
	}
	
	/**
	 * Returns count of datasets for given class
	 * 
	 * @param cls
	 * @return count
	 */
	public <T> long count(Class<T> cls){
		Query q = createQuery("select count(e) from " + cls.getSimpleName() + " e");
		return (long) executeQueryUniqueResult(q);
	}
	
	/**
	 * Returns a iterator with all entityClass instances in database
	 * 
	 * @param entityClass - given entity class
	 * @return {@link Iterator} of result set
	 */
	public <T> Iterable<T> scrollAll(Class<T> entityClass){
		return this.scroll(session.createCriteria("select e from "
						+ entityClass.getSimpleName() + " e"));
	}

	public <T> Collection<T> executeHQLQuery(final String query,
			final int maxResults) {
		return executeQuery(session.createQuery(query), maxResults);
	}

	public <T> Collection<T> executeSQLQuery(final String query,
			final int maxResults) {
		return executeQuery(session.createSQLQuery(query), maxResults, false);
	}

	public <T> Collection<T> executeNamedQuery(final String query,
			final int maxResults) {
		return executeQuery(session.getNamedQuery(query), maxResults);
	}

	public <T> Collection<T> executeHQLQuery(final String query) {
		return executeHQLQuery(query, 0);
	}

	public <T> Collection<T> executeSQLQuery(final String query) {
		return executeSQLQuery(query, 0);
	}

	public <T> Collection<T> executeNamedQuery(final String query) {
		return executeNamedQuery(query, 0);
	}

	public <T> T executeQueryUniqueResult(Query q) {
		return executeQueryUniqueResult(q, true);
	}

	@SuppressWarnings("unchecked")
	public <T> T executeQueryUniqueResult(Query q, boolean chacheable) {
		q = q.setMaxResults(1);
		q = q.setCacheable(chacheable);

		if (loggingEnabled)
			logQuery(true);
		Object uniqueResult = q.uniqueResult();
		return (T) uniqueResult;
	}
	
	public <T> T executeQueryUniqueResult(Criteria q) {
		return executeQueryUniqueResult(q, true);
	}

	@SuppressWarnings("unchecked")
	public <T> T executeQueryUniqueResult(Criteria crit, boolean chacheable) {
		crit = crit.setMaxResults(1);
		crit = crit.setCacheable(chacheable);

		if (loggingEnabled)
			logQuery(true);
		Object uniqueResult = crit.uniqueResult();
		return (T) uniqueResult;
	}

	public <T> T executeHQLQueryUniqueResult(final String query) {
		return executeQueryUniqueResult(session.createQuery(query));
	}

	public <T> T executeSQLQueryUniqueResult(final String query) {
		return executeQueryUniqueResult(session.createSQLQuery(query), false);
	}

	public <T> T executeNamedQueryUniqueResult(final String query) {
		return executeQueryUniqueResult(session.getNamedQuery(query));
	}

	public static <T> Collection<T> filterNull(Collection<T> input) {
		if (Objects.isNull(input))
			return Collections.emptyList();
		return input.stream().filter(Objects::nonNull)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private static void logQuery(boolean uniqueResult) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder(250);
		for (int i = 10; i >= 3; i--) {
			if (stackTrace.length > i) {
				sb.append(stackTrace[i].toString()).append(" -> ");
			}
		}
		if (uniqueResult)
			System.out.println("Execute unique result query from: "
					+ sb.toString());
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
		if (loggingEnabled)
			System.out.println("close session " + this);
	}
}
