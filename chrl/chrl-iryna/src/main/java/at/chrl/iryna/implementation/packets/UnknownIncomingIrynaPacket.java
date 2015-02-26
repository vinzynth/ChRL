/**
 * (C) ChRL 2014 - chrl-iryna - at.chrl.iryna.implementation.packets -
 * UnknownIncomingIrynaPacket.java Created: 02.08.2014 - 23:07:40
 */
package at.chrl.iryna.implementation.packets;

import java.nio.ByteBuffer;

import at.chrl.iryna.implementation.IrynaConnection;
import at.chrl.iryna.implementation.IrynaPacketIncoming;

/**
 * @author Vinzynth
 *
 */
public class UnknownIncomingIrynaPacket extends IrynaPacketIncoming {

	/**
	 * @param buf
	 * @param client
	 * @param opcode
	 */
	public UnknownIncomingIrynaPacket(ByteBuffer data, IrynaConnection client, int id) {
		super(data, client, id);
	}

	/**
	 * does nothing
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
	}

	/**
	 * does nothing
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
	}

}
