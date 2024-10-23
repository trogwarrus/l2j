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
package ai.areas.Conquest;

import java.util.HashSet;
import java.util.Set;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * Conquest Area Special Mobs AI.
 * @URL https://l2central.info/main/locations/activity/conquest/
 * @author CostyKiller
 */
public final class ConquestAreasSpecialMobs extends AbstractNpcAI
{
	// Special Mobs
	// Water zones
	private static final int SOUL_FLOWER_DWS = 19816; // Soul Flower Daril's Water Source
	private static final int SOUL_FLOWER_DPWS = 19817; // Soul Flower Daril Phran's Water Source
	private static final int SOUL_FLOWER_ASA_1 = 19818; // Soul Flower Asa Region 1
	private static final int SOUL_FLOWER_ASA_2 = 19819; // Soul Flower Asa Region 2
	private static final int SOUL_FLOWER_ASA_3 = 19820; // Soul Flower Asa Region 3
	private static final int SOUL_FLOWER_ANIMA_1 = 19821; // Soul Flower Anima Region 1
	private static final int SOUL_FLOWER_ANIMA_2 = 19822; // Soul Flower Anima Region 2
	private static final int SOUL_FLOWER_ANIMA_3 = 19823; // Soul Flower Anima Region 3
	private static final int SOUL_FLOWER_NOX_1 = 19824; // Soul Flower Nox Region 1
	private static final int SOUL_FLOWER_NOX_2 = 19825; // Soul Flower Nox Region 2
	private static final int SOUL_FLOWER_NOX_3 = 19826; // Soul Flower Nox Region 3
	private static final int SOUL_LIGHT_ASA_1 = 19830; // Soul Light Asa Region 1
	private static final int SOUL_LIGHT_ANIMA_1 = 19832; // Soul Light Anima Region 1
	private static final int SOUL_LIGHT_NOX_1 = 19831; // Soul Light Nox Region 1
	private static final int SOUL_TREE_ASA_3 = 19827; // Soul Tree
	private static final int SOUL_TREE_ANIMA_3 = 19829; // Soul Tree
	private static final int SOUL_TREE_NOX_3 = 19828; // Soul Tree
	// Fire zones
	private static final int SOUL_FLOWER_VITA = 19840; // Soul Flower Vita
	private static final int SOUL_FLOWER_IGNIS = 19841; // Soul Flower Ignis
	private static final int SOUL_TREE_VITA = 19842; // Soul Tree Vita
	private static final int SOUL_TREE_IGNIS = 19843; // Soul Tree Ignis
	private static final int CATSHI_AGEL_AVENGER = 27807; // Catshi Agel Avenger
	private static final int CATSHI_GOF_AVENGER = 27800; // Catshi Gof Avenger
	
