package org.l2jmobius.gameserver.model.rebirth;

public class RebirthReward
{
  private final Integer statPoints;
  private final Integer itemId;
  private final Integer itemCount;
  
  public RebirthReward(Integer statPoints, Integer itemId, Integer itemCount)
  {
    this.statPoints = statPoints;
    this.itemId = itemId;
    this.itemCount = itemCount;
  }
  
  public Integer getStatPoints()
  {
    return statPoints;
  }
  
  public Integer getItemId()
  {
    return itemId;
  }
  
  public Integer getItemCount()
  {
    return itemCount;
  }
}
