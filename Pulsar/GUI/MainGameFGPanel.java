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
   private static final int BUFFER_TILES = 4;
   private static boolean newActors = false;
   private int tickCounter;
   private boolean slowFlag;
   private boolean mediumFlag;
   private boolean fastFlag;
   
   public static void setNewActorsFlag(){newActors = true;}
   
	public MainGameFGPanel()
   {
      super(MAP_WIDTH_TILES + BUFFER_TILES, MAP_HEIGHT_TILES + BUFFER_TILES, SQUARE_TILE_PALETTE);
      setSize(50, 50);
      drawLockListFirst();
      GUITools.setAnimationManager(getAnimationManager());
      tickCounter = 1;
      GameEngine.setMapPanel(this);
   }
   
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(!GameEngine.getRunFlag())
         return;
      
      super.actionPerformed(ae);
      GameEngine.cleanUpSprites();
      setActorVisibility();
      if(newActors)
      {
         for(Actor a: GameEngine.getActorList())
            addNonlocking(a.getSprite());
         newActors = false;
      }
      tickCounter++;
      if(tickCounter > tickCounterReset)
         tickCounter = 1;
      slowFlag = (tickCounter % SLOW_TICK_COUNT == 0);
      mediumFlag = (tickCounter % MEDIUM_TICK_COUNT == 0);
      fastFlag = (tickCounter % FAST_TICK_COUNT == 0);
      
      // add burning effects
      if(mediumFlag)
      {
         for(int i = 0; i < GameEngine.getActorList().size(); i++)
         {
            Actor a = GameEngine.getActorList().elementAt(i);
            if(a.isOnFire() && GameEngine.playerCanSee(a))
               VisualEffectFactory.addFireParticle(a);
            if(a.isCharmed() && GameEngine.playerCanSee(a))
               VisualEffectFactory.addCharmedParticle(a);
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
         
         // draw the map
         for(int x = 0; x < columns(); x++)
         for(int y = 0; y < rows(); y++)
         {
            if(player.canSee(x + xCorner, y + yCorner))
            {
               // tile
               tile = zoneMap.getTile(x + xCorner, y + yCorner);
               int iconIndex = tile.getIconIndex();
               int fgColor = tile.getFGColor();
               // gear
               if(zoneMap.isGearAt(x + xCorner, y + yCorner))
               {
                  GearObj gear = zoneMap.getGearAt(x + xCorner, y + yCorner);
                  iconIndex = gear.getIconIndex();
                  fgColor = gear.getColor().getRGB();
               }
               // corpse
               else if(zoneMap.isCorpseAt(x + xCorner, y + yCorner))
               {
                  Corpse corpse = zoneMap.getCorpseAt(x + xCorner, y + yCorner);
                  iconIndex = corpse.getIconIndex();
                  fgColor = corpse.getColor();
               }
               setTile(x, y, iconIndex, fgColor, tile.getBGColor());
            }
            else
            {
               setTile(x, y, player.getLastSeen(x + xCorner, y + yCorner), MEMORY_COLOR, OUT_OF_SIGHT_COLOR);
            }
         }
         
         // draw motion sensor enemies
         if(player.hasMotionSensor())
         {
            Vector<Actor> prospectList = GameEngine.getAllActorsWithinRange(player.getMapLoc(), (MAP_WIDTH_TILES + BUFFER_TILES) / 2);
            for(Actor prospect : prospectList)
            {
               if((prospect.getAI().getPreviousAction() != ActorAction.DELAY || prospect.didForcedMovement()) &&
                  !GameEngine.playerCanSee(prospect))
               {
                  setTile(prospect.getMapLoc().x - xCorner, prospect.getMapLoc().y - yCorner, '?', SENSOR_COLOR, OUT_OF_SIGHT_COLOR);
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
                  Coord blastLocation = GameEngine.getDetonationLoc(player.getMapLoc(), GameEngine.getCursorLoc());
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
               // beam targeting
               else if(player.pendingAttackIsBeam())
               {
                  Vector<Coord> targetList = GameEngine.getBeam(player.getMapLoc(), GameEngine.getCursorLoc());
                  for(Coord targetTile : targetList)
                  {
                     if(GameEngine.playerCanSee(targetTile.x, targetTile.y))
                     {
                        setBGColor(targetTile.x - xCorner, targetTile.y - yCorner, TARGETING_GRADIENT[animationManager.mediumPulse()].getRGB());
                     }
                  }
                  if((GameEngine.playerCanSee(cursorLoc.x + xCorner, cursorLoc.y + yCorner) && !GameEngine.isActorAt(cursorLoc.x + xCorner, cursorLoc.y + yCorner)) ||
                     !GameEngine.playerCanSee(cursorLoc.x + xCorner, cursorLoc.y + yCorner))
                     setBGColor(cursorLoc.x, cursorLoc.y, TERMINAL_GRADIENT[animationManager.mediumPulse()].getRGB());
               }
               // melee targeting
               else if(player.pendingAttackIsMelee() || (player.getPendingGadget() != null && player.getPendingGadget().getPlaceAdjacent()))
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
               if(GameEngine.playerCanSee(cursorLoc.x + xCorner, cursorLoc.y + yCorner))
               {
                  Actor actorAt = GameEngine.getActorAt(cursorLoc.x + xCorner, cursorLoc.y + yCorner);
                  if(actorAt == null || actorAt == player)
                     setBGColor(cursorLoc.x, cursorLoc.y, TERMINAL_GRADIENT[animationManager.mediumPulse()].getRGB());
               }
            }
         }
         if(GameEngine.getGameMode() == GameMode.STANDARD)
         {
            
         }
      }
   }
   
   public void addNonlocking(MovementScript ms)
   {
      add(ms);
   }
   
   // if you're adding an animation not tied to an actor, pass null
   public void addLocking(MovementScript ms, Actor a)
   {
      UnboundTile ut = (UnboundTile)ms.getTarget();
      remove(ut);
      addLocking(ut);
      add(ms);
      if(a != null)
         GameEngine.registerMovingActor(a);
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
      if(actorList == null)
         return;
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