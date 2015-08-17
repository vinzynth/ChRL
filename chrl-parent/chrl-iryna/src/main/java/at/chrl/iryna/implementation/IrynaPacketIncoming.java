/**
 * (C) ChRL 2014 - chrl-iryna - at.chrl.iryna.implementation -
 * IrynaPacketIncoming.java Created: 02.08.2014 - 22:55:30
 */
package at.chrl.iryna.implementation;

import java.nio.ByteBuffer;

import at.chrl.iryna.IrynaConfig.Iryna;
import at.chrl.nutils.network.packet.BaseClientPacket;

/**
 * @author Vinzynth
 *
 */
public abstract class IrynaPacketIncoming extends BaseClientPacket<IrynaConnection> {

	/**
	 * @param buf
	 * @param opcode
	 */
	public IrynaPacketIncoming(ByteBuffer buf, IrynaConnection client, int opcode) {
		super(buf, opcode);
		setConnection(client);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			runImpl();
		} catch (Throwable e) {
			Iryna.PACKET_LOG.println("error handling gs (" + getConnection().getIP() + ") message " + this);
			e.printStackTrace(Iryna.PACKET_LOG);
		}
	}

	/**
	 * Send new GsServerPacket to connection that is owner of this packet. This
	 * method is equivalent to: getConnection().sendPacket(msg);
	 * 
	 * @param msg
	 */
	protected void sendPacket(IrynaPacketOutgoing msg) {
		getConnection().sendPacket(msg);
	}
}
