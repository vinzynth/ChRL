/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.iryna.implementation - IrynaConnectionFactoryImpl.java
 * Created: 29.07.2014 - 22:18:52
 */
package at.chrl.iryna.implementation;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import at.chrl.nutils.network.AConnection;
import at.chrl.nutils.network.ConnectionFactory;
import at.chrl.nutils.network.Dispatcher;

/**
 * @author Vinzynth
 *
 */
public class IrynaConnectionFactoryImpl implements ConnectionFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AConnection create(SocketChannel socket, Dispatcher dispatcher) throws IOException {
		return new IrynaConnection(socket, dispatcher);
	}
}
