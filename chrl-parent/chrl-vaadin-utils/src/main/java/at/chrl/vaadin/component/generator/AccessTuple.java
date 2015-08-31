/**
 * @author Christian Richard Leopold - ChRL
 * Aug 31, 2015 - 6:16:11 PM
 * chrl-vaadin-utils
 * at.chrl.vaadin.component.generator
 */
package at.chrl.vaadin.component.generator;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 31, 2015 - 6:16:11 PM
 *
 */
public final class AccessTuple<T> {

	private Function<?, T> getter;
	private BiConsumer<?, T> setter;
	/**
	 * 
	 */
	public AccessTuple(Function<?, T> getter, BiConsumer<?, T> setter) {
		this.getter = getter;
		this.setter = setter;
	}
	/**
	 * getter for getter
	 * @return getter
	 * 		AccessTuple.getter
	 */
	public Function<?, T> getGetter() {
		return this.getter;
	}
	/**
	 * setter for getter
	 * @param getter
	 *		new getter to set
	 */
	public void setGetter(Function<?, T> getter) {
		this.getter = getter;
	}
	/**
	 * getter for setter
	 * @return setter
	 * 		AccessTuple.setter
	 */
	public BiConsumer<?, T> getSetter() {
		return this.setter;
	}
	/**
	 * setter for setter
	 * @param setter
	 *		new setter to set
	 */
	public void setSetter(BiConsumer<?, T> setter) {
		this.setter = setter;
	}
}
