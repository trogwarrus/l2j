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
package ai.others.PrisonManager;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.instancemanager.ZoneManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.model.zone.ZoneType;
import org.l2jmobius.gameserver.model.zone.type.ScriptZone;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.prison.ExPrisonUserEnter;
import org.l2jmobius.gameserver.network.serverpackets.prison.ExPrisonUserExit;
import org.l2jmobius.gameserver.network.serverpackets.prison.ExPrisonUserInfo;

import ai.AbstractNpcAI;

/**
 * @author Fakee
 */
public class PrisonManager extends AbstractNpcAI
{
	// NPC
	private static final int MORENDO = 34637;
	private static final int POMPOSO = 34638;
	private static final int DOLLOROSO = 34639;
	private static final int DOLCE = 34640;
	private static final int CANTABILE = 34641;
	private static final int AMOROSO = 34642;
	private static final int DELICATO = 34643;
	private static final int COMODO = 34644;
	// Misc
	private static final ScriptZone PRISON_ZONE_1 = ZoneManager.getInstance().getZoneById(26010, ScriptZone.class);
	private static final ScriptZone PRISON_ZONE_2 = ZoneManager.getInstance().getZoneById(26011, ScriptZone.class);
	private static final ScriptZone PRISON_ZONE_3 = ZoneManager.getInstance().getZoneById(26012, ScriptZone.class);
	private static final ScriptZone PRISON_ENTER_ZONE = ZoneManager.getInstance().getZoneById(26013, ScriptZone.class);
	private static final ScriptZone PRISON_ENTER_ZONE2 = ZoneManager.getInstance().getZoneById(26014, ScriptZone.class);
	private static final Location EXIT_LOCATION1 = new Location(61072, -43395, -2992);
	private static final Location EXIT_LOCATION2 = new Location(59317, -43502, -2992);
	private static final Location EXIT_LOCATION3 = new Location(60026, -44630, -2992);
	
