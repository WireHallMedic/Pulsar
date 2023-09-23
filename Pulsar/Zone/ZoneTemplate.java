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
   
   public boolean[] getPassArray(int x, int y){return passArray[x][y];}
   public RoomTemplate getRoomTemplate(int x, int y){return roomTemplate[x][y];}
   
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm){this(input, rtm, false);}
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm, boolean mostRestrictive)
   {
      super(input);
      roomTemplateManager = rtm;
      validate();
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
   
   public char[][] getTileMap()
   {
      int roomWidth = roomTemplate[0][0].getWidth();
      int roomHeight = roomTemplate[0][0].getHeight();
      char[][] map = new char[width * roomWidth][height * roomHeight];
      for(int x = 0; x < map.length; x++)
      for(int y = 0; y < map[0].length; y++)
      {
         map[x][y] = roomTemplate[x / roomWidth][y / roomHeight].getCell(x % roomWidth, y % roomHeight);
      }
      return map;
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
      if(!checkConnectivity())
         throw new java.lang.Error("ZoneTemplate has potentially unreachable rooms.");
   }
   
   private boolean checkConnectivity()
   {
      // generate map using demo tileset; all # and .
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadDemos();
      setPassArray(ZoneTemplate.MOST_RESTRICTIVE);
      generateRooms();
      Coord origin = new Coord(-1, -1);
      char[][] tileMap = getTileMap();
      // create passability map
      boolean[][] passMap = new boolean[tileMap.length][tileMap[0].length];
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(tileMap[x][y] == '.')
         {
            origin.x = x;
            origin.y = y;
            passMap[x][y] = true;
         }
      }
      //create floodfill
      boolean[][] fillMap = FloodFill.fill(passMap, origin);
      // check all . tiles are flooded, and all # are not
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if((tileMap[x][y] == '.') != fillMap[x][y])
            return false;
      }
      return true;
   }
   
   public static void main(String[] args)
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadDemos();
      Vector<String> v = new Vector<String>();
      v.add("#..#");
      v.add(".??.");
      v.add(".??.");
      v.add("#..#");
      ZoneTemplate zt = new ZoneTemplate(v, rtm, false);
      zt.print();
   }
}