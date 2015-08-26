/**
 * @author bravestone Feb 12, 2015 - 5:13:57 PM bravestone-dataProvider
 *         com.bravestone.diana.repositories
 */
package at.chrl.spring.generics.repositories;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.chrl.nutils.ClassUtils;
import at.chrl.nutils.CollectionUtils;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.spring.generics.repositories.utils.RepositoryThreadPool;

/**
 * 
 * @author Christian Richard Leopold - ChRL <br>
 *         Feb 26, 2015 - 4:55:59 PM
 *
 * @param <T>
 *            Type of repository objects - accessable via {@link #getType()}
 */
@SuppressWarnings("unchecked")
public class GenericRepository<T> {

	public static final int FETCH_SIZE = 1500;
	
	private final Class<T> persistentClass;
	private String idFieldName;
	
	@Autowired
	private RepositoryThreadPool threadPool;

	public GenericRepository() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public GenericRepository(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}
	
	public Class<T> getType() {
		return persistentClass;
	}

	protected String getIdFieldName() {
		if(Objects.isNull(idFieldName))
			this.setIdFieldName();
		return idFieldName;
	}

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;

	protected Session getSession() {
		return entityManager.unwrap(HibernateEntityManager.class).getSession();
	}

	@PostConstruct
	private void setIdFieldName() {
		this.idFieldName = getSession().getSessionFactory().getClassMetadata(persistentClass).getIdentifierPropertyName();
	}

	@Transactional
	public T getById(Object id) {
		return entityManager.find(this.getType(), id);
	}

