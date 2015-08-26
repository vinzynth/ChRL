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
package at.chrl.jms;

import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;

/**
 * @author Vinzynth
 * 24.08.2015 - 22:27:05
 *
 */
public interface JmsNode<T> {
	
	/**
	 * Destination endpoint
	 * @return
	 */
	public String getDestinationName();

	/**
	 * Recieves message T and respones with return value 
	 * 
	 * @param message
	 * @return
	 */
	public JmsResponse<Message<T>> processMessage(T message) throws Exception;
}
