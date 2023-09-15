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
   private static GameMode gameMode = GameMode.OTHER_PANEL;
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
   
   
   // actor stuff
   //////////////////////////////////////////////////////////////
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
   
   // actor interactions
   /////////////////////////////////////////////////////////////////////
   
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
   
   public static Coord getClosestEmptyTile(int x, int y){return getClosestEmptyTile(new Coord(x, y));}
   public static Coord getClosestEmptyTile(Coord c)
   {
      int radius = 7;
      boolean[][] passMap = getZoneMap().getLowPassMapPortion(c, radius);
      SpiralSearch search = new SpiralSearch(passMap, new Coord(radius, radius));
      Coord searchLoc = search.getNext();
      while(searchLoc != null)
      {
         Coord trueLoc = new Coord(searchLoc.x + c.x - radius, searchLoc.y + c.y - radius);
         if(!isActorAt(trueLoc) && getZoneMap().getTile(trueLoc).isLowPassable())
         {
            return trueLoc;
         }
         searchLoc = search.getNext();
      }
      return null;
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
   
   public static void doDestructionEffect(int x, int y, MapTile mt)
   {
      Combat.nonWeaponExplosion(x, y);
   }
   
   public static void add(MovementScript ms){mapPanel.add(ms);}
   public static void addLocking(MovementScript ms){mapPanel.addLocking(ms);}
   public static void addNonlocking(MovementScript ms){mapPanel.addNonlocking(ms);}
   
   
   private static void incrementInitiative()
   {
      initiativeIndex++;
      if(initiativeIndex >= curZone.getActorList().size())
      {
         initiativeIndex = 0;
         if(getZoneMap() != null)
            getZoneMap().takeTurn();
      }
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////
   
   public static void end()
   {
      runFlag = false;
   }
   
   public static boolean isAnimationLocked()
   {
      return mapPanel.isAnimationLocked();
   }
   
   
   public static void buttonPressed(int triggerIndex)
   {
      MessagePanel.addMessage("You press the button.");
   }
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   
   public static void newGame()
   {
      /*
      ZoneMap map = ZoneMapFactory.getTestMap2();
      GameEngine.setCurZone(new Zone("Test Zone", -1, map));
      
      Player p = ActorFactory.getPlayer();
      p.setAllLocs(2, 12);
      setPlayer(p);
      
      
      for(int i = 0; i < 10; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(1, 9);
         }
         Actor e = ActorFactory.getAlienWorker();
         e.setAllLocs(c);
         add(e);
      }
      for(int i = 0; i < 15; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(1, 9);
         }
         Actor e = ActorFactory.getAlienHunter();
         e.setAllLocs(c);
         add(e);
      }
      for(int i = 0; i < 10; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(19, 28);
         }
         Actor e = ActorFactory.getAlienWorker();
         e.setAllLocs(c);
         add(e);
      }
      for(int i = 0; i < 5; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(19, 28);
         }
         Actor e = ActorFactory.getAlienHunter();
         e.setAllLocs(c);
         add(e);
      }
      for(int i = 0; i < 5; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(12, map.getWidth() - 1);
            c.y = randomInt(9, 19);
         }
         Actor e = ActorFactory.getAlienHunter();
         e.setAllLocs(c);
         add(e);
      }
      for(int i = 0; i < 5; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(12, map.getWidth() - 1);
            c.y = randomInt(9, 19);
         }
         Actor e = ActorFactory.getAlienWorker();
         e.setAllLocs(c);
         add(e);
      }
      
      for(int i = 0; i < 5; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c))
         {
            c.x = randomInt(10, map.getWidth());
            c.y = randomInt(0, map.getHeight());
         }
         for(int j = 0; j < 5; j++)
         {
            Actor e = ActorFactory.getAlienLarva();
            Coord loc = getClosestEmptyTile(c);
            if(loc != null)
            {
               e.setAllLocs(loc);
               add(e);
            }
         }
      }
   */
   
      // vacuum test
      ZoneMap map = ZoneMapFactory.getVacuumTest();
      GameEngine.setCurZone(new Zone("Test Zone", -1, map));
      
      Player p = ActorFactory.getPlayer();
      p.setAllLocs(2, 2);
      setPlayer(p);
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
               }
            }
         }
         // do cleanup
         cleanUpCheck();
         if(getPlayer().isDead())
         {
            cleanUpDead();
            MessagePanel.addMessage("You have died.", Color.RED);
            break;
         }
         // increment if acted
         if(!(curActor.isReadyToAct()))
            incrementInitiative(); 
      }
   }
   
   public void cleanUpCheck()
   {
      if(!isAnimationLocked())
      {
         cleanUpDead();
         cleanUpSprites();
      }
   }
   
   private void cleanUpSprites()
   {
      Vector<Actor> actorList = curZone.getActorList();
      for(int i = 0; i < actorList.size(); i++)
      {
         Actor curActor = actorList.elementAt(i);
         curActor.reconcileSprite();
      }
   }
   
   private void cleanUpDead()
   {
      Vector<Actor> actorList = curZone.getActorList();
      for(int i = 0; i < actorList.size(); i++)
      {
         Actor a = actorList.elementAt(i);
         if(a.isDead())
         {
            a.doDeathEffect();
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
      if(allLockingAreWalking())
      {
         if(curActor != player)
         {
            if(curActor.getAI().getPendingAction() == ActorAction.STEP)
            {
               return true;
            }
         }
      }
      return false;
   }
}