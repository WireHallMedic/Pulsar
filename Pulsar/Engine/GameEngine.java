package Pulsar.Engine;

import java.util.*;
import java.awt.*;
import Pulsar.Actor.*;
import Pulsar.Zone.*;
import Pulsar.Gear.*;
import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.lang.*;
import WidlerSuite.*;

public class GameEngine implements Runnable, AIConstants, EngineConstants
{
   private static Player player = null;
   private static Zone curZone = null;
   private static MainGameFGPanel mapPanel = null;
   private static int initiativeIndex;
   private static boolean runFlag = true;
   private static SquirrelRNG rng = new SquirrelRNG();
   private static GameMode gameMode = GameMode.STANDARD;
   private static Coord cursorLoc = null;
   
   // non-static variables
   Thread thread;


	public static Player getPlayer(){return player;}
	public static Vector<Actor> getActorList(){if(curZone != null) return curZone.getActorList(); return null;}
   public static ZoneMap getZoneMap(){if(curZone != null) return curZone.getMap(); return null;}
   public static Zone getCurZone(){return curZone;}
   public static MainGameFGPanel getMapPanel(){return mapPanel;}
   public static double random(){return rng.nextDouble();}
   public static GameMode getGameMode(){return gameMode;}
   public static Coord getCursorLoc(){return new Coord(cursorLoc);}


	public static void setActorList(Vector<Actor> a){if(curZone != null) curZone.setActorList(a);}
   public static void setMapPanel(MainGameFGPanel mp){mapPanel = mp;}
   public static void setCurZone(Zone z){curZone = z;}
   public static void setGameMode(GameMode gm){gameMode = gm;}
   public static void setCursorLoc(Coord c){cursorLoc = new Coord(c);}
   
   
   public static int randomInt(int lower, int upper)
   {
      return lower + (int)(random() * (upper - lower));
   }
   
	public static void setPlayer(Player p)
   {
      if(player != null)
      {
         remove(player);
         removeFromMapPanel(player);
      }
      player = p;
      add(player);
      addToMapPanel(player);
      initiativeIndex = 0;
   }
   
   public static void clearActorList()
   {
      if(curZone != null)
         curZone.setActorList(new Vector<Actor>());
   }
   
   public static void add(Actor a)
   {
      Vector<Actor> actorList = curZone.getActorList();
      if(actorList.contains(a))
      {
         System.out.println("Attempt to add duplicate actor.");
         return;
      }
      actorList.add(a);
      addToMapPanel(a);
   }
   
   public static void remove(Actor a)
   {
      Vector<Actor> actorList = curZone.getActorList();
      int actorIndex = actorList.indexOf(a);
      if(actorIndex > -1)
      {
         actorList.remove(a);
         removeFromMapPanel(a);
         if(actorIndex <= initiativeIndex)
            initiativeIndex--;
      }
   }
   
   private static void removeFromMapPanel(Actor a)
   {
      if(mapPanel != null)
         mapPanel.remove(a);
   }
   
   private static void addToMapPanel(Actor a)
   {
      if(mapPanel != null)
         mapPanel.add(a.getSprite());
   }
   
   public static Actor getActorAt(int x, int y){return getActorAt(new Coord(x, y));}
   public static Actor getActorAt(Coord c)
   {
      Vector<Actor> actorList = curZone.getActorList();
      for(int i = 0; i < actorList.size(); i++)
      {
         if(actorList.elementAt(i).getMapLoc().equals(c))
         {
            return actorList.elementAt(i);
         }
      }
      return null;
   }
   
   public static boolean isActorAt(int x, int y){return isActorAt(new Coord(x, y));}
   public static boolean isActorAt(Coord c)
   {
      return getActorAt(c) != null;
   }
   
   public static boolean playerCanSee(Actor a){return playerCanSee(a.getMapLoc());}
   public static boolean playerCanSee(int x, int y){return playerCanSee(new Coord(x, y));}
   public static boolean playerCanSee(Coord target)
   {
      // null protection because will be null in unit testing
      return player != null && player.canSee(target);
   }
   
