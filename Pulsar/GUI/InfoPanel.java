/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;

public class InfoPanel extends RogueTilePanel implements GUIConstants
{
   private static final int WIDTH_TILES = (TERMINAL_WIDTH_TILES - (MAP_HEIGHT_TILES * 2) - 4) / 2;
   private static final int HEIGHT_TILES = MAP_HEIGHT_TILES;
   private static final int X_ORIGIN = TERMINAL_WIDTH_TILES - WIDTH_TILES - 1;
   private static final int Y_ORIGIN = 1;
   
   public InfoPanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', TERMINAL_FG_COLOR, BG_COLOR);
      }
      write(X_ORIGIN + 5, Y_ORIGIN, "Info Panel", 10, 1);
   }
}