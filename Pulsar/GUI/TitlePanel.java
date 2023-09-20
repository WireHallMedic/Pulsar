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
      GUITools.setStandardBorder(this);
      setBackground(BG_COLOR);
      write(1, 1, "Title Panel", TERMINAL_WIDTH_TILES - 2, 1);
   }
   
   public void keyPressed(KeyEvent ke)
   {
      InnerPanel.setActivePanel(MainGameBGPanel.class); 
      GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(isVisible())
         super.actionPerformed(ae);
   }
}