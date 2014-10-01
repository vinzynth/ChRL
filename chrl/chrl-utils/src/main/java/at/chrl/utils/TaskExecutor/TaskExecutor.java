/**
 * This file is part of InPanic-Core.
 *
 * InPanic-Core is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * InPanic-Core is distibuted in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with InPanic-Core. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.utils.TaskExecutor;

import java.util.Collection;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Vinzynth
 * 28.08.2013 - 20:34:01
 *
 */
public final class TaskExecutor {
	
	private static final int threadCount = Runtime.getRuntime().availableProcessors() * 3/2;
	
	private static final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<Task>(threadCount*10);
	private static final Thread[] worker = new Thread[threadCount];
	
	public static final void runTaskGroup(final Collection<Task> tasks){
		runTaskGroup(false, tasks.toArray(new Task[tasks.size()]));
	}
	
	public static final void runTaskGroup(final boolean waitForFinish, final Collection<Task> tasks){
		runTaskGroup(waitForFinish, tasks.toArray(new Task[tasks.size()]));
	}
		
	public static final void runTaskGroup(final Task... tasks){
		runTaskGroup(false, tasks);
	}
	
	public static final void runTaskGroup(final boolean waitForFinish ,final Task... tasks){
		if(waitForFinish){
			for (Task task : tasks)
				runTask(task);
			for (Task task : tasks)
				joinTask(task);
		}
		else{
			for (Task task : tasks)
				runTask(task);
		}
	}
	
	/**
	 * @param renameSearchTask
	 */
	public static final <T extends Task> T joinTask(final T t) {
		boolean putBack = false;
		Object lock = new Object();
		int l = worker.length;
		for (int i = 0; i < l; i++) {
			if(worker[i] != null && worker[i].equals(Thread.currentThread())){
//				log.debug("Deadlock!");
				putBack = true;
				worker[i] = null;
				l = i;
				break;
			}
		}
		try {
			synchronized (lock) {
				while(t.workerId == -1)
					lock.wait(1);
				while(t.workerId != -2)
					lock.wait(1);
				if(putBack){
					for(int c = 0; worker[c] != null; c++){
						lock.wait(1);
						if(c >= 3)
							c = -1;
					}
					worker[l] = Thread.currentThread();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	static final void finishedTask(final Task t){
//		log.debug("finished: " + t.workerId);
		int id = t.workerId;
		if(!t.hasCallLater() && id >= 0){
			Task next = queue.poll();
//			log.debug("get next");
			if(next == null){
				worker[id] = null;
				t.workerId = -2;
//				log.debug("no next");
				return;
			}
			if(t.calls <= 0){
				worker[id] = new Thread(next);
				worker[id].setPriority(Thread.MAX_PRIORITY);
				next.workerId = id;
				t.workerId = -2;
				worker[id].start();
			}
			else
			{
				t.setCallLater(next);
				next.workerId = id;
				t.workerId = -2;
			}
//			log.debug("done next");
		}
	}
	
	public static final <T extends Task> T runInStream(final T task){
		runTask(task);
		return task;
	}
	
	public static final <T extends Task> T runInParallelStream(final T task){
		runTask(task);
		joinTask(task);
		return task;
	}
	
	public synchronized static final void runTask(final Task t){
		if(t == null)
			return;
		int l = worker.length;
		for (int i = 0; i < l; i++) {
			if(worker[i] == null){
//				log.debug("worker == null: " + i);
				worker[i] = new Thread(t);
				worker[i].setPriority(Thread.MAX_PRIORITY);
				t.workerId = i;
				worker[i].start();
				return ; //worker[i]
			}
//			log.debug(worker[i].toString());
		}
		queue.add(t);
		return;
	}
}
