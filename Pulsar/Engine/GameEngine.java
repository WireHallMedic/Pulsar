package Pulsar.Engine;

import java.util.*;
import Pulsar.Actor.*;
import Pulsar.Zone.*;
import Pulsar.GUI.*;

public class GameEngine
{
   private static Actor player = null;
	private static Vector<Actor> actorList = new Vector<Actor>();
   private static MainGameFGPanel mapPanel = null;


	public static Actor getPlayer(){return player;}
	public static Vector<Actor> getActorList(){return actorList;}


	public static void setActorList(Vector<Actor> a){actorList = a;}
   public static void setMapPanel(MainGameFGPanel mp){mapPanel = mp;}
   
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
      actorList.remove(a);
      removeFromMapPanel(a);
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
   
   public static void newGame()
   {
      Actor p = new Actor('@');
      p.setAllLocs(2, 3);
      setPlayer(p);
      Actor e = new Actor('e');
      e.setAllLocs(4, 3);
      add(e);
   }
}