   public static Actor getClosestVisibleActor(Actor origin)
   {
      Actor target = null;
      int distanceTo = 1000;
      for(Actor prospect : curZone.getActorList())
      {
         if(origin != prospect && origin.canSee(prospect))
         {
            int prospectDistance = WSTools.getAngbandMetric(origin.getMapLoc(), prospect.getMapLoc());
            if(prospectDistance < distanceTo)
            {
               target = prospect;
               distanceTo = prospectDistance;
            }
         }
      }
      return target;
   }
   
   public static Actor getClosestVisibleEnemy(Actor origin)
   {
      Actor target = null;
      int distanceTo = 1000;
      for(Actor prospect : curZone.getActorList())
      {
         if(origin.isHostile(prospect) && origin.canSee(prospect))
         {
            int prospectDistance = WSTools.getAngbandMetric(origin.getMapLoc(), prospect.getMapLoc());
            if(prospectDistance < distanceTo)
            {
               target = prospect;
               distanceTo = prospectDistance;
            }
         }
      }
      return target;
   }
   
   public static Vector<Actor> getVisibleEnemies(Actor origin)
   {
      Vector<Actor> visibleEnemyList = new Vector<Actor>();
      for(Actor a : curZone.getActorList())
      {
         if(origin.isHostile(a) && origin.canSee(a))
            visibleEnemyList.add(a);
      }
      return visibleEnemyList;
   }
   
   public static boolean blocksShooting(int x, int y){return blocksShooting(new Coord(x, y));}
   public static boolean blocksShooting(Coord target)
   {
      return isActorAt(target) || !getZoneMap().getTile(target).isHighPassable();
   }
   
