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
package org.l2jmobius.gameserver.network.serverpackets.virtualItem;

import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.VirtualItemHolder;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

public class ExVirtualItemSystem extends ServerPacket
{
	private final Player _player;
	private final int _type;
	private final boolean _result;
	private final int _selectIndexMain;
	private final int _selectIndexSub;
	private final int _selectSlot;
	private final List<VirtualItemHolder> _updateVisItemInfo;
	
	public ExVirtualItemSystem(Player player, int type, int selectIndexMain, int selectIndexSub, int selectSlot, List<VirtualItemHolder> updateVisItemInfo)
	{
		_player = player;
		_type = type;
		_result = false;
		_selectIndexMain = selectIndexMain;
		_selectIndexSub = selectIndexSub;
		_selectSlot = selectSlot;
		_updateVisItemInfo = updateVisItemInfo;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_VIRTUALITEM_SYSTEM.writeId(this, buffer);
		final long illusoryPointsAcquired = _player.getVariables().getLong(PlayerVariables.ILLUSORY_POINTS_ACQUIRED, 0);
		final long illusoryPointsUsed = _player.getVariables().getLong(PlayerVariables.ILLUSORY_POINTS_USED, 0);
		if (_type == 1)
		{
			_player.sendPacket(new ExVirtualItemSystemBaseInfo(_player));
		}
		// Reset all
		else if (_type == 3)
		{
			// Reset all used points
			_player.getVariables().set(PlayerVariables.ILLUSORY_POINTS_ACQUIRED, illusoryPointsAcquired + illusoryPointsUsed); // Total Illusory Points acquired
			_player.getVariables().set(PlayerVariables.ILLUSORY_POINTS_USED, 0); // Total Illusory Points used
			_player.sendPacket(new ExVirtualItemSystemBaseInfo(_player));
		}
		else
		{
			buffer.writeByte(_type);// var int cType;
			buffer.writeByte(_result);// var int cResult;
			buffer.writeInt((int) illusoryPointsAcquired);// var int nTotalGetVISPoint;
			buffer.writeInt((int) illusoryPointsUsed);// var int nTotalUsedVISPoint;
			buffer.writeInt(_selectIndexMain);// var int nSelectIndexMain;
			buffer.writeInt(_selectIndexSub);// var int nSelectIndexSub;
			buffer.writeInt(_selectSlot);// var int nSelectSlot;
			for (VirtualItemHolder virtualItem : _updateVisItemInfo)
			{
				// Item info
				buffer.writeInt(virtualItem.getIndexMain());// var int nIndexMain;
				buffer.writeInt(virtualItem.getIndexSub());// var int nIndexSub;
				buffer.writeInt(virtualItem.getSlot());// var int nSlot;
				buffer.writeInt(virtualItem.getCostVISPoint());// var int nCostVISPoint;
				buffer.writeInt(virtualItem.getItemClass());// var int nItemClass;
				buffer.writeInt(virtualItem.getEnchant());// var int nEnchant;
				
				// TODO: Add a item/skill check
				if (virtualItem.getItemClass() != 0)
				{
					if ((virtualItem.getItemClass() == 29630) || (virtualItem.getItemClass() == 29631) || (virtualItem.getItemClass() == 29632) || (virtualItem.getItemClass() == 29633))
					{
						// Skill
						final Skill skill = SkillData.getInstance().getSkill(virtualItem.getItemClass(), virtualItem.getEnchant());
						_player.addSkill(skill, true);
					}
					else
					{
						// Item
						Item visItem = new Item(virtualItem.getItemClass());
						visItem.setCount(1);
						visItem.setEnchantLevel(virtualItem.getEnchant()); // Fix enchant error
						visItem.setOwnerId(_player.getObjectId());
						_player.getInventory().equipItem(visItem);
						// send packets
						final InventoryUpdate iu = new InventoryUpdate();
						iu.addModifiedItem(visItem);
						_player.sendInventoryUpdate(iu);
					}
					
					// Addd used points to var
					_player.getVariables().set(PlayerVariables.ILLUSORY_POINTS_USED, _player.getVariables().getInt(PlayerVariables.ILLUSORY_POINTS_USED, 0) + virtualItem.getCostVISPoint()); // Total Illusory Points used
				}
			}
		}
		
		_player.sendPacket(new ExVirtualItemSystemBaseInfo(_player));
		_player.sendPacket(new ExVirtualItemSystemPointInfo(illusoryPointsAcquired));
	}
}

/*
 * struct _S_EX_VIRTUALITEM_SYSTEM { var int cType; var int cResult; var int nEndTime; var int nTotalGetVISPoint; var int nTotalUsedVISPoint; var int nSelectIndexMain; var int nSelectIndexSub; var int nSelectSlot; var array<_VirtualItemInfo> lstVISItemInfoList; };
 */