	public Collection<T> getByIds(Object... ids) {
		return getByIds(Arrays.asList(ids));
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
	@Transactional
	public Collection<T> getByIds(Collection<Object> ids) {
		if (Objects.isNull(this.idFieldName))
			return Collections.emptyList();
		return getSession().createCriteria(this.persistentClass).add(Restrictions.in(this.idFieldName, ids)).list();
	}

	/**
	 * get Objects for primary key
	 * 
	 * @param cls
	 *            - given persistence class
	 * @param ids
	 *            - identifier
	 * 
	 * @return Stream with given Objects (persistent)
	 */
	@Transactional
	public <K> Stream<T> streamObjectsForPK(Collection<K> ids) {
		if (ids == null || ids.isEmpty())
			return Stream.<T>empty();

		return stream(createCriteria(this.persistentClass)
				.add(Restrictions.in(this.idFieldName, ids)));
	}
	
	/**
	 * get Objects for primary key
	 * 
	 * @param cls
	 *            - given persistence class
	 * @param ids
	 *            - identifier
	 * 
	 * @return Stream with given Objects (persistent)
	 */
	@Transactional
	public <K> Iterable<T> scrollObjectsForPK(Collection<K> ids) {
		if (ids == null || ids.isEmpty())
			return Collections.<T>emptyList();

		return scroll(createCriteria(this.persistentClass)
				.add(Restrictions.in(this.idFieldName, ids)));
	}
	
	@Transactional
	public T persist(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	@Transactional
	public T remove(T entity) {
		entityManager.remove(entity);
		return entity;
	}

	@Transactional
	public T refresh(T entity) {
		entityManager.refresh(entity);
		return entity;
	}

	@Transactional
	public T merge(T entity) {
		entityManager.merge(entity);
		return entity;
	}

	@Transactional
	public T save(T entity) {
		getSession().save(entity);
		return entity;
	}

	@Transactional
	public T saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	@Transactional
	public T mergeWithSession(T entity) {
		getSession().merge(entity);
		return entity;
	}

	@Transactional
	public T persistWithSession(T entity) {
		getSession().persist(entity);
		return entity;
	}
	
	@Transactional
	public T delete(T entity) {
		getSession().delete(entity);
		return entity;
	}

	@Transactional
	public Map<Date, T> getOlderVersions(Object id){
		AuditReader reader = AuditReaderFactory.get(entityManager);
		List<Number> revisions = reader.getRevisions(this.getType(), id).stream().collect(Collectors.toList());
		List<T> collect = revisions.stream().map(n -> reader.find(this.getType(), id, n)).collect(Collectors.toList());
		List<Date> dates = revisions.stream().map(reader::getRevisionDate).collect(Collectors.toList());
		
		Map<Date, T> returnMe = CollectionUtils.newMap();
		
		final int to = Math.min(collect.size(), dates.size());
		
		for (int i = 0; i < to; i++) {
			returnMe.put(dates.get(i), collect.get(i));
		}
		
		return returnMe;
	}
	
	/**
	 * Create query
	 * 
	 * @param stmt
	 *            the statement
	 */
	public Query createQuery(String stmt) {
		return getSession().createQuery(stmt).setCacheable(true);
	}

	/**
	 * Create SQL query
	 * 
	 * @param stmt
	 *            the SQL statement
	 */
	public org.hibernate.SQLQuery createSQLQuery(String stmt) {
		return (SQLQuery) getSession().createSQLQuery(stmt).setCacheable(true);
	}

	@Transactional
	public Collection<T> getAll(int maxResults) {
		return this.executeQuery(
				getSession().createQuery("select e from "
						+ this.persistentClass.getSimpleName() + " e"), maxResults, true);
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
	public Collection<T> getAll() {
		return this.getAll(0);
	}
	
	/**
	 * Returns a iterator with all entityClass instances in database
	 * 
	 * @param entityClass - given entity class
	 * @return {@link Iterator} of result set
	 */
	public Iterable<T> scrollAll(Class<T> entityClass){
		return this.scroll(getSession().createCriteria(entityClass));
	}

	@Transactional
	public long count() {
		Query q = createQuery("select count(e) from " + this.persistentClass.getSimpleName() + " e");
		return (Long) executeQueryUniqueResult(q);
	}

	@Transactional
	public Collection<T> getLast(int count) {
		return entityManager.createQuery("select e from " + this.getType().getSimpleName() + " e order by e." + idFieldName + " desc").setMaxResults(count).getResultList();
	}
	
	/**
	 * Returns criteria associated with this session for given class
	 * 
	 * @param persistentClass
	 *            - given class
	 * @return empty criteria query
	 */
	public Criteria createCriteria(Class<T> persistentClass) {
		return getSession().createCriteria(persistentClass);
	}

	/**
	 * Executes given {@link Criteria}
	 * 
	 * @param crit
	 *            - criteria to execute
	 * @return result set as collection
	 */
	public Collection<T> executeQuery(Criteria crit) {
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
	public Collection<T> executeQuery(Criteria crit, final int maxResults) {
		return executeQuery(crit, maxResults, true);
	}

	/**
	 * Executes given {@link Query}
	 * 
	 * @param q
	 *            - query to execute
	 * @return result set as collection
	 */
	public Collection<T> executeQuery(Query q) {
		return executeQuery(q, 0, true);
	}
	
	public Collection<T> executeQuery(Query q, int maxResults) {
		return executeQuery(q, maxResults, true);
	}

	public Collection<T> executeHQLQuery(final String query,
			final int maxResults) {
		return executeQuery(getSession().createQuery(query), maxResults);
	}
	
	public Collection<T> executeSQLQuery(final String query,
			final int maxResults) {
		return executeQuery(getSession().createSQLQuery(query), maxResults, false);
	}

	public Collection<T> executeNamedQuery(final String query,
			final int maxResults) {
		return executeQuery(getSession().getNamedQuery(query), maxResults);
	}

	public Collection<T> executeHQLQuery(final String query) {
		return executeHQLQuery(query, 0);
	}

	public Collection<T> executeSQLQuery(final String query) {
		return executeSQLQuery(query, 0);
	}

	public Collection<T> executeNamedQuery(final String query) {
		return executeNamedQuery(query, 0);
	}
	
	/**
	 * Executes Update Statement
	 * 
	 * @param q
	 *            - given query
	 * @return affected row count
	 */
	@Transactional
	public int executeUpdate(Query q) {
		return q.executeUpdate();
	}

	public T executeQueryUniqueResult(Query q) {
		return executeQueryUniqueResult(q, true);
	}

	@Transactional
	public T executeQueryUniqueResult(Query q, boolean chacheable) {
		q = q.setMaxResults(1);
		q = q.setCacheable(chacheable);

		Object uniqueResult = q.uniqueResult();
		return (T) uniqueResult;
	}
	
	public T executeQueryUniqueResult(Criteria q) {
		return executeQueryUniqueResult(q, true);
	}

	@Transactional
	public T executeQueryUniqueResult(Criteria crit, boolean chacheable) {
		crit = crit.setMaxResults(1);
		crit = crit.setCacheable(chacheable);

		Object uniqueResult = crit.uniqueResult();
		return (T) uniqueResult;
	}

	public T executeHQLQueryUniqueResult(final String query) {
		return executeQueryUniqueResult(getSession().createQuery(query));
	}

	public T executeSQLQueryUniqueResult(final String query) {
		return executeQueryUniqueResult(getSession().createSQLQuery(query), false);
	}

	public T executeNamedQueryUniqueResult(final String query) {
		return executeQueryUniqueResult(getSession().getNamedQuery(query));
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
	@Transactional
	public Collection<T> executeQuery(Query q, final int maxResults,
			boolean chacheable) {
		if (maxResults > 0)
			q = q.setMaxResults(maxResults);
		q = q.setCacheable(chacheable);

		return SessionTemplate.filterNull(q.list());
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
	@Transactional
	public Collection<T> executeQuery(Criteria crit, final int maxResults,
			boolean chacheable) {
		if (maxResults > 0)
			crit = crit.setMaxResults(maxResults);
		crit = crit.setCacheable(chacheable);

		return SessionTemplate.filterNull(crit.list());
	}


	/**
	 * Scroll over the query results
	 * 
	 * @param q
	 *            - given query
	 * 
	 * @return {@link Iterable} instance
	 */
	@Transactional
	public Iterable<T> scroll(Query q) {
		return new QueryIterable<T>(new QueryIterator<T>(q, getSession()));
	}
	
	/**
	 * Scroll over the query results with a iterator
	 * 
	 * @see {@link SessionTemplate#scroll(Query)}
	 * 
	 * @param q
	 *            - given query
	 * 
	 * @return {@link Iterable} instance
	 */
	@Transactional
	public Iterator<T> iterator(Query q) {
		return new QueryIterator<T>(q, getSession());
	}

	/**
	 * Scroll over the query results
	 * 
	 * @param crit
	 *            - given criteria
	 * 
	 * @return {@link Iterable} instance
	 */
	@Transactional
	public Iterable<T> scroll(Criteria crit) {
		return new QueryIterable<T>(new QueryIterator<T>(crit, getSession()));
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
	@Transactional
	public Iterator<T> iterator(Criteria crit, boolean stateless) {
		return new QueryIterator<T>(crit, getSession());
	}

	/**
	 * Creates {@link Stream} with given query Result
	 * 
	 * @param q
	 *            - given Query q. q is marked as ReadOnly with
	 *            {@link Query#setReadOnly(true)}
	 * @return new {@link Stream} with given ResultSet
	 */
	public Stream<T> streamReadOnly(Query q) {
		return stream(q.setReadOnly(true));
	}

	/**
	 * crates a Stream with given {@link Query} q
	 * 
	 * @param q
	 *            - given Query
	 * @return new {@link Stream} with given ResultSet
	 */
	@Transactional
	public Stream<T> stream(Query q) {	
		return StreamSupport.<T> stream(Spliterators.spliteratorUnknownSize(
				new QueryIterator<T>(q.setCacheMode(CacheMode.IGNORE)
						.setFlushMode(FlushMode.MANUAL), getSession()),
				Spliterator.ORDERED | Spliterator.DISTINCT), false);
	}

	/**
	 * Creates {@link Stream} with given criteria Result
	 * 
	 * @param crit
	 *            - given Critera crit. crit is marked as ReadOnly with
	 *            {@link Query#setReadOnly(true)}
	 * @return new {@link Stream} with given ResultSet
	 */
	public Stream<T> streamReadOnly(Criteria crit) {
		return stream(crit.setReadOnly(true));
	}

	/**
	 * crates a Stream with given {@link Criteria} crit
	 * 
	 * @param crit
	 *            - given Criteria
	 * @return new {@link Stream} with given ResultSet
	 */
	@Transactional
	public Stream<T> stream(Criteria crit) {
		return StreamSupport.<T> stream(Spliterators.spliteratorUnknownSize(
				new QueryIterator<T>(crit.setCacheMode(CacheMode.IGNORE)
						.setFlushMode(FlushMode.MANUAL), getSession()),
				Spliterator.ORDERED | Spliterator.DISTINCT), false);
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
		private Session session;

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
		public QueryIterator(Query q, Session session) {
			this.session = session;
			this.stateless = false;
			this.queue = new ArrayDeque<>(FETCH_SIZE);

			final int fs = FETCH_SIZE;
			scroll = q.setFetchSize(fs).scroll(ScrollMode.FORWARD_ONLY);
			for (int i = 0; i < fs; i++)
				if (!scroll.next())
					break;
				else
					queue.add((T) scroll.get()[0]);
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
		public QueryIterator(Criteria crit, Session session) {
			this.session = session;
			this.stateless = false;
			this.queue = new ArrayDeque<>(FETCH_SIZE);

			final int fs = FETCH_SIZE;
			scroll = crit.setFetchSize(fs).scroll(ScrollMode.FORWARD_ONLY);
			for (int i = 0; i < fs; i++)
				if (!scroll.next())
					break;
				else
					queue.add((T) scroll.get()[0]);
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
				session.flush();
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
		private void nextBuffer() {
			if (!stateless) {
				session.flush();
				session.clear();
			}
			final int fs = FETCH_SIZE;
			for (int i = 0; i < fs; i++)
				if (!scroll.next())
					break;
				else
					queue.add((T) scroll.get()[0]);
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
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ClassUtils.getString(this, new Function<GenericRepository<T>, String>() {

			@Override
			public String apply(GenericRepository<T> t) {
				return t.getType().getSimpleName();
			}
		});
	}
}
