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
package org.l2jmobius.gameserver.model.actor.stat;

import java.util.logging.Logger;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.data.xml.ExperienceData;
import org.l2jmobius.gameserver.data.xml.PetDataTable;
import org.l2jmobius.gameserver.data.xml.SkillTreeData;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Pet;
import org.l2jmobius.gameserver.model.events.EventDispatcher;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayableExpChanged;
import org.l2jmobius.gameserver.model.events.returns.TerminateReturn;
import org.l2jmobius.gameserver.model.item.Weapon;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.network.serverpackets.ExNewSkillToLearnByLevelUp;

public class PlayableStat extends CreatureStat
{
	protected static final Logger LOGGER = Logger.getLogger(PlayableStat.class.getName());
	
	public PlayableStat(Playable player)
	{
		super(player);
	}
	
	public boolean addExp(long amount)
	{
		final Playable playable = getActiveChar();
		if (EventDispatcher.getInstance().hasListener(EventType.ON_PLAYABLE_EXP_CHANGED, playable))
		{
			final TerminateReturn term = EventDispatcher.getInstance().notifyEvent(new OnPlayableExpChanged(playable, getExp(), getExp() + amount), playable, TerminateReturn.class);
			if ((term != null) && term.terminate())
			{
				return false;
			}
		}
		
		if (((getExp() + amount) < 0) || ((amount > 0) && (getExp() == (getExpForLevel(getMaxLevel()) - 1))))
		{
			return true;
		}
		
		long value = amount;
		if ((getExp() + value) >= getExpForLevel(getMaxLevel()))
		{
			value = getExpForLevel(getMaxLevel()) - 1 - getExp();
		}
		
		final int oldLevel = getLevel();
		setExp(getExp() + value);
		int minimumLevel = 1;
		if (playable.isPet())
		{
			// get minimum level from NpcTemplate
			minimumLevel = PetDataTable.getInstance().getPetMinLevel(((Pet) playable).getTemplate().getId());
		}
		
		int level = minimumLevel; // minimum level
		for (int tmp = level; tmp <= getMaxLevel(); tmp++)
		{
			if (getExp() >= getExpForLevel(tmp))
			{
				continue;
			}
			level = --tmp;
			break;
		}
		
		if ((level != getLevel()) && (level >= minimumLevel))
		{
			addLevel(level - getLevel());
		}
		
		if ((getLevel() > oldLevel) && playable.isPlayer())
		{
			final Player player = playable.getActingPlayer();
			if (SkillTreeData.getInstance().hasAvailableSkills(player, player.getClassId()))
			{
				player.sendPacket(ExNewSkillToLearnByLevelUp.STATIC_PACKET);
			}
		}
		
		return true;
	}
	
	public boolean removeExp(long amount)
	{
		long value = amount;
		if (((getExp() - value) < getExpForLevel(getLevel())) && (!Config.PLAYER_DELEVEL || (Config.PLAYER_DELEVEL && (getLevel() <= Config.DELEVEL_MINIMUM))))
		{
			value = getExp() - getExpForLevel(getLevel());
		}
		
		if ((getExp() - value) < 0)
		{
			value = getExp() - 1;
		}
		
		setExp(getExp() - value);
		int minimumLevel = 1;
		final Playable playable = getActiveChar();
		if (playable.isPet())
		{
			// get minimum level from NpcTemplate
			minimumLevel = PetDataTable.getInstance().getPetMinLevel(((Pet) playable).getTemplate().getId());
		}
		int level = minimumLevel;
		for (int tmp = level; tmp <= getMaxLevel(); tmp++)
		{
			if (getExp() >= getExpForLevel(tmp))
			{
				continue;
			}
			level = --tmp;
			break;
		}
		if ((level != getLevel()) && (level >= minimumLevel))
		{
			addLevel(level - getLevel());
		}
		return true;
	}
	
	public boolean removeExpAndSp(long removeExp, long removeSp)
	{
		boolean expRemoved = false;
		boolean spRemoved = false;
		if (removeExp > 0)
		{
			expRemoved = removeExp(removeExp);
		}
		if (removeSp > 0)
		{
			spRemoved = removeSp(removeSp);
		}
		return expRemoved || spRemoved;
	}
	
	public boolean addLevel(int amount)
	{
		int value = amount;
		if ((getLevel() + value) > (getMaxLevel() - 1))
		{
			if (getLevel() < (getMaxLevel() - 1))
			{
				value = getMaxLevel() - 1 - getLevel();
			}
			else
			{
				return false;
			}
		}
		
		final boolean levelIncreased = (getLevel() + value) > getLevel();
		value += getLevel();
		setLevel(value);
		
		// Sync up exp with current level
		if ((getExp() >= getExpForLevel(getLevel() + 1)) || (getExpForLevel(getLevel()) > getExp()))
		{
			setExp(getExpForLevel(getLevel()));
		}
		
		final Playable playable = getActiveChar();
		if (!levelIncreased && playable.isPlayer() && !playable.isGM() && Config.DECREASE_SKILL_LEVEL)
		{
			((Player) playable).checkPlayerSkills();
		}
		
		if (!levelIncreased)
		{
			return false;
		}
		
		playable.getStatus().setCurrentHp(playable.getStat().getMaxHp());
		playable.getStatus().setCurrentMp(playable.getStat().getMaxMp());
		
		return true;
	}
	
	public boolean addSp(long amount)
	{
		if (amount < 0)
		{
			LOGGER.warning("wrong usage");
			return false;
		}
		
		final long currentSp = getSp();
		if (currentSp >= Config.MAX_SP)
		{
			return false;
		}
		
		long value = amount;
		if (currentSp > (Config.MAX_SP - value))
		{
			value = Config.MAX_SP - currentSp;
		}
		
		setSp(currentSp + value);
		return true;
	}
	
	public boolean removeSp(long amount)
	{
		final long currentSp = getSp();
		if (currentSp < amount)
		{
			setSp(getSp() - currentSp);
			return true;
		}
		setSp(getSp() - amount);
		return true;
	}
	
	public long getExpForLevel(int level)
	{
		return ExperienceData.getInstance().getExpForLevel(level);
	}
	
	@Override
	public Playable getActiveChar()
	{
		return (Playable) super.getActiveChar();
	}
	
	public int getMaxLevel()
	{
		return ExperienceData.getInstance().getMaxLevel();
	}
	
	@Override
	public int getPhysicalAttackRadius()
	{
		final Weapon weapon = getActiveChar().getActiveWeaponItem();
		return weapon != null ? weapon.getBaseAttackRadius() : super.getPhysicalAttackRadius();
	}
	
	@Override
	public int getPhysicalAttackAngle()
	{
		final Playable playable = getActiveChar();
		final Weapon weapon = playable.getActiveWeaponItem();
		return (weapon != null ? weapon.getBaseAttackAngle() + (int) playable.getStat().getValue(Stat.WEAPON_ATTACK_ANGLE_BONUS, 0) : super.getPhysicalAttackAngle());
	}
}
