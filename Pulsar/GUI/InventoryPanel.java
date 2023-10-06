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
   
   public static final int NORMAL_MODE = 0;
   public static final int CHOOSE_SLOT_MODE = 1;
   
   private Player player;
   private int spacerIndex = 5;
   private boolean doDrop = false;
   private int mode = NORMAL_MODE;
   
   public void set()
   {
      mode = NORMAL_MODE;
      for(int x = X_ORIGIN; x < X_ORIGIN + WIDTH_TILES; x++)
      for(int y = Y_ORIGIN; y < Y_ORIGIN + HEIGHT_TILES; y++)
         setTile(x, y, ' ', TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB());
         
      player = GameEngine.getPlayer();
      spacerIndex = 4 + player.getMaxGadgets();
      int listLen = spacerIndex + player.getInventory().getMaxSize() + 1;
      options = new String[listLen];
      for(int i = 0; i < listLen; i++)
      {
         options[i] = "";
      }
      String str = "Primary Weapon: ";
      if(player.getPrimaryWeapon() != null)
         str += player.getPrimaryWeapon().getName();
      else
         str += "None";
      options[PRIMARY_WEAPON] = str;
      str = "Secondary Weapon: ";
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
         if(curSelection instanceof Gadget)
         {
            Gadget gad = (Gadget)curSelection;
            name = gad.getSummary();
            summary = gad.getDescription();
         }
      }
      write(X_ORIGIN + WIDTH_TILES - DESCRIPTION_WIDTH, Y_ORIGIN, name, fgColor, bgColor, DESCRIPTION_WIDTH, 1);
      write(X_ORIGIN + WIDTH_TILES - DESCRIPTION_WIDTH, Y_ORIGIN + 2, summary, fgColor, bgColor, DESCRIPTION_WIDTH, 10);
      
      if(mode == NORMAL_MODE)
      {
         String[] strArr = {"[D] to Drop, ", "[E or ENTER] to Equip, ", "[ESC] to Exit"};
         int[] fgColorArr = {fgColor, fgColor, fgColor};
         if(selectionIndex < spacerIndex)
         {
            strArr[1] = "[E or ENTER] to Unquip, ";
            if(player.getInventory().isFull())
               fgColorArr[1] = INVALID_SELECTION_COLOR.getRGB();
         }
         if(curSelection == null)
         {
            fgColorArr[0] = INVALID_SELECTION_COLOR.getRGB();
            fgColorArr[1] = INVALID_SELECTION_COLOR.getRGB();
         }
         int xInset = 0;
         for(int i = 0; i < strArr.length; i++)
         {
            write(X_ORIGIN + xInset, Y_ORIGIN + HEIGHT_TILES - 1, strArr[i], fgColorArr[i], bgColor, WIDTH_TILES - xInset, 1);
            xInset += strArr[i].length();
         }
      }
      else if(mode == CHOOSE_SLOT_MODE)
      {
         String str = "";
         if(getSelectedGear() instanceof Weapon)
         {
            str = "[P]rimary or [S]econdary slot? ([ESCAPE] to cancel)";
         }
         else
            str = "Which gadget slot [1-" + player.getMaxGadgets() + "]? ([ESCAPE] to cancel)";
         write(X_ORIGIN, Y_ORIGIN + HEIGHT_TILES - 1, str, ORANGE.getRGB(), bgColor, WIDTH_TILES, 1);
      }
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
   
   private int getAutomaticSlot(GearObj obj)
   {
      if(obj instanceof Weapon)
      {
         if(!player.hasPrimaryWeapon())
            return 0;
         if(!player.hasSecondaryWeapon())
            return 1;
      }
      if(obj instanceof Gadget)
      {
         if(player.getGadgetList().size() < player.getMaxGadgets())
            return player.getGadgetList().size();
      }
      if(obj instanceof Shield ||
         obj instanceof Armor)
         return 0;
      return -1;
   }
   
   public void doSelection()
   {
      if(getSelectedGear() == null)
         return;
      if(doDrop)
      {
         player.drop(getSelectedGear());
         doDrop = false;
      }
      else
      {
         // unequip equipped item
         if(selectionIndex < spacerIndex)
         {
            if(!player.getInventory().isFull())
            {
               player.unequip(getSelectedGear());
            }
            else
               return;
         }
         // equip unequipped item
         else
         {
            int slot = getAutomaticSlot(getSelectedGear());
            if(slot != -1)
               player.equip(getSelectedGear(), 0);
            else
            {
               mode = CHOOSE_SLOT_MODE;
               return;
            }
         }
      }
      exitToMainGame();
   }
   
   public void exitToMainGame()
   {
      InnerPanel.setActivePanel(MainGameBGPanel.class); 
      GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
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
      if(mode == NORMAL_MODE)
      {
         switch(ke.getKeyCode())
         {
            case KeyEvent.VK_I :
            case KeyEvent.VK_ESCAPE :  exitToMainGame();
                                       return;
            case KeyEvent.VK_ENTER  :
            case KeyEvent.VK_SPACE  :  doSelection(); 
                                       return;
            case KeyEvent.VK_D      :  doDrop = true;
                                       doSelection(); 
                                       return;
         }
         super.keyPressed(ke);
      }
      else if(mode == CHOOSE_SLOT_MODE)
      {
         switch(ke.getKeyCode())
         {
            case KeyEvent.VK_ESCAPE :  mode = NORMAL_MODE;
                                       break;
            case KeyEvent.VK_P  :      if(getSelectedGear() instanceof Weapon)
                                       {
                                          player.equip(getSelectedGear(), 0);
                                          mode = NORMAL_MODE;
                                          exitToMainGame();
                                       }
                                       break;
            case KeyEvent.VK_S  :      if(getSelectedGear() instanceof Weapon)
                                       {
                                          player.equip(getSelectedGear(), 1);
                                          mode = NORMAL_MODE;
                                          exitToMainGame();
                                       }
                                       break;
            case KeyEvent.VK_1  :
            case KeyEvent.VK_2  :
            case KeyEvent.VK_3  :
            case KeyEvent.VK_4  :
            case KeyEvent.VK_5  :      if(getSelectedGear() instanceof Gadget)
                                       {
                                          int slot = ke.getKeyCode() - KeyEvent.VK_1;
                                          if(slot < player.getMaxGadgets())
                                          {
                                             player.equip(getSelectedGear(), slot);
                                             mode = NORMAL_MODE;
                                             exitToMainGame();
                                          }
                                       }
                                       break;
         }
      }
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