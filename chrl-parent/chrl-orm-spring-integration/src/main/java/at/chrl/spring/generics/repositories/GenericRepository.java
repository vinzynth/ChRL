/**
 * @author bravestone Feb 12, 2015 - 5:13:57 PM bravestone-dataProvider
 *         com.bravestone.diana.repositories
 */
package at.chrl.spring.generics.repositories;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;

import at.chrl.nutils.ClassUtils;
import at.chrl.nutils.CollectionUtils;

/**
 * 
 * @author Christian Richard Leopold - ChRL <br>
 *         Feb 26, 2015 - 4:55:59 PM
 *
 * @param <T>
 *            Type of repository objects - accessable via {@link #getType()}
 */
@org.springframework.stereotype.Repository
@Scope(value = "singleton")
@SuppressWarnings("unchecked")
public abstract class GenericRepository<T> {

	private final Class<T> persistentClass;
	private String idFieldName;

	public GenericRepository() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public GenericRepository(Class<T> cls) {
		this.persistentClass = cls;
		this.setIdFieldName();
	}
	
	public Class<T> getType() {
		return persistentClass;
	}

	protected String getIdFieldName() {
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

	@Transactional
	public Collection<T> getByIds(Collection<Object> ids) {
		if (Objects.isNull(this.idFieldName))
			return Collections.emptyList();
		return getSession().createCriteria(this.persistentClass).add(Restrictions.in(this.idFieldName, ids)).list();
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

	public Collection<T> getAll(int maxResults) {
		Query q = entityManager.createQuery("select e from " + this.getType().getSimpleName() + " e");
		if (maxResults > 0)
			q = q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public long count() {
		Query q = entityManager.createQuery("select count(e) from " + this.getType().getSimpleName() + " e");
		return (long) q.getResultList().get(0);
	}

	public Collection<T> getLast(int count) {
		return entityManager.createQuery("select e from " + this.getType().getSimpleName() + " e order by e." + idFieldName + " desc").setMaxResults(count).getResultList();
	}

	public Collection<T> executeQuery(Query q, final int maxResults) {
		if (maxResults > 0)
			q = q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public Collection<T> executeHQLQuery(final String query, final int maxResults) {
		return executeQuery(entityManager.createQuery(query), maxResults);
	}

	public Collection<T> executeSQLQuery(final String query, final int maxResults) {
		return executeQuery(entityManager.createNativeQuery(query), maxResults);
	}

	public Collection<T> executeNamedQuery(final String query, final int maxResults) {
		return executeQuery(entityManager.createNamedQuery(query), maxResults);
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
