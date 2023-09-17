package Pulsar.Zone;

import Pulsar.Engine.*;
import WidlerSuite.*;

public class ZoneMapFactory implements ZoneConstants, EngineConstants
{
   
   public static void addBorder(ZoneMap map, MapTile borderTile)
   {
      for(int x = 0; x < map.getWidth(); x++)
      {  
         map.setTile(x, 0, MapTileFactory.getTile(borderTile));
         map.setTile(x, map.getHeight() - 1, MapTileFactory.getTile(borderTile));
      }
      for(int y = 0; y < map.getHeight(); y++)
      {
         map.setTile(0, y, MapTileFactory.getTile(borderTile));
         map.setTile(map.getWidth() - 1, y, MapTileFactory.getTile(borderTile));
      }
   }
   
   public static void labelBulkheads(ZoneMap map)
   {
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(!(map.getTile(x, y) instanceof Vacuum) && isAdjacentToVacuum(map, x, y))
         {
            if(map.getTile(x, y) instanceof Door)
               map.getTile(x, y).setName("Bulkhead Door");
            else if(!map.getTile(x, y).isHighPassable())
               map.getTile(x, y).setName("Bulkhead");
         }
      }
   }
   
   public static boolean isAdjacentToVacuum(ZoneMap map, int x, int y)
   {
      return map.getTile(x + 1, y) instanceof Vacuum ||
             map.getTile(x - 1, y) instanceof Vacuum ||
             map.getTile(x, y + 1) instanceof Vacuum ||
             map.getTile(x, y - 1) instanceof Vacuum;
   }
   
   public static ZoneMap getTestMap()
   {
      int w = 15;
      int h = 15;
      ZoneMap m = new ZoneMap(w, h, MapTileFactory.getTile(TileType.CLEAR));
      addBorder(m, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(5, 5, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(5, 6, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(6, 5, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(6, 6, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(5, 7, MapTileFactory.getDoor());
      for(int y = 1; y < 6; y++)
         m.setTile(3, y, MapTileFactory.getTile(TileType.LOW_WALL));
      m.setTile(3, 6, MapTileFactory.getDoor());
      
      labelBulkheads(m);
      return m;
   }
   
   public static ZoneMap getTestMap2()
   {
      String[] rowArr = {
      "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv",
      "v#######################################################################################################v",
      "v#.....==......................#..................................#....................................#v",
      "v#.....==..........==..........#.......===........................#......===.................==........#v",
      "v#.................==..........#.......===......................../......===.................==........#v",
      "v#.......................==....#..................===.............#..........................==........#v",
      "v#.....==................==....#..................===.............#...===.............==...............#v",
      "v#.....==.......==............./..................===.............#...===.............==........===....#v",
      "v#..............==.............#..................................#...................==........===....#v",
      "v#.............................#..................................#....................................#v",
      "v##############/########################################################################/###############v",
      "v#.....................................................................................................#v",
      "v#.....................................................................................................#v",
      "v#....=.....#..........#..........#..........#..........#..........#..........#..........#..........#..#v",
      "v/....=................................................................................................#v",
      "v#....=................................................................................................#v",
      "v#~~~.......#..........#..........#..........#..........#..........#..........#..........#..........#..#v",
      "v#~~~..................................................................................................#v",
      "v#~~~..................................................................................................#v",
      "v########/###############################################################################/##############v",
      "v#................#..............................#....:.......:.......:.........#......................#v",
      "v#................#..............................#....:...............:.........#......................#v",
      "v#................#.............ccc.ccc........../....:.......:.......:.........#..cccccccc.cccccccc...#v",
      "v#.......cc.......#.............ccc.ccc..........#....:.......:.......:.........#..cccccccc.cccccccc...#v",
      "v#.......cc.......#..............................#....:.......:.......:........./......................#v",
      "v#................#.............ccc.ccc..........#....:.......:.......:.........#..cccccccc.cccccccc...#v",
      "v#................/.............ccc.ccc..........#............:.................#..cccccccc.cccccccc...#v",
      "v#................#..............................#....:.......:.......:.........#......................#v",
      "v#................#..............................#....:.......:.......:.........#......................#v",
      "v#######################################################################################################v",
      "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv",
      };
      int w = rowArr[0].length();
      int h = rowArr.length;
      for(int i = 0; i < 25; i++)
      {
         int x = 0;
         int y = 0;
         while(rowArr[y].charAt(x) != '.')
         {
            x = GameEngine.randomInt(3, w - 4);
            y = GameEngine.randomInt(3, h - 4);
         }
         char[] charArr = rowArr[y].toCharArray();
         charArr[x] = '0';
         rowArr[y] = new String(charArr);
      }
      ZoneMap m = new ZoneMap(w, h, MapTileFactory.getTile(TileType.CLEAR));
      for(int y = 0; y < h; y++)
      {
         for(int x = 0; x < w; x++)
         {
            char tileChar = rowArr[y].charAt(x);
            MapTile mapTile = MapTileFactory.getTile(TileType.HIGH_WALL);
            switch(tileChar)
            {
               case '.' : mapTile = MapTileFactory.getTile(TileType.CLEAR); break;
               case '#' : mapTile = MapTileFactory.getTile(TileType.HIGH_WALL); break;
               case 'c' : mapTile = MapTileFactory.getCrate(); break;
               case '=' : mapTile = MapTileFactory.getTile(TileType.LOW_WALL); break;
               case ':' : mapTile = MapTileFactory.getTile(TileType.WINDOW); break;
               case '/' : mapTile = MapTileFactory.getDoor(); break;
               case '0' : mapTile = MapTileFactory.getExplodingBarrel(); break;
               case '~' : mapTile = MapTileFactory.getWater(); break;
               case 'v' : mapTile = MapTileFactory.getVacuum();
            }
            m.setTile(x, y, mapTile);
         }
      }
      Button button = (Button)MapTileFactory.getButton(-1);
      button.setOneUse(false);
      m.setTile(6, 14, button);
      ButtonTrigger trigger = new ButtonTrigger(-1, ButtonAction.TOGGLE);
      trigger.addTarget(new Coord(1, 14));
      m.add(trigger);
      Door bulkheadDoor = (Door)m.getTile(1, 14);
      bulkheadDoor.setLocked(true);
      labelBulkheads(m);
      return m;
   }
   
   public static ZoneMap getVacuumTest()
   {
      String[] rowArr = {
         "vvvvvvvvvvvvvvvvvvvvv",
         "v###/###############v",
         "v#.......#.........#v",
         "v#.......#.........#v",
         "v#......./.........#v",
         "v#.......#.........#v",
         "v#.......#.........#v",
         "v#.......#.........#v",
         "v###################v",
         "vvvvvvvvvvvvvvvvvvvvv"
      };
      int w = rowArr[0].length();
      int h = rowArr.length;
      ZoneMap m = new ZoneMap(w, h, MapTileFactory.getTile(TileType.CLEAR));
      for(int y = 0; y < h; y++)
      {
         for(int x = 0; x < w; x++)
         {
            char tileChar = rowArr[y].charAt(x);
            MapTile mapTile = MapTileFactory.getTile(TileType.HIGH_WALL);
            switch(tileChar)
            {
               case '.' : mapTile = MapTileFactory.getTile(TileType.CLEAR); break;
               case '#' : mapTile = MapTileFactory.getTile(TileType.HIGH_WALL); break;
               case '/' : mapTile = MapTileFactory.getDoor(); break;
               case 'v' : mapTile = MapTileFactory.getVacuum(); break;
            }
            m.setTile(x, y, mapTile);
         }
      }
      labelBulkheads(m);
      return m;
   }
   
}