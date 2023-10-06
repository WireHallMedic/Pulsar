package Pulsar.GUI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.awt.event.*;

public class InventoryPanel extends SelectionPanel
{
   public static final int PRIMARY_WEAPON = 0;
   public static final int SECONDARY_WEAPON = 1;
   public static final int ARMOR = 2;
   public static final int SHIELD = 3;
   public static final int GADGET = 4
   ;
   private Player player;
   
   public void set()
   {
      player = GameEngine.getPlayer();
      int listLen = 2 + 1 + 1 + player.getMaxGadgets();
      options = new String[listLen];
      for(int i = 0; i < listLen; i++)
      {
         options[i] = "Pants";
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
      
      update();
   }
   
   public void update()
   {
      int fgColor = TERMINAL_FG_COLOR.getRGB();
      int bgColor = BG_COLOR.getRGB();
      for(int i = 0; i < options.length; i++)
      {
         write(X_ORIGIN, Y_ORIGIN + 2 + i, options[i], fgColor, bgColor, options[i].length(), 1);
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