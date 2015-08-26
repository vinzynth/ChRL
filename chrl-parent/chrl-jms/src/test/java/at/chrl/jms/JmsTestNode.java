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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import at.chrl.nutils.DatasetGenerator;
import at.chrl.nutils.Rnd;

/**
 * @author Vinzynth
 * 26.08.2015 - 01:15:52
 *
 */
@Component
public class JmsTestNode implements JmsNode<TestObject> {
	
	public static final String DESTINATION_NAME = "chrl-jms-test";
	
	public DatasetGenerator dataGen = new DatasetGenerator();
	
	@Autowired
	private ConfigurableApplicationContext context;
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.jms.JmsNode#getDestination()
	 */
	@Override
	public String getDestinationName() {
		return DESTINATION_NAME;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.jms.JmsNode#processMessage(java.lang.Object)
	 */
	@Override
	@JmsListener(destination = DESTINATION_NAME, containerFactory = "myJmsContainerFactory")
	public JmsResponse<Message<TestObject>> processMessage(TestObject message) throws Exception {
		System.out.println(message);
		
		Message<TestObject> build = MessageBuilder.withPayload(dataGen.generate(TestObject.class)).build();
		
		if(Rnd.nextBoolean()){
			context.close();
			return null;
		}
		return JmsResponse.forQueue(build, getDestinationName());
	}
	

}
