package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class ZoneTemplate extends MapTemplate implements ZoneConstants
{
   public static final boolean MOST_RESTRICTIVE = true;
   public static final boolean RANDOMIZE = false;
   
   public enum ZoneTemplateTile
   {
      DEFAULT_OOB    ('0'),
      ROOM           ('.'),
      WALL_BLOCK     ('#'),
      VACUUM_BLOCK   ('V'),
      INDETERMINATE  ('?');
      
      public char character;
      
      private ZoneTemplateTile(char c)
      {
         character = c;
      }
   }
   
   private RoomTemplateManager roomTemplateManager;
   private boolean[][][] passArray;
   private RoomTemplate[][] roomTemplate;
   private char[][] tileMap;
   
   public boolean[] getPassArray(int x, int y){return passArray[x][y];}
   public RoomTemplate getRoomTemplate(int x, int y){return roomTemplate[x][y];}
   public char[][] getTileMap(){return tileMap;}
   
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm){this(input, rtm, false);}
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm, boolean mostRestrictive)
   {
      super(input);
      validate();
      roomTemplateManager = rtm;
      setPassArray(mostRestrictive);
      generateRooms();
   }
   
   public void generateRooms()
   {
      roomTemplate = new RoomTemplate[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         roomTemplate[x][y] = roomTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         roomTemplate[x][y].rotateUntilMatches(passArray[x][y]);
      }
      setTileMap();
   }
   
   public void setPassArray(boolean mostRestrictive)
   {
      passArray = new boolean[width][height][4];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         boolean[] defaultArr = {false, false, false, false};
      }
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         setPassArrayCell(x, y, mostRestrictive);
      }
   }
   
   private void setPassArrayCell(int x, int y, boolean mostRestrictive)
   {
      char thisCell = getCell(x, y);
      char eastCell = '0';
      if(isInBounds(x + 1, y))
         eastCell = getCell(x + 1, y);
      char southCell = '0';
      if(isInBounds(x, y + 1))
         southCell = getCell(x, y + 1);
         
      if(thisCell == '.')
      {
         if(eastCell == '.' || eastCell == '?')
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(southCell == '.' || southCell == '?')
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
      else if(thisCell == '?')
      {
         boolean eastRoll = GameEngine.randomBoolean() && (!mostRestrictive);
         boolean southRoll = GameEngine.randomBoolean() && (!mostRestrictive);
         if(eastCell == '.' || (eastCell == '?' && eastRoll))
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(southCell == '.' || (southCell == '?' && southRoll))
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
   }
   
   public void setTileMap()
   {
      // create tile map
      int roomWidth = roomTemplate[0][0].getWidth();
      int roomHeight = roomTemplate[0][0].getHeight();
      int mapWidth = ((roomWidth - 1) * width) + 1;
      int mapHeight = ((roomHeight - 1) * height) + 1;
      char[][] newMap = new char[mapWidth][mapHeight];
      // fill with lowest priority tile (OOB)
      for(int x = 0; x < mapWidth; x++)
      for(int y = 0; y < mapHeight; y++)
         newMap[x][y] = '0';
      // place room at 0, 0
      placeRoom(newMap, 0, 0, roomTemplate[0][0]);
      // place all rooms at y = 0 (except 0, 0)
      for(int x = 1; x < width; x++)
         placeRoom(newMap, x * (roomWidth - 1), 0, roomTemplate[x][0]);
      // place all rooms at x = 0 (except 0, 0)
      for(int y = 1; y < height; y++)
         placeRoom(newMap, 0, y * (roomHeight - 1), roomTemplate[0][y]);
      // place all other rooms
      for(int x = 1; x < width; x++)
      for(int y = 1; y < height; y++)
         placeRoom(newMap, x * (roomWidth - 1), y * (roomHeight - 1), roomTemplate[x][y]);
      tileMap = newMap;
   }
   
   private void placeRoom(char[][] tileMap, int xOrigin, int yOrigin, RoomTemplate roomTemplate)
   {
      int roomWidth = roomTemplate.getWidth();
      int roomHeight = roomTemplate.getHeight();
      // north and south border cells
      for(int x = 0; x < roomWidth; x++)
      {
         tileMap[x + xOrigin][yOrigin] = pickBorderTile(tileMap[x + xOrigin][yOrigin], roomTemplate.getCell(x, 0));
         tileMap[x + xOrigin][yOrigin + roomHeight - 1] = pickBorderTile(tileMap[x + xOrigin][yOrigin + roomHeight - 1], roomTemplate.getCell(x, roomHeight - 1));
      }
      // east and west border cells
      for(int y = 1; y < roomHeight - 1; y++)
      {
         tileMap[xOrigin][y + yOrigin] = pickBorderTile(tileMap[xOrigin][y + yOrigin], roomTemplate.getCell(0, y));
         tileMap[xOrigin + roomWidth - 1][y + yOrigin] = pickBorderTile(tileMap[xOrigin + roomWidth - 1][y + yOrigin], roomTemplate.getCell(roomWidth - 1, y));
      }
      // non-border cells
      for(int x = 1; x < roomWidth - 1; x++)
      for(int y = 1; y < roomHeight - 1; y++)
         tileMap[x + xOrigin][y + yOrigin] = roomTemplate.getCell(x, y);
   }
   
   // returns the appropriate border tile from a pair
   public char pickBorderTile(char a, char b)
   {
      if(a == '/' || b == '/')
         return '/';
      if(a == '#' || b == '#')
         return '#';
      if(a == '.' || b == '.')
         return '.';
      if(a == 'V' || b == 'V')
         return 'V';
      return '0';
   }
   
   public void print()
   {
      char[][] charMap = getTileMap();
      for(int y = 0; y < charMap[0].length; y++)
      {
         for(int x = 0; x < charMap.length; x++)
         {
            System.out.print(charMap[x][y] + "");
         }
         System.out.println("");
      }
   }
   
   public void validate() 
   {
      if(!hasFullConnectivity())
         throw new java.lang.Error("ZoneTemplate has potentially unreachable rooms.");
      if(!hasNoIsolation())
         throw new java.lang.Error("ZoneTemplate has potentially isolated rooms.");
   }
   
   private boolean hasFullConnectivity()
   {
      // generate map using demo tileset; all # and .
      roomTemplateManager = new RoomTemplateManager();
      roomTemplateManager.loadDemos();
      setPassArray(ZoneTemplate.MOST_RESTRICTIVE);
      generateRooms();
      Coord origin = new Coord(-1, -1);
      char[][] tempMap = getTileMap();
      // create passability map
      boolean[][] passMap = new boolean[tempMap.length][tempMap[0].length];
      for(int x = 0; x < tempMap.length; x++)
      for(int y = 0; y < tempMap[0].length; y++)
      {
         if(tempMap[x][y] == '.')
         {
            origin.x = x;
            origin.y = y;
            passMap[x][y] = true;
         }
      }
      //create floodfill
      boolean[][] fillMap = FloodFill.fill(passMap, origin);
      // check all . tiles are flooded, and all # are not
      for(int x = 0; x < tempMap.length; x++)
      for(int y = 0; y < tempMap[0].length; y++)
      {
         if((tempMap[x][y] == '.') != fillMap[x][y])
            return false;
      }
      return true;
   }
   
   
   // Isolation can occur if there are two ? tiles adjacent to each other, each entirely surrounded by ? or impassable tiles
   private boolean hasNoIsolation()
   {
      boolean[][] surroundedMap = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         surroundedMap[x][y] = isSurroundedProbabilistic(x, y);

      for(int x = 0; x < width - 1; x++)
      for(int y = 0; y < height; y++)
      {
         if(surroundedMap[x][y] && surroundedMap[x + 1][y])
            return false;
      }
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height - 1; y++)
      {
         if(surroundedMap[x][y] && surroundedMap[x][y + 1])
            return false;
      }
      return true;
   }
   
   private boolean isSurroundedProbabilistic(int x, int y)
   {
      if(getCell(x, y) == '?')
      {
         if(!isInBounds(x + 1, y) || getCell(x + 1, y) != '.')
         if(!isInBounds(x - 1, y) || getCell(x - 1, y) != '.')
         if(!isInBounds(x, y + 1) || getCell(x, y + 1) != '.')
         if(!isInBounds(x, y - 1) || getCell(x, y - 1) != '.')
            return true;
      }
      return false;
   }
   
   public static void main(String[] args)
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadFromFile("Room Templates.txt");
      Vector<String> v = new Vector<String>();
      v.add("#..#");
      v.add(".??.");
      v.add(".??.");
      v.add("#..#");
      ZoneTemplate zt = new ZoneTemplate(v, rtm, false);
      zt.print();
   }
}