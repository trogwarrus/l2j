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
package org.l2jmobius.gameserver.network.clientpackets.homunculus;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExHomunculusCouponProbabilityList;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExHomunculusCreateProbabilityList;

/**
 * @author Index
 */
public class ExRequestHomunculusProbabilityList extends ClientPacket
{
	private int _type;
	private int _slotItemClassId;
	
	@Override
	protected void readImpl()
	{
		_type = readByte();
		_slotItemClassId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_type == 0)
		{
			player.sendPacket(new ExHomunculusCreateProbabilityList(player));
		}
		else
		{
			player.sendPacket(new ExHomunculusCouponProbabilityList(player, _slotItemClassId));
		}
	}
}
