/*
Displaying character attributes and gear
*/


package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import Pulsar.Engine.*;


public class CharacterPanel extends RogueTilePanel implements GUIConstants
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   
   
   public CharacterPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      setBorder();
      setBackground(BG_COLOR);
      write(1, 1, "Character Panel", TERMINAL_WIDTH_TILES - 2, 1);
   }
   
   private void setBorder()
   {
      int[][] borderTemplate = new int[TERMINAL_WIDTH_TILES][TERMINAL_HEIGHT_TILES];
      for(int x = 0; x < TERMINAL_WIDTH_TILES; x++)
      {
         borderTemplate[x][0] = 1;
         borderTemplate[x][TERMINAL_HEIGHT_TILES - 1] = 1;
      }
      for(int y = 0; y < TERMINAL_HEIGHT_TILES; y++)
      {
         borderTemplate[0][y] = 1;
         borderTemplate[TERMINAL_WIDTH_TILES - 1][y] = 1;
      }
      int[][] borderArr = BorderBuilder.getBorderTiles(borderTemplate);
      for(int x = 0; x < TERMINAL_WIDTH_TILES; x++)
      for(int y = 0; y < TERMINAL_HEIGHT_TILES; y++)
      {
         if(borderTemplate[x][y] != 0)
         {
            setTile(x, y, borderArr[x][y], TERMINAL_FG_COLOR, BG_COLOR);
         }
      }
   }
   
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_ESCAPE :
         case KeyEvent.VK_C : InnerPanel.setActivePanel(MainGameBGPanel.class); 
                              GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
                              break;
      }
   }
   
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
      
      String speedStr = "Attack Speed: " + GUITools.initToSec(player.getAttackSpeed()) + "     " +
                        "Movement Speed: " + GUITools.initToSec(player.getMoveSpeed()) + "     " +
                        "Interact Speed: " + GUITools.initToSec(player.getInteractSpeed());
      write(X_ORIGIN, Y_ORIGIN + 6 + additionalYSpacing, speedStr, TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
   }
   
   public void clear()
   {
      for(int x = X_ORIGIN; x < WIDTH_TILES; x++)
      for(int y = Y_ORIGIN; y < HEIGHT_TILES; y++)
      {
         setTile(x, y, ' ', TERMINAL_FG_COLOR, BG_COLOR);
      }
   }
}