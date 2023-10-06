package Pulsar.GUI;

import Pulsar.Gear.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.awt.event.*;

public class InventoryPanel extends SelectionPanel
{
   public static final int PRIMARY_WEAPON = 0;
   public static final int SECONDARY_WEAPON = 1;
   public static final int ARMOR = 2;
   public static final int SHIELD = 3;
   public static final int GADGET = 4;
   
   private Player player;
   private int spacerIndex = 5;
   
   public void set()
   {
      player = GameEngine.getPlayer();
      spacerIndex = 4 + player.getMaxGadgets();
      int listLen = spacerIndex + player.getInventory().getMaxSize() + 1;
      options = new String[listLen];
      for(int i = 0; i < listLen; i++)
      {
         options[i] = "";
      }
      options[PRIMARY_WEAPON] = "Primary Weapon: " + player.getPrimaryWeapon().getName();
      String str = "Secondary Weapon: ";
      if(player.getSecondaryWeapon() != null)
         str += player.getSecondaryWeapon().getName();
      else
         str += "None";
      options[SECONDARY_WEAPON] = str;
      str = "Armor: ";
      if(player.hasArmor())
         str += player.getArmor().getName();
      else
         str += "None";
      options[ARMOR] = str;
      str = "Shield: ";
      if(player.hasShield())
         str += player.getShield().getName();
      else
         str += "None";
      options[SHIELD] = str;
      for(int i = 0; i < player.getMaxGadgets(); i++)
      {
         str = "Gadget: ";
         if(player.getGadget(i) != null)
            str += player.getGadget(i).getName();
         else
            str += "None";
         options[GADGET + i] = str;
      }
      Inventory inventory = player.getInventory();
      int ySpace = spacerIndex + 1;
      for(int i = 0; i < player.getInventory().getMaxSize(); i++)
      {
         str = "";
         if(inventory.getItem(i) != null)
            str = inventory.getItem(i).getName();
         else
            str += "<Empty>";
         options[ySpace + i] = str;
      }
      
      update();
   }
   
   public void update()
   {
      int fgColor = TERMINAL_FG_COLOR.getRGB();
      int bgColor = BG_COLOR.getRGB();
      for(int i = 0; i < options.length; i++)
      {
         if(selectionIndex == i)
            bgColor = ORANGE.getRGB();
         else
            bgColor = BG_COLOR.getRGB();
         write(X_ORIGIN, Y_ORIGIN + i, options[i], fgColor, bgColor, options[i].length(), 1);
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
   
   public void next()
   {
      selectionIndex++;
      if(selectionIndex == spacerIndex)
         selectionIndex++;
      if(selectionIndex >= options.length)
         selectionIndex = 0;
   }
   
   public void previous()
   {
      selectionIndex--;
      if(selectionIndex == spacerIndex)
         selectionIndex--;
      if(selectionIndex < 0)
         selectionIndex = options.length - 1;
   }
}