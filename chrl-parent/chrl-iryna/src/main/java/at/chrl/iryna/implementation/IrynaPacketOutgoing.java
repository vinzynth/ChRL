/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.iryna.implementation - IrynaPacket.java
 * Created: 29.07.2014 - 22:33:16
 */
package at.chrl.iryna.implementation;

import java.nio.ByteBuffer;

import at.chrl.nutils.network.packet.BaseServerPacket;

/**
 * @author Vinzynth
 *
 */
public abstract class IrynaPacketOutgoing extends BaseServerPacket {

	/**
	 * Constructs a new outgoing packet with specified id.
	 *
	 * @param opcode
	 *            packet opcode.
	 */
	protected IrynaPacketOutgoing(int opcode) {
		super(opcode);
	}

	/**
	 * Write this packet data for given connection, to given buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	public final void write(IrynaConnection con, ByteBuffer buf) {
		buf.putShort((short) 0);
		writeImpl(con, buf);
		buf.flip();
		buf.putShort((short) buf.limit());
		buf.position(0);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	protected abstract void writeImpl(IrynaConnection con, ByteBuffer buf);
}
