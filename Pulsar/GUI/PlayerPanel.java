/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;
import java.awt.event.*;
import Pulsar.Engine.*;
import java.awt.*;

public class PlayerPanel extends MessagePanel
{
   private static final int WIDTH_TILES = (TERMINAL_WIDTH_TILES - (MAP_HEIGHT_TILES * 2) - 4) / 2;
   private static final int HEIGHT_TILES = MAP_HEIGHT_TILES;
   private static final int X_ORIGIN = 1;
   private static final int Y_ORIGIN = 1;
   private static boolean redrawF = false;
   
   public PlayerPanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', TERMINAL_FG_COLOR, BG_COLOR);
      }
      write(X_ORIGIN + 4, Y_ORIGIN, "Player Panel", 12, 1);
      redrawF = true;
   }
   
   public static void updatePlayerPanel()
   {
      redrawF = true;
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
      redrawF = false;
      clearPlayerPanel();
      write(X_ORIGIN, Y_ORIGIN, "Player Panel", WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 1, "Shield [        ]", SHIELD_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      write(X_ORIGIN, Y_ORIGIN + 2, "Health [        ]", HEALTH_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      
      int[] shieldBar = GameEngine.getPlayer().getShieldBar(8);
      int[] healthBar = GameEngine.getPlayer().getHealthBar(8);
      for(int i = 0; i < 8; i++)
      {
         setTile(X_ORIGIN + 8 + i, Y_ORIGIN + 1, shieldBar[i], SHIELD_COLOR, BG_COLOR);
         setTile(X_ORIGIN + 8 + i, Y_ORIGIN + 2, healthBar[i], HEALTH_COLOR, BG_COLOR);
      }
      
      Color c = new Color(Integer.MAX_VALUE);
      System.out.println(c.getRGB());
      System.out.println(Integer.MAX_VALUE);
      
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(redrawF)
         update();
      super.actionPerformed(ae);
   }
}