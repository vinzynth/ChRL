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
package at.chrl.callbacks.util;

import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.chrl.callbacks.Callback;
import at.chrl.callbacks.CallbackResult;
import at.chrl.nutils.ClassUtils;

/**
 * This class is used to manage global callbacks.<br>
 * Callbacks are stored according to their priority.
 *
 * @author SoulKeeper
 */
@SuppressWarnings("rawtypes")
public class GlobalCallbackHelper {

	private static final Logger log = LoggerFactory.getLogger(GlobalCallbackHelper.class);

	private static final CopyOnWriteArrayList<Callback> globalCallbacks = new CopyOnWriteArrayList<Callback>();

	/**
	 * Private constructor to prevent initialization
	 */
	private GlobalCallbackHelper() {

	}

	/**
	 * Registers global callback.<br>
	 * Please note that invoking this method from scripts can cause memory leak,
	 * callbacks are not weak references. You should unregister callback
	 * manually in case of global adding global callback.
	 *
	 * @param callback
	 *            callback to add
	 */
	public static <T> void addCallback(Callback<T> callback) {
		synchronized (GlobalCallbackHelper.class) {
			CallbacksUtil.insertCallbackToList(callback, globalCallbacks);
		}
	}

	/**
	 * Removes global callback from the list.<br>
	 *
	 * @param callback
	 *            callback to remove
	 */
	public static <T> void removeCallback(Callback<T> callback) {
		synchronized (GlobalCallbackHelper.class) {
			globalCallbacks.remove(callback);
		}
	}

	/**
	 * <b><font color="red">THIS METHOD SHOULD NOT BE CALLED MANUALLY</font></b>
	 *
	 * @param obj
	 *            method on whom was invoked
	 * @param callbackClass
	 *            what method was actually invoked
	 * @param args
	 *            method arguments
	 * @return result of invocation callbacks
	 */
	@SuppressWarnings({ "unchecked" })
	public static CallbackResult<?> beforeCall(Object obj, Class callbackClass, Object... args) {

		CallbackResult<?> cr = null;
		for (Callback cb : globalCallbacks) {
			if (!ClassUtils.isSubclass(cb.getBaseClass(), callbackClass)) {
				continue;
			}

			try {
				cr = cb.beforeCall(obj, args);
				if (cr.isBlockingCallbacks()) {
					break;
				}
			} catch (Exception e) {
				log.error("Exception in global callback", e);
			}
		}

		return cr == null ? CallbackResult.newContinue() : cr;
	}

	/**
	 * <b></><font color="red">THIS METHOD SHOULD NOT BE CALLED
	 * MANUALLY</font></b>
	 *
	 * @param obj
	 *            method on whom was invoked
	 * @param callbackClass
	 *            what method was actually invoked
	 * @param args
	 *            method arguments
	 * @param result
	 *            original method result
	 * @return global result, callback or method, doesn't matter
	 */
	@SuppressWarnings({ "unchecked" })
	public static CallbackResult<?> afterCall(Object obj, Class callbackClass, Object[] args, Object result) {

		CallbackResult<?> cr = null;
		for (Callback cb : globalCallbacks) {
			if (!ClassUtils.isSubclass(cb.getBaseClass(), callbackClass)) {
				continue;
			}

			try {
				cr = cb.afterCall(obj, args, result);
				if (cr.isBlockingCallbacks()) {
					break;
				}
			} catch (Exception e) {
				log.error("Exception in global callback", e);
			}
		}

		return cr == null ? CallbackResult.newContinue() : cr;
	}
}
