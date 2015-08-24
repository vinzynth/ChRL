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
import java.net.URI;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileSystemUtils;

/**
 * @author Vinzynth
 * 24.08.2015 - 22:28:30
 *
 */
@SpringBootApplication
public class JmsTest {

	public static void main(String[] args) throws Exception {
		 // Clean out any ActiveMQ data from a previous run
		FileSystemUtils.deleteRecursively(new File("activemq-data"));

		// Launch the application
//		ConfigurableApplicationContext context = SpringApplication.run(JmsTest.class, args);
		
		
		BrokerService broker = new BrokerService();
		broker.setPersistent(false);
		TransportConnector con = new TransportConnector();
		con.setUri(new URI("tcp://192.168.1.177:61616"));
		broker.addConnector(con);

		broker.start();
		
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.177:61616");
		Connection conn = activeMQConnectionFactory.createConnection();
		
		conn.start();

		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Create the destination (Topic or Queue)
		Destination destination = session.createQueue("mailbox-destination");

		// Create a MessageConsumer from the Session to the Topic or Queue
		MessageConsumer consumer = session.createConsumer(destination);

		// Wait for a message
		Message message = consumer.receive(100000);
		
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			System.out.println("Received: " + text);
		} else {
			System.out.println("Received: " + message);
		}
//		// Send a message
//		MessageCreator messageCreator = new MessageCreator() {
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				return session.createTextMessage("ping!");
//			}
//		};
//		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
//		System.out.println("Sending a new message.");
//		jmsTemplate.send("mailbox-destination", messageCreator);
	}
}
