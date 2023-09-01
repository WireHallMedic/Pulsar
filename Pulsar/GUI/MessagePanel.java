/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;

public class MessagePanel extends InfoPanel
{
   private static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   private static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - MAP_HEIGHT_TILES - 3;
   private static final int X_ORIGIN = 1;
   private static final int Y_ORIGIN = TERMINAL_HEIGHT_TILES - HEIGHT_TILES - 1;
   
   public MessagePanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', TERMINAL_FG_COLOR, BG_COLOR);
      }
      write(X_ORIGIN + ((WIDTH_TILES - 14) / 2), Y_ORIGIN, "Message Panel", 13, 1);
   }
}