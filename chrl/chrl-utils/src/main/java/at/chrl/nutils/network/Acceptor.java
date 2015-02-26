package at.chrl.nutils.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * This class represents an <code>Acceptor</code> that will accept sockets<br>
 * connections dispatched by Accept <code>Dispatcher</code>.
 * <code>Acceptor</code> is attachment<br>
 * of <code>ServerSocketChannel</code> <code>SelectionKey</code> registered on
 * Accept <code>Dispatcher</code> <code>Selector</code>.<br>
 * <code>Acceptor</code> will create new <code>AConnection</code> object using
 * <code>ConnectionFactory.create(SocketChannel socket)</code><br>
 * representing accepted socket, register it into one of ReadWrite
 * <code>Dispatcher</code><br>
 * <code>Selector</code> as ready for io read operations.<br>
 * 
 * @author -Nemesiss-
 * @see at.chrl.nutils.network.Dispatcher
 * @see java.nio.channels.ServerSocketChannel
 * @see java.nio.channels.SelectionKey
 * @see java.nio.channels.SocketChannel
 * @see java.nio.channels.Selector
 * @see at.chrl.nutils.network.AConnection
 * @see at.chrl.nutils.network.ConnectionFactory
 * @see at.chrl.nutils.network.NioServer
 */
public class Acceptor {
	/**
	 * <code>ConnectionFactory</code> that will create new
	 * <code>AConnection</code>
	 * 
	 * @see at.chrl.nutils.network.ConnectionFactory
	 * @see at.chrl.nutils.network.AConnection
	 */
	private final ConnectionFactory factory;

	/**
	 * <code>NioServer</code> that created this Acceptor.
	 * 
	 * @see at.chrl.nutils.network.NioServer
	 */
	private final NioServer nioServer;

	/**
	 * Constructor that accept <code>ConnectionFactory</code> and
	 * <code>NioServer</code> as parameter<br>
	 * 
	 * @param factory
	 *            <code>ConnectionFactory</code> that will be used to<br>
	 * @param nioServer
	 *            <code>NioServer</code> that created this Acceptor object<br>
	 *            creating new <code>AConnection</code> instances.
	 * @see at.chrl.nutils.network.ConnectionFactory
	 * @see at.chrl.nutils.network.NioServer
	 * @see at.chrl.nutils.network.AConnection
	 */
	Acceptor(ConnectionFactory factory, NioServer nioServer) {
		this.factory = factory;
		this.nioServer = nioServer;
	}

	/**
	 * Method called by Accept <code>Dispatcher</code> <code>Selector</code>
	 * when socket<br>
	 * connects to <code>ServerSocketChannel</code> listening for connections.<br>
	 * New instance of <code>AConnection</code> will be created by
	 * <code>ConnectionFactory</code>,<br>
	 * socket representing accepted connection will be register into<br>
	 * one of ReadWrite <code>Dispatchers</code>
	 * <code>Selector as ready for io read operations.<br>
	 * 
	 * @param key
	 *            <code>SelectionKey</code> representing
	 *            <code>ServerSocketChannel</code> that is accepting<br>
	 *            new socket connection.
	 * @throws IOException
	 * @see at.chrl.nutils.network.Dispatcher
	 * @see java.nio.channels.ServerSocketChannel
	 * @see java.nio.channels.SelectionKey
	 * @see java.nio.channels.SocketChannel
	 * @see java.nio.channels.Selector
	 * @see at.chrl.nutils.network.AConnection
	 * @see at.chrl.nutils.network.ConnectionFactory
	 */
	public final void accept(SelectionKey key) throws IOException {
		/**
		 * For an accept to be pending the channel must be a server socket
		 * channel.
		 */
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		/** Accept the connection and make it non-blocking */
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		Dispatcher dispatcher = nioServer.getReadWriteDispatcher();
		factory.create(socketChannel, dispatcher);
	}
}
