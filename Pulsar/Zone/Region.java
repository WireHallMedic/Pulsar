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
      for(int x = 0; x < regionIndexMap.length; x++)
      for(int y = 0; y < regionIndexMap[0].length; y++)
         if(regionIndexMap[x][y] == index)
            tileList.add(new Coord(x, y));
   }
   
   public void setClosetList(Vector<Closet> closetListIn, int[][] regionIndexMap)
   {
      closetList = new Vector<Closet>();
      for(Closet c : closetListIn)
      {
         if(regionIndexMap[c.getOrigin().x][c.getOrigin().y] == index)
            closetList.add(c);
      }
   }
   
   public void setConnectionList(int[][] regionIndexMap, char[][] tileMap)
   {
      connectionList = new Vector<Coord>();
      for(int x = 1; x < tileMap.length - 1; x++)
      for(int y = 1; y < tileMap[0].length - 1; y++)
      {
         if(tileMap[x][y] == TEMPLATE_DOOR && regionIndexMap[x][y] == -1)
         {
            boolean connectsToThisRegion = false;
            if(regionIndexMap[x - 1][y] == index)
               connectsToThisRegion = true;
            if(regionIndexMap[x + 1][y] == index)
               connectsToThisRegion = true;
            if(regionIndexMap[x][y + 1] == index)
               connectsToThisRegion = true;
            if(regionIndexMap[x][y - 1] == index)
               connectsToThisRegion = true;
            if(connectsToThisRegion)
               connectionList.add(new Coord(x, y));
         }
      }
   }
}