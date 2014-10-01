/*
 *  This file is part of Aion-Finish <Ver:3.0>
 *
 *  Aion-Finish is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Aion-Finish is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Aion-Finish.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.iryna.implementation;

import java.nio.ByteBuffer;

import at.chrl.iryna.IrynaConfig.Iryna;
import at.chrl.iryna.implementation.packets.UnknownIncomingIrynaPacket;


/**
 * @author Vinzynth
 */
public class IrynaPacketHandler
{
	/**
	 * Reads one packet from given ByteBuffer
	 * 
	 * @param data
	 * @param client
	 * @return AionClientPacket object from binary data
	 */
	public static IrynaPacketIncoming handle(ByteBuffer data, IrynaConnection client)
	{
		int id = data.get() & 0xff;

		//TODO: implement packet handler
		
		return unknownPacket(data, client, id);
	}

	/**
	 * Logs unknown packet.
	 * 
	 * @param state
	 * @param id
	 */
	private static IrynaPacketIncoming unknownPacket(ByteBuffer data, IrynaConnection client, int id)
	{
		Iryna.err.println(String.format("Unknown packet recived from Aion client: 0x%02X clientIp=%s", id, client.getIP()));
		return new UnknownIncomingIrynaPacket(data, client, id);
	}
}
