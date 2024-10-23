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
package org.l2jmobius.gameserver.network.serverpackets.homunculus;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.xml.HomunculusCreationData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExShowHomunculusBirthInfo extends ServerPacket
{
	private final int _hpPoints;
	private final int _spPoints;
	private final int _vpPoints;
	private final int _homunculusCreateTime;
	private final int _feeHpPoints;
	private final int _feeSpPoints;
	private final int _feeVpPoints;
	
	public ExShowHomunculusBirthInfo(Player player)
	{
		_hpPoints = player.getVariables().getInt(PlayerVariables.HOMUNCULUS_HP_POINTS, 0);
		_spPoints = player.getVariables().getInt(PlayerVariables.HOMUNCULUS_SP_POINTS, 0);
		_vpPoints = player.getVariables().getInt(PlayerVariables.HOMUNCULUS_VP_POINTS, 0);
		_homunculusCreateTime = (int) (player.getVariables().getLong(PlayerVariables.HOMUNCULUS_CREATION_TIME, 0) / 1000);
		_feeHpPoints = HomunculusCreationData.getInstance().getDefaultTemplate().getHPFeeCount();
		_feeSpPoints = (int) HomunculusCreationData.getInstance().getDefaultTemplate().getSPFeeCount();
		_feeVpPoints = HomunculusCreationData.getInstance().getDefaultTemplate().getVPFeeCount();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SHOW_HOMUNCULUS_BIRTH_INFO.writeId(this, buffer);
		int creationStage = 0;
		if (_homunculusCreateTime > 0)
		{
			if (((System.currentTimeMillis() / 1000) >= _homunculusCreateTime) && (_hpPoints == _feeHpPoints) && (_spPoints == _feeSpPoints) && (_vpPoints == _feeVpPoints))
			{
				creationStage = 2;
			}
			else
			{
				creationStage = 1;
			}
		}
		buffer.writeInt(creationStage); // in creation process (0: can create, 1: in process, 2: can awake
		buffer.writeInt(_hpPoints); // hp points
		buffer.writeInt(_spPoints); // sp points
		buffer.writeInt(_vpPoints); // vp points
		buffer.writeLong(_homunculusCreateTime); // finish time
		// buffer.writeInt(0); // JP = 0. ?
	}
}
