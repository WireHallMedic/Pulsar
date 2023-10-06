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
   public static final int DESCRIPTION_WIDTH = 25;
   
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
         str = "Gadget " + (i + 1) + ": ";
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
      fgColor = TERMINAL_FG_COLOR.getRGB();
      bgColor = BG_COLOR.getRGB();
      
      GearObj curSelection = getSelectedGear();
      String name = "";
      String summary = "";
      if(curSelection != null)
      {
         name = curSelection.getName();
         summary = curSelection.getSummary();
      }
      write(X_ORIGIN + WIDTH_TILES - DESCRIPTION_WIDTH, Y_ORIGIN, name, fgColor, bgColor, DESCRIPTION_WIDTH, 1);
      write(X_ORIGIN + WIDTH_TILES - DESCRIPTION_WIDTH, Y_ORIGIN + 1, summary, fgColor, bgColor, DESCRIPTION_WIDTH, 10);
      
      String str = "";
      if(selectionIndex < spacerIndex)
         str = "[D] to Drop, [E or ENTER] to Unquip, [ESC] to Exit";
      if(selectionIndex > spacerIndex)
         str ="[D] to Drop, [E or ENTER] to Equip, [ESC] to Exit";
      write(X_ORIGIN, Y_ORIGIN + HEIGHT_TILES - 1, str, fgColor, bgColor, WIDTH_TILES, 1);
   }
   
   public GearObj getSelectedGear()
   {
      if(selectionIndex == PRIMARY_WEAPON)
         return player.getPrimaryWeapon();
      if(selectionIndex == SECONDARY_WEAPON)
         return player.getSecondaryWeapon();
      if(selectionIndex == ARMOR)
         return player.getArmor();
      if(selectionIndex == SHIELD)
         return player.getShield();
      if(selectionIndex >= GADGET && selectionIndex < spacerIndex)
         return player.getGadget(selectionIndex - GADGET);
      if(selectionIndex > spacerIndex)
         return player.getInventory().getItem(selectionIndex - spacerIndex - 1);
      return null;
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