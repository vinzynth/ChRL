/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 3:08:45 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories
 */
package at.chrl.spring.generics.repositories;

import java.util.function.Consumer;
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

	public void radiusSearch(double latitude, double longitude, double radius, Consumer<Stream<T>> streamConsumer) {
		QueryBuilder builder = repository.getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(this.getType()).get();

		Query luceneQuery = builder.spatial()
				.within(radius, Unit.KM)
				.ofLatitude(latitude)
				.andLongitude(longitude)
				.createQuery();
		
		org.hibernate.Query hibQuery = repository.getFullTextSession().createFullTextQuery(luceneQuery, this.getType());
		repository.process(s -> streamConsumer.accept(s.stream(hibQuery)));
	}
	
	public void boxSearch(double southWestLat, double southWestLon, double northEastLat, double northEastLon, Consumer<Stream<T>> streamConsumer) {
		double kmLat = (northEastLat - southWestLat) * 110.574d;
		double kmLon = Math.cos((southWestLon - northEastLon) * Math.PI/180) * 111.320d;
		radiusSearch((northEastLat + southWestLat)/2, (northEastLon + southWestLon)/2,
				Math.sqrt(kmLat*kmLat + kmLon*kmLon)/2,
				streamConsumer
				);
	}
}
