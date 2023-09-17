package Pulsar.Zone;

import java.util.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class Zone
{
	private int id;
	private ZoneMap map;
	private Vector<Actor> actorList;
	private String name;


	public int getId(){return id;}
	public ZoneMap getMap(){return map;}
	public Vector<Actor> getActorList(){return actorList;}
	public String getName(){return name;}


	public void setId(int i){id = i;}
	public void setMap(ZoneMap m){map = m;}
	public void setActorList(Vector<Actor> a){actorList = a;}
	public void setName(String n){name = n;}


   public Zone()
   {
      this("Unknown Zone", -1, null);
   }
   
   public Zone(String n, int zoneID, ZoneMap zoneMap)
   {
      id = zoneID;
      name = n;
      map = zoneMap;
      actorList = new Vector<Actor>();
   }

}