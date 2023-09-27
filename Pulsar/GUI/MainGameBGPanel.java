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
   private static Color borderFlashColor = null;
   private static int borderFlashDuration = 0;
   private static int borderFlashDelay = 0;
   
   public MainGameBGPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      setBorder(BG_COLOR);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(isVisible())
      {
         if(borderFlashColor != null && borderFlashDelay == 0)
         {
            setBorder(borderFlashColor);
         }
         super.actionPerformed(ae);
      }
      
      if(borderFlashDelay == 0 && borderFlashDuration > 0)
      {
         borderFlashDuration--;
         if(borderFlashDuration == 0)
         {
            borderFlashColor = null;
            setBorder(BG_COLOR);
         }
      }
      
      if(borderFlashDelay > 0)
      {
         borderFlashDelay--;
      }
   }
   
   public static void setBorderFlash(Color c){setBorderFlash(c, 0);}
   public static void setBorderFlash(Color c, int delay)
   {
      borderFlashDelay = delay;
      borderFlashDuration = 2;
      borderFlashColor = c;
   }
   
   private void setBorder(Color bgColor)
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
            setTile(x, y, borderArr[x][y], TERMINAL_FG_COLOR, bgColor);
         }
      }
      this.repaint();
   }
}