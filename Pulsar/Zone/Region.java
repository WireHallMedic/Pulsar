/*
A region is a contiguous section of a ZoneMap that only connects to corridors
*/

package Pulsar.Zone;

import WidlerSuite.*;
import java.util.*;


public class Region implements ZoneConstants
{
	private Vector<Coord> tileList;
	private Vector<Closet> closetList;
	private Vector<Coord> connectionList;
	private int index;


	public Vector<Coord> getTileList(){return tileList;}
	public Vector<Closet> getClosetList(){return closetList;}
	public Vector<Coord> getConnectionList(){return connectionList;}
	public int getIndex(){return index;}


	public void setTileList(Vector<Coord> t){tileList = t;}
	public void setClosetList(Vector<Closet> c){closetList = c;}
	public void setConnectionList(Vector<Coord> c){connectionList = c;}
	public void setIndex(int i){index = i;}


   public Region()
   {
      tileList = new Vector<Coord>();
      closetList = new Vector<Closet>();
      connectionList = new Vector<Coord>();
      index = -1;
   }
   
   public Region(ZoneDivisor zd, int i)
   {
      this();
      setFromZoneDivisor(zd, i);
      System.out.println(serialize());
      
   }
   
   public String serialize()
   {
      return String.format("Region %d (Tiles: %d, Closets: %d, Connections: %d)", index, tileList.size(),  
                           closetList.size(), connectionList.size());
   }
   
   public void setFromZoneDivisor(ZoneDivisor zd, int i)
   {
      setIndex(i);
      setTileList(zd.getRegionMap());
      setClosetList(zd.getClosetList(), zd.getRegionMap());
      setConnectionList(zd.getRegionMap(), zd.getTileMap());
   }
   
   
   public void setTileList(int[][] regionIndexMap)
   {
      tileList = new Vector<Coord>();
   }
   
   public void setClosetList(Vector<Closet> closetList, int[][] regionIndexMap)
   {
      tileList = new Vector<Coord>();
   }
   
   public void setConnectionList(int[][] regionIndexMap, char[][] tileMap)
   {
      connectionList = new Vector<Coord>();
   }
}