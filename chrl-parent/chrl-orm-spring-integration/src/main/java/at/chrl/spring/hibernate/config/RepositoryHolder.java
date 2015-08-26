/**
 * @author Christian Richard Leopold - ChRL
 * Aug 21, 2015 - 4:57:21 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.hibernate.config
 */
package at.chrl.spring.hibernate.config;

import at.chrl.spring.generics.repositories.GenericIndexedRepository;
import at.chrl.spring.generics.repositories.GenericRepository;
import at.chrl.spring.generics.repositories.IndexSearcher;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 21, 2015 - 4:57:21 PM
 *
 */
public interface RepositoryHolder {
	
	public <R extends GenericRepository<T>, T> R getRepository(final Class<T> cls);
	
	public <R extends GenericIndexedRepository<T>, T> R getIndexedRepository(final Class<T> cls);
	
	public <S extends IndexSearcher<T>, T> S getIndexSearcher(final Class<S> searcherClass, final Class<T> type);
}
