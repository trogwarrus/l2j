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
package org.l2jmobius.gameserver.network.serverpackets.autopeel;

import java.util.Collection;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExResultItemAutoPeel extends ServerPacket
{
	private final boolean _result;
	private final long _totalPeelCount;
	private final long _remainingPeelCount;
	private final Collection<ItemHolder> _itemList;
	
	public ExResultItemAutoPeel(boolean result, long totalPeelCount, long remainingPeelCount, Collection<ItemHolder> itemList)
	{
		_result = result;
		_totalPeelCount = totalPeelCount;
		_remainingPeelCount = remainingPeelCount;
		_itemList = itemList;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_RESULT_ITEM_AUTO_PEEL.writeId(this, buffer);
		buffer.writeByte(_result);
		buffer.writeLong(_totalPeelCount);
		buffer.writeLong(_remainingPeelCount);
		buffer.writeInt(_itemList.size());
		for (ItemHolder holder : _itemList)
		{
			buffer.writeInt(holder.getId());
			buffer.writeLong(holder.getCount());
			buffer.writeInt(0); // Announce level.
			buffer.writeByte(0); // Enchanted.
			buffer.writeByte(0); // Grade color.
		}
	}
}
