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
package ai.areas.Conquest.ConquestTeleportDevice;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.instancemanager.RankManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.ClanMember;

import ai.AbstractNpcAI;

/**
 * Teleport Device AI.
 * @author CostyKiller
 */
public class ConquestTeleportDevice extends AbstractNpcAI
{
	// NPCs
	private static final int DEVICE1 = 34596; // Teleport Device 1 ( Conquest Town Teleporter)
	private static final int DEVICE2 = 34597; // Teleport Device 2 ( Water Side Teleporter)
	private static final int DEVICE3 = 34661; // Teleport Device 3 ( Fire Side Teleporter)
	
	// Enter Locations
	private static final Location[] ENTER_LOCS =
	{
		// Water Area
		new Location(-525, -201894, -3016), // Fire & Water Border (Water Side)
		// Fire Area
		new Location(446, -201800, -3000), // Fire & Water Border (Fire Side)
	};
	
	// Hunt Locations
	private static final Location[] HUNT_LOCS =
	{
		// Water Areas
		new Location(-16330, -189326, -4005), // Conquest Town
		new Location(-10724, -200409, -3468), // Zone 1 - Asa
		new Location(-28380, -214417, -3200), // Zone 2 - Anima
		new Location(-2570, -213261, -3603), // Zone 3 - Nox
		new Location(-11731, -215556, -2800), // Zone 4 - Callide Hall
		new Location(-24036, -220963, -3511), // Eigis Seat
		// Fire Areas
		new Location(7515, -202872, -3184), // Fire Fortress
		new Location(21937, -225270, -3800), // Kellond's Secret Barracks
		new Location(10256, -217176, -3568), // Zone 1 - Vita (Lv. 124)
		new Location(16126, -204634, -3892), // Zone 2 - Ignis (Lv. 128)
		new Location(13359, -208806, -4116), // Fire Source (Common Area)
		new Location(17955, -215075, -4174), // Fire Source (Central Area)
	};
	
	private ConquestTeleportDevice()
	{
		addTalkId(DEVICE1, DEVICE2, DEVICE3);
		addFirstTalkId(DEVICE1, DEVICE2, DEVICE3);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34596-01.html":
			case "34597-01.html":
			case "34661-01.html":
			{
				htmltext = event;
				break;
			}
			case "checkPrivs":
			{
				checkPrivs(player);
				break;
			}
			case "waterZone0":
			{
				player.teleToLocation(ENTER_LOCS[0], 0, player.getInstanceWorld());
				break;
			}
			case "fireZone0":
			{
				player.teleToLocation(ENTER_LOCS[1], 0, player.getInstanceWorld());
				break;
			}
			case "waterZone1":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[0], 0, player.getInstanceWorld());
				}
				break;
			}
			case "waterZone2":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[1], 0, player.getInstanceWorld());
				}
				break;
			}
			case "waterZone3":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[2], 0, player.getInstanceWorld());
				}
				break;
			}
			case "waterZone4":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[3], 0, player.getInstanceWorld());
				}
				break;
			}
			case "waterZone5":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[4], 0, player.getInstanceWorld());
				}
				break;
			}
			case "waterZone6":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[5], 0, player.getInstanceWorld());
				}
				break;
			}
			case "fireZone1":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[6], 0, player.getInstanceWorld());
				}
				break;
			}
			case "fireZone2":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[7], 0, player.getInstanceWorld());
				}
				break;
			}
			case "fireZone3":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[8], 0, player.getInstanceWorld());
				}
				break;
			}
			case "fireZone4":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[9], 0, player.getInstanceWorld());
				}
				break;
			}
			case "fireZone5":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[10], 0, player.getInstanceWorld());
				}
				break;
			}
			case "fireZone6":
			{
				if (checkPrivs(player))
				{
					player.teleToLocation(HUNT_LOCS[11], 0, player.getInstanceWorld());
				}
				break;
			}
		}
		return htmltext;
	}
	
	private boolean checkPrivs(Player player)
	{
		final int prevSeasonRank1Id = RankManager.getInstance().getPreviousConquestRankList().get(1) != null ? RankManager.getInstance().getPreviousConquestRankList().get(1).getInt("charId") : 0;
		final int currentSeasonRank1Id = RankManager.getInstance().getCurrentConquestRankList().get(1) != null ? RankManager.getInstance().getCurrentConquestRankList().get(1).getInt("charId") : 0;
		if (Config.CONQUEST_TELEPORTS_FOR_ALL)
		{
			player.sendMessage("You are free to use this teleport device.");
			return true;
		}
		// Check if rank 1 player id is in checker clan or not
		else if (player.getClan() != null)
		{
			for (ClanMember member : player.getClan().getMembers())
			{
				if ((member.getObjectId() == prevSeasonRank1Id) || (member.getObjectId() == (currentSeasonRank1Id)))
				{
					player.sendMessage("You are free to use this teleport device.");
					return true;
				}
				player.sendMessage("You can't use this teleport device, because you don't belong to the conquest conqueror clan.");
				return false;
			}
		}
		// If no clan
		player.sendMessage("You can't use this teleport device, because you don't belong to the conquest conqueror clan.");
		return false;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return npc.getId() + ".html";
	}
	
	public static void main(String[] args)
	{
		new ConquestTeleportDevice();
	}
}
