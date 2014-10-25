/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.nutils.network.util;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import at.chrl.nutils.concurrent.RunnableWrapper;

import static at.chrl.nutils.Constants.log;

/**
 * @author -Nemesiss-
 */
public class ThreadPoolManager implements Executor
{
	/**
	 * PriorityThreadFactory creating new threads for ThreadPoolManager
	 * 
	 */
	private static class PriorityThreadFactory implements ThreadFactory
	{
		/**
		 * Priority of new threads
		 */
		private int				prio;
		/**
		 * Thread group name
		 */
		private String			name;
		/**
		 * Number of created threads
		 */
		private AtomicInteger	threadNumber	= new AtomicInteger(1);
		/**
		 * ThreadGroup for created threads
		 */
		private ThreadGroup		group;

		/**
		 * Constructor.
		 * 
		 * @param name
		 * @param prio
		 */
		public PriorityThreadFactory(final String name, final int prio)
		{
			this.prio = prio;
			this.name = name;
			group = new ThreadGroup(this.name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Thread newThread(final Runnable r)
		{
			Thread t = new Thread(group, r);
			t.setName(name + "-" + threadNumber.getAndIncrement());
			t.setPriority(prio);
			t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
			return t;
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ThreadPoolManager	instance	= new ThreadPoolManager();
	}
	
	/**
	 * @return ThreadPoolManager instance.
	 */
	public static final ThreadPoolManager getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * STPE for normal scheduled tasks
	 */
	private ScheduledThreadPoolExecutor	scheduledThreadPool;

	/**
	 * TPE for execution of gameserver client packets
	 */
	private final ThreadPoolExecutor	generalPacketsThreadPool;

	/**
	 * Constructor.
	 */
	private ThreadPoolManager()
	{
		new DeadLockDetector(60, DeadLockDetector.RESTART).start();

		scheduledThreadPool = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 5, new PriorityThreadFactory("ScheduledThreadPool", Thread.NORM_PRIORITY));

		generalPacketsThreadPool = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

	}

	/**
	 * Executes Runnable - GameServer Client packet.
	 * 
	 * @param pkt
	 */
	@Override
	public void execute(final Runnable pkt)
	{
		generalPacketsThreadPool.execute(new RunnableWrapper(pkt));
	}

	/**
	 * @return the packetsThreadPool
	 */
	public ThreadPoolExecutor getPacketsThreadPool()
	{
		return generalPacketsThreadPool;
	}

	/**
	 * Schedule
	 * 
	 * @param <T>
	 * @param r
	 * @param delay
	 * @return ScheduledFuture
	 */

	@SuppressWarnings("unchecked")
	public <T extends Runnable> ScheduledFuture<T> schedule(final T r, long delay)
	{
		try
		{
			if (delay < 0)
				delay = 0;
			return (ScheduledFuture<T>) scheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
		}
		catch (RejectedExecutionException e)
		{
			return null; /* shutdown, ignore */
		}
	}

	/**
	 * Schedule at fixed rate
	 * 
	 * @param <T>
	 * @param r
	 * @param initial
	 * @param delay
	 * @return ScheduledFuture
	 */
	@SuppressWarnings("unchecked")
	public <T extends Runnable> ScheduledFuture<T> scheduleAtFixedRate(final T r, long initial, long delay)
	{
		try
		{
			if (delay < 0)
				delay = 0;
			if (initial < 0)
				initial = 0;
			return (ScheduledFuture<T>) scheduledThreadPool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
		}
		catch (RejectedExecutionException e)
		{
			return null;
		}
	}

	/**
	 * Shutdown all thread pools.
	 */
	public void shutdown()
	{
		try
		{
			scheduledThreadPool.shutdown();
			generalPacketsThreadPool.shutdown();
			scheduledThreadPool.awaitTermination(2, TimeUnit.SECONDS);
			generalPacketsThreadPool.awaitTermination(2, TimeUnit.SECONDS);
			log.info("All ThreadPools are now stopped.");
		}
		catch (InterruptedException e)
		{
			log.error("Can't shutdown ThreadPoolManager", e);
		}
	}
}
