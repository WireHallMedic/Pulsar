/*
Displaying character attributes and gear
*/


package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import Pulsar.Engine.*;


public class CharacterPanel extends RogueTilePanel implements GUIConstants, KeyListener
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   
   
   public CharacterPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      GUITools.setStandardBorder(this);
      setBackground(BG_COLOR);
      write(1, 1, "Character Panel", TERMINAL_WIDTH_TILES - 2, 1);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(isVisible())
         super.actionPerformed(ae);
   }
      
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_ESCAPE :
         case KeyEvent.VK_ENTER  :
         case KeyEvent.VK_SPACE  :
         case KeyEvent.VK_C : InnerPanel.setActivePanel(MainGameBGPanel.class); 
                              GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
                              break;
      }
   }
   public void keyTyped(KeyEvent ke){}
   public void keyReleased(KeyEvent ke){}
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
      {
         update();
      }
      super.setVisible(v);
   }
   
   public void update()
   {
      clear();
      Player player = GameEngine.getPlayer();
      int additionalYSpacing = 0;
      write(X_ORIGIN, Y_ORIGIN, player.getName(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 1, "Health [        ]", HEALTH_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 2, "Shield [        ]", SHIELD_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 3, "Weapon [        ]", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
         
      write(X_ORIGIN + 18, Y_ORIGIN + 3, player.getWeapon().getAmmoCount(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), 5, 1);
      
      int[] shieldBar = player.getShieldBar(8);
      int[] healthBar = player.getHealthBar(8);
      int[] weaponBar = player.getWeaponBar(8);
      int[] altWeaponBar = player.getAltWeaponBar(8);
      for(int i = 0; i < 8; i++)
      {
         setTile(X_ORIGIN + 8 + i, Y_ORIGIN + 1, healthBar[i], HEALTH_COLOR, BG_COLOR);
         setTile(X_ORIGIN + 8 + i, Y_ORIGIN + 2, shieldBar[i], SHIELD_COLOR, BG_COLOR);
         setTile(X_ORIGIN + 8 + i, Y_ORIGIN + 3, weaponBar[i], TERMINAL_FG_COLOR, BG_COLOR);
      }
      
      String healthString = player.getCurHealth() + "/" + player.getMaxHealth();
      write(X_ORIGIN + 18, Y_ORIGIN + 1, healthString, HEALTH_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 24, 2);
      if(player.getShield() != null)
      {
         String chargeSummary = player.getShield().getChargeSummary();
         if(chargeSummary.length() < 7)
            chargeSummary = " " + chargeSummary;
         write(X_ORIGIN + 17, Y_ORIGIN + 2, chargeSummary, SHIELD_COLOR.getRGB(), BG_COLOR.getRGB(), 7, 1);
         String shieldString = player.getShield().getName() + ": " + player.getShield().getShortSummary();
         write(X_ORIGIN + 24, Y_ORIGIN + 2, shieldString, TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 24, 2);
      }
      String weaponString = player.getWeapon().getName() + ": " + player.getWeapon().getSummary();
      write(X_ORIGIN + 24, Y_ORIGIN + 3, weaponString, TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 24, 2);
      if(getIcon(X_ORIGIN + 24, Y_ORIGIN + 4) != ' ')
         additionalYSpacing++;
      
      write(X_ORIGIN, Y_ORIGIN + 4 + additionalYSpacing, "AltWpn [        ]", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN + 18, Y_ORIGIN + 4 + additionalYSpacing, player.getAltWeapon().getAmmoCount(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), 5, 1);
      for(int i = 0; i < 8; i++)
      {
         setTile(X_ORIGIN + 8 + i, Y_ORIGIN + 4 + additionalYSpacing, altWeaponBar[i], TERMINAL_FG_COLOR, BG_COLOR);
      }
      weaponString = player.getAltWeapon().getName() + ": " + player.getAltWeapon().getSummary();
      write(X_ORIGIN + 24, Y_ORIGIN + 4 + additionalYSpacing, weaponString, TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 24, 2);
      if(getIcon(X_ORIGIN + 24, Y_ORIGIN + 5 + additionalYSpacing) != ' ')
         additionalYSpacing++;
      
      String armorStr = player.hasArmor() ? player.getArmor().getName() + ": " + player.getArmor().getSummary() : "No Armor";
      write(X_ORIGIN, Y_ORIGIN + 5 + additionalYSpacing, armorStr, TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      if(getIcon(X_ORIGIN + 24, Y_ORIGIN + 6 + additionalYSpacing) != ' ')
         additionalYSpacing++;
      
      String speedStr = "Attack Speed: " + GUITools.initToSec(player.getAttackSpeed().timeCost) + "     " +
                        "Movement Speed: " + GUITools.initToSec(player.getMoveSpeed().timeCost) + "     " +
                        "Interact Speed: " + GUITools.initToSec(player.getInteractSpeed().timeCost);
      write(X_ORIGIN, Y_ORIGIN + 6 + additionalYSpacing, speedStr, TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      
      // Gadgets
      for(int i = 0; i < player.getGadgetList().size(); i++)
      {
         Gadget g = player.getGadget(i);
         write(X_ORIGIN, Y_ORIGIN + 7 + additionalYSpacing, g.getSummary() + ": " + g.getDescription(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
         additionalYSpacing++;
      }
      
      // temporary status effects
      Vector<StatusEffect> statusEffectList = player.getTempStatusEffectList();
      for(int i = 0; i < statusEffectList.size(); i++)
      {
         StatusEffect se = statusEffectList.elementAt(i);
         Color c = TERMINAL_FG_COLOR;
         if(se.isNegative())
            c = WARNING_COLOR;
         write(X_ORIGIN, Y_ORIGIN + 8 + additionalYSpacing, se.getName() + " " + GUITools.initToSec(se.getRemainingDuration()), c.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
         additionalYSpacing++;
      }
      if(statusEffectList.size() > 0)
         additionalYSpacing++;
      
      // inventory
      write(X_ORIGIN, Y_ORIGIN + 8 + additionalYSpacing, player.getInventory().toString(),TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 2);
      additionalYSpacing++;
   }
   
   public void clear()
   {
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(X_ORIGIN + x, Y_ORIGIN + y, ' ', TERMINAL_FG_COLOR, BG_COLOR);
      }
   }
}