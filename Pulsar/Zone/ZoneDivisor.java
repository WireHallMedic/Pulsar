package Pulsar.Zone;

import WidlerSuite.*;
import java.util.*;

public class ZoneDivisor implements ZoneConstants
{
   private ZoneTemplate template;
   private boolean[][] floodMap;
   
   public ZoneDivisor(ZoneTemplate zt)
   {
      template = zt;
      setFloodMap();
   }
   
   public void setFloodMap()
   {
      char[][] tileMap = template.getTileMap();
      floodMap = new boolean[tileMap.length][tileMap[0].length];
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(tileMap[x][y] == TEMPLATE_CLEAR || tileMap[x][y] == TEMPLATE_DOOR)
            floodMap[x][y] = true;
      }
   }
   
   public void print()
   {
      for(int y = 0; y < floodMap[0].length; y++)
      {
         for(int x = 0; x < floodMap.length; x++)
         {
            if(floodMap[x][y])
               System.out.print(".");
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