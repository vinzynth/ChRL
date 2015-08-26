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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.util.FileSystemUtils;

import at.chrl.nutils.DatasetGenerator;

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
		ConfigurableApplicationContext context = SpringApplication.run(JmsTest.class, args);
		
		DatasetGenerator dataGen = new DatasetGenerator();
		
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTypeIdPropertyName("jtype");
        jmsTemplate.setMessageConverter(converter);
//        jmsTemplate.setDefaultDestinationName("chrl-jms-test");
        
        System.out.println("Sending a new message.");
        jmsTemplate.convertAndSend("chrl-jms-test", dataGen.generate(TestObject.class));
        System.out.println("Sent a new message.");
        
        
	}
}
