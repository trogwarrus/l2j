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
package org.l2jmobius.gameserver.network.clientpackets.mablegame;

import org.l2jmobius.gameserver.data.xml.MableGameData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.mablegame.ExMableGamePrison;
import org.l2jmobius.gameserver.network.serverpackets.mablegame.ExMableGameShowPlayerState;

public class ExRequestMableGameOpen extends ClientPacket
{
	@Override
	public void readImpl()
	{
	}
	
	@Override
	public void runImpl()
	{
		if (!MableGameData.getInstance().isEnabled())
		{
			return;
		}
		
		final Player player = getClient().getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(new ExMableGameShowPlayerState(player));
		
		final MableGameData.MableGamePlayerState playerState = MableGameData.getInstance().getPlayerState(player.getAccountName());
		if (playerState.getRemainingPrisonRolls() > 0)
		{
			player.sendPacket(new ExMableGamePrison(MableGameData.MIN_PRISON_DICE, MableGameData.MAX_PRISON_DICE, playerState.getRemainingPrisonRolls()));
		}
	}
}
