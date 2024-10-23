/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.network.clientpackets;

/**
 * @author NviX
 */
public class RequestAutoUse extends ClientPacket
{
	@Override
	protected void readImpl()
	{
		// final int unk1 = readByte(); // C - true. This is summary amount of next data received.
		// PacketLogger.info("received packet RequestAutoUse with unk1:" + unk1);
		// final int unk2 = readByte();
		// PacketLogger.info("and unk2: " + unk2);
		// final int unk3 = readByte(); // Can target mobs, that attacked by other players?
		// PacketLogger.info("and unk3: " + unk3);
		// final int unk4 = readByte(); // Auto pickup?
		// PacketLogger.info("and unk4: " + unk4);
		// final int unk5 = readByte();
		// PacketLogger.info("and unk5: " + unk5);
		// final int unk6 = readByte();
		// PacketLogger.info("and unk6: " + unk6);
		// final int unk7 = readByte(); // short range :1; long: 0
		// PacketLogger.info("and unk7: " + unk7);
		// final int unk8 = readByte(); // received 51 when logged in game...
		// PacketLogger.info("and unk8: " + unk8);
		// final int unk9 = readByte();
		// PacketLogger.info("and unk9: " + unk9);
		// final int unk10 = readByte();
		// PacketLogger.info("and unk10: " + unk10);
		// final int unk11 = readByte();
		// PacketLogger.info("and unk11: " + unk11);
		// final int unk12 = readByte(); // enable/ disable?
		// PacketLogger.info("and unk12: " + unk12);
	}
	
	@Override
	protected void runImpl()
	{
	}
}