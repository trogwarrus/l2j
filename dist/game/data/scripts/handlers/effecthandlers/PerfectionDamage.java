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
package handlers.effecthandlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.l2jmobius.Config;
import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.enums.ShotType;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.events.EventDispatcher;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.impl.creature.OnCreatureAttackPerfection;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.AbnormalType;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.stats.Formulas;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ExMagicAttackInfo;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

/**
 * Perfection damage effect implementation.
 * @author Atronic
 */
public class PerfectionDamage extends AbstractEffect
{
	private final double _pAtkMod;
	private final double _criticalChance;
	private final boolean _overHit;
	private final Set<AbnormalType> _abnormals;
	private final double _abnormalPower;
	
	private final double _perfectionRate;
	private final double _perfectionMod;
	
	public PerfectionDamage(StatSet params)
	{
		_pAtkMod = params.getDouble("pAtkMod", 1.0);
		_criticalChance = params.getDouble("criticalChance", 10);
		_overHit = params.getBoolean("overHit", false);
		
		final String abnormals = params.getString("abnormalType", "");
		if (!abnormals.isEmpty())
		{
			_abnormals = new HashSet<>();
			for (String slot : abnormals.split(";"))
			{
				_abnormals.add(AbnormalType.getAbnormalType(slot));
			}
		}
		else
		{
			_abnormals = Collections.<AbnormalType> emptySet();
		}
		_abnormalPower = params.getDouble("abnormalPower", 1);
		_perfectionRate = params.getDouble("perfectionRate", 1.0);
		_perfectionMod = params.getDouble("perfectionMod", 1.0);
	}
	
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return !Formulas.calcSkillEvasion(effector, effected, skill);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.PHYSICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effector.isAlikeDead())
		{
			return;
		}
		
		if (effected.isPlayer() && effected.getActingPlayer().isFakeDeath() && Config.FAKE_DEATH_DAMAGE_STAND)
		{
			effected.stopFakeDeath(true);
		}
		
		if (_overHit && effected.isAttackable())
		{
			((Attackable) effected).overhitEnabled(true);
		}
		
		final int perfectionChance = Rnd.get(100);
		double attack;
		if ((_perfectionRate > 1.0) && (perfectionChance <= (_perfectionRate * (1 + (effector.getStat().getValue(Stat.PERFECTION_RATE, 0) / 100)))))
		{
			attack = effector.getPAtk() * (_perfectionMod + (effector.getStat().getValue(Stat.PERFECTION_POWER, 0) / 100.0));
		}
		else
		{
			attack = effector.getPAtk() * _pAtkMod;
		}
		
		// Check if we apply an abnormal modifier.
		if (!_abnormals.isEmpty())
		{
			for (AbnormalType abnormal : _abnormals)
			{
				if (effected.hasAbnormalType(abnormal))
				{
					attack += _abnormalPower;
					break;
				}
			}
		}
		
		double baseMod;
		final boolean critical = Formulas.calcCrit(_criticalChance, effector, effected, skill);
		if ((_perfectionRate > 1.0) && (perfectionChance <= (_perfectionRate * (1 + (effector.getStat().getValue(Stat.PERFECTION_RATE, 0) / 100)))))
		{
			if (critical) // When perfection is triggered with a critical hit the perfection damage is reduced . Totally guessed the number based on retail.
			{
				baseMod = attack * 1.2 * effector.getLevelMod();
			}
			else
			{
				baseMod = attack * effector.getLevelMod();
			}
			
			if (EventDispatcher.getInstance().hasListener(EventType.ON_CREATURE_ATTACK_PERFECTION, effector))
			{
				EventDispatcher.getInstance().notifyEvent(new OnCreatureAttackPerfection(effector, effected, skill), effector);
			}
		}
		else
		{
			baseMod = attack * effector.getLevelMod();
		}
		
		final boolean ss = skill.useSoulShot() && (effector.isChargedShot(ShotType.SOULSHOTS) || effector.isChargedShot(ShotType.BLESSED_SOULSHOTS));
		final byte shld = Formulas.calcShldUse(effector, effected);
		double damage = Formulas.calcBlowDamage(effector, effected, skill, false, baseMod, shld, ss);
		SystemMessage sm = new SystemMessage(SystemMessageId.C1_MAKES_A_MARVELLOUS_STRIKE);
		sm.addString(effector.getName());
		
		if (critical && (perfectionChance >= (_perfectionRate * (1 + (effector.getStat().getValue(Stat.PERFECTION_RATE, 0) / 100)))))
		{
			damage *= 2;
			effector.doAttack(damage, effected, skill, false, false, true, false);
		}
		else if (critical && (perfectionChance <= (_perfectionRate * (1 + (effector.getStat().getValue(Stat.PERFECTION_RATE, 0) / 100)))))
		{
			
			damage *= 2;
			effector.doAttack(damage, effected, skill, false, false, true, false);
			effector.sendPacket(sm);
			effector.sendPacket(new ExMagicAttackInfo(effector.getObjectId(), effected.getObjectId(), ExMagicAttackInfo.PERFECTION));
		}
		else if (perfectionChance <= (_perfectionRate * (1 + (effector.getStat().getValue(Stat.PERFECTION_RATE, 0) / 100))))
		{
			effector.doAttack(damage, effected, skill, false, false, false, false);
			effector.sendPacket(sm);
			effector.sendPacket(new ExMagicAttackInfo(effector.getObjectId(), effected.getObjectId(), ExMagicAttackInfo.PERFECTION));
		}
		else
		{
			effector.doAttack(damage, effected, skill, false, false, false, false);
		}
	}
}
