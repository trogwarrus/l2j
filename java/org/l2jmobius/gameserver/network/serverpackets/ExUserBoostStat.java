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

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.enums.BonusExpType;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.stat.PlayerStat;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class ExUserBoostStat extends ServerPacket
{
	private final Player _player;
	private final BonusExpType _type;
	
	public ExUserBoostStat(Player player, BonusExpType type)
	{
		_player = player;
		_type = type;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_USER_BOOST_STAT.writeId(this, buffer);
		int count = 0;
		int bonus = 0;
		final PlayerStat stat = _player.getStat();
		switch (_type)
		{
			case VITALITY:
			{
				if (stat.getVitalityPoints() > 0)
				{
					count = (int) (stat.getValue(Stat.VITALITY_SKILLS, 0) + 1);
					bonus = (int) (((stat.getMul(Stat.VITALITY_EXP_RATE, 1) - 1) + (_player.hasPremiumStatus() ? Config.RATE_VITALITY_EXP_PREMIUM_MULTIPLIER : Config.RATE_VITALITY_EXP_MULTIPLIER)) * 100d);
				}
				break;
			}
			case BUFFS:
			{
				count = (int) stat.getValue(Stat.BONUS_EXP_BUFFS, 0);
				bonus = (int) stat.getValue(Stat.ACTIVE_BONUS_EXP, 0);
				break;
			}
			case PASSIVE:
			{
				count = (int) stat.getValue(Stat.BONUS_EXP_PASSIVES, 0);
				bonus = (int) (stat.getValue(Stat.BONUS_EXP, 0) - stat.getValue(Stat.ACTIVE_BONUS_EXP, 0));
				break;
			}
		}
		buffer.writeByte(_type.getId());
		buffer.writeByte(count);
		buffer.writeShort(bonus);
	}
}
