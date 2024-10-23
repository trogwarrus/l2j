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
package org.l2jmobius.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.Henna;
import org.l2jmobius.gameserver.model.stats.BaseStat;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * This server packet sends the player's henna information using the Game Master's UI.
 * @author KenM, Zoey76
 */
public class GMHennaInfo extends ServerPacket
{
	private final Player _player;
	private final List<Henna> _hennas = new ArrayList<>();
	
	public GMHennaInfo(Player player)
	{
		_player = player;
		for (int i = 1; i < 4; i++)
		{
			if (player.getHenna(i) != null)
			{
				_hennas.add(player.getHenna(i));
			}
		}
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.GMHENNA_INFO.writeId(this, buffer);
		buffer.writeShort(_player.getHennaValue(BaseStat.INT)); // equip INT
		buffer.writeShort(_player.getHennaValue(BaseStat.STR)); // equip STR
		buffer.writeShort(_player.getHennaValue(BaseStat.CON)); // equip CON
		buffer.writeShort(_player.getHennaValue(BaseStat.MEN)); // equip MEN
		buffer.writeShort(_player.getHennaValue(BaseStat.DEX)); // equip DEX
		buffer.writeShort(_player.getHennaValue(BaseStat.WIT)); // equip WIT
		buffer.writeShort(_player.getHennaValue(BaseStat.LUC)); // equip LUC
		buffer.writeShort(_player.getHennaValue(BaseStat.CHA)); // equip CHA
		buffer.writeInt(3); // Slots
		buffer.writeInt(_hennas.size()); // Size
		for (Henna henna : _hennas)
		{
			buffer.writeInt(henna.getDyeId());
			buffer.writeInt(1);
		}
		if (_player.getHenna(4) != null)
		{
			buffer.writeInt(_player.getHenna(4).getDyeId());
			buffer.writeInt(_player.getHenna(4).getDuration()); // Premium Slot Dye Time Left
			buffer.writeInt(_player.getHenna(4).isAllowedClass(_player.getClassId()));
		}
		else
		{
			buffer.writeInt(0); // Premium Slot Dye ID
			buffer.writeInt(0); // Premium Slot Dye Time Left
			buffer.writeInt(0); // Premium Slot Dye ID isValid
		}
	}
}
