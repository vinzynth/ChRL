/**
 * @author Christian Richard Leopold - ChRL
 * Aug 28, 2015 - 2:46:39 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.generics.repositories.utils.impl
 */
package at.chrl.spring.generics.repositories.utils.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;

import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.spring.hibernate.config.SessionTemplateFactory;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 28, 2015 - 2:46:39 PM
 *
 */
public class TransactionQueue {
	
	public static final int BATCH_SIZE = 1500;
	public static final int MAX_THREAD_POOL_SIZE = 100;
	public static final double STEP = 0.007;
	
	private volatile double threshold = STEP;
	
	private BlockingQueue<Object> processFunctionQueue = new LinkedBlockingQueue<>();
	private Collection<TransactionThread> workingThreads = new ConcurrentLinkedQueue<>();
	private SessionTemplateFactory sessionTemplateFactory;
	
	private CountDownLatch lock = null;
	private BiConsumer<SessionTemplate, Object> function;
	
	/**
	 * 
	 */
	public TransactionQueue(SessionTemplateFactory sessionTemplateFactory, BiConsumer<SessionTemplate, Object> function) {
		this.sessionTemplateFactory = sessionTemplateFactory;
		this.function = function;
	}
	
	void addToQueue(Object o){
		if(processFunctionQueue.size() >= 100*BATCH_SIZE){
			lock = new CountDownLatch(1);
			//FIXME: call system overload
		}
		
		if(Objects.nonNull(lock))
			try {
				lock.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		processFunctionQueue.add(o);
		
		float load = processFunctionQueue.size();
		
		if(load >= Math.sqrt(threshold)*BATCH_SIZE) newThread(1);
		
		if(workingThreads.isEmpty()){
			threshold = STEP;
			newThread(1);
		}
	}
	
	private void newThread(int count){
		final int c = Math.min(MAX_THREAD_POOL_SIZE - workingThreads.size(), count);
		for (int i = 0; i < c; i++) {
//			System.err.println("[Start new Transaction Thread] " + workingThreads.size() + " threshold: " + threshold);
			workingThreads.add(new TransactionThread(processFunctionQueue, sessionTemplateFactory, this));			
			threshold = threshold + STEP;
		}
	}
	
	void threadFinished(TransactionThread t){
//		System.err.println("[Stop new Transaction Thread]");
		workingThreads.remove(t);
		threshold = threshold - STEP;
		if(Objects.nonNull(lock)){
			lock.countDown();
			if(lock.getCount() <= 0)
				lock = null;
		}
	}
	

	BiConsumer<SessionTemplate, Object> getFunction() {
		return function;
	}
}
