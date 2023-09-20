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
   private static boolean runFlag = false;
   private static SquirrelRNG rng = new SquirrelRNG();
   private static GameMode gameMode = GameMode.OTHER_PANEL;
   private static Coord cursorLoc = null;
   private static Vector<Actor> deadActorList = new Vector<Actor>();
   private static Vector<Actor> movingActorList = new Vector<Actor>();
   private static boolean allLockingAreWalking = false;

	public static Player getPlayer(){return player;}
	public static Vector<Actor> getActorList(){if(curZone != null) return curZone.getActorList(); return null;}
   public static ZoneMap getZoneMap(){if(curZone != null) return curZone.getMap(); return null;}
   public static Zone getCurZone(){return curZone;}
   public static MainGameFGPanel getMapPanel(){return mapPanel;}
   public static double random(){return rng.nextDouble();}
   public static GameMode getGameMode(){return gameMode;}
   public static Coord getCursorLoc(){return new Coord(cursorLoc);}
   public static boolean getRunFlag(){return runFlag;}
   public static Vector<Actor> getMovingActorList(){return movingActorList;}


	public static void setActorList(Vector<Actor> a){if(curZone != null) curZone.setActorList(a);}
   public static void setMapPanel(MainGameFGPanel mp){mapPanel = mp;}
   public static void setCurZone(Zone z){curZone = z;}
   public static void setGameMode(GameMode gm){gameMode = gm;}
   public static void setCursorLoc(Coord c){cursorLoc = new Coord(c);}
   
   // non-static variables
   private Thread thread;

   
   public static void registerDeadActor(Actor a)
   {
      deadActorList.add(a);
   }
   
   public static void registerMovingActor(Actor a)
   {
      movingActorList.add(a);
   }
   
   
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
      curZone.add(a);
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
      return getCurZone().getActorAt(c);
   }
   
   public static boolean isActorAt(int x, int y){return isActorAt(new Coord(x, y));}
   public static boolean isActorAt(Coord c)
   {
      return getCurZone().isActorAt(c);
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
   
   public static Vector<Actor> getAllActorsWithinRange(Coord origin, int range)
   {
      return curZone.getAllActorsWithinRange(origin, range);
   }
   
   public static Coord getClosestEmptyTile(int x, int y){return getClosestEmptyTile(new Coord(x, y));}
   public static Coord getClosestEmptyTile(Coord c)
   {
      return getCurZone().getClosestEmptyTile(c);
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
   
   public static Coord getActualTarget(Coord origin, Coord target)
   {
      Vector<Coord> lineOfFire = StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN);
      for(Coord c : lineOfFire)
      {
         if(blocksShooting(c))
         {
           return(c);
         }
      }
      return target;
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
      switch(mt.getOnDestructionEffect())
      {
         case EXPLOSION : Combat.nonWeaponExplosion(x, y); break;
         case FLOOD     : getZoneMap().flood(x, y, randomInt(5, 10)); break;
      }
   }
   
   public static void add(MovementScript ms){mapPanel.add(ms);}
   public static void addLocking(MovementScript ms, Actor a){mapPanel.addLocking(ms, a);}
   public static void addNonlocking(MovementScript ms){mapPanel.addNonlocking(ms);}
   
   
   private void incrementInitiative()
   {
      initiativeIndex++;
      if(initiativeIndex >= curZone.getActorList().size())
      {
         initiativeIndex = 0;
         if(getZoneMap() != null)
         {
            getZoneMap().takeTurn();
         }
      }
   }
   
   
   public static void pullTowardsVacuum(boolean[][] depressurizationMap, Vector<Coord> breachList, boolean[][] airPassMap)
   {
      for(Coord breach : breachList)
         airPassMap[breach.x][breach.y] = true;
      DijkstraMap airflowMap = new DijkstraMap(airPassMap);
      for(Coord breach : breachList)
         airflowMap.addGoal(breach);
      airflowMap.setSearchDiagonal(true);
      airflowMap.process();
      for(int i = 0; i < getActorList().size(); i++)
      {
         Actor a = getActorList().elementAt(i);
         if(depressurizationMap[a.getMapLoc().x][a.getMapLoc().y])
         {
            boolean nextToVacuum = false;
            for(Coord breach : breachList)
               if(a.getMapLoc().equals(breach))
                  nextToVacuum = true;
            if(nextToVacuum)
            {
               if(playerCanSee(a))
               {
                  MessagePanel.addMessage(a.getName() + " is violently sucked out into space!");
               }
               a.kill();
            }
            else
            {
               Vector<Coord> pullLoc = airflowMap.getLowestAdjacent(a.getMapLoc());
               if(pullLoc != null && a.canStep(pullLoc.elementAt(0)))
               {
                  // only pull if loc is not farther from breach
                  Coord closestBreach = EngineTools.getClosest(a.getMapLoc(), breachList);
                  if(EngineTools.getDistanceTo(closestBreach, a.getMapLoc()) > EngineTools.getDistanceTo(closestBreach, pullLoc.elementAt(0)))
                  {
                     a.reconcileSprite();
                     MovementScript ms = MovementScriptFactory.getPushScript(a, pullLoc.elementAt(0));
                     a.setDidForcedMovement(true);
                     GameEngine.getMapPanel().addLocking(ms, a);
                     a.setMapLoc(pullLoc.elementAt(0));
                  }
               }
            }
         }
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
      getZoneMap().buttonPressed(triggerIndex);
   }
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   
   public static void newGame()
   {
      
      ZoneMap map = ZoneMapFactory.getTestMap2();
      Zone zone = new Zone("Test Zone", -1, map);
      GameEngine.setCurZone(zone);
      
      Player p = ActorFactory.getPlayer();
      p.setAllLocs(2, 12);
      setPlayer(p);
      
   //   ActorFactory.populateWithAliens(zone, p.getMapLoc());
      ActorFactory.populateWithMercenaries(zone, p.getMapLoc());
      
      
         
   /*
      // vacuum test
      ZoneMap map = ZoneMapFactory.getVacuumTest();
      GameEngine.setCurZone(new Zone("Test Zone", -1, map));
      
      Player p = ActorFactory.getPlayer();
      p.setAllLocs(2, 2);
      setPlayer(p);*/
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
         cleanUpCheck();
         // increment if acted
         if(!(curActor.isReadyToAct()))
            incrementInitiative();
         if(getPlayer().isDead())
            break;
         thread.yield();
      }
   }
   
   public void cleanUpCheck()
   {
      cleanUpDead();
      cleanUpSprites();
      if(getPlayer().isDead())
      {
         MessagePanel.addMessage("You have died.", Color.RED);
      }
      for(int i = 0; i < getActorList().size(); i++)
      {
         Actor a = getActorList().elementAt(i);
         if((a.getMapLoc().x != a.getSprite().getXLoc() ||
            a.getMapLoc().y != a.getSprite().getYLoc()) &&
            !movingActorList.contains(a))
         {
            System.out.println(a);
         }
      }
   }
   
   private void cleanUpSprites()
   {
      Actor curActor;
      boolean newLockingAreWalking = true;
      for(int i = 0; i < movingActorList.size(); i++)
      {
         curActor = movingActorList.elementAt(i);
         if(!mapPanel.isOnLockList(curActor))
         {
            curActor.reconcileSprite();
            movingActorList.removeElementAt(i);
            i--;
         }
         else
         {
            newLockingAreWalking = newLockingAreWalking && curActor.getAI().getPreviousAction() == ActorAction.STEP && !curActor.didForcedMovement();
         }
      }
      allLockingAreWalking = newLockingAreWalking;
   }
   
   private void cleanUpDead()
   {
      Actor a;
      while(deadActorList.size() > 0)
      {
         a = deadActorList.elementAt(deadActorList.size() - 1);
         deadActorList.removeElementAt(deadActorList.size() - 1);
         a.doDeathEffect();
         getZoneMap().dropCorpse(a);
         remove(a);
      }
   }
   
   public boolean allLockingAreWalking()
   {
      return allLockingAreWalking;
   }
   
   public boolean animationAllowsAction(Actor curActor)
   {
      // no animation lock, proceed
      if(!isAnimationLocked())
         return true;
      
      // curent actor is locked, deny
      if(mapPanel.isOnLockList(curActor))
         return false;
      
      // NPCs can step if all locking actors are only stepping
      if(allLockingAreWalking())
         if(curActor != player)
            return curActor.getAI().getPendingAction().canUseDuringAnimationLock;
      return false;
   }
}