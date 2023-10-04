package Pulsar.Zone;

import WidlerSuite.*;
import java.util.*;

public class ZoneDivisor implements ZoneConstants
{
   public static final int MAX_CELL_SIZE = 16;
   
   private ZoneTemplate template;
   private char[][] tileMap;
   private boolean[][] floodMap;
   private int[][] regionMap;
   private Vector<Room> cellList;
   private int regionCount;
   
   public int getCellCount(){return cellList.size();}
   
   public ZoneDivisor(ZoneTemplate zt)
   {
      template = zt;
      tileMap = template.getTileMap();
      setFloodMapRegion();
      setRegionMap();
      setFloodMapCell();
      setCellList();
   }
   
   private boolean isLowPassableTile(char c)
   {
      switch(c)
      {
         case TEMPLATE_CLEAR :
         case TEMPLATE_RUBBLE :
         case TEMPLATE_WATER :   return true;
      }
      return false;
   }
   
   public void setFloodMapRegion()
   {
      floodMap = new boolean[tileMap.length][tileMap[0].length];
      // mark passability based on low/door passability
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(isLowPassableTile(tileMap[x][y]) || tileMap[x][y] == TEMPLATE_DOOR)
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
         if(isLowPassableTile(tileMap[x][y]))
            floodMap[x][y] = true;
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
   private void setCellList()
   {
      cellList = new Vector<Room>();
      for(int xLoc = 0; xLoc < floodMap.length; xLoc++)
      for(int yLoc = 0; yLoc < floodMap[0].length; yLoc++)
      {
         if(floodMap[xLoc][yLoc])
         {
            boolean[][] subFloodMap = FloodFill.fill(floodMap, new Coord(xLoc, yLoc));
            if(trueCount(subFloodMap) <= MAX_CELL_SIZE)
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
               if(doorCount(start, end) == 1)
               {
                  Room room = new Room();
                  room.origin = start;
                  Coord size = new Coord(end);
                  size.subtract(start);
                  size.add(new Coord(1, 1));
                  room.size = size;
                  cellList.add(room);
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
   
   private int doorCount(Coord start, Coord end)
   {
      start = new Coord(start);
      end = new Coord(end);
      start.subtract(new Coord(1, 1));
      end.add(new Coord(1, 1));
      int count = 0;
      for(int x = start.x; x <= end.x; x++)
      {
         if(tileMap[x][start.y] == TEMPLATE_DOOR)
            count++;
         if(tileMap[x][end.y] == TEMPLATE_DOOR)
            count++;
      }
      for(int y = start.y; y <= end.y; y++)
      {
         if(tileMap[start.x][y] == TEMPLATE_DOOR)
            count++;
         if(tileMap[end.x][y] == TEMPLATE_DOOR)
            count++;
      }
      return count;
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
      for(int y = 0; y < floodMap[0].length; y++)
      {
         for(int x = 0; x < floodMap.length; x++)
         {
            if(regionMap[x][y] != -1)
               System.out.print(regionMap[x][y] + "");
            else
               System.out.print(tileMap[x][y]);
         }
         System.out.println();
      }
   }
   
   public static void main(String[] args)
   {
      Vector<String> v = new Vector<String>();
      v.add("#...#");
      v.add("##c##");
      v.add("++C++");
      v.add("++c++");
      v.add("++C++");
      v.add("##c##");
      v.add("#...#");
      ZoneTemplate template = new ZoneTemplate(v);
      ZoneDivisor divisor = new ZoneDivisor(template);
      divisor.print();
      System.out.println("Regions: " + divisor.regionCount);
      System.out.println("Cells:   " + divisor.getCellCount());
   }
}