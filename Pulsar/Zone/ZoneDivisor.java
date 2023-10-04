package Pulsar.Zone;

import WidlerSuite.*;
import java.util.*;

public class ZoneDivisor implements ZoneConstants
{
   private ZoneTemplate template;
   private boolean[][] floodMap;
   private int[][] regionMap;
   
   public ZoneDivisor(ZoneTemplate zt)
   {
      template = zt;
      setFloodMap();
      setRegionMap();
   }
   
   public void setFloodMap()
   {
      char[][] tileMap = template.getTileMap();
      floodMap = new boolean[tileMap.length][tileMap[0].length];
      // mark passability based on low/door passability
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(tileMap[x][y] == TEMPLATE_CLEAR || tileMap[x][y] == TEMPLATE_DOOR)
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
   
   public void setRegionMap()
   {
      char[][] tileMap = template.getTileMap();
      regionMap = new int[tileMap.length][tileMap[0].length];
      int index = 1;
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(floodMap[x][y] && regionMap[x][y] == 0)
         {
            setRegion(x, y, index);
            index++;
         }
      }
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
   
   public void print()
   {
      for(int y = 0; y < floodMap[0].length; y++)
      {
         for(int x = 0; x < floodMap.length; x++)
         {
            if(floodMap[x][y])
               System.out.print(regionMap[x][y] + "");
            else
               System.out.print("#");
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
   }
}