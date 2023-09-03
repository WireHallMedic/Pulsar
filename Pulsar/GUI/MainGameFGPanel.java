/*
This is the map portion of the main game screen.
*/

package Pulsar.GUI;

import Pulsar.Engine.*;
import Pulsar.Zone.*;
import Pulsar.Actor.*;
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
      
      GameEngine.setMapPanel(this);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      ZoneMap zoneMap = GameEngine.getZoneMap();
      Actor player = GameEngine.getPlayer();
      
      if(zoneMap != null && player != null)
      {
         MapTile tile;
         for(int x = 0; x < getWidth(); x++)
         for(int y = 0; y < getHeight(); y++)
         {
            tile = zoneMap.getTile(x, y);
            setTile(x, y, tile.getIconIndex(), tile.getFGColor(), tile.getBGColor());
         }
      }
      
      super.actionPerformed(ae);
   }
}