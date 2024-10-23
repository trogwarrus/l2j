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
import org.l2jmobius.gameserver.model.homunculus.Homunculus;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExDeleteHomunculusDataResult;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExHomunculusPointInfo;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExShowHomunculusList;

/**
 * @author Mobius
 */
public class RequestExDeleteHomunculusData extends ClientPacket
{
	private int _slot;
	
	@Override
	protected void readImpl()
	{
		_slot = readInt(); // Position?
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Homunculus homunculus = player.getHomunculusList().get(_slot);
		if (homunculus.isActive())
		{
			player.sendPacket(SystemMessageId.A_HOMUNCULUS_CAN_T_BE_DESTROYED_IF_THERE_ARE_ESTABLISHED_RELATIONS_WITH_IT_BREAK_THE_RELATIONS_AND_TRY_AGAIN);
			return;
		}
		
		if (player.getHomunculusList().remove(homunculus))
		{
			long evolutionPoints = player.getVariables().getLong(PlayerVariables.HOMUNCULUS_EVOLUTION_POINTS, 0);
			
			switch (homunculus.getType())
			{
				case 0:
					evolutionPoints += 1;
					break;
				case 1:
					evolutionPoints += 15;
					break;
				case 2:
					evolutionPoints += 500;
					break;
				case 3:
					evolutionPoints += 1000;
					break;
				default:
					break;
			}
			
			player.getVariables().set(PlayerVariables.HOMUNCULUS_EVOLUTION_POINTS, evolutionPoints);
			player.sendPacket(new ExHomunculusPointInfo(player));
			player.sendPacket(new ExDeleteHomunculusDataResult());
			player.sendPacket(new ExShowHomunculusList(player));
		}
	}
}
