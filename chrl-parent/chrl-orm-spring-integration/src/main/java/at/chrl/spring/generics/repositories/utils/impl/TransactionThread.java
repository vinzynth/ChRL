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

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import at.chrl.nutils.streams.InfiniteStreams;

/**
 * @author Vinzynth
 * 27.08.2015 - 20:02:04
 *
 */
@EnableTransactionManagement
public class TransactionThread {
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;
	private final Thread thread;

	/**
	 * 
	 */
	public TransactionThread(BlockingQueue<Function<EntityManager, ?>> processQueue) {
		thread = new Thread(() -> {
			InfiniteStreams.getQueueStream(processQueue).forEach(this::process);
		});
		getThread().setDaemon(true);
		getThread().start();
	}
	
	int i = 0;

	public void process(Function<EntityManager, ?> f){
		if(Objects.isNull(f))
			return;
		try {
			f.apply(entityManager);			
		} catch (Exception e) {
			renewTransaction();
			f.apply(entityManager);
		}
		if(i++ % 10 == 0)
			renewTransaction();
	}
	
	public void renewTransaction(){}

	/**
	 * @return the thread
	 */
	public Thread getThread() {
		return thread;
	}
}
