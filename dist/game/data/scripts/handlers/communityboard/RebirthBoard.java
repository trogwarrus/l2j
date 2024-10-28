package handlers.communityboard;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import org.l2jmobius.gameserver.cache.HtmCache;
import org.l2jmobius.gameserver.handler.CommunityBoardHandler;
import org.l2jmobius.gameserver.handler.IWriteBoardHandler;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.rebirth.RebirthSystem;
import org.l2jmobius.gameserver.model.rebirth.RebirthRequirement;

public class RebirthBoard implements IWriteBoardHandler
{
  private static final String NAVIGATION_PATH = "data/html/CommunityBoard/Custom/navigation.html";
  
  private static final String[] COMMANDS =
  {
    "_bbsrebirth_requirements",
    "_bbsrebirth_confirm"
  };
  
  @Override
  public String[] getCommunityBoardCommands()
  {
    return COMMANDS;
  }
  
  @Override
  public boolean parseCommunityBoardCommand(String command, Player player)
  {
    if (command.equals("_bbsrebirth_requirements"))
    {
      showRebirthRequirements(player, false);
      return true;
    }
    else if (command.equals("_bbsrebirth_confirm"))
    {
      boolean result = RebirthSystem.performRebirth(player);
      if (result)
      {
        showRebirthRequirements(player, true);
      }
      return true;
    }
    return false;
  }
  
  @Override
  public boolean writeCommunityBoardCommand(Player player, String arg1, String arg2, String arg3, String arg4, String arg5)
  {
    return false;
  }
  
  private void showRebirthRequirements(Player player, boolean rebirthCompleted)
  {
    String html = loadHtmlTemplate("data/html/CommunityBoard/Custom/rebirth/requirements.html");
    final String navigation = HtmCache.getInstance().getHtm(player, NAVIGATION_PATH);
    
    // Получаем требования для следующего уровня перерождения
    RebirthRequirement requirement = RebirthSystem.getRebirthRequirements(player);
    
    String rebirthCompletedMessage = rebirthCompleted ? "<center><font color=\"LEVEL\">Перерождение успешно завершено!</font></center><br>" : "";
    html = html.replace("{{REBIRTH_COMPLETED_MESSAGE}}", rebirthCompletedMessage);
    
    if (requirement != null)
    {
      html = html.replace("{{REQUIRED_LEVEL}}", String.valueOf(requirement.getRequiredLevel()));
      
      // Составляем HTML-список требуемых предметов с иконками и названиями
      StringBuilder itemsHtml = new StringBuilder();
      for (Map.Entry<Integer, Integer> entry : requirement.getRequiredItems().entrySet())
      {
        int itemId = entry.getKey();
        int itemCount = entry.getValue();
        String itemName = RebirthSystem.getItemName(itemId);
        String itemIcon = RebirthSystem.getItemIconPath(itemId);
        
        itemsHtml.append("<tr>").append("<td><img src=\"").append(itemIcon).append("\" width=32 height=32></td>").append("<td>").append(itemName).append(" x").append(itemCount).append("</td>").append("</tr>");
      }
      html = html.replace("{{REQUIRED_ITEMS}}", itemsHtml.toString());
    }
    else
    {
      html = html.replace("{{REQUIRED_LEVEL}}", "Не найден");
      html = html.replace("{{REQUIRED_ITEMS}}", "<tr><td colspan=2>Требования не найдены</td></tr>");
    }
    
    html = html.replace("%navigation%", navigation);
    // Отправляем HTML-контент игроку
    CommunityBoardHandler.separateAndSend(html, player);
  }
  
  private String loadHtmlTemplate(String filePath)
  {
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
    {
      String line;
      while ((line = reader.readLine()) != null)
      {
        content.append(line).append("\n");
      }
    }
    catch (IOException e)
    {
      System.err.println("Ошибка при загрузке HTML шаблона: " + e.getMessage());
    }
    return content.toString();
  }
}