   public static boolean hasClearShot(Actor o, Actor t){return hasClearShot(o.getMapLoc(), t.getMapLoc());}
   public static boolean hasClearShot(Coord origin, Coord target)
   {
      Vector<Coord> interveningTiles = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN_AND_TARGET);
      for(Coord c : interveningTiles)
      {
         if(blocksShooting(c))
            return false;
      }
      return true;
   }
   
   // used for planning, so an actor doesn't think they can hide behind themselves
   public static boolean hasClearShotIgnoring(Actor o, Actor t, Actor i){return hasClearShotIgnoring(o.getMapLoc(), t.getMapLoc(), i.getMapLoc());}
   public static boolean hasClearShotIgnoring(Coord origin, Coord target, Coord ignoring)
   {
      Vector<Coord> interveningTiles = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN_AND_TARGET);
      for(Coord c : interveningTiles)
      {
         if(blocksShooting(c) && !c.equals(ignoring))
            return false;
      }
      return true;
   }
   
   // returns, in order of preference: location of an actor on the line, loc immedeatly preceeding a wall, target
   public static Coord getDetonationLoc(int xOrigin, int yOrigin, int xTarget, int yTarget){return getDetonationLoc(new Coord(xOrigin, yOrigin), new Coord(xTarget, yTarget));}
   public static Coord getDetonationLoc(Coord origin, Coord target)
   {
      if(origin.equals(target))
         return origin;
      Vector<Coord> lineOfFire = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);
      Coord lastGoodTarget = origin;
      for(Coord loc : lineOfFire)
      {
         if(isActorAt(loc))
            return loc;
         if(!getZoneMap().getTile(loc).isHighPassable())
            return lastGoodTarget;
         lastGoodTarget = loc;
      }
      return lastGoodTarget;
   }
   
   public static void add(MovementScript ms){mapPanel.add(ms);}
   public static void addLocking(MovementScript ms){mapPanel.addLocking(ms);}
   public static void addNonlocking(MovementScript ms){mapPanel.addNonlocking(ms);}
   
   
   private static void incrementInitiative()
   {
      initiativeIndex++;
      if(initiativeIndex >= curZone.getActorList().size())
         initiativeIndex = 0;
   }
   
   public static void newGame()
   {
      ZoneMap map = ZoneMapFactory.getTestMap2();
      GameEngine.setCurZone(new Zone("Test Zone", -1, map));
      
      Player p = ActorFactory.getPlayer();
      p.setAllLocs(2, 12);
      setPlayer(p);
      
      for(int i = 0; i < 12; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(1, 9);
            Actor e = ActorFactory.getMeleeTestEnemy();
            e.setAllLocs(c);
            add(e);
         }
      }
      for(int i = 0; i < 12; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(19, 28);
            Actor e = ActorFactory.getMeleeTestEnemy();
            e.setAllLocs(c);
            add(e);
         }
      }
      for(int i = 0; i < 5; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(12, map.getWidth() - 1);
            c.y = randomInt(9, 19);
            Actor e = ActorFactory.getTestEnemy();
            e.setAllLocs(c);
            add(e);
         }
      }
      /*
      Actor e = ActorFactory.getTestEnemy();
      e.setAllLocs(7, 5);
      e.setShield(new Shield());
  //    add(e);
      e = ActorFactory.getWanderTestEnemy();
      e.setName("Wander Test Enemy 1");
  //    e.setAllLocs(7, 6);
      e.setAllLocs(5, 4);
      e.getSprite().setFGColor(Color.ORANGE.getRGB());
      e.setFlying(true);
      add(e);
      
      e = ActorFactory.getWanderTestEnemy();
      e.setName("Wander Test Enemy 2");
      e.setAllLocs(6, 4);
      e.getSprite().setFGColor(Color.ORANGE.getRGB());
      add(e);
      
      
      e = ActorFactory.getGoat();
      e.setAllLocs(3, 8);
   //   add(e);*/
   
   }
   
   public static void end()
   {
      runFlag = false;
   }
   
   public static boolean isAnimationLocked()
   {
      return mapPanel.isAnimationLocked();
   }
   
   // non-static section
   ////////////////////////////////////////////////////////////////////
   public GameEngine()
   {
      thread = new Thread(this);
   }
   
   public void begin()
   {
      runFlag = true;
      thread = new Thread(this);
      thread.start();
   }
   
   public void run()
   {
      Vector<Actor> actorList = curZone.getActorList();
      initiativeIndex = 0;
      Actor curActor = null;
      while(true)
      {
         if(actorList.size() == 0)
            continue;
         
         curActor = actorList.elementAt(initiativeIndex);
         // charge current actor
         curActor.charge();
         // act if able
         if(curActor.isReadyToAct())
         {
            if(!(curActor.hasPlan()))
               curActor.plan();
            if(curActor.hasPlan())
            {
               if(curActor.getAI().getPendingAction() == ActorAction.CONTEXT_SENSITIVE)
                  curActor.getAI().interpertContext();
               if(curActor.getAI().getPendingAction() == ActorAction.USE)
                  curActor.getAI().interpertUse();
            }
            if(curActor.hasPlan())
            {
               if(animationAllowsAction(curActor))
               {
                  curActor.act();
                  InfoPanel.updateInfoPanel();
                  PlayerPanel.updatePlayerPanel();
               }
            }
         }
         // increment if acted
         if(!(curActor.isReadyToAct()))
            incrementInitiative();
         // do cleanup
         cleanUpDead();
         cleanUpSprites();
         if(getPlayer().isDead())
         {
            MessagePanel.addMessage("You have died.", Color.RED);
            break;
         }
      }
   }
   
   public void cleanUpSprites()
   {
      Vector<Actor> actorList = curZone.getActorList();
      for(int i = 0; i < actorList.size(); i++)
      {
         Actor curActor = actorList.elementAt(i);
         if(!mapPanel.isOnLockList(curActor))
            curActor.reconcileSprite();
      }
   }
   
   public void cleanUpDead()
   {
      Vector<Actor> actorList = curZone.getActorList();
      for(int i = 0; i < actorList.size(); i++)
      {
         Actor a = actorList.elementAt(i);
         if(a.isDead())
         {
            remove(a);
            i--;
         }
      }
   }
   
   public boolean allLockingAreWalking()
   {
      Vector<Actor> lockingActorList = new Vector<Actor>();
      Vector<Actor> actorList = curZone.getActorList();
      
      for(Actor a : actorList)
      {
         if(mapPanel.isOnLockList(a))
            lockingActorList.add(a);
      }
      for(Actor a : lockingActorList)
      {
         if(a.getAI().getPreviousAction() != ActorAction.STEP)
         {
            return false;
         }
      }
      return true;
   }
   
   public boolean animationAllowsAction(Actor curActor)
   {
      // no animation lock, proceed
      if(!isAnimationLocked())
      {
         return true;
      }
      
      // NPCs can step if all locking actors are only stepping
      if(allLockingAreWalking() && curActor != player && curActor.getAI().getPendingAction() == ActorAction.STEP)
         return true;
      return false;
   }
}