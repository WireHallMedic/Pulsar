/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;

public class PlayerPanel extends MessagePanel
{
   private static final int WIDTH_TILES = (TERMINAL_WIDTH_TILES - (MAP_HEIGHT_TILES * 2) - 4) / 2;
   private static final int HEIGHT_TILES = MAP_HEIGHT_TILES;
   private static final int X_ORIGIN = 1;
   private static final int Y_ORIGIN = 1;
   
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
}