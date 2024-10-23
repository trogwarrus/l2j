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

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.clan.ClanMember;
import org.l2jmobius.gameserver.model.clan.ClanPrivilege;
import org.l2jmobius.gameserver.network.SystemMessageId;

public class RequestGiveNickName extends ClientPacket
{
	private String _target;
	private String _title;
	
	@Override
	protected void readImpl()
	{
		_target = readString();
		_title = readString();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Noblesse can bestow a title to themselves
		if ((player.getNobleLevel() > 0) && _target.equalsIgnoreCase(player.getName()))
		{
			player.setTitle(_title);
			player.sendPacket(SystemMessageId.YOUR_TITLE_HAS_BEEN_CHANGED);
			player.broadcastTitleInfo();
		}
		else
		{
			// Can the player change/give a title?
			if (!player.hasClanPrivilege(ClanPrivilege.CL_MANAGE_TITLES))
			{
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			
			final Clan clan = player.getClan();
			if (clan.getLevel() < 3)
			{
				player.sendPacket(SystemMessageId.THE_TITLE_CHANGE_FUNCTION_IS_AVAILABLE_ONLY_IF_THE_CLAN_IS_LV_3_OR_HIGHER);
				return;
			}
			
			final ClanMember member1 = clan.getClanMember(_target);
			if (member1 != null)
			{
				final Player member = member1.getPlayer();
				if (member != null)
				{
					// is target from the same clan?
					member.setTitle(_title);
					member.sendPacket(SystemMessageId.YOUR_TITLE_HAS_BEEN_CHANGED);
					member.broadcastTitleInfo();
				}
				else
				{
					player.sendPacket(SystemMessageId.THAT_PLAYER_IS_NOT_ONLINE);
				}
			}
			else
			{
				player.sendPacket(SystemMessageId.THE_TARGET_MUST_BE_A_CLAN_MEMBER);
			}
		}
	}
}