	public PrisonManager()
	{
		addFirstTalkId(MORENDO, POMPOSO, DOLLOROSO, DOLCE, CANTABILE, AMOROSO, DELICATO, COMODO);
		addTalkId(MORENDO, POMPOSO, DOLLOROSO, DOLCE, CANTABILE, AMOROSO, DELICATO, COMODO);
		addEnterZoneId(PRISON_ZONE_1.getId(), PRISON_ZONE_2.getId(), PRISON_ZONE_3.getId(), PRISON_ENTER_ZONE.getId(), PRISON_ENTER_ZONE2.getId());
		addExitZoneId(PRISON_ZONE_1.getId(), PRISON_ZONE_2.getId(), PRISON_ZONE_3.getId(), PRISON_ENTER_ZONE2.getId());
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34637.html":
			case "34637-no.html":
			case "34638.html":
			case "34638-no.html":
			case "34639.html":
			case "34639-no.html":
			case "34640.html":
			case "34640-no.html":
			case "34641.html":
			case "34642.html":
			case "34643.html":
			case "34644.html":
			{
				htmltext = event;
				break;
			}
			case "Prison2_GetPoint":
			{
				if (!PRISON_ZONE_2.isCharacterInZone(player))
				{
					break;
				}
				
				if (player.getVariables().getInt(PlayerVariables.PRISON_2_POINTS, 0) >= 108)
				{
					break;
				}
				
				player.getVariables().set(PlayerVariables.PRISON_2_POINTS, player.getVariables().getInt(PlayerVariables.PRISON_2_POINTS, 0) + 1);
				player.getVariables().storeMe();
				
				player.sendPacket(new ExPrisonUserInfo(player, 2));
				
				if (npc.getId() == 34640)
				{
					htmltext = "34640-no.html";
				}
				else if (npc.getId() == 34638)
				{
					htmltext = "34638-no.html";
				}
				
				npc.doDie(null);
				break;
			}
			case "Prison3_GetPoint":
			{
				if (!PRISON_ZONE_3.isCharacterInZone(player))
				{
					break;
				}
				
				if (player.getVariables().getInt(PlayerVariables.PRISON_3_POINTS, 0) >= 54)
				{
					break;
				}
				
				player.getVariables().set(PlayerVariables.PRISON_3_POINTS, player.getVariables().getInt(PlayerVariables.PRISON_3_POINTS, 0) + 1);
				player.getVariables().storeMe();
				
				player.sendPacket(new ExPrisonUserInfo(player, 3));
				
				if (npc.getId() == 34637)
				{
					htmltext = "34637-no.html";
				}
				else if (npc.getId() == 34639)
				{
					htmltext = "34639-no.html";
				}
				
				npc.doDie(null);
				break;
			}
			case "PRISON_ZONE_CHECK":
			{
				if (PRISON_ZONE_1.isCharacterInZone(player))
				{
					player.setReputation(21600);
					player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, 0);
					player.getVariables().storeMe();
					player.sendPacket(new ExPrisonUserInfo(player, 0));
					player.sendPacket(new ExPrisonUserExit());
					player.teleToLocation(EXIT_LOCATION1);
				}
				else if (PRISON_ZONE_2.isCharacterInZone(player))
				{
					if (player.getVariables().getInt(PlayerVariables.PRISON_2_POINTS, 0) >= 108)
					{
						player.setReputation(25200);
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, 0);
						player.getVariables().set(PlayerVariables.PRISON_2_POINTS, 0);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 0));
						player.sendPacket(new ExPrisonUserExit());
						player.teleToLocation(EXIT_LOCATION2);
					}
					else
					{
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, System.currentTimeMillis() + 300000);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 2));
						startQuestTimer("PRISON_ZONE_CHECK", 300000, null, player);
					}
				}
				else if (PRISON_ZONE_3.isCharacterInZone(player))
				{
					if (player.getVariables().getInt(PlayerVariables.PRISON_3_POINTS, 0) >= 54)
					{
						player.setReputation(28800);
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, 0);
						player.getVariables().set(PlayerVariables.PRISON_3_POINTS, 0);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 0));
						player.sendPacket(new ExPrisonUserExit());
						player.teleToLocation(EXIT_LOCATION3);
					}
					else
					{
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, System.currentTimeMillis() + 300000);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 3));
						startQuestTimer("PRISON_ZONE_CHECK", 300000, null, player);
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onEnterZone(Creature creature, ZoneType zone)
	{
		if (creature.isPlayer())
		{
			final Player player = creature.getActingPlayer();
			final long currentTime = System.currentTimeMillis();
			final int waitTime = (int) (player.getVariables().getLong(PlayerVariables.PRISON_WAIT_TIME, 0) / 1000);
			
			if (zone.getId() == PRISON_ZONE_1.getId())
			{
				if ((waitTime > 0) && ((currentTime / 1000) >= waitTime))
				{
					player.setReputation(21600);
					player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, 0);
					player.getVariables().storeMe();
					player.sendPacket(new ExPrisonUserInfo(player, 0));
					player.sendPacket(new ExPrisonUserExit());
					player.teleToLocation(EXIT_LOCATION1);
				}
				else
				{
					player.sendPacket(new ExPrisonUserInfo(player, 1));
					player.sendPacket(new ExPrisonUserEnter(1));
					player.sendPacket(SystemMessageId.THE_DEATH_MELODY_HAS_FOUND_A_WARRIOR_WHOSE_SOUL_IS_CURSED_AND_BROUGH_THEM_TO_THE_CURSED_VILLAGE);
					startQuestTimer("PRISON_ZONE_CHECK", player.getVariables().getLong(PlayerVariables.PRISON_WAIT_TIME, 0) - currentTime, null, player);
				}
			}
			else if (zone.getId() == PRISON_ZONE_2.getId())
			{
				if ((waitTime > 0) && ((currentTime / 1000) >= waitTime))
				{
					if (player.getVariables().getInt(PlayerVariables.PRISON_2_POINTS, 0) >= 108)
					{
						player.setReputation(25200);
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, 0);
						player.getVariables().set(PlayerVariables.PRISON_2_POINTS, 0);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 0));
						player.sendPacket(new ExPrisonUserExit());
						player.teleToLocation(EXIT_LOCATION2);
					}
					else
					{
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, currentTime + 300000);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 3));
						startQuestTimer("PRISON_ZONE_CHECK", 300000, null, player);
					}
				}
				else
				{
					player.sendPacket(new ExPrisonUserInfo(player, 2));
					player.sendPacket(new ExPrisonUserEnter(2));
					player.sendPacket(SystemMessageId.THE_DEATH_MELODY_HAS_FOUND_A_WARRIOR_WHOSE_SOUL_IS_CURSED_AND_BROUGH_THEM_TO_THE_CURSED_VILLAGE);
					startQuestTimer("PRISON_ZONE_CHECK", player.getVariables().getLong(PlayerVariables.PRISON_WAIT_TIME, 0) - currentTime, null, player);
				}
			}
			else if (zone.getId() == PRISON_ZONE_3.getId())
			{
				if ((waitTime > 0) && ((currentTime / 1000) >= waitTime))
				{
					if (player.getVariables().getInt(PlayerVariables.PRISON_3_POINTS, 0) >= 54)
					{
						player.setReputation(28800);
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, 0);
						player.getVariables().set(PlayerVariables.PRISON_3_POINTS, 0);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 0));
						player.sendPacket(new ExPrisonUserExit());
						player.teleToLocation(EXIT_LOCATION3);
					}
					else
					{
						player.getVariables().set(PlayerVariables.PRISON_WAIT_TIME, currentTime + 300000);
						player.getVariables().storeMe();
						player.sendPacket(new ExPrisonUserInfo(player, 3));
						startQuestTimer("PRISON_ZONE_CHECK", 300000, null, player);
					}
				}
				else
				{
					player.sendPacket(new ExPrisonUserInfo(player, 3));
					player.sendPacket(new ExPrisonUserEnter(3));
					player.sendPacket(SystemMessageId.THE_DEATH_MELODY_HAS_FOUND_A_WARRIOR_WHOSE_SOUL_IS_CURSED_AND_BROUGH_THEM_TO_THE_CURSED_VILLAGE);
					startQuestTimer("PRISON_ZONE_CHECK", player.getVariables().getLong(PlayerVariables.PRISON_WAIT_TIME, 0) - currentTime, null, player);
				}
			}
			else if (zone.getId() == PRISON_ENTER_ZONE.getId())
			{
				player.sendPacket(SystemMessageId.THIS_SWEET_SCENT_OF_BLOOD_SPILLED_ON_THE_GROUND_THOSE_SWEET_SCREAMS_OF_SOULS_THAT_PIERCE_THE_SILENCE);
			}
			else if (zone.getId() == PRISON_ENTER_ZONE2.getId())
			{
				player.sendPacket(SystemMessageId.NARCISSUS_IS_A_MIGHT_FALLEN_ANGEL_WHO_CAN_ENSLAVE_OTHER_WITH_HIS_SONGS);
			}
		}
		return super.onEnterZone(creature, zone);
	}
	
	@Override
	public String onExitZone(Creature creature, ZoneType zone)
	{
		if (creature.isPlayer())
		{
			final Player player = creature.getActingPlayer();
			if (player.getVariables().getLong(PlayerVariables.PRISON_WAIT_TIME, 0) > 0)
			{
				// Wait exit zone to finish and teleport player.
				ThreadPool.schedule(() ->
				{
					final int pkKills = player.getPkKills();
					if ((pkKills >= 10) && (pkKills < 35))
					{
						player.teleToLocation(61414, -42632, -2992);
					}
					else if ((pkKills >= 35) && (pkKills < 40))
					{
						player.teleToLocation(59147, -42547, -3000);
					}
					else if (pkKills >= 40)
					{
						player.teleToLocation(58969, -44995, -2992);
					}
				}, 100);
			}
			else if ((zone.getId() == PRISON_ZONE_1.getId()) || (zone.getId() == PRISON_ZONE_2.getId()) || (zone.getId() == PRISON_ZONE_3.getId()))
			{
				player.sendPacket(new ExPrisonUserInfo(player, 0));
				player.sendPacket(new ExPrisonUserExit());
			}
			else if (zone.getId() == PRISON_ENTER_ZONE2.getId())
			{
				player.sendPacket(SystemMessageId.GO_MY_LOYAL_SERVANTS_BRING_ME_THOSE_WHO_WALLOW_IN_SHAME_AND_CURSES);
			}
		}
		return super.onExitZone(creature, zone);
	}
	
	public static void main(String[] args)
	{
		new PrisonManager();
	}
}
