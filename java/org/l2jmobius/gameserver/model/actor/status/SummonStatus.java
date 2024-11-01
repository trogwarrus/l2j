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
package org.l2jmobius.gameserver.model.actor.status;

import org.l2jmobius.gameserver.model.Duel;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.Summon;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.util.Util;

public class SummonStatus extends PlayableStatus
{
	public SummonStatus(Summon activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	@Override
	public void reduceHp(double amount, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption)
	{
		final Summon summon = getActiveChar();
		if ((attacker == null) || summon.isDead())
		{
			return;
		}
		
		final Player attackerPlayer = attacker.getActingPlayer();
		if ((attackerPlayer != null) && ((summon.getOwner() == null) || (summon.getOwner().getDuelId() != attackerPlayer.getDuelId())))
		{
			attackerPlayer.setDuelState(Duel.DUELSTATE_INTERRUPTED);
		}
		
		double value = amount;
		final Player caster = summon.getTransferingDamageTo();
		if (summon.getOwner().getParty() != null)
		{
			if ((caster != null) && Util.checkIfInRange(1000, summon, caster, true) && !caster.isDead() && summon.getParty().getMembers().contains(caster))
			{
				int transferDmg = ((int) value * (int) summon.getStat().getValue(Stat.TRANSFER_DAMAGE_TO_PLAYER, 0)) / 100;
				transferDmg = Math.min((int) caster.getCurrentHp() - 1, transferDmg);
				if (transferDmg > 0)
				{
					int membersInRange = 0;
					for (Player member : caster.getParty().getMembers())
					{
						if (Util.checkIfInRange(1000, member, caster, false) && (member != caster))
						{
							membersInRange++;
						}
					}
					if (attacker.isPlayable() && (caster.getCurrentCp() > 0))
					{
						if (caster.getCurrentCp() > transferDmg)
						{
							caster.getStatus().reduceCp(transferDmg);
						}
						else
						{
							transferDmg = (int) (transferDmg - caster.getCurrentCp());
							caster.getStatus().reduceCp((int) caster.getCurrentCp());
						}
					}
					if (membersInRange > 0)
					{
						caster.reduceCurrentHp(transferDmg / membersInRange, attacker, null);
						value -= transferDmg;
					}
				}
			}
		}
		else if ((caster != null) && (caster == summon.getOwner()) && Util.checkIfInRange(1000, summon, caster, true) && !caster.isDead()) // when no party, transfer only to owner (caster)
		{
			int transferDmg = ((int) value * (int) summon.getStat().getValue(Stat.TRANSFER_DAMAGE_TO_PLAYER, 0)) / 100;
			transferDmg = Math.min((int) caster.getCurrentHp() - 1, transferDmg);
			if (transferDmg > 0)
			{
				if (attacker.isPlayable() && (caster.getCurrentCp() > 0))
				{
					if (caster.getCurrentCp() > transferDmg)
					{
						caster.getStatus().reduceCp(transferDmg);
					}
					else
					{
						transferDmg = (int) (transferDmg - caster.getCurrentCp());
						caster.getStatus().reduceCp((int) caster.getCurrentCp());
					}
				}
				
				caster.reduceCurrentHp(transferDmg, attacker, null);
				value -= transferDmg;
			}
		}
		
		super.reduceHp(value, attacker, awake, isDOT, isHPConsumption);
	}
	
	@Override
	public Summon getActiveChar()
	{
		return (Summon) super.getActiveChar();
	}
}
