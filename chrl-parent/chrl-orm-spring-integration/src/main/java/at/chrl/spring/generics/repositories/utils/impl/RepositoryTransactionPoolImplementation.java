/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 7:40:37 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils.impl
 */
package at.chrl.spring.generics.repositories.utils.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool;
import at.chrl.spring.hibernate.config.SessionTemplateFactory;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 7:40:37 PM
 *
 */
@EnableAsync
public class RepositoryTransactionPoolImplementation implements RepositoryTransactionPool, ApplicationContextAware{
	
	@Autowired
	private SessionTemplateFactory sessionTemplateFactory;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;

	private TransactionQueue asyncPersistTransactionQueue;
	private TransactionQueue asyncRefreshTransactionQueue;
	private TransactionQueue asyncMergeTransactionQueue;
	private TransactionQueue asyncSaveTransactionQueue;
	private TransactionQueue asyncSaveOrUpdateTransactionQueue;
	private TransactionQueue asyncDeleteTransactionQueue;
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#getSession()
	 */
	@Override
	public Session getSession() {
		return entityManager.unwrap(HibernateEntityManager.class).getSession();
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#getIdentifierPropertyName(java.lang.Class)
	 */
	@Override
	@Transactional
	public String getIdentifierPropertyName(Class<?> cls) {
		return getSession().getSessionFactory().getClassMetadata(cls).getIdentifierPropertyName();
	}
	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#getAuditReader()
	 */
	@Override
	public AuditReader getAuditReader() {
		return AuditReaderFactory.get(entityManager);
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.asyncPersistTransactionQueue = new TransactionQueue(sessionTemplateFactory, (sf, o) -> { try { sf.persist(o);} catch(Exception e){e.printStackTrace();}});
        this.asyncRefreshTransactionQueue = new TransactionQueue(sessionTemplateFactory, (sf, o) -> { try { sf.refresh(o);} catch(Exception e){e.printStackTrace();}});
        this.asyncMergeTransactionQueue = new TransactionQueue(sessionTemplateFactory, (sf, o) -> { try { sf.merge(o);} catch(Exception e){e.printStackTrace();}});
        this.asyncSaveTransactionQueue = new TransactionQueue(sessionTemplateFactory, (sf, o) -> { try { sf.save(o);} catch(Exception e){e.printStackTrace();}});
        this.asyncSaveOrUpdateTransactionQueue = new TransactionQueue(sessionTemplateFactory, (sf, o) -> { try { sf.saveOrUpdate(o);} catch(Exception e){e.printStackTrace();}});
        this.asyncDeleteTransactionQueue = new TransactionQueue(sessionTemplateFactory, (sf, o) -> { try { sf.delete(o);} catch(Exception e){e.printStackTrace();}});
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#asyncPersist(java.lang.Object)
	 */
	@Override
	public <T> void asyncPersist(T entity) {
		asyncPersistTransactionQueue.addToQueue(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#asyncRefresh(java.lang.Object)
	 */
	@Override
	public <T> void asyncRefresh(T entity) {
		asyncRefreshTransactionQueue.addToQueue(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#asyncMerge(java.lang.Object)
	 */
	@Override
	public <T> void asyncMerge(T entity) {
		asyncMergeTransactionQueue.addToQueue(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#asyncSave(java.lang.Object)
	 */
	@Override
	public <T> void asyncSave(T entity) {
		asyncSaveTransactionQueue.addToQueue(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#asyncSaveOrUpdate(java.lang.Object)
	 */
	@Override
	public <T> void asyncSaveOrUpdate(T entity) {
		asyncSaveOrUpdateTransactionQueue.addToQueue(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#asyncDelete(java.lang.Object)
	 */
	@Override
	public <T> void asyncDelete(T entity) {
		asyncDeleteTransactionQueue.addToQueue(entity);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryTransactionPool#getSessionTemplate()
	 */
	@Override
	public SessionTemplate getSessionTemplate() {
		return sessionTemplateFactory.createTemplate();
	}
}