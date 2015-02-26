/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.iryna.implementation -
 * IrynaConnection.java Created: 29.07.2014 - 22:20:00
 */
package at.chrl.iryna.implementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.PriorityBlockingQueue;

import at.chrl.iryna.ConnectionState;
import at.chrl.iryna.IrynaConfig;
import at.chrl.iryna.IrynaConfig.Iryna;
import at.chrl.nutils.network.AConnection;
import at.chrl.nutils.network.Dispatcher;
import at.chrl.nutils.network.PacketProcessor;

/**
 * @author Vinzynth
 *
 */
public class IrynaConnection extends AConnection {

	private ConnectionState state = ConnectionState.DISCONNECTED;

	private final PriorityBlockingQueue<IrynaPacketOutgoing> messagesToSend = new PriorityBlockingQueue<>();

	private final static PacketProcessor<IrynaConnection> processor = new PacketProcessor<IrynaConnection>(IrynaConfig.PACKET_PROCESSOR_MIN_THREADS, IrynaConfig.PACKET_PROCESSOR_MAX_THREADS);

	/**
	 * @param sc
	 * @param d
	 * @throws IOException
	 */
	public IrynaConnection(SocketChannel sc, Dispatcher d) throws IOException {
		super(sc, d);
		this.state = ConnectionState.CONNECTED;

		Iryna.out.println("[Iryna] Connection from: " + getIP());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.network.AConnection#processData(java.nio.ByteBuffer)
	 */
	@Override
	protected boolean processData(ByteBuffer data) {

		IrynaPacketIncoming packet = IrynaPacketHandler.handle(data, this);
		Iryna.PACKET_LOG.println("[Iryna] " + getIP() + " - Received Packet: " + packet.toString());

		if (packet.read())
			processor.executePacket(packet);

		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.network.AConnection#writeData(java.nio.ByteBuffer)
	 */
	@Override
	protected boolean writeData(ByteBuffer data) {
		IrynaPacketOutgoing packet = messagesToSend.poll();
		if (packet == null)
			return false;

		packet.write(this, data);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.network.AConnection#getDisconnectionDelay()
	 */
	@Override
	protected long getDisconnectionDelay() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.network.AConnection#onDisconnect()
	 */
	@Override
	protected void onDisconnect() {
		Iryna.out.println("[Iryna] Disconnected: " + getIP());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see at.chrl.nutils.network.AConnection#onServerClose()
	 */
	@Override
	protected void onServerClose() {
		close(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[Iryna] " + getIP();
	}

	/**
	 * @return the state
	 */
	public final ConnectionState getState() {
		return state;
	}

	/**
	 * @param msg
	 */
	public final void sendPacket(IrynaPacketOutgoing msg) {
		/**
		 * Connection is already closed or waiting for last (close packet) to be
		 * sent
		 */
		if (isWriteDisabled())
			return;

		Iryna.PACKET_LOG.println(String.format("[Iryna] sending packet: %s", msg));

		messagesToSend.add(msg);
		enableWriteInterest();
	}

	public void close(boolean forced) {
		close(null, forced); // TODO create close Packet
	}

	/**
	 * Its guaranted that closePacket will be sent before closing connection,
	 * but all past and future packets wont. Connection will be closed [by
	 * Dispatcher Thread], and onDisconnect() method will be called to clear all
	 * other things. forced means that server shouldn't wait with removing this
	 * connection.
	 * 
	 * @param closePacket
	 *            Packet that will be send before closing.
	 * @param forced
	 *            have no effect in this implementation.
	 */
	public void close(IrynaPacketOutgoing closePacket, boolean forced) {
		if (isWriteDisabled())
			return;

		Iryna.PACKET_LOG.println("[Iryna] sending packet: " + closePacket + " and closing connection after that.");

		pendingClose = true;
		isForcedClosing = forced;
		messagesToSend.clear();
		messagesToSend.add(closePacket);
		enableWriteInterest();
	}
}
