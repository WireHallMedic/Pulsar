package Pulsar.Zone;

import java.util.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class Zone
{
	private int id;
	private ZoneMap map;
	private Vector<Actor> actorList;
	private String name;
   private ZoneBuilder zoneBuilder;


	public int getID(){return id;}
	public ZoneMap getMap(){return map;}
	public Vector<Actor> getActorList(){return actorList;}
	public String getName(){return name;}
   public ZoneBuilder getZoneBuilder(){return zoneBuilder;}


	public void setID(int i){id = i;}
	public void setMap(ZoneMap m){map = m;}
	public void setActorList(Vector<Actor> a){actorList = a;}
	public void setName(String n){name = n;}
   public void setZoneBuilder(ZoneBuilder zb){zoneBuilder = zb;}


   public Zone()
   {
      this("Unknown Zone", -1, null, null);
   }
   
   public Zone(String n, int zoneID, ZoneMap zoneMap, ZoneBuilder zb)
   {
      id = zoneID;
      name = n;
      map = zoneMap;
      zoneBuilder = zb;
      actorList = new Vector<Actor>();
   }
   
   public void add(Actor a)
   {
      if(actorList.contains(a))
      {
         System.out.println("Attempt to add duplicate actor.");
         return;
      }
      actorList.add(a);
   }
   
   public void remove(Actor a)
   {
      actorList.remove(a);
   }
   
   public Actor getActorAt(int x, int y){return getActorAt(new Coord(x, y));}
   public Actor getActorAt(Coord c)
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
   
   public boolean isActorAt(int x, int y){return isActorAt(new Coord(x, y));}
   public boolean isActorAt(Coord c)
   {
      return getActorAt(c) != null;
   }
   
   public Vector<Actor> getAllActorsWithinRange(Coord origin, int range)
   {
      Vector<Actor> list = new Vector<Actor>();
      for(int i = 0; i < actorList.size(); i++)
      {
         if(EngineTools.getDistanceTo(actorList.elementAt(i).getMapLoc(), origin) <= range)
            list.add(actorList.elementAt(i));
      }
      return list;
   }
   
   // nearest low passable tile with no actor in it
   public Coord getClosestEmptyTile(int x, int y){return getClosestEmptyTile(new Coord(x, y));}
   public Coord getClosestEmptyTile(Coord c)
   {
      int radius = 7;
      boolean[][] passMap = getMap().getLowPassMapPortion(c, radius);
      SpiralSearch search = new SpiralSearch(passMap, new Coord(radius, radius));
      Coord searchLoc = search.getNext();
      while(searchLoc != null)
      {
         Coord trueLoc = new Coord(searchLoc.x + c.x - radius, searchLoc.y + c.y - radius);
         if(!isActorAt(trueLoc) && getMap().getTile(trueLoc).isLowPassable())
         {
            return trueLoc;
         }
         searchLoc = search.getNext();
      }
      return null;
   }

}