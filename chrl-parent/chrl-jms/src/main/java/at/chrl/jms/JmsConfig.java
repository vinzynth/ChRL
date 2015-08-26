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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.Enumeration;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

/**
 * @author Vinzynth
 * 24.08.2015 - 22:26:57
 *
 */
@EnableJms
@Configuration
public class JmsConfig {
	
	@Bean(destroyMethod = "stop")
	public BrokerService getActiveMQBrokerService() throws Exception{
		BrokerService broker = new BrokerService();
		broker.setPersistent(false);
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		while(e.hasMoreElements())
		{
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration<InetAddress> ee = n.getInetAddresses();
			while (ee.hasMoreElements()) {
				InetAddress i = (InetAddress) ee.nextElement();
				if (i instanceof Inet4Address){
					TransportConnector con = new TransportConnector();
					con.setUri(new URI("tcp://" + i.getHostAddress() + ":61616"));
					broker.addConnector(con);
				}
			}
		}
		broker.start();
		return broker;
	}
	
	@Bean
	public JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
		if(connectionFactory instanceof ActiveMQConnectionFactory){
			ActiveMQConnectionFactory activeMqConnectionFactory = (ActiveMQConnectionFactory) connectionFactory;
			activeMqConnectionFactory.setOptimizeAcknowledge(true);
			System.out.println("tweak me");
		}
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTypeIdPropertyName("jtype");
		factory.setMessageConverter(converter);
		factory.setConnectionFactory(connectionFactory);
		factory.setConcurrency("3-3000");
		factory.setPubSubDomain(false);
		factory.setReplyPubSubDomain(false);
		return factory;
	}
}
