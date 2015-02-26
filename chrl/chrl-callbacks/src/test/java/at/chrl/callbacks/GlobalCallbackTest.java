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
package at.chrl.callbacks;

import org.junit.Test;

import at.chrl.callbacks.metadata.GlobalCallback;
import at.chrl.callbacks.util.GlobalCallbackHelper;
import at.chrl.callbacks.util.JavaAgentUtils;

/**
 * @author Vinzynth 08.11.2014 - 01:15:45
 *
 */
public class GlobalCallbackTest {

	@Test
	public void test() {
		JavaAgentUtils.isConfigured();

		GlobalCallbackHelper.addCallback(new TestCallback());

		CallbackObject callbackObject = new CallbackObject();
		CallbackObject callbackObject2 = new CallbackObject();

		callbackObject.call();
		callbackObject2.call();

	}

	public static class CallbackObject {

		@GlobalCallback(TestCallback.class)
		public void call() {
			System.out.println("called");
		}
	}

}
