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

import at.chrl.callbacks.metadata.ObjectCallback;
import at.chrl.callbacks.util.JavaAgentUtils;

/**
 * @author Vinzynth 08.11.2014 - 01:15:45
 *
 */
public class ObjectCallbackTest {

	@Test
	public void test() {
		try {
			JavaAgentUtils.isConfigured();			
		} catch (Error e) {
			return;
		}

		CallbackObject co = new CallbackObject();
		CallbackObject co2 = new CallbackObject();

		co.call();
		co2.call();

		((EnhancedObject) co).addCallback(new TestCallback());

		co.call();
		co2.call();
	}

	public static class CallbackObject {

		@ObjectCallback(TestCallback.class)
		public void call() {
			System.out.println(this + " called");
		}
	}
}
