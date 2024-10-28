package org.l2jmobius.gameserver.model.rebirth;

import java.util.List;
import java.util.Map;

public class RebirthRequirement
{
  private final int requiredLevel;
  private final Map<Integer, Integer> requiredItems;
  private final List<RebirthReward> rewards;
  
  public RebirthRequirement(int requiredLevel, Map<Integer, Integer> requiredItems, List<RebirthReward> rewards)
  {
    this.requiredLevel = requiredLevel;
    this.requiredItems = requiredItems;
    this.rewards = rewards;
  }
  
  public int getRequiredLevel()
  {
    return requiredLevel;
  }
  
  public Map<Integer, Integer> getRequiredItems()
  {
    return requiredItems;
  }
  
  public List<RebirthReward> getRewards()
  {
    return rewards;
  }
}
