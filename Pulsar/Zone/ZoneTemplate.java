package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class ZoneTemplate extends MapTemplate implements ZoneConstants
{
   public static final boolean MOST_RESTRICTIVE = true;
   public static final boolean RANDOMIZE = false;

   
   private RoomTemplateManager roomTemplateManager;
   private RoomTemplateManager corridorTemplateManager;
   private ObstacleTemplateManager obstacleTemplateManager;
   private boolean[][][] passArray;
   private boolean[][] corridorArray;
   private RoomTemplate[][] baseRoomTemplate;
   private RoomTemplate[][] rolledRoomTemplate;
   private char[][] tileMap;
   private Vector<ButtonTrigger> triggerList;
   
   public boolean[] getPassArray(int x, int y){return passArray[x][y];}
   public boolean[][] getCorridorArray(){return corridorArray;}
   public RoomTemplate getBaseRoomTemplate(int x, int y){return baseRoomTemplate[x][y];}
   public RoomTemplate getRolledRoomTemplate(int x, int y){return rolledRoomTemplate[x][y];}
   public char[][] getTileMap(){return tileMap;}
   public Vector<ButtonTrigger> getTriggerList(){return triggerList;}
   
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm){this(input, rtm, false);}
   public ZoneTemplate(Vector<String> input, RoomTemplateManager rtm, boolean mostRestrictive)
   {
      super(input);
      validateInitial();
      roomTemplateManager = rtm;
      corridorTemplateManager = new RoomTemplateManager();
      corridorTemplateManager.loadFromFile("Corridor Templates.txt");
      obstacleTemplateManager = new ObstacleTemplateManager();
      obstacleTemplateManager.loadFromFile("Obstacle Templates.txt");
      setPassArray(mostRestrictive);
      triggerList = new Vector<ButtonTrigger>();
      generateRooms();
   }
   
   public void generateRooms(){generateRooms(false);}
   public void generateRooms(boolean testing)
   {
      baseRoomTemplate = new RoomTemplate[width][height];
      rolledRoomTemplate = new RoomTemplate[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(corridorArray[x][y] && !testing)
            baseRoomTemplate[x][y] = corridorTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         else
            baseRoomTemplate[x][y] = roomTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         baseRoomTemplate[x][y].rotateUntilMatches(passArray[x][y]);
         rolledRoomTemplate[x][y] = null;
      }
      setObstacles();
      process();
      validateButtons();
   }
   
   public void setPassArray(boolean mostRestrictive)
   {
      passArray = new boolean[width][height][4];
      corridorArray = new boolean[width][height];
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
   
   private boolean isSection(char c)
   {
      return c >= '1' && c <= '9';
   }
   
   private boolean isGuaranteedPassable(char c)
   {
      return c == '.' || c == 'c';
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
      
      if(thisCell == 'c')
         corridorArray[x][y] = true;
      
      // clear and corridor
      if(thisCell == '.' || thisCell == 'c')
      {
         if(isGuaranteedPassable(eastCell) || eastCell == '?' || isSection(eastCell))
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(isGuaranteedPassable(southCell) || southCell == '?' || isSection(southCell))
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
      
      // clear and sections
      if(isSection(thisCell))
      {
         if(isGuaranteedPassable(eastCell) || eastCell == '?' || eastCell == thisCell)
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(isGuaranteedPassable(southCell) || southCell == '?' || southCell == thisCell)
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
      
      // probabalistic
      else if(thisCell == '?')
      {
         boolean eastRoll = GameEngine.randomBoolean() && (!mostRestrictive);
         boolean southRoll = GameEngine.randomBoolean() && (!mostRestrictive);
         if(isGuaranteedPassable(eastCell) || isSection(eastCell) || (eastCell == '?' && eastRoll))
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(isGuaranteedPassable(southCell) || isSection(southCell) ||  (southCell == '?' && southRoll))
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
   }
   
   public void setObstacles()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         rolledRoomTemplate[x][y] = baseRoomTemplate[x][y].resolveObstacles(obstacleTemplateManager);
      }
   }
   
   public void process()
   {
      // clear trigger list
      triggerList = new Vector<ButtonTrigger>();
      // create tile map
      int roomWidth = rolledRoomTemplate[0][0].getWidth();
      int roomHeight = rolledRoomTemplate[0][0].getHeight();
      int mapWidth = ((roomWidth - 1) * width) + 1;
      int mapHeight = ((roomHeight - 1) * height) + 1;
      char[][] newMap = new char[mapWidth][mapHeight];
      // fill with lowest priority tile (OOB)
      for(int x = 0; x < mapWidth; x++)
      for(int y = 0; y < mapHeight; y++)
         newMap[x][y] = '0';
      // place room at 0, 0
      placeRoom(newMap, 0, 0, rolledRoomTemplate[0][0].resolveProbTiles());
      // place all rooms at y = 0 (except 0, 0)
      for(int x = 1; x < width; x++)
         placeRoom(newMap, x * (roomWidth - 1), 0, rolledRoomTemplate[x][0].resolveProbTiles());
      // place all rooms at x = 0 (except 0, 0)
      for(int y = 1; y < height; y++)
         placeRoom(newMap, 0, y * (roomHeight - 1), rolledRoomTemplate[0][y].resolveProbTiles());
      // place all other rooms
      for(int x = 1; x < width; x++)
      for(int y = 1; y < height; y++)
         placeRoom(newMap, x * (roomWidth - 1), y * (roomHeight - 1), rolledRoomTemplate[x][y].resolveProbTiles());
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
      
      // button triggers
      for(ButtonTrigger trigger : roomTemplate.getTriggerList())
      {
         trigger = new ButtonTrigger(trigger);
         trigger.shift(xOrigin, yOrigin);
         triggerList.add(trigger);
      }
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
   
   // has the side effects, set rooms after
   public boolean validateInitial() 
   {
      if(!hasFullConnectivity())
         throw new java.lang.Error("ZoneTemplate has potentially unreachable rooms.");
      if(!hasNoIsolation())
         throw new java.lang.Error("ZoneTemplate has potentially isolated rooms.");
      return true;
   }
   
   public boolean validateButtons()
   {
      int buttonsFound = 0;
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(tileMap[x][y] == TEMPLATE_BUTTON)
         {
            Coord c = new Coord(x, y);
            if(triggerList.size() == 0)
            {
               throw new java.lang.Error("ZoneTemplate has button with no trigger.");
            }
            boolean foundF = false;
            for(ButtonTrigger trigger : triggerList)
            {
               
               if(trigger.getCallerLoc() == null)
               {
                  throw new java.lang.Error("ZoneTemplate has ButtonTrigger with no callerLoc.");
               }
               if(trigger.getCallerLoc().equals(c))
               {
                  buttonsFound++;
                  foundF = true;
                  break;
               }
            }
            if(!foundF)
            {
               String buttons = "";
               for(int xx = 0; xx < tileMap.length; xx++)
               for(int yy = 0; yy < tileMap[0].length; yy++)
               {
                  if(tileMap[xx][yy] == TEMPLATE_BUTTON)
                     buttons += "[" + xx + ", " + yy + "] ";
               }
               throw new java.lang.Error("ZoneTemplate has ButtonTrigger with bad callerLoc: expected " + c + ", found: " + buttons + " buttons with " + triggerList.size() + " triggers.");
            }
         }
      }
      if(buttonsFound != triggerList.size())
         throw new java.lang.Error("ZoneTemplate has " + triggerList.size() + " ButtonTriggers, but " + buttonsFound + "buttons were found.");
      return true;
   }
   
   private boolean hasFullConnectivity()
   {
      // generate map using demo tileset; all # and .
      roomTemplateManager = new RoomTemplateManager();
      roomTemplateManager.loadDemos();
      setPassArray(ZoneTemplate.MOST_RESTRICTIVE);
      generateRooms(true);
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
   
   // returns a zone template that includes at least one of each RoomTemplate
   public static ZoneTemplate getDemo(){return getDemo("Room Templates.txt");}
   public static ZoneTemplate getDemo(String roomTemplateFileName)
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadFromFile(roomTemplateFileName);
      Vector<String> v = new Vector<String>();
      v.add("#.??");
      v.add("?.?.");
      v.add("??#.");
      v.add("?.#.");
      return new ZoneTemplate(v, rtm, false);
   }
   
   public static ZoneTemplate getBasicZoneTemplate()
   {
   
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadFromFile("Room Templates.txt");
      Vector<String> v = new Vector<String>();
      v.add("?.?###");
      v.add("?.?.##");
      v.add("?.??..");
      v.add("?.?.##");
      v.add("?.?###");
      return new ZoneTemplate(v, rtm, false);
   }
   
   public static ZoneTemplate getSectionTemplate()
   {
   
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadFromFile("Room Templates.txt");
      Vector<String> v = new Vector<String>();
      v.add("110001122");
      v.add("11cc011c2");
      v.add("220c000c0");
      v.add("22ccccccc");
      v.add("220c00020");
      v.add("11ccc1122");
      v.add("110001122");
      return new ZoneTemplate(v, rtm, false);
   }
   
   public static void main(String[] args)
   {
      ZoneTemplate zt = ZoneTemplate.getSectionTemplate();
    //  zt.validateInitial();
      zt.print();
   }

}