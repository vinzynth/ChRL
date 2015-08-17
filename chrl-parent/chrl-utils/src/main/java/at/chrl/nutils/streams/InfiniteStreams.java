/**
 * (C) ChRL 2015
 */
package at.chrl.nutils.streams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;

import at.chrl.nutils.Rnd;


/**
 * @author vinzynth
 * Jul 12, 2015 - 12:24:45 AM
 *
 */
public final class InfiniteStreams {

	/**
	 * returns a infinite stream which counts
	 * 
	 * @param start - start element
	 * @param step - counting step
	 * @return infinite stream
	 */
	public static Stream<Integer> getInfiniteCountingStream(int start, int step){
		return Stream.iterate(start, i -> i +step);
	}
	
	/**
	 * returns a infinite stream which counts and calculates modulo
	 * 
	 * @param start - start element 
	 * @param mod - modulo factor
	 * @param step - counting step
	 * 
	 * @return infinite stream
	 */
	public static Stream<Integer> getInfiniteModuloStream(int start, int step, int mod){
		return Stream.iterate(start, i -> (i+step) % mod);
	}
	
	/**
	 * returns a infinite stream with random numbers
	 * 
	 * @return infinites stream
	 */
	public static Stream<Float> getInfinteRandomStream(){
		return Stream.generate(() -> ((Float)Rnd.get()));
	}
	
	/**
	 * Gets infinite queue working stream
	 * 
	 * @param queue - target queue
	 * @return infinite stream which returns head of queue as it gets available
	 */
	public static <T> Stream<T> getQueueStream(final BlockingQueue<T> queue){
		return Stream.generate(() -> {
			try {
				return queue.take();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	/**
	 * Creates some threads which poll the given queue and work down the stack
	 * 
	 * @param queue
	 * @param streamStack
	 * @param threads
	 * @return Collection of started threads
	 */
	public static <T> Collection<Thread> workQueueStream(final BlockingQueue<T> queue, Consumer<Stream<T>> streamStack, int threads){
		ArrayList<Thread> workingThreads = new ArrayList<>(threads);
		for (int i = 0; i < threads; i++) {
			Thread t = new Thread(() -> {
				streamStack.accept(InfiniteStreams.getQueueStream(queue));
			});
			workingThreads.add(t);
			t.setDaemon(true);
			t.start();
		}
		return workingThreads;
	}
}
