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

import java.net.URI;

import javax.jms.ConnectionFactory;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

/**
 * @author Vinzynth
 * 24.08.2015 - 22:26:57
 *
 */
@EnableJms
@Configuration
public class JmsConfig {
	
	@Bean
	public BrokerService getActiveMQBrokerService() throws Exception{
		BrokerService broker = new BrokerService();
		broker.setPersistent(false);
		TransportConnector con = new TransportConnector();
		con.setUri(new URI("tcp://192.168.1.177:61616"));
		broker.addConnector(con);
		broker.start();
		return broker;
	}
	
	@Bean
	public JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
//		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setConcurrency("3-3000");
		return factory;
	}
}
