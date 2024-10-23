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

import org.l2jmobius.gameserver.data.xml.HomunculusData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.homunculus.Homunculus;
import org.l2jmobius.gameserver.model.homunculus.HomunculusTemplate;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExDeleteHomunculusDataResult;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExHomunculusPointInfo;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExRequestHomunculusEvolve;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExShowHomunculusBirthInfo;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExShowHomunculusCouponUi;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExShowHomunculusList;
import org.l2jmobius.gameserver.network.serverpackets.homunculus.ExSummonHomunculusCouponResult;

/**
 * @author Atronic
 */
public class RequestExHomunculusEvolve extends ClientPacket
{
	private int _evolveHomunculus;
	private int _materialHomunculus;
	
	@Override
	protected void readImpl()
	{
		_evolveHomunculus = readInt();
		_materialHomunculus = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((player.getAdena() < 10_000_000_000L) || (player.getInventory().getInventoryItemCount(82903, -1) < 200) || (player.getVariables().getInt(PlayerVariables.HOMUNCULUS_EVOLUTION_POINTS, 0) < 5000))
		{
			player.sendPacket(new ExRequestHomunculusEvolve(false));
			return;
		}
		
		final Homunculus homunculusMaterial = player.getHomunculusList().get(_materialHomunculus);
		final Homunculus homunculusEvolve = player.getHomunculusList().get(_evolveHomunculus);
		final int homunculusEvolveId = homunculusEvolve.getId();
		
		int homunculusHeroId = 0;
		
		switch (homunculusEvolveId)
		{
			case 3:
				homunculusHeroId = 40;
				break;
			case 6:
				homunculusHeroId = 41;
				break;
			case 9:
				homunculusHeroId = 42;
				break;
			case 12:
				homunculusHeroId = 43;
				break;
			case 15:
				homunculusHeroId = 44;
				break;
			
			case 18:
				homunculusHeroId = 45;
				break;
			case 21:
				homunculusHeroId = 46;
				break;
			case 24:
				homunculusHeroId = 47;
				break;
			case 27:
				homunculusHeroId = 48;
				break;
			case 30:
				homunculusHeroId = 49;
				break;
			case 33:
				homunculusHeroId = 50;
				break;
			case 36:
				homunculusHeroId = 51;
				break;
			case 39:
				homunculusHeroId = 52;
				break;
			default:
				break;
		}
		
		final HomunculusTemplate template = HomunculusData.getInstance().getTemplate(homunculusHeroId);
		final Homunculus homunculusNew = new Homunculus(template, player.getHomunculusList().size(), 1, 0, 0, 0, 0, 0, 0, false);
		
		player.destroyItemByItemId("HomunculusEvolve", 57, 10_000_000_000L, player, true);
		player.destroyItemByItemId("HomunculusEvolve", 82903, 200, player, true);
		player.getVariables().set(PlayerVariables.HOMUNCULUS_EVOLUTION_POINTS, player.getVariables().getInt(PlayerVariables.HOMUNCULUS_EVOLUTION_POINTS) - 5000);
		
		if (player.getHomunculusList().add(homunculusNew))
		{
			player.sendPacket(new ExShowHomunculusCouponUi());
			player.sendPacket(new ExShowHomunculusList(player));
			player.sendPacket(new ExShowHomunculusBirthInfo(player));
			player.sendPacket(new ExSummonHomunculusCouponResult(1, homunculusNew.getSlot()));
			player.sendPacket(new ExRequestHomunculusEvolve(true));
			
		}
		
		if (player.getHomunculusList().remove(homunculusMaterial))
		{
			player.sendPacket(new ExHomunculusPointInfo(player));
			player.sendPacket(new ExDeleteHomunculusDataResult());
			player.sendPacket(new ExShowHomunculusList(player));
		}
		if (player.getHomunculusList().remove(homunculusEvolve))
		{
			player.sendPacket(new ExHomunculusPointInfo(player));
			player.sendPacket(new ExDeleteHomunculusDataResult());
			player.sendPacket(new ExShowHomunculusList(player));
		}
	}
}