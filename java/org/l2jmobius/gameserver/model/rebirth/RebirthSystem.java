package org.l2jmobius.gameserver.model.rebirth;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.data.xml.ItemData;
import org.l2jmobius.gameserver.model.item.ItemTemplate;

public class RebirthSystem
{
  private static final Map<Integer, RebirthRequirement> rebirthRequirements = new HashMap<>();
  
  static
  {
    loadRebirthRequirements();
  }
  
  private static void loadRebirthRequirements()
  {
    try
    {
      File file = new File("data/RebirthRequirements.xml");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(file);
      doc.getDocumentElement().normalize();
      
      NodeList rebirthList = doc.getElementsByTagName("rebirth");
      
      for (int i = 0; i < rebirthList.getLength(); i++)
      {
        Element rebirthElement = (Element) rebirthList.item(i);
        
        int level = Integer.parseInt(rebirthElement.getAttribute("level"));
        int requiredLevel = Integer.parseInt(rebirthElement.getElementsByTagName("requiredLevel").item(0).getTextContent());
        
        Map<Integer, Integer> items = new HashMap<>();
        NodeList itemList = rebirthElement.getElementsByTagName("item");
        
        for (int j = 0; j < itemList.getLength(); j++)
        {
          Element itemElement = (Element) itemList.item(j);
          int itemId = Integer.parseInt(itemElement.getAttribute("id"));
          int itemCount = Integer.parseInt(itemElement.getAttribute("count"));
          items.put(itemId, itemCount);
        }
        
        // Загружаем награды
        List<RebirthReward> rewards = new ArrayList<>();
        NodeList rewardList = rebirthElement.getElementsByTagName("reward");
        
        for (int k = 0; k < rewardList.getLength(); k++)
        {
          Element rewardElement = (Element) rewardList.item(k);
          Integer statPoints = rewardElement.hasAttribute("statPoints") ? Integer.parseInt(rewardElement.getAttribute("statPoints")) : null;
          Integer itemId = rewardElement.hasAttribute("itemId") ? Integer.parseInt(rewardElement.getAttribute("itemId")) : null;
          Integer itemCount = rewardElement.hasAttribute("itemCount") ? Integer.parseInt(rewardElement.getAttribute("itemCount")) : null;
          
          rewards.add(new RebirthReward(statPoints, itemId, itemCount));
        }
        
        rebirthRequirements.put(level, new RebirthRequirement(requiredLevel, items, rewards));
      }
      
    }
    catch (Exception e)
    {
      System.err.println("Ошибка при загрузке требований для перерождений из XML: " + e.getMessage());
    }
  }
  
  public static RebirthRequirement getRebirthRequirements(Player player)
  {
    int nextRebirthLevel = player.getRebirthCount() + 1;
    return rebirthRequirements.get(nextRebirthLevel);
  }
  
  public static boolean performRebirth(Player player)
  {
    int currentRebirth = player.getRebirthCount() + 1;
    RebirthRequirement requirement = rebirthRequirements.get(currentRebirth);
    
    if (requirement == null)
    {
      player.sendMessage("Требования для перерождения не найдены.");
      return false;
    }
    
    // Проверка уровня
    if (player.getLevel() < requirement.getRequiredLevel())
    {
      player.sendMessage("Вы должны достичь уровня " + requirement.getRequiredLevel() + " для перерождения.");
      return false;
    }
    
    // Проверка и удаление предметов
    for (Map.Entry<Integer, Integer> entry : requirement.getRequiredItems().entrySet())
    {
      int itemId = entry.getKey();
      int itemCount = entry.getValue();
      Item requiredItem = player.getInventory().getItemByItemId(itemId);
      
      if (requiredItem == null || requiredItem.getCount() < itemCount)
      {
        player.sendMessage("Для перерождения вам нужно " + itemCount + " шт. предмета с ID " + itemId + ".");
        return false;
      }
      
      player.destroyItemByItemId("Rebirth Requirement", itemId, itemCount, player, true);
    }
    
    // Увеличиваем счётчик перерождений и сбрасываем уровень
    player.setRebirthCount(currentRebirth);
    player.setExp(0);
    player.getStat().setLevel(1);
    player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
    player.setCurrentCp(player.getMaxCp());
    player.broadcastUserInfo();
    player.sendMessage("Поздравляем! Вы прошли перерождение.");
    
    // Выдача бонусов
    giveRebirthBonus(player);
    return true;
  }
  
  private static void giveRebirthBonus(Player player)
  {
    RebirthRequirement requirement = rebirthRequirements.get(player.getRebirthCount());
    
    if (requirement != null)
    {
      for (RebirthReward reward : requirement.getRewards())
      {
        if (reward.getStatPoints() != null)
        {
          player.sendMessage("Вы получили " + reward.getStatPoints() + " бонусных очков за перерождение.");
        }
        if (reward.getItemId() != null && reward.getItemCount() != null)
        {
          player.sendMessage("Вы получили " + reward.getItemCount() + " шт. предмета ID " + reward.getItemId() + " за перерождение.");
        }
      }
    }
    else
    {
      player.sendMessage("Требования для текущего уровня перерождения не найдены.");
    }
  }
  
  // Метод для получения названия предмета по ID
  public static String getItemName(int itemId)
  {
    ItemTemplate item = ItemData.getInstance().getTemplate(itemId);
    return (item != null) ? item.getName() : "Неизвестный предмет";
  }
  
  // Метод для получения пути иконки по ID
  public static String getItemIconPath(int itemId)
  {
    ItemTemplate item = ItemData.getInstance().getTemplate(itemId);
    return (item != null) ? item.getIcon() : "icon.item.default.png";
  }
}
