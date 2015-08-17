package at.chrl.utils.ThresholdTimeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;

public abstract class ThresholdTimeoutService<T> {

	private PriorityBlockingQueue<T> queue;
	private volatile int threshold;
	private volatile int timeout;
	private volatile int processesRunning;
	private volatile Timer timer;

	/**
	 * fires {@link #processBeforeAll(Object)}, {@link #processAll(Collection)}
	 * and {@link #processAfterAll(Object)} when threshold or timeout is reached
	 * 
	 * @param threshold
	 * @param timeout
	 *            in Seconds
	 */
	public ThresholdTimeoutService(int threshold, int timeout) {
		this(threshold, timeout, null);
	}

	public ThresholdTimeoutService(int threshold, int timeout, Comparator<T> cmp) {
		this.threshold = threshold;
		this.timeout = timeout;
		this.processesRunning = 0;
		this.queue = new PriorityBlockingQueue<>(100, cmp);
		this.replaceTimer(timeout);
	}

	private void replaceTimer(int timeout) {
		if (this.timer != null) {
			this.timer.cancel();
			this.timer.purge();
		}
		this.timer = new Timer(true);
		this.timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				refresh(false);
			}
		}, timeout * 1_000, timeout * 1_000);
	}

	@SafeVarargs
	public final boolean addToQueue(T... data) {
		return addToQueue(Arrays.asList(data));
	}

	public final boolean addToQueue(Iterable<T> data) {
		boolean returnMe = true;
		for (T t : data)
			returnMe = queue.add(t) && returnMe;

		new Thread(new Runnable() {
			@Override
			public void run() {
				refresh(true);
			}
		}).start();

		return returnMe;
	}

	/**
	 * pre processing
	 * 
	 * @param arg
	 * @return true if object should be removed from further processing
	 * @throws Exception
	 */
	public abstract boolean processBeforeAll(T arg) throws Exception;

	/**
	 * main processing
	 * 
	 * @param args
	 * @return true if you want to make post processing
	 */
	public abstract boolean processAll(Collection<T> args);

	/**
	 * post processing
	 * 
	 * @param arg
	 * @throws Exception
	 */
	public abstract void processAfterAll(T arg) throws Exception;

	private final void refresh(boolean countCheck) {
		if (countCheck && queue.size() < this.threshold)
			return;
		processesRunning++;
		ArrayList<T> curr = new ArrayList<>(queue.size());
		while (queue.peek() != null)
			curr.add(queue.poll());

		LinkedList<T> err = new LinkedList<>();

		// process before
		for (T t : curr) {
			try {
				if (this.processBeforeAll(t))
					err.add(t);
			} catch (Exception e) {
				err.add(t);
				e.printStackTrace();
			}
		}

		// remove error objects
		while (err.peek() != null)
			curr.remove(err.poll());

		if (curr.size() > 0) {
			if (this.processAll(curr)) {
				// process after
				for (T t : curr) {
					try {
						this.processAfterAll(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		processesRunning--;
	}

	public final int getTimeout() {
		return timeout;
	}

	public final void setTimeout(int timeout) {
		this.timeout = timeout;
		this.replaceTimer(timeout);
	}

	public final int getThreshold() {
		return threshold;
	}

	public final void setThreshold(int threshold) {
		this.threshold = threshold;
		this.refresh(true);
	}

	public int getProcessesRunning() {
		return processesRunning;
	}
}
