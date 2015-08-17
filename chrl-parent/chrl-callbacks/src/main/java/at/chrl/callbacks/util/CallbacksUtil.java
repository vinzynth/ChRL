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

import java.util.List;

import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import at.chrl.callbacks.Callback;
import at.chrl.callbacks.CallbackPriority;

@SuppressWarnings("rawtypes")
public class CallbacksUtil {

	/**
	 * Checks if annotation is present on method
	 *
	 * @param method
	 *            Method to check
	 * @param annotation
	 *            Annotation to look for
	 * @return result
	 */
	public static boolean isAnnotationPresent(CtMethod method, Class<? extends java.lang.annotation.Annotation> annotation) {
		for (Object o : method.getMethodInfo().getAttributes()) {
			if (o instanceof AnnotationsAttribute) {
				AnnotationsAttribute attribute = (AnnotationsAttribute) o;
				if (attribute.getAnnotation(annotation.getName()) != null) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns priority of callback.<br>
	 * Method checks if callback is instance of
	 * {@link at.chrl.callbacks.CallbackPriority}, and returns
	 * <p/>
	 * 
	 * <pre>
	 * {@link at.chrl.callbacks.CallbackPriority#DEFAULT_PRIORITY} - {@link at.chrl.callbacks.CallbackPriority#getPriority()}
	 * </pre>
	 * <p/>
	 * .<br>
	 * If callback is not instance of CallbackPriority then it returns
	 * {@link at.chrl.callbacks.CallbackPriority#DEFAULT_PRIORITY}
	 *
	 * @param callback
	 *            priority to get from
	 * @return priority of callback
	 */
	public static int getCallbackPriority(Callback callback) {
		if (callback instanceof CallbackPriority) {
			CallbackPriority instancePriority = (CallbackPriority) callback;
			return CallbackPriority.DEFAULT_PRIORITY - instancePriority.getPriority();
		} else {
			return CallbackPriority.DEFAULT_PRIORITY;
		}
	}

	protected static void insertCallbackToList(Callback callback, List<Callback> list) {
		int callbackPriority = CallbacksUtil.getCallbackPriority(callback);

		if (!list.isEmpty()) {
			// hand-made sorting, if needed to insert to the middle
			for (int i = 0, n = list.size(); i < n; i++) {
				Callback c = list.get(i);

				int cPrio = CallbacksUtil.getCallbackPriority(c);

				if (callbackPriority < cPrio) {
					list.add(i, callback);
					return;
				}
			}
		}
		// add last
		list.add(callback);
	}
}
