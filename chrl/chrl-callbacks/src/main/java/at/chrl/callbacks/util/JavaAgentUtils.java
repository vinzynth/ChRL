/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * ChRL Util Collection. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.callbacks.util;

import at.chrl.callbacks.EnhancedObject;
import at.chrl.callbacks.metadata.GlobalCallback;
import at.chrl.callbacks.metadata.ObjectCallback;

/**
 * @author Vinzynth 08.11.2014 - 02:37:29
 *
 */
public class JavaAgentUtils {
	static {
		GlobalCallbackHelper.addCallback(new CheckCallback());
	}

	public static boolean isConfigured() {
		JavaAgentUtils jau = new JavaAgentUtils();
		if (!(jau instanceof EnhancedObject))
			throw new Error("Please configure -javaagent:<path-to-chrl-callbacks-jar> jvm option.");

		if (!checkGlobalCallback())
			throw new Error("Global callbacks are not working correctly!");

		((EnhancedObject) jau).addCallback(new CheckCallback());
		if (!jau.checkObjectCallback())
			throw new Error("Object callbacks are not working correctly!");

		return true;
	}

	@GlobalCallback(CheckCallback.class)
	private static boolean checkGlobalCallback() {
		return false;
	}

	@ObjectCallback(CheckCallback.class)
	private boolean checkObjectCallback() {
		return false;
	}
}
