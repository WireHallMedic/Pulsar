package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;

public class ZoneTemplate extends MapTemplate implements ZoneConstants
{
   
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
   
   private boolean[][][] passArray;
   private RoomTemplate[][] roomTemplate;
   
   public boolean[] getPassArray(int x, int y){return passArray[x][y];}
   public RoomTemplate getRoomTemplate(int x, int y){return roomTemplate[x][y];}
   
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm){this(input, rtm, false);}
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm, boolean mostRestrictive)
   {
      super(input);
      setPassArray(mostRestrictive);
      generateRooms(rtm);
   }
   
   public void generateRooms(RoomTemplateManager rtm)
   {
      roomTemplate = new RoomTemplate[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         roomTemplate[x][y] = rtm.random(RoomTemplate.determineType(passArray[x][y]));
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
   
   public static void main(String[] args)
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadDemos();
      Vector<String> v = new Vector<String>();
      v.add("#.#");
      v.add("...");
      v.add("#.#");
      ZoneTemplate zt = new ZoneTemplate(v, rtm, true);
      zt.print();
      zt.setPassArray(false);
      System.out.println("");
      zt.print();
      
      RoomTemplate rt = rtm.random(RoomTemplate.RoomTemplateType.TERMINAL);
      rt.print();
      System.out.println("" + rt.passNorth());
      System.out.println("" + rt.passEast());
      System.out.println("" + rt.passSouth());
      System.out.println("" + rt.passWest());
      
      rt.rotate();
      rt.print();
   }
}