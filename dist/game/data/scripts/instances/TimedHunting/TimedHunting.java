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
package instances.TimedHunting;

import org.l2jmobius.commons.util.CommonUtil;
import org.l2jmobius.gameserver.data.xml.TimedHuntingZoneData;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.holders.TimedHuntingZoneHolder;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import instances.AbstractInstance;

/**
 * @author Mobius, Tanatos
 */
public class TimedHunting extends AbstractInstance
{
	// NPCs
	// Fioren's Crystal Prison
	private static final int FIOREN = 34634;
	private static final int RESEARCHERS_TELEPORTER_105 = 34529; // Storm Isle
	private static final int RESEARCHERS_TELEPORTER_110 = 34618; // Isle of Prayer
	private static final int RESEARCHERS_TELEPORTER_115 = 34624; // Alligator Island
	private static final int RESEARCHERS_TELEPORTER_120 = 34682; // Frost Island
	// Jamoa's Camp
	private static final int JAMOA = 34635;
	private static final int EXPEDITION_TELEPORTER_105 = 34526; // Primeval Isle
	private static final int EXPEDITION_TELEPORTER_110 = 34626; // Swamp of Screams
	private static final int EXPEDITION_TELEPORTER_115 = 34628; // Blazing Swamp
	private static final int EXPEDITION_TELEPORTER_120 = 34684; // Wasteland
	// Pantheon's Museum
	private static final int PANTHEON = 34636;
	private static final int OVERSEER_TELEPORTER_105 = 34524; // Golden Altar
	private static final int OVERSEER_TELEPORTER_110 = 34630; // Mimir's Altar
	private static final int OVERSEER_TELEPORTER_115 = 34632; // Plunderous Plains
	private static final int OVERSEER_TELEPORTER_120 = 34686; // Elven Forest
	// Pantheon's Museum Monsters
	private static final int[] PANTHEON_MONSTERS =
	{
		24470,
		24471,
		24472,
		24473,
		24474,
		24475,
		24837,
		24838,
		24839,
		24840,
		24841,
		24842,
		24939,
		24940,
		24941,
		24942,
		24943,
		24944,
		24974,
		24975,
		24976,
		24977,
		24978,
		24979
	};
	// Abandoned Coal Mines
	private static final int INVESTIGATORS_TELEPORTER = 34551;
	// Imperial Tomb
	private static final int SEARCH_TEAM_TELEPORTER = 34552;
	// Ravaged Innadril
	private static final int LIONEL_HUNTER = 34646; // Heine
	private static final int PATROL_TELEPORTER_105 = 34568; // Field of Silence
	private static final int PATROL_TELEPORTER_115 = 34647; // Alligator Beach
	private static final int PATROL_TELEPORTER_120 = 34688; // Alligator Island
	// Otherworldly Atelia Refinery
	private static final int ATELIA_REFINERY_TELEPORT_DEVICE = 34583; // Otherworldly Atelia Refinery
	// Tower of Insolence
	private static final int TELEPORT_SCOUT = 34549; // Tower of Insolence
	private static final int SPACETEMPORAL_RIFT = 34616; // Tower of Insolence
	// Skills
	private static final SkillHolder MORE_ADENA = new SkillHolder(32930, 1);
	// Locations
	private static final Location FIELD_OF_SILENCE = new Location(95983, 170989, -3640);
	private static final Location FIELD_OF_WHISPERS = new Location(95981, 210144, -3456);
	private static final Location ALLIGATOR_BEACH = new Location(114572, 202589, -3408);
	private static final Location ALLIGATOR_ISLAND = new Location(121342, 185640, -3587);
	// Misc
	private static final int[] TEMPLATE_IDS =
	{
		1001, // Fioren's Crystal Prison
		1006, // Jamoa's Camp
		1007, // Pantheon's Museum
		1011, // Abandoned Coal Mines
		1012, // Imperial Tomb
		1013, // Ravaged Innadril
		1015, // Otherworldly Atelia Refinery
		1020, // Tower of Insolence
	};
	
