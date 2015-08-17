package at.chrl.nutils.concurrent;

import java.util.concurrent.TimeUnit;

import static at.chrl.nutils.Constants.log;

/**
 * @author NB4L1
 */
public class ExecuteWrapper implements Runnable {
	private final Runnable runnable;

	public ExecuteWrapper(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public final void run() {
		ExecuteWrapper.execute(runnable, getMaximumRuntimeInMillisecWithoutWarning());
	}

	protected long getMaximumRuntimeInMillisecWithoutWarning() {
		return Long.MAX_VALUE;
	}

	public static void execute(Runnable runnable) {
		execute(runnable, Long.MAX_VALUE);
	}

	public static void execute(Runnable runnable, long maximumRuntimeInMillisecWithoutWarning) {
		long begin = System.nanoTime();

		try {
			runnable.run();
		} catch (RuntimeException e) {
			log.warn("Exception in a Runnable execution:", e);
		} finally {
			long runtimeInNanosec = System.nanoTime() - begin;
			Class<? extends Runnable> clazz = runnable.getClass();

			RunnableStatsManager.handleStats(clazz, runtimeInNanosec);

			long runtimeInMillisec = TimeUnit.NANOSECONDS.toMillis(runtimeInNanosec);

			if (runtimeInMillisec > maximumRuntimeInMillisecWithoutWarning) {
				StringBuilder tb = new StringBuilder();

				tb.append(clazz);
				tb.append(" - execution time: ");
				tb.append(runtimeInMillisec);
				tb.append("msec");

				log.warn(tb.toString());
			}
		}
	}
}
