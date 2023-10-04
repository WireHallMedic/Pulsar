package Pulsar.Zone;

import WidlerSuite.*;
import java.util.*;

public class ZoneDivisor implements ZoneConstants
{
   
   private ZoneTemplate template;
   private char[][] tileMap;
   private boolean[][] floodMap;
   private int[][] regionMap;
   private Vector<Closet> closetList;
   private Vector<Region> regionList;
   private int regionCount;
   
   public int getCellCount(){return closetList.size();}
   public int[][] getRegionMap(){return regionMap;}
   public Vector<Closet> getClosetList(){return closetList;}
   public Vector<Region> getRegionList(){return regionList;}
   public char[][] getTileMap(){return tileMap;}
   
   public ZoneDivisor(ZoneTemplate zt)
   {
      template = zt;
      tileMap = template.getTileMap();
      setFloodMapRegion();
      setRegionMap();
      setFloodMapCell();
      setClosetList();
      setRegionList();
   }
   
   private void setRegionList()
   {
      regionList = new Vector<Region>();
      for(int i = 0; i < regionCount; i++)
         regionList.add(new Region(this, i));
   }
   
   private boolean isHighPassableTile(char c)
   {
      switch(c)
      {
         case TEMPLATE_VACUUM :
         case TEMPLATE_WALL :
         case TEMPLATE_DOOR :
         case TEMPLATE_WINDOW :
         case TEMPLATE_OOB :     return false;
      }
      return true;
   }
   
