package Pulsar.Zone;

import Pulsar.Engine.*;

public class ZoneMapFactory implements ZoneConstants
{
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
      
      return m;
   }
   
   public static ZoneMap getTestMap2()
   {
      String[] rowArr = {
      "#######################################################################################################",
      "#.....==......................#..................................#....................................#",
      "#.....==..........==..........#.......===........................#......===.................==........#",
      "#.................==..........#.......===......................../......===.................==........#",
      "#.......................==....#..................===.............#..........................==........#",
      "#.....==................==....#..................===.............#...===.............==...............#",
      "#.....==.......==............./..................===.............#...===.............==........===....#",
      "#..............==.............#..................................#...................==........===....#",
      "#.............................#..................................#....................................#",
      "##############/########################################################################/###############",
      "#.....................................................................................................#",
      "#.....................................................................................................#",
      "#..........#..........#..........#..........#..........#..........#..........#..........#..........#..#",
      "#......=..............................................................................................#",
      "#.....................................................................................................#",
      "#..........#..........#..........#..........#..........#..........#..........#..........#..........#..#",
      "#.....................................................................................................#",
      "#.....................................................................................................#",
      "########/###############################################################################/##############",
      "#................#..............................#....:.......:.......:.........#......................#",
      "#................#..............................#....:...............:.........#......................#",
      "#................#.............ccc.ccc........../....:.......:.......:.........#..cccccccc.cccccccc...#",
      "#.......cc.......#.............ccc.ccc..........#....:.......:.......:.........#..cccccccc.cccccccc...#",
      "#.......cc.......#..............................#....:.......:.......:........./......................#",
      "#................#.............ccc.ccc..........#....:.......:.......:.........#..cccccccc.cccccccc...#",
      "#................/.............ccc.ccc..........#............:.................#..cccccccc.cccccccc...#",
      "#................#..............................#....:.......:.......:.........#......................#",
      "#................#..............................#....:.......:.......:.........#......................#",
      "#######################################################################################################",
      };
      int w = rowArr[0].length();
      int h = rowArr.length;
      for(int i = 0; i < 25; i++)
      {
         int x = 0;
         int y = 0;
         while(rowArr[y].charAt(x) != '.')
         {
            x = GameEngine.randomInt(0, w);
            y = GameEngine.randomInt(0, h);
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
            }
            m.setTile(x, y, mapTile);
         }
      }
      m.setTile(1, h / 2, MapTileFactory.getButton(-1));
      
      return m;
   }
   
   public static void addBorder(ZoneMap map, MapTile borderTile)
   {
      for(int x = 0; x < map.getWidth(); x++)
      {
         map.setTile(x, 0, new MapTile(borderTile));
         map.setTile(x, map.getHeight() - 1, new MapTile(borderTile));
      }
      for(int y = 0; y < map.getHeight(); y++)
      {
         map.setTile(0, y, new MapTile(borderTile));
         map.setTile(map.getWidth() - 1, y, new MapTile(borderTile));
      }
   }
}