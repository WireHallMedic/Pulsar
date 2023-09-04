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
      super(MAP_WIDTH_TILES + 2, MAP_HEIGHT_TILES + 2, SQUARE_TILE_PALETTE);
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
         int x_corner = player.getSpriteXLoc() - (columns() / 2);
         int y_corner = player.getSpriteYLoc() - (rows() / 2);
         double xScroll = 0.0 - player.getSprite().getXOffset();
         double yScroll = 0.0 - player.getSprite().getYOffset();
         setCornerTile(x_corner, y_corner);
         setScroll(xScroll, yScroll);
         
         for(int x = 0; x < columns(); x++)
         for(int y = 0; y < rows(); y++)
         {
            if(GameEngine.playerCanSee(x + x_corner, y + y_corner))
               {
               tile = zoneMap.getTile(x + x_corner, y + y_corner);
               setTile(x, y, tile.getIconIndex(), tile.getFGColor(), tile.getBGColor());
            }
            else
            {
               setTile(x, y, ' ', Color.WHITE, BG_COLOR);
            }
         }
      }
      setActorVisibility();
      super.actionPerformed(ae);
   }
   
   public void addNonlocking(MovementScript ms)
   {
      add(ms);
   }
   
   public void addLocking(MovementScript ms)
   {
      UnboundTile ut = (UnboundTile)ms.getTarget();
      remove(ut);
      addLocking(ut);
      add(ms);
   }
   
   public boolean isOnLockList(Actor a)
   {
      return animationManager.getLockList().contains(a.getSprite());
   }
   
   private void setActorVisibility()
   {
      Actor player = GameEngine.getPlayer();
      for(Actor actor : GameEngine.getActorList())
      {
         if(player.canSee(actor))
            actor.getSprite().setVisible(true);
         else
            actor.getSprite().setVisible(false);
      }
   }
}