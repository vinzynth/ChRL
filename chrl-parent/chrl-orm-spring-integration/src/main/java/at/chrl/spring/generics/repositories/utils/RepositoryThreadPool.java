/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 7:32:32 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils
 */
package at.chrl.spring.generics.repositories.utils;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 7:32:32 PM
 *
 */
public interface RepositoryThreadPool {

	public <T> T persist(T entity);
	public <T> T remove(T entity);
	public <T> T refresh(T entity);
	public <T> T merge(T entity);
	public <T> T save(T entity);
	public <T> T saveOrUpdate(T entity);
	public <T> T mergeWithSession(T entity);
	public <T> T persistWithSession(T entity);
	public <T> T delete(T entity);
	
	public ScrollableResults scroll(Criteria crit);
	public ScrollableResults scroll(Query query);
	public <T> List<T> list(Criteria crit);
	public <T> List<T> list(Query query);
	public <T> T uniqueResult(Criteria crit);
	public <T> T uniqueResult(Query query);
	public int executeUpdate(Query query);
}
