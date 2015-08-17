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

/**
 * @author Vinzynth 08.11.2014 - 01:15:45
 *
 */
public class TestCallback implements Callback<Object> {

	@Override
	public CallbackResult<Object> beforeCall(Object obj, Object[] args) {
		Thread.dumpStack();
		System.out.println("before Call");
		return CallbackResult.newContinue();
	}

	@Override
	public CallbackResult<Object> afterCall(Object obj, Object[] args, Object methodResult) {
		System.out.println("after Call");
		return CallbackResult.newContinue();
	}
}
