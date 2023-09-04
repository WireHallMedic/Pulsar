package Pulsar.Engine;

import java.util.*;
import java.awt.*;
import Pulsar.Actor.*;
import Pulsar.Zone.*;
import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.lang.*;
import WidlerSuite.*;

public class GameEngine implements Runnable, AIConstants
{
   private static Actor player = null;
	private static Vector<Actor> actorList = new Vector<Actor>();
   private static ZoneMap zoneMap = null;
   private static MainGameFGPanel mapPanel = null;
   private static int initiativeIndex;
   private static boolean runFlag = true;
   
   // non-static variables
   Thread thread;


	public static Actor getPlayer(){return player;}
	public static Vector<Actor> getActorList(){return actorList;}
   public static ZoneMap getZoneMap(){return zoneMap;}
   public static MainGameFGPanel getMapPanel(){return mapPanel;}


	public static void setActorList(Vector<Actor> a){actorList = a;}
   public static void setMapPanel(MainGameFGPanel mp){mapPanel = mp;}
   public static void setZoneMap(ZoneMap zm){zoneMap = zm;}
   
   
	public static void setPlayer(Actor p)
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
   public static boolean playerCanSee(Coord c){return playerCanSee(c.x, c.y);}
   public static boolean playerCanSee(int x, int y)
   {
      Coord origin = player.getMapLoc();
      Coord target = new Coord(x, y);
      return zoneMap.canSee(origin, target, player.getVisionRange());
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
      Actor p = ActorFactory.getPlayer();
      p.setAllLocs(2, 3);
      setPlayer(p);
      Actor e = new Actor('e');
      e.setAllLocs(4, 3);
      add(e);
      e = new Actor('e');
      e.setAllLocs(6, 3);
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
            if(curActor.getAI().getPendingAction() == ACTION.CONTEXT_SENSITIVE)
               curActor.getAI().interpertContext();
            if(curActor.hasPlan())
               if(!mapPanel.isAnimationLocked())
                  curActor.act();
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
}