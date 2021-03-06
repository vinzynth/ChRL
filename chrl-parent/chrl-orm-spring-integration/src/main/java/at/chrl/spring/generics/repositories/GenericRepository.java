/**
 * @author bravestone Feb 12, 2015 - 5:13:57 PM bravestone-dataProvider
 *         com.bravestone.diana.repositories
 */
package at.chrl.spring.generics.repositories;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.chrl.nutils.ClassUtils;
import at.chrl.nutils.CollectionUtils;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool;
import at.chrl.spring.hibernate.config.SessionTemplateFactory;

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

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;
	
	public Session getSession() {
		return entityManager.unwrap(HibernateEntityManager.class).getSession();
	}
	
	private final Class<T> persistentClass;
	private String idFieldName;

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
	
	@Autowired
	protected RepositoryTransactionPool transactionPool;
	
	@Autowired
	protected SessionTemplateFactory sessionTemplateFactory;
	
	@PostConstruct
	private void setIdFieldName() {
		this.idFieldName = transactionPool.getIdentifierPropertyName(this.persistentClass);
	}

	@Transactional
	public Map<Date, T> getOlderVersions(Object id){
		AuditReader reader = AuditReaderFactory.get(entityManager);
		List<Number> revisions = reader.getRevisions(this.getType(), id).stream().collect(Collectors.toList());
		List<T> collect = revisions.stream().map(n -> reader.find(this.getType(), id, n)).collect(Collectors.toList());
		List<Date> dates = revisions.stream().map(reader::getRevisionDate).collect(Collectors.toList());
		
		collect.forEach(entityManager::detach);
		
		Map<Date, T> returnMe = CollectionUtils.newMap();
		
		final int to = Math.min(collect.size(), dates.size());
		
		for (int i = 0; i < to; i++) {
			returnMe.put(dates.get(i), collect.get(i));
		}
		
		return returnMe;
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

	public void process(Consumer<SessionTemplate> consumer){
		try (SessionTemplate session = sessionTemplateFactory.createTemplate(entityManager)){
			consumer.accept(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Transactional
	public void persist(T entity){
		entityManager.persist(entity);
	}
	
	@Transactional
	public void refresh(T entity){
		entityManager.refresh(entity);
	}
	
	@Transactional
	public T merge(T entity){
		return entityManager.merge(entity);
	}
	
	@Transactional
	public void save(T entity){
		getSession().save(entity);
	}
	
	@Transactional
	public void saveOrUpdate(T entity){
		getSession().saveOrUpdate(entity);
	}
	
	@Transactional
	public void delete(T entity){
		try {
			getSession().delete(entity);			
		} catch (IllegalArgumentException e) {
			getSession().delete(getSession().merge(entity));
		}
	}
	
	@Transactional
	public T getById(Serializable id){
		return getSession().get(this.persistentClass, id);
	}
	
	public void getByIds(Collection<Serializable> ids, Consumer<Stream<T>> streamConsumer){
		process(s -> streamConsumer.accept(s.streamObjectsForPK(this.persistentClass, ids)));
	}
	
	public void getAll(int maxResults, Consumer<Collection<T>> collectionConsumer){
		process(s -> collectionConsumer.accept(s.getAll(this.persistentClass, maxResults)));
	}
	
	public Collection<T> getAll(){
		return getAll(0);
	}
	
	public Collection<T> getAll(int maxResults){
		Collection<T> col = CollectionUtils.newList();
		getAll(maxResults, c -> col.addAll(c));
		return col;
	}

	public void asyncPersist(T entity){
		transactionPool.asyncPersist(entity);
	}
	public void asyncRefresh(T entity){
		transactionPool.asyncRefresh(entity);
	}
	public void asyncMerge(T entity){
		transactionPool.asyncMerge(entity);
	}
	public void asyncSave(T entity){
		transactionPool.asyncSave(entity);
	}
	public void asyncSaveOrUpdate(T entity){
		transactionPool.asyncSaveOrUpdate(entity);
	}
	public void asyncDelete(T entity){
		transactionPool.asyncDelete(entity);
	}
	public void asyncPersist(Collection<T> entities){
		entities.forEach(entity -> transactionPool.asyncPersist(entity));
	}
	public void asyncRefresh(Collection<T> entities){
		entities.forEach(entity -> transactionPool.asyncRefresh(entity));
	}
	public void asyncMerge(Collection<T> entities){
		entities.forEach(entity -> transactionPool.asyncMerge(entity));
	}
	public void asyncSave(Collection<T> entities){
		entities.forEach(entity -> transactionPool.asyncSave(entity));
	}
	public void asyncSaveOrUpdate(Collection<T> entities){
		entities.forEach(entity -> transactionPool.asyncSaveOrUpdate(entity));
	}
	public void asyncDelete(Collection<T> entities){
		entities.forEach(entity -> transactionPool.asyncDelete(entity));
	}
	public void asyncPersist(T... entities){
		Arrays.stream(entities).forEach(entity -> transactionPool.asyncPersist(entity));
	}
	public void asyncRefresh(T... entities){
		Arrays.stream(entities).forEach(entity -> transactionPool.asyncRefresh(entity));
	}
	public void asyncMerge(T... entities){
		Arrays.stream(entities).forEach(entity -> transactionPool.asyncMerge(entity));
	}
	public void asyncSave(T... entities){
		Arrays.stream(entities).forEach(entity -> transactionPool.asyncSave(entity));
	}
	public void asyncSaveOrUpdate(T... entities){
		Arrays.stream(entities).forEach(entity -> transactionPool.asyncSaveOrUpdate(entity));
	}
	public void asyncDelete(T... entities){
		Arrays.stream(entities).forEach(entity -> transactionPool.asyncDelete(entity));
	}
}