   public void setFloodMapRegion()
   {
      floodMap = new boolean[tileMap.length][tileMap[0].length];
      // mark passability based on low/door passability
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(isHighPassableTile(tileMap[x][y]) || tileMap[x][y] == TEMPLATE_DOOR)
            floodMap[x][y] = true;
      }
      // block off corridors
      int w = template.getRoomWidth() - 1;
      int h = template.getRoomHeight() - 1;
      for(int x = 0; x < template.getWidth(); x++)
      for(int y = 0; y < template.getHeight(); y++)
      {
         if(template.isCorridor(x, y))
         {
            int startX = x * w;
            int startY = y * h;
            for(int xx = 0; xx < template.getRoomWidth(); xx++)
            for(int yy = 0; yy < template.getRoomHeight(); yy++)
            {
               floodMap[xx + (x * w)][yy + (y * h)] = false; 
            }
         }
      }
   }
   
   public void setFloodMapCell()
   {
      floodMap = new boolean[tileMap.length][tileMap[0].length];
      // mark passability based on low/door passability
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(isHighPassableTile(tileMap[x][y]))
            floodMap[x][y] = true;
      }
   }
   
   public void setFloodMapConnection()
   {
      floodMap = new boolean[tileMap.length][tileMap[0].length];
      // mark passability based on low/door passability
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(isHighPassableTile(tileMap[x][y]) || tileMap[x][y] == TEMPLATE_DOOR)
            floodMap[x][y] = true;
      }
      // block off non-corridors
      int w = template.getRoomWidth() - 1;
      int h = template.getRoomHeight() - 1;
      for(int x = 0; x < template.getWidth(); x++)
      for(int y = 0; y < template.getHeight(); y++)
      {
         if(!template.isCorridor(x, y))
         {
            int startX = x * w;
            int startY = y * h;
            for(int xx = 0; xx < template.getRoomWidth(); xx++)
            for(int yy = 0; yy < template.getRoomHeight(); yy++)
            {
               if(tileMap[xx + (x * w)][yy + (y * h)] != TEMPLATE_DOOR)
                  floodMap[xx + (x * w)][yy + (y * h)] = false; 
            }
         }
      }
      // clean up isolated cells (doors)
      for(int x = 1; x < tileMap.length - 1; x++)
      for(int y = 1; y < tileMap[0].length - 1; y++)
      {
         if(floodMap[x][y] &&
            !floodMap[x + 1][y] &&
            !floodMap[x - 1][y] &&
            !floodMap[x][y + 1] &&
            !floodMap[x][y - 1])
            floodMap[x][y] = false;
      }
      
   }
   
   public void setRegionMap()
   {
      regionMap = new int[tileMap.length][tileMap[0].length];
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
         regionMap[x][y] = -1;
      int index = 0;
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(floodMap[x][y] && regionMap[x][y] == -1)
         {
            setRegion(x, y, index);
            index++;
         }
      }
      regionCount = index;
   }
   
   private void setRegion(int xLoc, int yLoc, int index)
   {
      boolean[][] subFloodMap = FloodFill.fill(floodMap, new Coord(xLoc, yLoc));
      for(int x = 0; x < regionMap.length; x++)
      for(int y = 0; y < regionMap[0].length; y++)
      {
         if(subFloodMap[x][y])
            regionMap[x][y] = index;
      }
   }
   
   // a cell is a room 4x4 or smaller, with a single door and no other entrances
   private void setClosetList()
   {
      closetList = new Vector<Closet>();
      for(int xLoc = 0; xLoc < floodMap.length; xLoc++)
      for(int yLoc = 0; yLoc < floodMap[0].length; yLoc++)
      {
         if(floodMap[xLoc][yLoc])
         {
            boolean[][] subFloodMap = FloodFill.fill(floodMap, new Coord(xLoc, yLoc));
            if(trueCount(subFloodMap) <= MAX_CLOSET_SIZE)
            {
               // possible cell
               Coord start = new Coord(xLoc, yLoc);
               Coord end = new Coord(xLoc, yLoc);
               for(int x = 0; x < floodMap.length; x++)
               for(int y = 0; y < floodMap[0].length; y++)
               {
                  if(subFloodMap[x][y])
                  {
                     start.x = Math.min(start.x, x);
                     end.x = Math.max(end.x, x);
                     start.y = Math.min(start.y, y);
                     end.y = Math.max(end.y, y);
                  }
               }
               Closet c = validateCloset(start, end);
               if(c != null)
               {
                   closetList.add(c);
               }
            }
            closeFloodMapSection(subFloodMap);
         }
      }
   }
   
   private int trueCount(boolean[][] subFloodMap)
   {
      int count = 0;
      for(int x = 0; x < floodMap.length; x++)
      for(int y = 0; y < floodMap[0].length; y++)
      {
         if(subFloodMap[x][y])
            count++;
      }
      return count;
   }
   
   private Closet validateCloset(Coord start, Coord end)
   {
      start = new Coord(start);
      end = new Coord(end);
      start.subtract(new Coord(1, 1));
      end.add(new Coord(1, 1));
      int count = 0;
      Coord lastDoor = new Coord(-1, -1);
      for(int x = start.x; x <= end.x; x++)
      {
         if(tileMap[x][start.y] == TEMPLATE_DOOR)
         {
            lastDoor.x = x;
            lastDoor.y = start.y;
            count++;
         }
         if(tileMap[x][end.y] == TEMPLATE_DOOR)
         {
            lastDoor.x = x;
            lastDoor.y = end.y;
            count++;
         }
      }
      for(int y = start.y; y <= end.y; y++)
      {
         if(tileMap[start.x][y] == TEMPLATE_DOOR)
         {
            lastDoor.x = start.x;
            lastDoor.y = y;
            count++;
         }
         if(tileMap[end.x][y] == TEMPLATE_DOOR)
         {
            lastDoor.x = end.x;
            lastDoor.y = y;
            count++;
         }
      }
      if(count == 1)
      {
         start.add(new Coord(1, 1));
         end.subtract(new Coord(1, 1));
         Coord size = new Coord(end);
         size.subtract(start);
         size.add(new Coord(1, 1));
         return new Closet(start, size, lastDoor);
      }
      return null;
   }
   
   private void closeFloodMapSection(boolean[][] subFloodMap)
   {
      for(int x = 0; x < floodMap.length; x++)
      for(int y = 0; y < floodMap[0].length; y++)
      {
         if(subFloodMap[x][y])
            floodMap[x][y] = false;
      }
   }
   
   public void print()
   {
      char[][] charMap = new char[floodMap.length][floodMap[0].length];
      for(int x = 0; x < charMap.length; x++)
      for(int y = 0; y < charMap[0].length; y++)
      {
         if(regionMap[x][y] != -1)
            charMap[x][y] = (char)('0' + regionMap[x][y]);
         else
            charMap[x][y] = ' ';//tileMap[x][y];
      }
      
      for(Closet closet : closetList)
      {
         for(int x = 0; x < closet.getSize().x; x++)
         for(int y = 0; y < closet.getSize().y; y++)
            charMap[closet.getOrigin().x + x][closet.getOrigin().y + y] = '!';
         charMap[closet.getDoorLoc().x][closet.getDoorLoc().y] = '+';
      }
      
      
      for(int y = 0; y < charMap[0].length; y++)
      {
         for(int x = 0; x < charMap.length; x++)
         {
            System.out.print(charMap[x][y] + "");
         }
         System.out.println();
      }
      System.out.println();
      setFloodMapConnection();
      for(int y = 0; y < charMap[0].length; y++)
      {
         for(int x = 0; x < charMap.length; x++)
         {
            if(floodMap[x][y])
               System.out.print(tileMap[x][y] + "");
            else
               System.out.print(" ");
         }
         System.out.println();
      }
   }
   
   public static void main(String[] args)
   {
      Vector<String> v = new Vector<String>();
      v.add("..cc++");
      v.add("c###c#");
      v.add("c#ccc#");
      v.add("c#c#c#");
      v.add("..c#++");
      ZoneTemplate template = new ZoneTemplate(v);
      ZoneDivisor divisor = new ZoneDivisor(template);
      divisor.print();
      System.out.println("Regions: " + divisor.regionCount);
      System.out.println("Closets:   " + divisor.getCellCount());
      template.print();
   }
}