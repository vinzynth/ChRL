/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 3:08:34 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories
 */
package at.chrl.spring.generics.repositories;

import java.util.function.Function;
import java.util.stream.Stream;

import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import at.chrl.spring.hibernate.config.RepositoryHolder;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 3:08:34 PM
 *
 */
public abstract class IndexSearcher<T> {
	
	@Autowired
	protected RepositoryHolder repositoryHolder;
	
	protected GenericIndexedRepository<T> repository;

	public IndexSearcher(final GenericIndexedRepository<T> repository) {
		this.repository = repository;
	}
	
	protected Stream<T> search(final Function<FullTextEntityManager, Stream<T>> func){
		return func.apply(repository.getFullTextEntityManager());
	}
	
	protected Stream<T> searchWithSession(final Function<FullTextSession, Stream<T>> func){
		return func.apply(repository.getFullTextSession());
	}

	public Class<T> getType() {
		return repository.getType();
	}
}
