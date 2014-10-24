/**
 * @author Vinzynth
 * Aug 12, 2014 - 10:31:10 AM
 * chrl-utils
 * at.chrl.nutils.interfaces
 */
package at.chrl.nutils.interfaces;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author Vinzynth
 *
 * @param <C>
 * 		Subtype of nested collection with E elements
 * @param <E>
 * 		Type of elements in the nested collection
 */
public interface INestedCollection<C extends Collection<E>, E> extends Collection<E>{
	
	/**
	 * returns nested collection of type C
	 * 
	 * @see {@link java.util.Collection}
	 * @see {@link INestedCollection}
	 * @return
	 * 		nested collection
	 */
	public C getNestedCollection();
	
	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#size()
	 */
	@Override
	public default int size() {
		return getNestedCollection().size();
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public default boolean isEmpty() {
		return getNestedCollection().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public default boolean contains(Object o) {
		return getNestedCollection().contains(o);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public default Iterator<E> iterator() {
		return getNestedCollection().iterator();
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public default Object[] toArray() {
		return getNestedCollection().toArray();
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@Override
	public default <T> T[] toArray(T[] a) {
		return getNestedCollection().toArray(a);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public default boolean add(E e) {
		return getNestedCollection().add(e);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public default boolean remove(Object o) {
		return getNestedCollection().remove(o);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public default boolean containsAll(Collection<?> c) {
		return getNestedCollection().containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public default boolean addAll(Collection<? extends E> c) {
		return getNestedCollection().addAll(c);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public default boolean removeAll(Collection<?> c) {
		return getNestedCollection().removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public default boolean retainAll(Collection<?> c) {
		return getNestedCollection().retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 * @see java.util.Collection#clear()
	 */
	@Override
	public default void clear() {
		getNestedCollection().clear();
	}

}
