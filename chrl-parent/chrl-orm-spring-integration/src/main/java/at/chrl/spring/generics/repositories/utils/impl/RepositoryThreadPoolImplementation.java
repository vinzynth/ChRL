/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 7:40:37 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils.impl
 */
package at.chrl.spring.generics.repositories.utils.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import at.chrl.spring.generics.repositories.utils.RepositoryThreadPool;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 7:40:37 PM
 *
 */
public class RepositoryThreadPoolImplementation implements RepositoryThreadPool{

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	@PostConstruct
	public void setUp(){
		threadPoolTaskExecutor.setMaxPoolSize(100);
	}
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;

	protected Session getSession() {
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
	@Transactional
	private <T> Future<T> asyncPersist(T entity){
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
	@Transactional
	private <T> Future<T> asyncRemove(T entity){
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
	@Transactional
	private <T> Future<T> asyncRefresh(T entity){
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
	@Transactional
	private <T> Future<T> asyncMerge(T entity){
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
	@Transactional
	private <T> Future<T> asyncSave(T entity){
		getSession().save(entity);
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
	@Transactional
	private <T> Future<T> asyncSaveOrUpdate(T entity){
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
	@Transactional
	private <T> Future<T> asyncMergeWithSession(T entity){
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
	@Transactional
	private <T> Future<T> asyncPersistWithSession(T entity){
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
	@Transactional
	private <T> Future<T> asyncDelete(T entity){
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@Transactional
	private Future<Integer> asyncExecuteUpdate(Query query){
		return new AsyncResult<Integer>(query.executeUpdate());
	}

}
