package at.chrl.nutils.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * This interface defines a factory for connection implementations.<br>
 * It is used by the class {@link at.chrl.nutils.network.Acceptor Acceptor} to
 * create actual connection implementations.<br>
 * 
 * @author -Nemesiss-
 * @see at.chrl.nutils.network.Acceptor
 * 
 */
public interface ConnectionFactory {
	/**
	 * Create a new {@link at.chrl.nutils.network.AConnection AConnection}
	 * instance.<br>
	 * 
	 * @param socket
	 *            that new {@link at.chrl.nutils.network.AConnection
	 *            AConnection} instance will represent.<br>
	 * @param dispatcher
	 *            to wich new connection will be registered.<br>
	 * @return a new instance of {@link at.chrl.nutils.network.AConnection
	 *         AConnection}<br>
	 * @throws IOException
	 * @see at.chrl.nutils.network.AConnection
	 * @see at.chrl.nutils.network.Dispatcher
	 */
	public AConnection create(SocketChannel socket, Dispatcher dispatcher) throws IOException;
}
