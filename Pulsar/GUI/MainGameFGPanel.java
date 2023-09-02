/*
This is the map portion of the main game screen.
*/

package Pulsar.GUI;

import Pulsar.Engine.*;
import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainGameFGPanel extends RogueTilePanel implements GUIConstants
{
   public MainGameFGPanel()
   {
      super(MAP_WIDTH_TILES, MAP_HEIGHT_TILES, SQUARE_TILE_PALETTE);
      setSize(50, 50);
      
      for(int x = 0; x < MAP_WIDTH_TILES; x++)
      for(int y = 0; y < MAP_HEIGHT_TILES; y++)
      {
         setTile(x, y, '.', Color.YELLOW, Color.BLACK);
      }
      
      GameEngine.setMapPanel(this);
   }
}