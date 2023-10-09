package Pulsar.Zone;

import WidlerSuite.*;
import Pulsar.Engine.*;

public class ZoneDecorator
{
   public static final double STORAGE_ROOM_CHANCE = .3333;
   public static final double STORAGE_ROOM_CRATE_CHANCE = .5;
   
   private static ZoneBuilder zoneBuilder = null;
   
   public static void decorate(ZoneBuilder zb)
   {
      zoneBuilder = zb;
      addCrates();
   }
   
   public static void addCrates()
   {
      for(Closet closet : zoneBuilder.getClosetList())
      {
         if(GameEngine.random() <= STORAGE_ROOM_CHANCE)
            addCratesToCloset(closet);
      }
      for(int x = 0; x < zoneBuilder.getWidth(); x++)
      for(int y = 0; y < zoneBuilder.getHeight(); y++)
      {
         switch(zoneBuilder.getCell(x, y))
         {
            case ZoneBuilder.OPEN_ROOM :     addCrates(x, y, GameEngine.randomInt(3, 6)); 
                                             break;
            case ZoneBuilder.CLOSED_ROOM :   addCrates(x, y, GameEngine.randomInt(3, 6)); 
                                             break;
            case ZoneBuilder.CORRIDOR :      if(GameEngine.randomInt(0, 3) == 0)
                                                addCrates(x, y, GameEngine.randomInt(1, 4)); 
                                             break;
            case ZoneBuilder.STARTING_ROOM : break;
            case ZoneBuilder.OOB_ROOM :      break;
         }
      }
      addBossCrates(zoneBuilder.getBossRoom().x, zoneBuilder.getBossRoom().y);
   }
   
   private static void addCrates(int xLoc, int yLoc, int numOfCrates){addCrates(xLoc, yLoc, numOfCrates, false);}
   private static void addCrates(int xLoc, int yLoc, int numOfCrates, boolean lootCrate)
   {
      int cratesPlaced = 0;
      int minX = getMinX(xLoc);
      int minY = getMinY(yLoc);
      int maxX = getMaxX(xLoc);
      int maxY = getMaxY(yLoc);
      char crateChar = ZoneBuilder.TEMPLATE_CRATE;
      if(lootCrate)
         crateChar = ZoneBuilder.TEMPLATE_LOOT_CRATE;
      int consecutiveLoops = 0;
      while(cratesPlaced < numOfCrates && consecutiveLoops < 20)
      {
         consecutiveLoops++;
         int x = GameEngine.randomInt(minX, maxX);
         int y = GameEngine.randomInt(minY, maxY);
         if(zoneBuilder.canPlaceCrate(x, y))
         {
            zoneBuilder.getTileMap()[x][y] = crateChar;
            cratesPlaced++;
            consecutiveLoops = 0;
         }
      }
   }
   
   private static void addBossCrates(int xLoc, int yLoc)
   {
      addCrates(xLoc, yLoc, GameEngine.randomInt(4, 7), true);
   }
   
   private static void addCratesToCloset(Closet c)
   {
      for(int x = c.getOrigin().x; x < c.getOrigin().x + c.getSize().x; x++)
      for(int y = c.getOrigin().y; y < c.getOrigin().y + c.getSize().y; y++)
      {
         if(!zoneBuilder.isAdjacentToDoor(x, y))
         if(GameEngine.random() <= STORAGE_ROOM_CRATE_CHANCE)
         {
            zoneBuilder.getTileMap()[x][y] = ZoneBuilder.TEMPLATE_CRATE;
         }
      }
   }
   
   private static int getMinX(int x)
   {
      return x * (zoneBuilder.getRoomWidth() - 1);
   }
   
   private static int getMinY(int y)
   {
      return y * (zoneBuilder.getRoomHeight() - 1);
   }
   
   private static int getMaxX(int x)
   {
      return getMinX(x) + zoneBuilder.getRoomWidth();
   }
   
   private static int getMaxY(int y)
   {
      return getMinY(y) + zoneBuilder.getRoomHeight();
   }

}