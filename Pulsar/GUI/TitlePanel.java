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


public class TitlePanel extends RogueTilePanel implements GUIConstants
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   
   
   public TitlePanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      setBorder();
      setBackground(BG_COLOR);
      write(1, 1, "Title Panel", TERMINAL_WIDTH_TILES - 2, 1);
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
      InnerPanel.setActivePanel(MainGameBGPanel.class); 
      GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
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