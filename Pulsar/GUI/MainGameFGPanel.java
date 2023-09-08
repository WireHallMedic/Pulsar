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
import java.util.*;

public class MainGameFGPanel extends RogueTilePanel implements GUIConstants, EngineConstants
{
   public static final Color[] TARGETING_GRADIENT = WSTools.getGradient(Color.BLACK, RETICULE_COLOR, 21);
   public static final Color[] BAD_TARGETING_GRADIENT = WSTools.getGradient(Color.BLACK, INVALID_RETICULE_COLOR, 21);
   
	public MainGameFGPanel()
   {
      super(MAP_WIDTH_TILES + 2, MAP_HEIGHT_TILES + 2, SQUARE_TILE_PALETTE);
      setSize(50, 50);
      drawLockListFirst();
      
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
         int xCorner = player.getSpriteXLoc() - (columns() / 2);
         int yCorner = player.getSpriteYLoc() - (rows() / 2);
         double xScroll = 0.0 - player.getSprite().getXOffset();
         double yScroll = 0.0 - player.getSprite().getYOffset();
         setCornerTile(xCorner, yCorner);
         setScroll(xScroll, yScroll);
         
         for(int x = 0; x < columns(); x++)
         for(int y = 0; y < rows(); y++)
         {
            if(GameEngine.playerCanSee(x + xCorner, y + yCorner))
               {
               tile = zoneMap.getTile(x + xCorner, y + yCorner);
               setTile(x, y, tile.getIconIndex(), tile.getFGColor(), tile.getBGColor());
            }
            else
            {
               setTile(x, y, ' ', Color.WHITE, BG_COLOR);
            }
         }
         
         if(GameEngine.getGameMode() == GameMode.LOOK)
         {
            Coord cursorLoc = GameEngine.getCursorLoc();
            if(cursorLoc != null)
            {
               cursorLoc.x -= xCorner;
               cursorLoc.y -= yCorner;
               int fgColor = getFGColor(cursorLoc);
               int iconIndex = getIcon(cursorLoc);
               setTile(cursorLoc.x, cursorLoc.y, iconIndex, fgColor, TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB());
            }
         }
         if(GameEngine.getGameMode() == GameMode.TARGETING)
         {
            Coord cursorLoc = GameEngine.getCursorLoc();
            if(cursorLoc != null)
            {
               cursorLoc.x -= xCorner;
               cursorLoc.y -= yCorner;
               Coord playerLoc = GameEngine.getPlayer().getMapLoc();
               playerLoc.x -= xCorner;
               playerLoc.y -= yCorner;
               Vector<Coord> targetingLine = StraightLine.findLine(playerLoc, cursorLoc, StraightLine.REMOVE_ORIGIN);
               if(targetingLine.size() == 0)
                  targetingLine.add(playerLoc);
               boolean clearPath = true;
               for(Coord c : targetingLine)
               {
                  int fgColor = getFGColor(c);
                  int iconIndex = getIcon(c);
                  int bgColor = BAD_TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB();
                  if(clearPath)
                     bgColor = TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB();
                  setTile(c.x, c.y, iconIndex, fgColor, bgColor);
                  if(GameEngine.blocksShooting(c.x + xCorner, c.y + yCorner))
                     clearPath = false;
               }
            }
         }
         if(GameEngine.getGameMode() == GameMode.STANDARD)
         {
            
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
   
   public boolean isOnLockList(Actor a){return isOnLockList(a.getSprite());}
   public boolean isOnLockList(UnboundTile ut)
   {
      return animationManager.getLockList().contains(ut);
   }
   
   public boolean shouldDelayRemoval(UnboundTile ut)
   {
      return isOnLockList(ut) || hasScriptFor(ut);
   }
   
   public boolean hasScriptFor(UnboundTile ut)
   {
      for(MovementScript script : animationManager.getScriptList())
      {
         if(script.getTarget() == ut)
            return true;
      }
      return false;
   }
   
   // actors are removed by expiration to avoid being readded by movement scripts
   public void remove(Actor a)
   {
      a.getSprite().forceExpire();
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