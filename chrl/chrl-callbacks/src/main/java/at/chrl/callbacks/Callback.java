/**
 * This file is part of aion-lightning <aion-lightning.org>.
 * 
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.callbacks;

/**
 * Basic callback class.<br>
 * Each enhanced method will call "beforeCall" and "afterCall" methods
 *
 * @author SoulKeeper
 */
@SuppressWarnings("rawtypes")
public interface Callback<T> {

	/**
	 * Method that is called before actual method is invoked.<br>
	 * <p/>
	 * Callback should return one of the following results:
	 * <ul>
	 * <li>{@link CallbackResult#newContinue()}</li>
	 * <li>{@link CallbackResult#newCallbackBlocker()}</li>
	 * <li>{@link CallbackResult#newFullBlocker(Object)}</li>
	 * </ul>
	 * <p/>
	 * if result is not {@link CallbackResult#newFullBlocker(Object)} then
	 * method will be executed normally. In other case
	 * {@link CallbackResult#getResult()} will be returned.
	 *
	 * @param obj
	 *            on whom method should be invoked
	 * @param args
	 *            method args
	 * @return see {@link CallbackResult}
	 */
	public default CallbackResult beforeCall(T obj, Object[] args){
		return CallbackResult.newContinue();
	}

	/**
	 * Method that is called after actual method call.<br>
	 * <p/>
	 * Callback should return one of the following results:
	 * <ul>
	 * <li>{@link CallbackResult#newContinue()}</li>
	 * <li>{@link CallbackResult#newCallbackBlocker()}</li>
	 * <li>{@link CallbackResult#newFullBlocker(Object)}</li>
	 * </ul>
	 * <p/>
	 * if result is not {@link CallbackResult#newFullBlocker(Object)} then
	 * mehodResult will return unmodified. In other case
	 * {@link CallbackResult#getResult()} will be returned.
	 *
	 * @param obj
	 *            on whom method was called
	 * @param args
	 *            method args
	 * @param methodResult
	 *            result of method invocation
	 * @return see {@link CallbackResult}
	 */
	public default CallbackResult afterCall(T obj, Object[] args, Object methodResult){
		return CallbackResult.newContinue();
	}

	/**
	 * Returns base class that will be used as callback identificator.<br>
	 * {@link at.chrl.callbacks.metadata.ObjectCallback#value()} should contain
	 * class that will be invoked
	 *
	 * @return base class of callback
	 */
	@SuppressWarnings("unchecked")
	public default Class<? extends Callback<?>> getBaseClass(){
		return (Class<? extends Callback<?>>) this.getClass();
	}
}
