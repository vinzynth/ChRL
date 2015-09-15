/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 7:32:32 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils
 */
package at.chrl.spring.generics.repositories.utils;

import java.util.function.BiConsumer;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;

import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.spring.generics.repositories.utils.impl.RepositoryTransactionPoolImplementation;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 7:32:32 PM
 *
 * <p>
 * 
 * Default implementation: {@link RepositoryTransactionPoolImplementation} 
 *
 */
public interface RepositoryTransactionPool {

	public <T> void asyncPersist(T entity);
	public <T> void asyncRefresh(T entity);
	public <T> void asyncMerge(T entity);
	public <T> void asyncSave(T entity);
	public <T> void asyncSaveOrUpdate(T entity);
	public <T> void asyncDelete(T entity);
	
	public String getIdentifierPropertyName(Class<?> cls);
	
	public AuditReader getAuditReader();
	
	public Session getSession();
	public EntityManager getEntityManager();
	public SessionTemplate getSessionTemplate();
	
	public void addAfterFunctionHook(BiConsumer<SessionTemplate, Object> function);
}
