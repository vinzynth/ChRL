/**
 * @author Christian Richard Leopold - ChRL
 * Aug 28, 2015 - 3:57:44 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils
 */
package at.chrl.spring.generics.repositories.utils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 28, 2015 - 3:57:44 PM
 *
 */
@SuppressWarnings("unchecked")
public interface RepositoryTransactionPoolConsumer<T> {

	public RepositoryTransactionPool getTransactionPool();
	
	public default void asyncPersist(T entity){
		getTransactionPool().asyncPersist(entity);
	}
	public default void asyncRefresh(T entity){
		getTransactionPool().asyncRefresh(entity);
	}
	public default void asyncMerge(T entity){
		getTransactionPool().asyncMerge(entity);
	}
	public default void asyncSave(T entity){
		getTransactionPool().asyncSave(entity);
	}
	public default void asyncSaveOrUpdate(T entity){
		getTransactionPool().asyncSaveOrUpdate(entity);
	}
	public default void asyncDelete(T entity){
		getTransactionPool().asyncDelete(entity);
	}
	public default void asyncPersist(Collection<T> entities){
		entities.forEach(entity -> getTransactionPool().asyncPersist(entity));
	}
	public default void asyncRefresh(Collection<T> entities){
		entities.forEach(entity -> getTransactionPool().asyncRefresh(entity));
	}
	public default void asyncMerge(Collection<T> entities){
		entities.forEach(entity -> getTransactionPool().asyncMerge(entity));
	}
	public default void asyncSave(Collection<T> entities){
		entities.forEach(entity -> getTransactionPool().asyncSave(entity));
	}
	public default void asyncSaveOrUpdate(Collection<T> entities){
		entities.forEach(entity -> getTransactionPool().asyncSaveOrUpdate(entity));
	}
	public default void asyncDelete(Collection<T> entities){
		entities.forEach(entity -> getTransactionPool().asyncDelete(entity));
	}
	public default void asyncPersist(T... entities){
		Arrays.stream(entities).forEach(entity -> getTransactionPool().asyncPersist(entity));
	}
	public default void asyncRefresh(T... entities){
		Arrays.stream(entities).forEach(entity -> getTransactionPool().asyncRefresh(entity));
	}
	public default void asyncMerge(T... entities){
		Arrays.stream(entities).forEach(entity -> getTransactionPool().asyncMerge(entity));
	}
	public default void asyncSave(T... entities){
		Arrays.stream(entities).forEach(entity -> getTransactionPool().asyncSave(entity));
	}
	public default void asyncSaveOrUpdate(T... entities){
		Arrays.stream(entities).forEach(entity -> getTransactionPool().asyncSaveOrUpdate(entity));
	}
	public default void asyncDelete(T... entities){
		Arrays.stream(entities).forEach(entity -> getTransactionPool().asyncDelete(entity));
	}
}
