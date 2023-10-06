package Pulsar.GUI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.awt.event.*;

public class InventoryPanel extends SelectionPanel
{
   private Player player;
   
   public void set()
   {
      player = GameEngine.getPlayer();
      int listLen = 2 + 1 + 1 + player.getMaxGadgets();
      options = new String[listLen];
      for(int i = 0; i < listLen; i++)
         options[i] = "Pants";
      update();
   }
   
   public void update()
   {
      int fgColor = TERMINAL_FG_COLOR.getRGB();
      int bgColor = BG_COLOR.getRGB();
      String secondaryWeapon = "None";
      write(X_ORIGIN, Y_ORIGIN, "Primary Weapon: " + player.getPrimaryWeapon().getName(), fgColor, bgColor, WIDTH_TILES, 1);
      if(player.getSecondaryWeapon() != null)
         secondaryWeapon = player.getSecondaryWeapon().getName();
      write(X_ORIGIN, Y_ORIGIN + 1, "Secondary Weapon: " + secondaryWeapon, fgColor, bgColor, WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 2, "Armor: " + player.getArmor().getName(), fgColor, bgColor, WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 3, "Shield: " + player.getShield().getName(), fgColor, bgColor, WIDTH_TILES, 1);
      for(int i = 0; i < player.getMaxGadgets(); i++)
      {
         String gadgetStr = "<None>";
         if(player.getGadget(i) != null)
            gadgetStr = player.getGadget(i).getName();
         write(X_ORIGIN, Y_ORIGIN + 4 + i, gadgetStr, fgColor, bgColor, gadgetStr.length(), 1);
      }
      for(int i = 0; i < options.length; i++)
      {
         write(X_ORIGIN, Y_ORIGIN + 5 + player.getMaxGadgets() + i, options[i], fgColor, bgColor, options[i].length(), 1);
      }
   }
   
   public void doSelection()
   {
   
   }
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
         set();
      super.setVisible(v);
   }
   
   @Override
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_I :
         case KeyEvent.VK_ESCAPE : InnerPanel.setActivePanel(MainGameBGPanel.class); 
                                   GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
                                   return;
      }
      super.keyPressed(ke);
   }
}