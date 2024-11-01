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

import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Creature;

public class AttackableStatus extends NpcStatus
{
	public AttackableStatus(Attackable activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHpConsumption)
	{
		final Attackable attackable = getActiveChar();
		if (attackable.isDead())
		{
			return;
		}
		
		if (value > 0)
		{
			if (attackable.isOverhit())
			{
				attackable.setOverhitValues(attacker, value);
			}
			else
			{
				attackable.overhitEnabled(false);
			}
		}
		else
		{
			attackable.overhitEnabled(false);
		}
		
		super.reduceHp(value, attacker, awake, isDOT, isHpConsumption);
		
		if (!attackable.isDead())
		{
			// And the attacker's hit didn't kill the mob, clear the over-hit flag
			attackable.overhitEnabled(false);
		}
	}
	
	@Override
	public boolean setCurrentHp(double newHp, boolean broadcastPacket)
	{
		return super.setCurrentHp(newHp, true);
	}
	
	@Override
	public Attackable getActiveChar()
	{
		return (Attackable) super.getActiveChar();
	}
}