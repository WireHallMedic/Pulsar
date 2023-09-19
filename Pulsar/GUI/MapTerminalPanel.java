/*
Displaying terminals the player encounters during missions
*/


package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import Pulsar.Engine.*;


public class MapTerminalPanel extends RogueTilePanel implements GUIConstants
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   private static Vector<String> pageList = null;
   private int curPageIndex;
   
   public static void setPageList(Vector<String> pl){pageList = pl;}
   
   
   public MapTerminalPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      setBorder();
      setBackground(BG_COLOR);
      update();
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
         case KeyEvent.VK_ENTER  :
         case KeyEvent.VK_SPACE  :
         case KeyEvent.VK_ESCAPE :  InnerPanel.setActivePanel(MainGameBGPanel.class); 
                                    GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
                                    break;
         case KeyEvent.VK_NUMPAD4 :
         case KeyEvent.VK_LEFT :    previousPage();
                                    break;
         case KeyEvent.VK_NUMPAD6 :
         case KeyEvent.VK_RIGHT :   nextPage();
                                    break;
      }
   }
   
   public void nextPage()
   {
      if(pageList == null)
         return;
      if(curPageIndex < pageList.size() - 1)
         curPageIndex++;
      update();
   }
   
   public void previousPage()
   {
      if(pageList == null)
         return;
      if(curPageIndex > 0)
         curPageIndex--;
      update();
   }
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
      {
         curPageIndex = 0;
         update();
      }
      super.setVisible(v);
   }
   
   public void update()
   {
      clear();
      String str = "No data.";
      if(pageList != null)
         str = pageList.elementAt(curPageIndex);
      write(X_ORIGIN, Y_ORIGIN, str, WIDTH_TILES, HEIGHT_TILES);
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