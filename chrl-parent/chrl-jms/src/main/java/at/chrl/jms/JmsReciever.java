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

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

/**
 * @author Vinzynth
 * 24.08.2015 - 22:27:05
 *
 */
@Component
public class JmsReciever {
	
	@Autowired
	ConfigurableApplicationContext context;

	/**
	 * When you receive a message, print it out, then shut down the application.
	 * Finally, clean up any ActiveMQ server stuff.
	 */
	@JmsListener(destination = "mailbox-destination")
	public void receiveMessage(String message) {
		System.out.println("Received <" + message + ">");
		context.close();
		FileSystemUtils.deleteRecursively(new File("activemq-data"));
	}
}
