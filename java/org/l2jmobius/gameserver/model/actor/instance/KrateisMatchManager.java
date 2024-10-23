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
package org.l2jmobius.gameserver.model.actor.instance;

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.instancemanager.games.KrateisCubeManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.templates.NpcTemplate;
import org.l2jmobius.gameserver.model.krateisCube.KrateiArena;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ExPVPMatchCCRetire;

/**
 * @author LordWinter
 */
public class KrateisMatchManager extends Folk
{
	private int _arenaId = 0;
	
	public KrateisMatchManager(NpcTemplate template)
	{
		super(template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		final KrateiArena arena = KrateisCubeManager.getInstance().getArenaId(getArenaId());
		if (command.startsWith("TeleToArena"))
		{
			if ((arena != null) && arena.isRegisterPlayer(player))
			{
				if (arena.isActiveNow())
				{
					player.teleToLocation(arena.getBattleLoc().get(Rnd.get(arena.getBattleLoc().size())), true);
					arena.addEffects(player);
				}
				else
				{
					showChatWindow(player, "data/html/krateisCube/" + getId() + "-01.htm");
				}
			}
			else
			{
				player.sendPacket(SystemMessageId.YOU_CANNOT_ENTER_AS_YOU_DON_T_MEET_THE_REQUIREMENTS);
				player.teleToLocation(-70381, -70937, -1428, 0, true);
			}
		}
		else if (command.startsWith("TeleFromArena"))
		{
			if ((arena != null) && arena.isRegisterPlayer(player))
			{
				arena.removePlayer(player);
				player.stopAllEffects();
				player.sendPacket(ExPVPMatchCCRetire.STATIC);
				player.broadcastStatusUpdate();
				player.broadcastUserInfo();
				player.getSummonedNpcs().forEach(summon -> stopAllEffects());
			}
			player.teleToLocation(-70381, -70937, -1428, 0, true);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public String getHtmlPath(int npcId, int value, Player player)
	{
		String pom = "";
		
		if (value == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-0" + value;
		}
		
		return "data/html/krateisCube/" + pom + ".htm";
	}
	
	public void setArenaId(int id)
	{
		_arenaId = id;
	}
	
	public int getArenaId()
	{
		return _arenaId;
	}
}
