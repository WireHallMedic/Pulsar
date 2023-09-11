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


public class HelpPanel extends RogueTilePanel implements GUIConstants
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   private String[][] pageArray;
   private int curPageIndex;
   
   
   public HelpPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      setBorder();
      setBackground(BG_COLOR);
      pageArray = getPages();
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
         case KeyEvent.VK_ESCAPE :
         case KeyEvent.VK_H : InnerPanel.setActivePanel(MainGameBGPanel.class); 
                              GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
                              break;
         case KeyEvent.VK_NUMPAD4 :
         case KeyEvent.VK_LEFT : previousPage();
                                 break;
         case KeyEvent.VK_NUMPAD6 :
         case KeyEvent.VK_RIGHT : nextPage();
                                  break;
      }
   }
   
   public void nextPage()
   {
      curPageIndex++;
      if(curPageIndex >= pageArray.length)
         curPageIndex = 0;
      update();
   }
   
   public void previousPage()
   {
      curPageIndex--;
      if(curPageIndex < 0)
         curPageIndex = pageArray.length - 1;
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
      String[] pageText = pageArray[curPageIndex];
      for(int i = 0; i < pageText.length; i++)
         write(X_ORIGIN, Y_ORIGIN + i, pageText[i], WIDTH_TILES, 1);
   }
   
   public void clear()
   {
      for(int x = X_ORIGIN; x < WIDTH_TILES; x++)
      for(int y = Y_ORIGIN; y < HEIGHT_TILES; y++)
      {
         setTile(x, y, ' ', TERMINAL_FG_COLOR, BG_COLOR);
      }
   }
   
   public String[][] getPages()
   {
      String[] pageOne = {
      "Page One",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "Press Escape to exit"
      };
      String[] pageTwo = {
      "Page Two",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "Press Escape to exit"
      };
      String[] pageThree = {
      "Page Three",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "Press Escape to exit"
      };
      String[][] pageArr = {pageOne, pageTwo, pageThree};
      return pageArr;
   }
}