	// Trigger Mobs Water Zones
	private static final Set<Integer> TRIGGER_MOBS_LV112_DWS = new HashSet<>(); // Daril's Water Source
	static
	{
		TRIGGER_MOBS_LV112_DWS.add(27701);
		TRIGGER_MOBS_LV112_DWS.add(27702);
		TRIGGER_MOBS_LV112_DWS.add(27703);
		TRIGGER_MOBS_LV112_DWS.add(27704);
		TRIGGER_MOBS_LV112_DWS.add(27705);
		TRIGGER_MOBS_LV112_DWS.add(27706);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV116_DPWS = new HashSet<>(); // Daril Phran's Water Source
	static
	{
		TRIGGER_MOBS_LV116_DPWS.add(27707);
		TRIGGER_MOBS_LV116_DPWS.add(27708);
		TRIGGER_MOBS_LV116_DPWS.add(27709);
		TRIGGER_MOBS_LV116_DPWS.add(27710);
		TRIGGER_MOBS_LV116_DPWS.add(27711);
		TRIGGER_MOBS_LV116_DPWS.add(27712);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV116_ASA_1 = new HashSet<>(); // Asa Region 1
	static
	{
		TRIGGER_MOBS_LV116_ASA_1.add(27713);
		TRIGGER_MOBS_LV116_ASA_1.add(27714);
		TRIGGER_MOBS_LV116_ASA_1.add(27715);
		TRIGGER_MOBS_LV116_ASA_1.add(27716);
		TRIGGER_MOBS_LV116_ASA_1.add(27717);
		TRIGGER_MOBS_LV116_ASA_1.add(27718);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV120_ASA_2 = new HashSet<>(); // Asa Region 2
	static
	{
		TRIGGER_MOBS_LV120_ASA_2.add(27719);
		TRIGGER_MOBS_LV120_ASA_2.add(27720);
		TRIGGER_MOBS_LV120_ASA_2.add(27721);
		TRIGGER_MOBS_LV120_ASA_2.add(27722);
		TRIGGER_MOBS_LV120_ASA_2.add(27723);
		TRIGGER_MOBS_LV120_ASA_2.add(27724);
		TRIGGER_MOBS_LV120_ASA_2.add(27725);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV124_ASA_3 = new HashSet<>(); // Asa Region 3
	static
	{
		TRIGGER_MOBS_LV124_ASA_3.add(27726);
		TRIGGER_MOBS_LV124_ASA_3.add(27727);
		TRIGGER_MOBS_LV124_ASA_3.add(27728);
		TRIGGER_MOBS_LV124_ASA_3.add(27729);
		TRIGGER_MOBS_LV124_ASA_3.add(27730);
		TRIGGER_MOBS_LV124_ASA_3.add(27731);
		TRIGGER_MOBS_LV124_ASA_3.add(27732);
		TRIGGER_MOBS_LV124_ASA_3.add(27733);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV116_ANIMA_1 = new HashSet<>(); // Anima Region 1
	static
	{
		TRIGGER_MOBS_LV116_ANIMA_1.add(27755);
		TRIGGER_MOBS_LV116_ANIMA_1.add(27756);
		TRIGGER_MOBS_LV116_ANIMA_1.add(27757);
		TRIGGER_MOBS_LV116_ANIMA_1.add(27758);
		TRIGGER_MOBS_LV116_ANIMA_1.add(27759);
		TRIGGER_MOBS_LV116_ANIMA_1.add(27760);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV120_ANIMA_2 = new HashSet<>(); // Anima Region 2
	static
	{
		TRIGGER_MOBS_LV120_ANIMA_2.add(27761);
		TRIGGER_MOBS_LV120_ANIMA_2.add(27762);
		TRIGGER_MOBS_LV120_ANIMA_2.add(27763);
		TRIGGER_MOBS_LV120_ANIMA_2.add(27764);
		TRIGGER_MOBS_LV120_ANIMA_2.add(27765);
		TRIGGER_MOBS_LV120_ANIMA_2.add(27766);
		TRIGGER_MOBS_LV120_ANIMA_2.add(27767);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV124_ANIMA_3 = new HashSet<>(); // Anima Region 3
	static
	{
		TRIGGER_MOBS_LV124_ANIMA_3.add(27768);
		TRIGGER_MOBS_LV124_ANIMA_3.add(27769);
		TRIGGER_MOBS_LV124_ANIMA_3.add(27770);
		TRIGGER_MOBS_LV124_ANIMA_3.add(27771);
		TRIGGER_MOBS_LV124_ANIMA_3.add(27772);
		TRIGGER_MOBS_LV124_ANIMA_3.add(27773);
		TRIGGER_MOBS_LV124_ANIMA_3.add(27774);
		TRIGGER_MOBS_LV124_ANIMA_3.add(27775);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV116_NOX_1 = new HashSet<>(); // Nox Region 1
	static
	{
		TRIGGER_MOBS_LV116_NOX_1.add(27734);
		TRIGGER_MOBS_LV116_NOX_1.add(27735);
		TRIGGER_MOBS_LV116_NOX_1.add(27736);
		TRIGGER_MOBS_LV116_NOX_1.add(27737);
		TRIGGER_MOBS_LV116_NOX_1.add(27738);
		TRIGGER_MOBS_LV116_NOX_1.add(27739);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV120_NOX_2 = new HashSet<>(); // Nox Region 2
	static
	{
		TRIGGER_MOBS_LV120_NOX_2.add(27740);
		TRIGGER_MOBS_LV120_NOX_2.add(27741);
		TRIGGER_MOBS_LV120_NOX_2.add(27742);
		TRIGGER_MOBS_LV120_NOX_2.add(27743);
		TRIGGER_MOBS_LV120_NOX_2.add(27745);
		TRIGGER_MOBS_LV120_NOX_2.add(27746);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV124_NOX_3 = new HashSet<>(); // Nox Region 3
	static
	{
		TRIGGER_MOBS_LV124_NOX_3.add(27747);
		TRIGGER_MOBS_LV124_NOX_3.add(27748);
		TRIGGER_MOBS_LV124_NOX_3.add(27749);
		TRIGGER_MOBS_LV124_NOX_3.add(27750);
		TRIGGER_MOBS_LV124_NOX_3.add(27751);
		TRIGGER_MOBS_LV124_NOX_3.add(27752);
		TRIGGER_MOBS_LV124_NOX_3.add(27753);
		TRIGGER_MOBS_LV124_NOX_3.add(27754);
	}
	// Trigger Mobs Fire Zones
	private static final Set<Integer> TRIGGER_MOBS_LV116_FF = new HashSet<>(); // Fiery Field
	static
	{
		TRIGGER_MOBS_LV116_FF.add(27801);
		TRIGGER_MOBS_LV116_FF.add(27802);
		TRIGGER_MOBS_LV116_FF.add(27803);
		TRIGGER_MOBS_LV116_FF.add(27804);
		TRIGGER_MOBS_LV116_FF.add(27805);
		TRIGGER_MOBS_LV116_FF.add(27806);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV120_GOF = new HashSet<>(); // Garden of Flames
	static
	{
		TRIGGER_MOBS_LV120_GOF.add(27799);
		TRIGGER_MOBS_LV120_GOF.add(27798);
		TRIGGER_MOBS_LV120_GOF.add(27797);
		TRIGGER_MOBS_LV120_GOF.add(27796);
		TRIGGER_MOBS_LV120_GOF.add(27795);
		TRIGGER_MOBS_LV120_GOF.add(27794);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV124_VITA = new HashSet<>(); // Vita
	static
	{
		TRIGGER_MOBS_LV124_VITA.add(27808);
		TRIGGER_MOBS_LV124_VITA.add(27810);
		TRIGGER_MOBS_LV124_VITA.add(27811);
		TRIGGER_MOBS_LV124_VITA.add(27812);
		TRIGGER_MOBS_LV124_VITA.add(27813);
		TRIGGER_MOBS_LV124_VITA.add(27814);
		TRIGGER_MOBS_LV124_VITA.add(27815);
		TRIGGER_MOBS_LV124_VITA.add(27816);
	}
	private static final Set<Integer> TRIGGER_MOBS_LV128_IGNIS = new HashSet<>(); // Ignis
	static
	{
		TRIGGER_MOBS_LV128_IGNIS.add(27817);
		TRIGGER_MOBS_LV128_IGNIS.add(27819);
		TRIGGER_MOBS_LV128_IGNIS.add(27820);
		TRIGGER_MOBS_LV128_IGNIS.add(27821);
		TRIGGER_MOBS_LV128_IGNIS.add(27822);
		TRIGGER_MOBS_LV128_IGNIS.add(27823);
		TRIGGER_MOBS_LV128_IGNIS.add(27824);
		TRIGGER_MOBS_LV128_IGNIS.add(27825);
	}
	// Misc
	private static final int MOB_SPAWN_CHANCE = 1; // 1% chance to spawn
	
	private ConquestAreasSpecialMobs()
	{
		super();
		addKillId(TRIGGER_MOBS_LV112_DWS);
		addKillId(TRIGGER_MOBS_LV116_DPWS);
		addKillId(TRIGGER_MOBS_LV116_ASA_1);
		addKillId(TRIGGER_MOBS_LV120_ASA_2);
		addKillId(TRIGGER_MOBS_LV124_ASA_3);
		addKillId(TRIGGER_MOBS_LV116_ANIMA_1);
		addKillId(TRIGGER_MOBS_LV120_ANIMA_2);
		addKillId(TRIGGER_MOBS_LV124_ANIMA_3);
		addKillId(TRIGGER_MOBS_LV116_NOX_1);
		addKillId(TRIGGER_MOBS_LV120_NOX_2);
		addKillId(TRIGGER_MOBS_LV124_NOX_3);
		addKillId(TRIGGER_MOBS_LV116_FF);
		addKillId(TRIGGER_MOBS_LV120_GOF);
		addKillId(TRIGGER_MOBS_LV124_VITA);
		addKillId(TRIGGER_MOBS_LV128_IGNIS);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (getRandom(100) < MOB_SPAWN_CHANCE)
		{
			final Integer npcId = npc.getId();
			if (TRIGGER_MOBS_LV112_DWS.contains(npcId))
			{
				addSpawn(SOUL_FLOWER_DWS, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV116_DPWS.contains(npcId))
			{
				addSpawn(SOUL_FLOWER_DPWS, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV116_ASA_1.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_ASA_1 : SOUL_LIGHT_ASA_1, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV120_ASA_2.contains(npcId))
			{
				addSpawn(SOUL_FLOWER_ASA_2, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV124_ASA_3.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_ASA_3 : SOUL_TREE_ASA_3, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV116_ANIMA_1.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_ANIMA_1 : SOUL_LIGHT_ANIMA_1, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV120_ANIMA_2.contains(npcId))
			{
				addSpawn(SOUL_FLOWER_ANIMA_2, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV124_ANIMA_3.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_ANIMA_3 : SOUL_TREE_ANIMA_3, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV116_NOX_1.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_NOX_1 : SOUL_LIGHT_NOX_1, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV120_NOX_2.contains(npcId))
			{
				addSpawn(SOUL_FLOWER_NOX_2, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV124_NOX_3.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_NOX_3 : SOUL_TREE_NOX_3, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV116_FF.contains(npcId))
			{
				addSpawn(CATSHI_AGEL_AVENGER, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV120_GOF.contains(npcId))
			{
				addSpawn(CATSHI_GOF_AVENGER, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV124_VITA.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_VITA : SOUL_TREE_VITA, npc, true, 0, true);
			}
			else if (TRIGGER_MOBS_LV128_IGNIS.contains(npcId))
			{
				addSpawn(getRandomBoolean() ? SOUL_FLOWER_IGNIS : SOUL_TREE_IGNIS, npc, true, 0, true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new ConquestAreasSpecialMobs();
	}
}
