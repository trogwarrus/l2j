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
package events.UniqueGacha;

import java.io.File;
import java.math.BigDecimal;
import java.util.logging.Level;

import org.w3c.dom.Document;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.enums.UniqueGachaRank;
import org.l2jmobius.gameserver.instancemanager.events.UniqueGachaManager;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.LongTimeEvent;
import org.l2jmobius.gameserver.model.quest.Quest;

public class UniqueGacha extends LongTimeEvent implements IXmlReader
{
	public UniqueGacha()
	{
		if (isEventPeriod())
		{
			load();
		}
	}
	
	@Override
	public void load()
	{
		parseDatapackFile("data/scripts/events/UniqueGacha/rewards.xml");
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (event.equals("RELOAD"))
		{
			load();
		}
		return null;
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		final UniqueGachaManager manager = UniqueGachaManager.getInstance();
		forEach(doc, "list", listNode ->
		{
			final StatSet paramSet = new StatSet();
			paramSet.set("isActive", isEventPeriod());
			paramSet.set("activeUntilPeriod", getEventPeriod().getEndDate().getTime());
			forEach(listNode, "configuration", configurationNode ->
			{
				forEach(configurationNode, "param", paramNode ->
				{
					final StatSet forParamSet = new StatSet(parseAttributes(paramNode));
					if (!forParamSet.isEmpty())
					{
						final String paramName = forParamSet.getString("name");
						final String paramValue = forParamSet.getString("value");
						paramSet.set(paramName, paramValue);
					}
				});
			});
			forEach(listNode, "rewards", rewardsNode ->
			{
				forEach(rewardsNode, "reward", rewardNode ->
				{
					final UniqueGachaRank rank = UniqueGachaRank.valueOf(new StatSet(parseAttributes(rewardNode)).getString("rank"));
					forEach(rewardNode, "item", itemNode ->
					{
						final StatSet itemSet = new StatSet(parseAttributes(itemNode));
						final int itemId = itemSet.getInt("id");
						final long count = Long.parseLong(itemSet.getString("count", "1").replaceAll("_", ""));
						final int chance = parseDoubleWithoutPoint(itemSet.getString("chance"), "0", UniqueGachaManager.MINIMUM_CHANCE_AFTER_DOT);
						if (chance == 1)
						{
							System.currentTimeMillis();
						}
						final int enchantLevel = itemSet.getInt("enchantLevel", 0);
						manager.addReward(rank, itemId, count, chance, enchantLevel);
					});
				});
			});
			forEach(listNode, "roll", rollNode ->
			{
				paramSet.set("currencyItemId", new StatSet(parseAttributes(rollNode)).getInt("currencyItemId", 57));
				forEach(rollNode, "game", gameNode ->
				{
					final StatSet gameSet = new StatSet(parseAttributes(gameNode));
					final int gameCount = gameSet.getInt("gameCount");
					final long count = Long.parseLong(gameSet.getString("costCount", "1").replaceAll("_", ""));
					manager.addGameCost(gameCount, count);
				});
			});
			manager.recalculateChances();
			manager.setParameters(paramSet);
		});
	}
	
	public static int parseDoubleWithoutPoint(String value, String defaultValue, int scaleTo)
	{
		return parseValue(value, new BigDecimal(defaultValue)).scaleByPowerOfTen(scaleTo).intValue();
	}
	
	public static BigDecimal parseValue(String val, BigDecimal def)
	{
		try
		{
			return (val == null) || val.isEmpty() ? def : new BigDecimal(val);
		}
		catch (Exception e)
		{
			Quest.LOGGER.log(Level.WARNING, "Value parse error, value type: double, input data: " + val, e);
		}
		
		return def;
	}
	
	public static void main(String[] args)
	{
		new UniqueGacha();
	}
}
