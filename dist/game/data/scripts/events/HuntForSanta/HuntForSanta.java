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
package events.HuntForSanta;

import org.l2jmobius.gameserver.enums.SkillFinishType;
import org.l2jmobius.gameserver.model.Party;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.ListenerRegisterType;
import org.l2jmobius.gameserver.model.events.annotations.RegisterEvent;
import org.l2jmobius.gameserver.model.events.annotations.RegisterType;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerLogin;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.quest.LongTimeEvent;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.skill.SkillCaster;
import org.l2jmobius.gameserver.util.Util;

/**
 * The Hunt for Santa Begins!<br>
 * Info - http://www.lineage2.com/en/news/events/hunt-for-santa.php
 * @author Mobius
 */
public class HuntForSanta extends LongTimeEvent
{
	// NPC
	private static final int NOELLE = 34008;
	// Skills
	private static final SkillHolder BUFF_STOCKING = new SkillHolder(16419, 1);
	private static final SkillHolder BUFF_TREE = new SkillHolder(16420, 1);
	private static final SkillHolder BUFF_SNOWMAN = new SkillHolder(16421, 1);
	// Item
	private static final int SANTAS_MARK = 40313;
	
	private HuntForSanta()
	{
		addStartNpc(NOELLE);
		addFirstTalkId(NOELLE);
		addTalkId(NOELLE);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34008.htm":
			case "34008-1.htm":
			{
				htmltext = event;
				break;
			}
			case "receiveBuffStocking":
			{
				htmltext = applyBuff(npc, player, BUFF_STOCKING.getSkill());
				startQuestTimer("rewardBuffStocking" + player.getObjectId(), 7200000, null, player);
				break;
			}
			case "receiveBuffTree":
			{
				htmltext = applyBuff(npc, player, BUFF_TREE.getSkill());
				startQuestTimer("rewardBuffTree" + player.getObjectId(), 7200000, null, player);
				break;
			}
			case "receiveBuffSnowman":
			{
				htmltext = applyBuff(npc, player, BUFF_SNOWMAN.getSkill());
				startQuestTimer("rewardBuffSnowman" + player.getObjectId(), 7200000, null, player);
				break;
			}
			case "receiveBuffAll":
			{
				htmltext = applyAllBuffs(npc, player);
				startQuestTimer("rewardBuffStocking" + player.getObjectId(), 7200000, null, player);
				startQuestTimer("rewardBuffTree" + player.getObjectId(), 7200000, null, player);
				startQuestTimer("rewardBuffSnowman" + player.getObjectId(), 7200000, null, player);
				break;
			}
			case "changeBuff":
			{
				removeBuffs(player);
				htmltext = "34008-1.htm";
				break;
			}
		}
		
		if (event.startsWith("rewardBuffStocking") //
			|| event.startsWith("rewardBuffSnowman") //
			|| event.startsWith("rewardBuffTree"))
		{
			if ((player != null) && (player.isOnlineInt() == 1))
			{
				giveItems(player, SANTAS_MARK, 1);
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "34008.htm";
	}
	
	private String applyBuff(Npc npc, Player player, Skill skill)
	{
		removeBuffs(player);
		SkillCaster.triggerCast(npc, player, skill);
		return "34008-2.htm";
	}
	
	private String applyAllBuffs(Npc npc, Player player)
	{
		final Party party = player.getParty();
		if ((party != null) && (party.getLeader() == player) && ((party.getMemberCount() > 6) || (party.getRaceCount() > 2)))
		{
			for (Player member : party.getMembers())
			{
				if (Util.calculateDistance(npc, member, false, false) < 500)
				{
					removeBuffs(member);
					SkillCaster.triggerCast(npc, member, BUFF_STOCKING.getSkill());
					SkillCaster.triggerCast(npc, member, BUFF_TREE.getSkill());
					SkillCaster.triggerCast(npc, member, BUFF_SNOWMAN.getSkill());
				}
			}
			return "34008-2.htm";
		}
		else if (party == null)
		{
			return "34008-3.htm";
		}
		return "34008-4.htm";
	}
	
	private void removeBuffs(Player player)
	{
		player.getEffectList().stopSkillEffects(SkillFinishType.REMOVED, BUFF_STOCKING.getSkill());
		player.getEffectList().stopSkillEffects(SkillFinishType.REMOVED, BUFF_TREE.getSkill());
		player.getEffectList().stopSkillEffects(SkillFinishType.REMOVED, BUFF_SNOWMAN.getSkill());
		cancelQuestTimer("rewardBuffStocking" + player.getObjectId(), null, player);
		cancelQuestTimer("rewardBuffTree" + player.getObjectId(), null, player);
		cancelQuestTimer("rewardBuffSnowman" + player.getObjectId(), null, player);
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGIN)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerLogin(OnPlayerLogin event)
	{
		final Player player = event.getPlayer();
		final BuffInfo buffStocking = player.getEffectList().getBuffInfoBySkillId(BUFF_STOCKING.getSkillId());
		final BuffInfo buffTree = player.getEffectList().getBuffInfoBySkillId(BUFF_TREE.getSkillId());
		final BuffInfo buffSnowman = player.getEffectList().getBuffInfoBySkillId(BUFF_SNOWMAN.getSkillId());
		if (buffStocking != null)
		{
			cancelQuestTimer("rewardBuffStocking" + player.getObjectId(), null, player);
			startQuestTimer("rewardBuffStocking" + player.getObjectId(), buffStocking.getTime() * 1000, null, player);
		}
		if (buffTree != null)
		{
			cancelQuestTimer("rewardBuffTree" + player.getObjectId(), null, player);
			startQuestTimer("rewardBuffTree" + player.getObjectId(), buffTree.getTime() * 1000, null, player);
		}
		if (buffSnowman != null)
		{
			cancelQuestTimer("rewardBuffSnowman" + player.getObjectId(), null, player);
			startQuestTimer("rewardBuffSnowman" + player.getObjectId(), buffSnowman.getTime() * 1000, null, player);
		}
	}
	
	public static void main(String[] args)
	{
		new HuntForSanta();
	}
}
