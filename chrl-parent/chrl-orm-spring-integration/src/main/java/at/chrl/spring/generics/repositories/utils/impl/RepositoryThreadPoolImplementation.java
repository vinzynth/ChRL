/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 7:40:37 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils.impl
 */
package at.chrl.spring.generics.repositories.utils.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;

import at.chrl.spring.generics.repositories.utils.RepositoryThreadPool;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 7:40:37 PM
 *
 */
public class RepositoryThreadPoolImplementation implements RepositoryThreadPool{

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#persist(java.lang.Object)
	 */
	@Override
	public <T> T persist(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#remove(java.lang.Object)
	 */
	@Override
	public <T> T remove(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#refresh(java.lang.Object)
	 */
	@Override
	public <T> T refresh(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#merge(java.lang.Object)
	 */
	@Override
	public <T> T merge(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#save(java.lang.Object)
	 */
	@Override
	public <T> T save(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public <T> T saveOrUpdate(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#mergeWithSession(java.lang.Object)
	 */
	@Override
	public <T> T mergeWithSession(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#persistWithSession(java.lang.Object)
	 */
	@Override
	public <T> T persistWithSession(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#delete(java.lang.Object)
	 */
	@Override
	public <T> T delete(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#scroll(org.hibernate.Criteria)
	 */
	@Override
	public ScrollableResults scroll(Criteria crit) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#scroll(org.hibernate.Query)
	 */
	@Override
	public ScrollableResults scroll(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#list(org.hibernate.Criteria)
	 */
	@Override
	public List<?> list(Criteria crit) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#list(org.hibernate.Query)
	 */
	@Override
	public List<?> list(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#uniqueResult(org.hibernate.Criteria)
	 */
	@Override
	public Object uniqueResult(Criteria crit) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#uniqueResult(org.hibernate.Query)
	 */
	@Override
	public Object uniqueResult(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.generics.repositories.utils.RepositoryThreadPool#executeUpdate(org.hibernate.Query)
	 */
	@Override
	public int executeUpdate(Query query) {
		// TODO Auto-generated method stub
		return 0;
	}

}
