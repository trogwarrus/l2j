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

import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.Skill;

/**
 * Trigger Skill By Dual Range effect implementation.
 * @author Mobius, fruit
 */
public class TriggerSkillByTripleRange extends AbstractEffect
{
	private final SkillHolder _closeSkill;
	private final SkillHolder _midSkill;
	private final SkillHolder _rangeSkill;
	private final int _closeDistance;
	private final int _midDistance;
	private final boolean _adjustLevel;
	
	public TriggerSkillByTripleRange(StatSet params)
	{
		// Just use closeSkill, midSkill and rangeSkill parameters.
		_closeSkill = new SkillHolder(params.getInt("closeSkill"), params.getInt("closeSkillLevel", 1));
		_midSkill = new SkillHolder(params.getInt("midSkill"), params.getInt("midSkillLevel", 1));
		_rangeSkill = new SkillHolder(params.getInt("rangeSkill"), params.getInt("rangeSkillLevel", 1));
		_closeDistance = params.getInt("closeDistance", 120);
		_midDistance = params.getInt("rangeDistance", 360);
		_adjustLevel = params.getBoolean("adjustLevel", true);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.DUAL_RANGE;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effected == null) || !effector.isPlayer())
		{
			return;
		}
		
		final double distance = effector.calculateDistance3D(effected);
		final SkillHolder skillHolder;
		if (distance < _closeDistance)
		{
			skillHolder = _closeSkill;
		}
		else if (distance < _midDistance)
		{
			skillHolder = _midSkill;
		}
		else
		{
			skillHolder = _rangeSkill;
		}
		
		final Skill triggerSkill = _adjustLevel ? SkillData.getInstance().getSkill(skillHolder.getSkillId(), skill.getLevel()) : skillHolder.getSkill();
		if (triggerSkill == null)
		{
			return;
		}
		
		if (effected.isPlayable() && !effected.isAutoAttackable(effector))
		{
			effector.getActingPlayer().updatePvPStatus();
		}
		
		effector.getActingPlayer().useMagic(triggerSkill, null, true, triggerSkill.getCastRange() > 360);
	}
}
