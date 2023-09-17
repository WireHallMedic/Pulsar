/*
This is the map portion of the main game screen.
*/

package Pulsar.GUI;

import Pulsar.Engine.*;
import Pulsar.Zone.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import Pulsar.AI.*;
import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class MainGameFGPanel extends RogueTilePanel implements GUIConstants, EngineConstants, GearConstants, AIConstants
{
   public static final Color[] TARGETING_GRADIENT = WSTools.getGradient(DEFAULT_TILE_BG_COLOR, RETICULE_COLOR, 21);
   public static final Color[] BAD_TARGETING_GRADIENT = WSTools.getGradient(DEFAULT_TILE_BG_COLOR, INVALID_RETICULE_COLOR, 21);
   public static final Color[] TERMINAL_GRADIENT = WSTools.getGradient(DEFAULT_TILE_BG_COLOR, TERMINAL_FG_COLOR, 21);
   public static final int SLOW_TICK_COUNT = 20;
   public static final int MEDIUM_TICK_COUNT = 10;
   public static final int FAST_TICK_COUNT = 5;
   private static final int tickCounterReset = SLOW_TICK_COUNT;
   private int tickCounter;
   private boolean slowFlag;
   private boolean mediumFlag;
   private boolean fastFlag;
   
	public MainGameFGPanel()
   {
      super(MAP_WIDTH_TILES + 2, MAP_HEIGHT_TILES + 2, SQUARE_TILE_PALETTE);
      setSize(50, 50);
      drawLockListFirst();
      GUITools.setAnimationManager(getAnimationManager());
      tickCounter = 1;
      
      GameEngine.setMapPanel(this);
   }
   
   
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      tickCounter++;
      if(tickCounter > tickCounterReset)
         tickCounter = 1;
      slowFlag = (tickCounter % SLOW_TICK_COUNT == 0);
      mediumFlag = (tickCounter % MEDIUM_TICK_COUNT == 0);
      fastFlag = (tickCounter % FAST_TICK_COUNT == 0);
      
      // add burning effects
      if(mediumFlag)
      {
         for(Actor a : GameEngine.getActorList())
         {
            if(a.isOnFire() && GameEngine.playerCanSee(a))
               VisualEffectFactory.addFireParticle(a);
         }
      }
      
      ZoneMap zoneMap = GameEngine.getZoneMap();
      Player player = GameEngine.getPlayer();
      
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
               if(player.hasMotionSensor())
               {
                  Actor a = GameEngine.getActorAt(x + xCorner, y + yCorner);
                  if(a != null && a.getAI().getPreviousAction() != ActorAction.DELAY)
                     setTile(x, y, '?', SENSOR_COLOR, BG_COLOR);
               }
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
               // shotgun targeting
               if(player.pendingAttackIsSpread())
               {
                  Vector<Coord> sprayTargets = EngineTools.getShotgunSprayLine(playerLoc, cursorLoc);
                  for(Coord sprayTarget : sprayTargets)
                  {
                     Vector<Coord> targetingLine = StraightLine.findLine(playerLoc, sprayTarget, StraightLine.REMOVE_ORIGIN);
                     boolean clearPath = true;
                     for(Coord c : targetingLine)
                     {
                        if(GameEngine.playerCanSee(c.x + xCorner, c.y + yCorner) && EngineTools.getDistanceTo(playerLoc, c) <= SHOTGUN_MAX_RANGE)
                        {
                           int bgColor = BAD_TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB();
                           if(clearPath)
                              bgColor = TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB();
                           setBGColor(c.x, c.y, bgColor);
                           if(!GameEngine.getZoneMap().getTile(c.x + xCorner, c.y + yCorner).isHighPassable())
                              clearPath = false;
                        }
                        else
                           break;
                     }
                  }
                  // don't obscure targets with the blue cursor, it makes it hard to tell if you can hit them or not
                  if((GameEngine.playerCanSee(cursorLoc.x + xCorner, cursorLoc.y + yCorner) && !GameEngine.isActorAt(cursorLoc.x + xCorner, cursorLoc.y + yCorner)) ||
                     !GameEngine.playerCanSee(cursorLoc.x + xCorner, cursorLoc.y + yCorner))
                     setBGColor(cursorLoc.x, cursorLoc.y, TERMINAL_GRADIENT[animationManager.mediumPulse()].getRGB());
               }
               // blast targeting
               else if(player.pendingAttackIsBlast())
               {
                  Coord blastLocation = GameEngine.getDetonationLoc(GameEngine.getPlayer().getMapLoc(), GameEngine.getCursorLoc());
                  for(int x = -1; x < 2; x++)
                  for(int y = -1; y < 2; y++)
                  {
                     if(GameEngine.playerCanSee(blastLocation.x + x, blastLocation.y + y))
                     {
                        setBGColor(blastLocation.x + x - xCorner, blastLocation.y + y - yCorner, TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB());
                     }
                  }
                  setBGColor(cursorLoc.x, cursorLoc.y, TERMINAL_GRADIENT[animationManager.mediumPulse()].getRGB());
               }
               // melee targeting
               else if(player.pendingAttackIsMelee())
               {
                  Vector<Coord> targetingLine = StraightLine.findLine(playerLoc, cursorLoc, StraightLine.REMOVE_ORIGIN);
                  if(targetingLine.size() == 0)
                     targetingLine.add(playerLoc);
                  for(int i = 0; i < targetingLine.size(); i++)
                  {
                     Coord c = targetingLine.elementAt(i);
                     int bgColor = BAD_TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB();
                     if(i == 0)
                        bgColor = TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB();
                     setBGColor(c.x, c.y, bgColor);
                  }
               }
               // straight line targeting
               else
               {
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
                     if(GameEngine.playerCanSee(c.x + xCorner, c.y + yCorner))
                        setTile(c.x, c.y, iconIndex, fgColor, bgColor);
                     if(GameEngine.blocksShooting(c.x + xCorner, c.y + yCorner))
                        clearPath = false;
                  }
               }
               // don't obscure targets with the blue cursor, it makes it hard to tell if you can hit them or not
               if((GameEngine.playerCanSee(cursorLoc.x + xCorner, cursorLoc.y + yCorner) && !GameEngine.isActorAt(cursorLoc.x + xCorner, cursorLoc.y + yCorner)) ||
                  !GameEngine.playerCanSee(cursorLoc.x + xCorner, cursorLoc.y + yCorner))
                  setBGColor(cursorLoc.x, cursorLoc.y, TERMINAL_GRADIENT[animationManager.mediumPulse()].getRGB());
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
      Vector<Actor> actorList = GameEngine.getActorList();
      for(int i = 0; i < actorList.size(); i++)
      {
         Actor actor = actorList.elementAt(i);
         if(player.canSee(actor))
            actor.getSprite().setVisible(true);
         else
            actor.getSprite().setVisible(false);
      }
   }
}