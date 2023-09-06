package Pulsar.Engine;

import java.util.*;
import java.awt.*;
import Pulsar.Actor.*;
import Pulsar.Zone.*;
import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.lang.*;
import WidlerSuite.*;

public class GameEngine implements Runnable, AIConstants, EngineConstants
{
   private static Player player = null;
	private static Vector<Actor> actorList = new Vector<Actor>();
   private static ZoneMap zoneMap = null;
   private static MainGameFGPanel mapPanel = null;
   private static int initiativeIndex;
   private static boolean runFlag = true;
   private static SquirrelRNG rng = new SquirrelRNG();
   private static GameMode gameMode = GameMode.STANDARD;
   private static Coord cursorLoc = null;
   
   // non-static variables
   Thread thread;


	public static Player getPlayer(){return player;}
	public static Vector<Actor> getActorList(){return actorList;}
   public static ZoneMap getZoneMap(){return zoneMap;}
   public static MainGameFGPanel getMapPanel(){return mapPanel;}
   public static double random(){return rng.nextDouble();}
   public static GameMode getGameMode(){return gameMode;}
   public static Coord getCursorLoc(){return new Coord(cursorLoc);}


	public static void setActorList(Vector<Actor> a){actorList = a;}
   public static void setMapPanel(MainGameFGPanel mp){mapPanel = mp;}
   public static void setZoneMap(ZoneMap zm){zoneMap = zm;}
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
         actorList.remove(player);
         removeFromMapPanel(player);
      }
      player = p;
      actorList.add(player);
      addToMapPanel(player);
      initiativeIndex = 0;
   }
   
   public static void clearActorList()
   {
      actorList = new Vector<Actor>();
   }
   
   public static void add(Actor a)
   {
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
      int actorIndex = actorList.indexOf(a);
      if(actorIndex > -1)
      {
         actorList.remove(a);
         removeFromMapPanel(a);
         if(actorIndex >= initiativeIndex)
            initiativeIndex--;
      }
   }
   
   private static void removeFromMapPanel(Actor a)
   {
      if(mapPanel != null)
         mapPanel.remove(a.getSprite());
   }
   
   private static void addToMapPanel(Actor a)
   {
      if(mapPanel != null)
         mapPanel.add(a.getSprite());
   }
   
   public static Actor getActorAt(int x, int y){return getActorAt(new Coord(x, y));}
   public static Actor getActorAt(Coord c)
   {
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
      return player.canSee(target);
   }
   
   public static void add(MovementScript ms){mapPanel.add(ms);}
   public static void addLocking(MovementScript ms){mapPanel.addLocking(ms);}
   public static void addNonlocking(MovementScript ms){mapPanel.addNonlocking(ms);}
   
   
   private static void incrementInitiative()
   {
      initiativeIndex++;
      if(initiativeIndex >= actorList.size())
         initiativeIndex = 0;
   }
   
   public static void newGame()
   {
      Player p = ActorFactory.getPlayer();
      p.setAllLocs(2, 3);
      setPlayer(p);
      Actor e = new Actor('e');
      e.setName("Enemy 1");
      e.setAllLocs(4, 1);
      e.setAI(new WanderAI(e));
      add(e);
      e = new Actor('e');
      e.setName("Enemy 2");
      e.setAllLocs(6, 1);
      e.setAI(new WanderAI(e));
      e.getSprite().setFGColor(Color.ORANGE.getRGB());
      add(e);
      zoneMap = ZoneMapFactory.getTestMap();
   }
   
   public static void end()
   {
      runFlag = false;
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
            if(curActor.getAI().getPendingAction() == ActorAction.CONTEXT_SENSITIVE)
               curActor.getAI().interpertContext();
            if(curActor.hasPlan())
            {
               if(animationAllowsAction(curActor))
               {
                  curActor.act();
                  InfoPanel.updateInfoPanel();
               }
            }
         }
         // increment if acted
         if(!(curActor.isReadyToAct()))
            incrementInitiative();
         cleanUpSprites();
      }
   }
   
   public void cleanUpSprites()
   {
      for(int i = 0; i < actorList.size(); i++)
      {
         Actor curActor = actorList.elementAt(i);
         if(!mapPanel.isOnLockList(curActor))
            curActor.reconcileSprite();
      }
   }
   
   public boolean allLockingAreWalking()
   {
      Vector<Actor> lockingActorList = new Vector<Actor>();
      
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
      if(!mapPanel.isAnimationLocked())
      {
         return true;
      }
      
      // NPCs can step if all locking actors are only stepping
      if(allLockingAreWalking() && curActor != player && curActor.getAI().getPendingAction() == ActorAction.STEP)
         return true;
      return false;
   }
}