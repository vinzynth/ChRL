/**
 * @author bravestone Feb 12, 2015 - 5:28:52 PM bravestone-dataProvider
 *         com.bravestone.diana.repositories
 */
package at.chrl.spring.generics.repositories;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.apache.lucene.search.Query;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.exception.EmptyQueryException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import at.chrl.orm.hibernate.configuration.JPAConfig;

/**
 * @author bravestone
 *
 */
@SuppressWarnings("unchecked")
public class GenericIndexedRepository<T> extends GenericRepository<T> {
	
	public GenericIndexedRepository(Class <T> cls) {
		super(cls);
	}
	
	public GenericIndexedRepository() {
		super();
	}
	
	@Autowired
	protected JPAConfig jpaConfig;

	protected FullTextEntityManager fullTextEntityManager;

	public FullTextEntityManager getFullTextEntityManager() {
		if (Objects.isNull(fullTextEntityManager))
			fullTextEntityManager = Search.getFullTextEntityManager(transactionPool.getEntityManager());
		return fullTextEntityManager;
	}

	protected FullTextSession fullTextSession;

	public FullTextSession getFullTextSession() {
		if (Objects.isNull(fullTextSession))
			fullTextSession = org.hibernate.search.Search.getFullTextSession(transactionPool.getSession());
		return fullTextSession;
	}

	// @PostConstruct
	public void updateIndex() {
		System.out.println("Updating index for " + this.getType().getSimpleName());
		updateIndexPrivate();
		System.out.println("Updating index for " + this.getType().getSimpleName() + " finished");
	}

	private void updateIndexPrivate() {
		try {
			// getFullTextSession().createIndexer(this.getType()).startAndWait();

			FullTextSession fullTextSession = getFullTextSession();
			fullTextSession.setFlushMode(FlushMode.MANUAL);
			fullTextSession.setCacheMode(CacheMode.IGNORE);
			Transaction transaction = fullTextSession.beginTransaction();

			int batchSize;
			try {
				batchSize = Integer.parseInt(jpaConfig.DEFAULT_BATCH_FETCH_SIZE);
			} catch (Exception e) {
				batchSize = 500;
			}
			// Scrollable results will avoid loading too many objects in memory
			ScrollableResults results = fullTextSession.createCriteria(this.getType()).setFetchSize(batchSize).scroll(ScrollMode.FORWARD_ONLY);
			int index = 0;
			while (results.next()) {
				index++;
				fullTextSession.index(results.get(0)); // index each element
				if (index % batchSize == 0) {
					fullTextSession.flushToIndexes(); // apply changes to
														// indexes
					fullTextSession.clear(); // free memory since the queue is
												// processed
				}
			}
			transaction.commit();
		} catch (NullPointerException e) {
			fullTextEntityManager = Search.getFullTextEntityManager(transactionPool.getEntityManager());
			updateIndexPrivate();
		} catch (Exception e) {
			System.out.println("Error rebuilding index: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public Collection<T> searchIndex(final String searchString, final String... fields) {
		QueryBuilder qb = getFullTextEntityManager().getSearchFactory().buildQueryBuilder().forEntity(getType()).get();

		Query createQuery;
		try {
			createQuery = qb.bool().must(qb.keyword().onFields(fields).matching(searchString).createQuery()).createQuery();
		} catch (EmptyQueryException e) {
			System.out.println("Index Search for " + getType().getSimpleName() + " | " + searchString.toString() + " | no hits");
			return Collections.emptyList();
		}

		FullTextQuery ftq = getFullTextEntityManager().createFullTextQuery(createQuery, getType());

		System.out.println("Index Search for " + getType().getSimpleName() + " | " + createQuery.toString() + " | " + ftq.getResultSize() + " hits");

		return ftq.getResultList();
	}
}
