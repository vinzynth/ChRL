/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 3:08:45 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories
 */
package at.chrl.spring.generics.repositories;

import java.util.stream.Stream;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;

/**
 * @author Christian Richard Leopold - ChRL <br>
 *         Aug 26, 2015 - 3:08:45 PM
 *
 */
public class SpatialIndexSearcher<T> extends IndexSearcher<T> {

	public SpatialIndexSearcher(final GenericIndexedRepository<T> repository) {
		super(repository);
	}

	public Stream<T> radiusSearch(double latitude, double longitude, double radius) {
		return searchWithSession(fullTextSession -> {
			QueryBuilder builder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(this.getType()).get();

			Query luceneQuery = builder.spatial()
					.within(radius, Unit.KM)
					.ofLatitude(latitude)
					.andLongitude(longitude)
					.createQuery();

			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(luceneQuery, this.getType());
			return repository.stream(hibQuery);
		});
	}
}
