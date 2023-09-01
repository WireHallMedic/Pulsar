/*
This is the non-map portion of the main game screen.
*/

package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainGameBGPanel extends PlayerPanel implements GUIConstants
{
   public MainGameBGPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      setBorder();
   }
   
   private void setBorder()
   {
      int[][] borderTemplate = new int[TERMINAL_WIDTH_TILES][TERMINAL_HEIGHT_TILES];
      for(int x = 0; x < TERMINAL_WIDTH_TILES; x++)
      {
         borderTemplate[x][0] = 1;
         borderTemplate[x][MAP_WIDTH_TILES + 1] = 1;
         borderTemplate[x][TERMINAL_HEIGHT_TILES - 1] = 1;
      }
      for(int y = 0; y < TERMINAL_HEIGHT_TILES; y++)
      {
         borderTemplate[0][y] = 1;
         borderTemplate[TERMINAL_WIDTH_TILES - 1][y] = 1;
      }
      for(int y = 0; y < MAP_HEIGHT_TILES + 1; y++)
      {
         borderTemplate[MAP_X_INSET_TILES - 1][y] = 1;
         borderTemplate[TERMINAL_WIDTH_TILES - MAP_X_INSET_TILES][y] = 1;
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
}