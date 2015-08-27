/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 7:40:37 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils.impl
 */
package at.chrl.spring.generics.repositories.utils.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import at.chrl.spring.generics.repositories.utils.RepositoryThreadPool;
import at.chrl.spring.hibernate.config.SessionTemplateFactory;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 7:40:37 PM
 *
 */
@EnableAsync
public class RepositoryThreadPoolImplementation implements RepositoryThreadPool, DisposableBean, ApplicationContextAware{

	public static final int BATCH_SIZE = 1500;
	public static final int MAX_THREAD_POOL_SIZE = 30;
	
	private BlockingQueue<Object> processFunctionQueue = new LinkedBlockingQueue<>();

	private CountDownLatch lock = null;
	
	private void addToQueue(Object o){
		if(processFunctionQueue.size() >= 100*BATCH_SIZE){
			lock = new CountDownLatch(1);
			//TODO: call system overload
		}
		
		if(Objects.nonNull(lock))
			try {
				lock.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		processFunctionQueue.add(o);
		
		if(processFunctionQueue.size() >= 2.00*BATCH_SIZE) newThread(6);
		if(processFunctionQueue.size() >= 1.75*BATCH_SIZE) newThread(5);
		if(processFunctionQueue.size() >= 1.50*BATCH_SIZE) newThread(4);
		if(processFunctionQueue.size() >= 1.25*BATCH_SIZE) newThread(3);
		if(processFunctionQueue.size() >= 1.00*BATCH_SIZE) newThread(3);
		if(processFunctionQueue.size() >= 0.80*BATCH_SIZE) newThread(2);
		if(processFunctionQueue.size() >= 0.60*BATCH_SIZE) newThread(2);
		if(processFunctionQueue.size() >= 0.40*BATCH_SIZE) newThread(2);
		if(processFunctionQueue.size() >= 0.20*BATCH_SIZE) newThread(1);
		if(processFunctionQueue.size() >= 0.10*BATCH_SIZE) newThread(1);
		if(processFunctionQueue.size() >= 0.05*BATCH_SIZE) newThread(1);
		if(workingThreads.isEmpty()) newThread(1);
	}
	
	@Autowired
	private SessionTemplateFactory sessionTemplateFactory;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;

	private Collection<TransactionThread> workingThreads = new ConcurrentLinkedQueue<>();;
	
	void threadFinished(TransactionThread t){
//		System.err.println("[Stop new Transaction Thread]");
		workingThreads.remove(t);
		if(Objects.nonNull(lock)){
			lock.countDown();
			if(lock.getCount() <= 0)
				lock = null;
		}
	}
	
	private void newThread(int count){
		final int c = Math.min(MAX_THREAD_POOL_SIZE - workingThreads.size(), count);
		for (int i = 0; i < c; i++) {
//			System.err.println("[Start new Transaction Thread]");
			workingThreads.add(new TransactionThread(processFunctionQueue, sessionTemplateFactory, this));			
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#getSession()
	 */
	@Override
	public Session getSession() {
		return entityManager.unwrap(HibernateEntityManager.class).getSession();
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#persist(java.lang.Object)
	 */
	@Override
	public <T> T persist(T entity) {
		try {
			return asyncPersist(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public <T> Future<T> asyncPersist(T entity){
		entityManager.persist(entity);
		return new AsyncResult<>(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#remove(java.lang.Object)
	 */
	@Override
	public <T> T remove(T entity) {
		try {
			return asyncRemove(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public <T> Future<T> asyncRemove(T entity){
		entityManager.remove(entity);
		return new AsyncResult<>(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#refresh(java.lang.Object)
	 */
	@Override
	public <T> T refresh(T entity) {
		try {
			return asyncRefresh(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public <T> Future<T> asyncRefresh(T entity){
		entityManager.refresh(entity);
		return new AsyncResult<>(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#merge(java.lang.Object)
	 */
	@Override
	public <T> T merge(T entity) {
		try {
			return asyncMerge(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public <T> Future<T> asyncMerge(T entity){
		return new AsyncResult<>(entityManager.merge(entity));
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#save(java.lang.Object)
	 */
	@Override
	public <T> T save(T entity) {
		try {
			return asyncSave(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Async
	public <T> Future<T> asyncSave(T entity){
		this.addToQueue(entity);
//		getSession().save(entity);
		return new AsyncResult<T>(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public <T> T saveOrUpdate(T entity) {
		try {
			return asyncSaveOrUpdate(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public <T> Future<T> asyncSaveOrUpdate(T entity){
		getSession().saveOrUpdate(entity);
		return new AsyncResult<T>(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#mergeWithSession(java.lang.Object)
	 */
	@Override
	public <T> T mergeWithSession(T entity) {
		try {
			return asyncMergeWithSession(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Async
	public <T> Future<T> asyncMergeWithSession(T entity){
		return new AsyncResult<>((T)getSession().merge(entity));
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#persistWithSession(java.lang.Object)
	 */
	@Override
	public <T> T persistWithSession(T entity) {
		try {
			return asyncPersistWithSession(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public <T> Future<T> asyncPersistWithSession(T entity){
		getSession().persist(entity);
		return new AsyncResult<>(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#delete(java.lang.Object)
	 */
	@Override
	public <T> T delete(T entity) {
		try {
			return asyncDelete(entity).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public <T> Future<T> asyncDelete(T entity){
		getSession().delete(entity);
		return new AsyncResult<T>(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#scroll(org.hibernate.Criteria)
	 */
	@Override
	public ScrollableResults scroll(Criteria crit) {
		try {
			return asyncScroll(crit).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	private Future<ScrollableResults> asyncScroll(Criteria crit){
		return new AsyncResult<ScrollableResults>(crit.scroll(ScrollMode.FORWARD_ONLY));
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#scroll(org.hibernate.Query)
	 */
	@Override
	public ScrollableResults scroll(Query query) {
		try {
			return asyncScroll(query).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Async
	private Future<ScrollableResults> asyncScroll(Query query){
		return new AsyncResult<ScrollableResults>(query.scroll(ScrollMode.FORWARD_ONLY));
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#list(org.hibernate.Criteria)
	 */
	@Override
	public <T> List<T> list(Criteria crit) {
		try {
			return this.<T>asyncList(crit).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Async
	private <T> Future<List<T>> asyncList(Criteria crit){
		return new AsyncResult(crit.list());
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#list(org.hibernate.Query)
	 */
	@Override
	public <T> List<T> list(Query query) {
		try {
			return this.<T>asyncList(query).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Async
	private <T> Future<List<T>> asyncList(Query query){
		return new AsyncResult(query.list());
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#uniqueResult(org.hibernate.Criteria)
	 */
	@Override
	public <T> T uniqueResult(Criteria crit) {
		try {
			return this.<T>asyncUniqueResult(crit).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Async
	private <T> Future<T> asyncUniqueResult(Criteria crit){
		return new AsyncResult<T>((T) crit.uniqueResult());
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#uniqueResult(org.hibernate.Query)
	 */
	@Override
	public <T> T uniqueResult(Query query) {
		try {
			return this.<T>asyncUniqueResult(query).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Async
	private <T> Future<T> asyncUniqueResult(Query query){
		return new AsyncResult<T>((T) query.uniqueResult());
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#executeUpdate(org.hibernate.Query)
	 */
	@Override
	public int executeUpdate(Query query) {
		try {
			return this.asyncExecuteUpdate(query).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Async
	private Future<Integer> asyncExecuteUpdate(Query query){
		return new AsyncResult<Integer>(query.executeUpdate());
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#find(java.lang.Object)
	 */
	@Override
	public <T> T find(Class<T> cls, Object id) {
		try {
			return this.asyncFind(cls, id).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Async
	public <T> Future<T> asyncFind(Class<T> cls, Object id){
		return new AsyncResult<>(entityManager.find(cls, id));
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#getIdentifierPropertyName(java.lang.Class)
	 */
	@Override
	public String getIdentifierPropertyName(Class<?> cls) {
		return getSession().getSessionFactory().getClassMetadata(cls).getIdentifierPropertyName();
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#createCriteria(java.lang.Class)
	 */
	@Override
	public Criteria createCriteria(Class<?> cls) {
		return getSession().createCriteria(cls);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#createQuery(java.lang.String)
	 */
	@Override
	public Query createQuery(String stmt) {
		return getSession().createQuery(stmt);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#createSQLQuery(java.lang.String)
	 */
	@Override
	public SQLQuery createSQLQuery(String stmt) {
		return getSession().createSQLQuery(stmt);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#getNamedQuery(java.lang.String)
	 */
	@Override
	public Query getNamedQuery(String query) {
		return getSession().getNamedQuery(query);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#getAuditReader()
	 */
	@Override
	public AuditReader getAuditReader() {
		return AuditReaderFactory.get(entityManager);
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
//		workingThreads.stream().ma
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
	}
}