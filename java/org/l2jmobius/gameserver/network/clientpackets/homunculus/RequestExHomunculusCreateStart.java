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

import org.l2jmobius.gameserver.data.xml.HomunculusCreationData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExHomunculusCreateStartResult;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExShowHomunculusBirthInfo;

/**
 * @author Mobius
 */
public class RequestExHomunculusCreateStart extends ClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final int fee = Math.toIntExact(HomunculusCreationData.getInstance().getDefaultTemplate().getItemFee().get(0).getCount());
		if (player.getAdena() < fee)
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_ADENA_2);
			return;
		}
		
		player.reduceAdena("Homunculus creation", fee, player, true);
		player.getVariables().set(PlayerVariables.HOMUNCULUS_CREATION_TIME, System.currentTimeMillis() + HomunculusCreationData.getInstance().getDefaultTemplate().getCreationTime());
		
		player.sendPacket(new ExShowHomunculusBirthInfo(player));
		player.sendPacket(new ExHomunculusCreateStartResult(player));
	}
}
