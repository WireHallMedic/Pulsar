/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;
import java.awt.event.*;
import Pulsar.Engine.*;
import Pulsar.Actor.*;
import java.awt.*;
import java.util.*;

public class PlayerPanel extends MessagePanel
{
   private static final int WIDTH_TILES = (TERMINAL_WIDTH_TILES - (MAP_HEIGHT_TILES * 2) - 4) / 2;
   private static final int HEIGHT_TILES = MAP_HEIGHT_TILES;
   private static final int X_ORIGIN = 1;
   private static final int Y_ORIGIN = 1;
   public static final Color[] SHIELD_WARNING_GRADIENT = WSTools.getGradient(SHIELD_COLOR, WARNING_COLOR, 21);
   public static final Color[] AMMO_WARNING_GRADIENT = WSTools.getGradient(TERMINAL_FG_COLOR, WARNING_COLOR, 21);
   
   public PlayerPanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', TERMINAL_FG_COLOR, BG_COLOR);
      }
      write(X_ORIGIN + 4, Y_ORIGIN, "Player Panel", 12, 1);
   }
   
   public void clearPlayerPanel()
   {
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, ' ', TERMINAL_FG_COLOR, BG_COLOR);
      }
   }
   
   public void update()
   {
      clearPlayerPanel();
      
      // basic status
      Player player = GameEngine.getPlayer();
      Color shieldColor = SHIELD_COLOR;
      Color mainWeaponColor = TERMINAL_FG_COLOR;
      Color altWeaponColor = TERMINAL_FG_COLOR;
      if(player.hasShield() && player.getShield().getCurCharge() == 0)
         shieldColor = animationManager.fastBlink() ? SHIELD_COLOR : WARNING_COLOR;
      if(player.getWeapon() != null && player.getWeapon().getRemainingShots() == 0)
         mainWeaponColor = animationManager.fastBlink() ? TERMINAL_FG_COLOR : WARNING_COLOR;
      if(player.getAltWeapon() != null && player.getAltWeapon().getRemainingShots() == 0)
         altWeaponColor = animationManager.fastBlink() ? TERMINAL_FG_COLOR : WARNING_COLOR;
      write(X_ORIGIN, Y_ORIGIN, "Player Panel", WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 1, "Health[        ]", HEALTH_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 2, "Shield[        ]", shieldColor.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 3, "Weapon[        ]", mainWeaponColor.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 4, "AltWpn[        ]", altWeaponColor.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      int[] ammoCountArr = player.getWeapon().getAmmoCountTiles();
      for(int i = 0; i < ammoCountArr.length; i++)
         setTile(X_ORIGIN + 16 + i, Y_ORIGIN + 3, ammoCountArr[i], mainWeaponColor, BG_COLOR);
      ammoCountArr = player.getAltWeapon().getAmmoCountTiles();
      for(int i = 0; i < ammoCountArr.length; i++)
         setTile(X_ORIGIN + 16 + i, Y_ORIGIN + 4, ammoCountArr[i], altWeaponColor, BG_COLOR);
      
      int[] shieldBar = player.getShieldBar(8);
      int[] healthBar = player.getHealthBar(8);
      int[] weaponBar = player.getWeaponBar(8);
      int[] altWeaponBar = player.getAltWeaponBar(8);
      for(int i = 0; i < 8; i++)
      {
         setTile(X_ORIGIN + 7 + i, Y_ORIGIN + 1, healthBar[i], HEALTH_COLOR, BG_COLOR);
         setTile(X_ORIGIN + 7 + i, Y_ORIGIN + 2, shieldBar[i], shieldColor, BG_COLOR);
         setTile(X_ORIGIN + 7 + i, Y_ORIGIN + 3, weaponBar[i], mainWeaponColor, BG_COLOR);
         setTile(X_ORIGIN + 7 + i, Y_ORIGIN + 4, altWeaponBar[i], altWeaponColor, BG_COLOR);
      }
      
      write(X_ORIGIN, Y_ORIGIN + 5, player.getWeapon().getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 6, player.getWeapon().getSummary(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 3);
      
      int additionalYSpacing = 0;
      for(int i = 0; i < 3; i++)
         if(getIcon(X_ORIGIN, Y_ORIGIN + 7 + i) != ' ')
            additionalYSpacing++;
            
      // gadgets
      int totalGadgets = player.getGadgetList().size();
      for(int i = 0; i < totalGadgets; i++)
      {
         write(X_ORIGIN, Y_ORIGIN + 7 + additionalYSpacing, (1 + i) + ": " + player.getGadget(i).getShortSummary(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
         additionalYSpacing++;
      }
      
      // temporary status effects
      Vector<StatusEffect> statusEffectList = player.getTemporaryStatusEffects();
      for(int i = 0; i < statusEffectList.size(); i++)
      {
         StatusEffect se = statusEffectList.elementAt(i);
         Color c = TERMINAL_FG_COLOR;
         if(se.isNegative())
            c = WARNING_COLOR;
         write(X_ORIGIN, Y_ORIGIN + 7 + additionalYSpacing, se.getName() + " " + GUITools.initToSec(se.getRemainingDuration()), c.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
         additionalYSpacing++;
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      update();
      super.actionPerformed(ae);
   }
}