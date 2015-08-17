/**
 * This file is part of InPanic-Core.
 *
 * InPanic-Core is a free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * InPanic-Core is distibuted in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * InPanic-Core. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.utils.TaskExecutor;

/**
 * @author Vinzynth 28.08.2013 - 20:33:29
 *
 */
public abstract class Task implements Runnable, Comparable<Task> {

	protected int priority = 10;

	int calls = 250;

	public Task() {
		callLater = null;
		workerId = -1;
	}

	int workerId;
	private Task callLater;

	public final void setCallLater(final Task t) {
		t.calls = this.calls - 1;
		this.callLater = t;
		this.workerId = -1;
	}

	public final boolean hasCallLater() {
		return callLater != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		this.runTask();
		TaskExecutor.finishedTask(this);
		if (this.callLater != null)
			callLater.run();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(Task o) {
		return this.priority - o.priority;
	}

	/**
	 * Method called by Task Executer and by Functional Interace implementation
	 * 
	 * @see {@link TaskExecutor}
	 */
	public abstract void runTask();
}
