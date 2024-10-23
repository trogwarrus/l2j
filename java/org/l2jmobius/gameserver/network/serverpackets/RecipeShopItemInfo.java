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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

public class RecipeShopItemInfo extends ServerPacket
{
	private final Player _manufacturer;
	private final int _recipeId;
	private final Boolean _success;
	private final long _manufacturePrice;
	private final long _offeringMaximumAdena;
	private final double _craftRate;
	private final double _craftCritical;
	
	public RecipeShopItemInfo(Player manufacturer, int recipeId, boolean success, long manufacturePrice, long offeringMaximumAdena)
	{
		_manufacturer = manufacturer;
		_recipeId = recipeId;
		_success = success;
		_manufacturePrice = manufacturePrice;
		_offeringMaximumAdena = offeringMaximumAdena;
		_craftRate = _manufacturer.getStat().getValue(Stat.CRAFT_RATE, 0);
		_craftCritical = _manufacturer.getStat().getValue(Stat.CRAFTING_CRITICAL, 0);
	}
	
	public RecipeShopItemInfo(Player manufacturer, int recipeId, long manufacturePrice, long offeringMaximumAdena)
	{
		_manufacturer = manufacturer;
		_recipeId = recipeId;
		_success = null;
		_manufacturePrice = manufacturePrice;
		_offeringMaximumAdena = offeringMaximumAdena;
		_craftRate = _manufacturer.getStat().getValue(Stat.CRAFT_RATE, 0);
		_craftCritical = _manufacturer.getStat().getValue(Stat.CRAFTING_CRITICAL, 0);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.RECIPE_SHOP_ITEM_INFO.writeId(this, buffer);
		buffer.writeInt(_manufacturer.getObjectId());
		buffer.writeInt(_recipeId);
		buffer.writeInt((int) _manufacturer.getCurrentMp());
		buffer.writeInt(_manufacturer.getMaxMp());
		buffer.writeInt(_success == null ? -1 : (_success ? 1 : 0)); // item creation none/success/failed
		buffer.writeLong(_manufacturePrice);
		buffer.writeByte(_offeringMaximumAdena > 0); // Trigger offering window if 1
		buffer.writeLong(_offeringMaximumAdena);
		buffer.writeDouble(Math.min(_craftRate, 100.0));
		buffer.writeByte(_craftCritical > 0);
		buffer.writeDouble(Math.min(_craftCritical, 100.0));
		buffer.writeByte(0); // find me
	}
}
