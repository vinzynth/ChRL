/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or
 * modify  it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ChRL Util Collection.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.spring.generics.repositories.utils.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;

import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.spring.hibernate.config.SessionTemplateFactory;

/**
 * @author Vinzynth
 * 27.08.2015 - 20:02:04
 *
 */
public final class TransactionThread {
	
	private final Thread thread;
		
	private SessionTemplateFactory sessionFactory;
	private SessionTemplate session;
	private int i = 0;

	private BlockingQueue<Object> processQueue;
	private BiConsumer<SessionTemplate, Object> function;
	private Collection<BiConsumer<SessionTemplate, Object>> afterFunctionHooks;

	
	/**
	 * 
	 */
	public TransactionThread(BlockingQueue<Object> processQueue, SessionTemplateFactory sessionFactory, TransactionQueue transactionQueue) {
		this.processQueue = processQueue;
		this.sessionFactory = sessionFactory;
		this.function = transactionQueue.getFunction();
		this.afterFunctionHooks = new ArrayList<>(transactionQueue.getAfterFunctionHooks());
		this.thread = new Thread(() -> {
			for (Object object : new Iterable<Object>() {

				@Override
				public Iterator<Object> iterator() {
					return new Iterator<Object>() {
						
						@Override
						public Object next() {
							try {
								return processQueue.take();
							} catch (InterruptedException e) {
								System.err.println("[Stop Transaction Thread] " + Thread.currentThread().getName());
							}
							return null;
						}
						
						@Override
						public boolean hasNext() {
							return !processQueue.isEmpty();
						}
					};
				}
			}) {
				process(object);
			}
			transactionQueue.threadFinished(TransactionThread.this);
		});
		
		this.thread.setName("TransactionThread_" + System.nanoTime());
		this.thread.setDaemon(true);
		this.thread.start();
	}
	
	private final SessionTemplate getSession() {
		if(Objects.nonNull(session))
			return this.session;
		this.session = sessionFactory.createTemplate();
		return this.session;
	}
	

	public void process(Object f){
		if(Objects.isNull(f)){
			return;
		}
		try {
			function.accept(getSession(), f);
			afterFunctionHooks.forEach(h -> h.accept(getSession(), f));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(processQueue.isEmpty() || ++i % TransactionQueue.BATCH_SIZE == 0){
			i = 0;
			try {
				this.session.close();
				this.session = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the thread
	 */
	public Thread getThread() {
		return thread;
	}
}