	public TimedHunting()
	{
		super(TEMPLATE_IDS);
		addStartNpc(FIOREN, JAMOA, PANTHEON, INVESTIGATORS_TELEPORTER, SEARCH_TEAM_TELEPORTER, LIONEL_HUNTER, ATELIA_REFINERY_TELEPORT_DEVICE, TELEPORT_SCOUT);
		addFirstTalkId(FIOREN, JAMOA, PANTHEON, INVESTIGATORS_TELEPORTER, SEARCH_TEAM_TELEPORTER, LIONEL_HUNTER, ATELIA_REFINERY_TELEPORT_DEVICE, TELEPORT_SCOUT);
		addFirstTalkId(RESEARCHERS_TELEPORTER_105, RESEARCHERS_TELEPORTER_110, RESEARCHERS_TELEPORTER_115, RESEARCHERS_TELEPORTER_120);
		addFirstTalkId(EXPEDITION_TELEPORTER_105, EXPEDITION_TELEPORTER_110, EXPEDITION_TELEPORTER_115, EXPEDITION_TELEPORTER_120);
		addFirstTalkId(OVERSEER_TELEPORTER_105, OVERSEER_TELEPORTER_110, OVERSEER_TELEPORTER_115, OVERSEER_TELEPORTER_120);
		addFirstTalkId(INVESTIGATORS_TELEPORTER);
		addFirstTalkId(SEARCH_TEAM_TELEPORTER);
		addFirstTalkId(PATROL_TELEPORTER_105, PATROL_TELEPORTER_115, PATROL_TELEPORTER_120);
		addFirstTalkId(ATELIA_REFINERY_TELEPORT_DEVICE);
		addFirstTalkId(TELEPORT_SCOUT, SPACETEMPORAL_RIFT);
		addTalkId(FIOREN, JAMOA, PANTHEON, INVESTIGATORS_TELEPORTER, SEARCH_TEAM_TELEPORTER, LIONEL_HUNTER, ATELIA_REFINERY_TELEPORT_DEVICE, TELEPORT_SCOUT);
		addTalkId(RESEARCHERS_TELEPORTER_105, RESEARCHERS_TELEPORTER_110, RESEARCHERS_TELEPORTER_115, RESEARCHERS_TELEPORTER_120);
		addTalkId(EXPEDITION_TELEPORTER_105, EXPEDITION_TELEPORTER_110, EXPEDITION_TELEPORTER_115, EXPEDITION_TELEPORTER_120);
		addTalkId(OVERSEER_TELEPORTER_105, OVERSEER_TELEPORTER_110, OVERSEER_TELEPORTER_115, OVERSEER_TELEPORTER_120);
		addTalkId(INVESTIGATORS_TELEPORTER);
		addTalkId(SEARCH_TEAM_TELEPORTER);
		addTalkId(PATROL_TELEPORTER_105, PATROL_TELEPORTER_115, PATROL_TELEPORTER_120);
		addTalkId(ATELIA_REFINERY_TELEPORT_DEVICE);
		addTalkId(TELEPORT_SCOUT, SPACETEMPORAL_RIFT);
		addKillId(PANTHEON_MONSTERS);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (event.startsWith("ENTER"))
		{
			final int zoneId = Integer.parseInt(event.split(" ")[1]);
			final TimedHuntingZoneHolder huntingZone = TimedHuntingZoneData.getInstance().getHuntingZone(zoneId);
			if (huntingZone == null)
			{
				return null;
			}
			
			if (huntingZone.isSoloInstance())
			{
				enterInstance(player, npc, huntingZone.getInstanceId());
			}
			else
			{
				Instance world = null;
				for (Instance instance : InstanceManager.getInstance().getInstances())
				{
					if (instance.getTemplateId() == huntingZone.getInstanceId())
					{
						world = instance;
						break;
					}
				}
				
				if (world == null)
				{
					world = InstanceManager.getInstance().createInstance(huntingZone.getInstanceId(), player);
				}
				
				player.teleToLocation(huntingZone.getEnterLocation(), world);
			}
		}
		
		if (event.equals("toFieldOfSilence"))
		{
			player.teleToLocation(FIELD_OF_SILENCE);
		}
		else if (event.equals("toFieldOfWhispers"))
		{
			if (player.getLevel() < 110)
			{
				return "34646-1.html";
			}
			player.teleToLocation(FIELD_OF_WHISPERS);
		}
		else if (event.equals("toAlligatorBeach"))
		{
			if (player.getLevel() < 115)
			{
				return "34646-1.html";
			}
			player.teleToLocation(ALLIGATOR_BEACH);
		}
		else if (event.equals("toAlligatorIsland"))
		{
			if (player.getLevel() < 120)
			{
				return "34646-1.html";
			}
			player.teleToLocation(ALLIGATOR_ISLAND);
		}
		
		return null;
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isPet)
	{
		final Instance world = npc.getInstanceWorld();
		if (isInInstance(world) && CommonUtil.contains(PANTHEON_MONSTERS, npc.getId()) && (getRandom(100) < 5) && (!killer.isAffectedBySkill(MORE_ADENA.getSkillId())))
		{
			MORE_ADENA.getSkill().applyEffects(killer, killer);
		}
		return super.onKill(npc, killer, isPet);
	}
	
	public static void main(String[] args)
	{
		new TimedHunting();
	}
}
