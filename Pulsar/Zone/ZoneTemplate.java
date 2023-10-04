package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class ZoneTemplate extends MapTemplate implements ZoneConstants
{
   public static final boolean MOST_RESTRICTIVE = true;
   public static final boolean RANDOMIZE = false;
   public static final char OPEN_ROOM = TEMPLATE_CLEAR;
   public static final char CLOSED_ROOM = '+';
   public static final char OOB_ROOM = TEMPLATE_OOB;
   public static final char INCLUSIVE_CORRIDOR = 'C';
   public static final char EXCLUSIVE_CORRIDOR = 'c';
   public static final char STARTING_ROOM = 'S';

   
   private RoomTemplateManager openTemplateManager;
   private RoomTemplateManager closedTemplateManager;
   private RoomTemplateManager corridorTemplateManager;
   private ObstacleTemplateManager obstacleTemplateManager;
   private RoomTemplateManager startTemplateManager;
   private boolean[][][] passArray;
   private RoomTemplate[][] baseRoomTemplate;
   private RoomTemplate[][] rolledRoomTemplate;
   private char[][] tileMap;
   private Vector<ButtonTrigger> triggerList;
   private int roomWidth;
   private int roomHeight;
   
   public boolean[] getPassArray(int x, int y){return passArray[x][y];}
   public RoomTemplate getBaseRoomTemplate(int x, int y){return baseRoomTemplate[x][y];}
   public RoomTemplate getRolledRoomTemplate(int x, int y){return rolledRoomTemplate[x][y];}
   public char[][] getTileMap(){return tileMap;}
   public Vector<ButtonTrigger> getTriggerList(){return triggerList;}
   public int getRoomWidth(){return roomWidth;}
   public int getRoomHeight(){return roomHeight;}
   
   public ZoneTemplate(Vector<String> input, RoomTemplateManager openTM, RoomTemplateManager closedTM,
                       RoomTemplateManager corridorTM, ObstacleTemplateManager obstacleTM, RoomTemplateManager startTM)
   {
      super(input);
      openTemplateManager = openTM;
      closedTemplateManager = closedTM;
      corridorTemplateManager = corridorTM;
      obstacleTemplateManager = obstacleTM;
      startTemplateManager = startTM;
      if(openTemplateManager == null)
         openTemplateManager = new RoomTemplateManager("Open Room Templates.txt");
      if(closedTemplateManager == null)
         closedTemplateManager = new RoomTemplateManager("Closed Room Templates.txt");
      if(corridorTemplateManager == null)
         corridorTemplateManager = new RoomTemplateManager("Corridor Templates.txt");
      if(obstacleTemplateManager == null)
         obstacleTemplateManager = new ObstacleTemplateManager("Obstacle Templates.txt");
      if(startTemplateManager == null)
         startTemplateManager = new RoomTemplateManager("Starting Room Templates.txt");
      RoomTemplate rt = openTemplateManager.random(RoomTemplate.RoomTemplateType.BLOCK);
      roomWidth = rt.getWidth();
      roomHeight = rt.getHeight();
      setPassArray();
      triggerList = new Vector<ButtonTrigger>();
      generateRooms();
      cleanUpStruts();
   }
   
   public ZoneTemplate(ZoneTemplate that)
   {
      this(that.serialize(), that.openTemplateManager, that.closedTemplateManager, 
           that.corridorTemplateManager, that.obstacleTemplateManager, that.startTemplateManager);
   }
   
   public ZoneTemplate(Vector<String> input)
   {
      this(input, null, null, null, null, null);
   }
   
   public Vector<String> serialize()
   {
      Vector<String> vect = new Vector<String>();
      for(int y = 0; y < getHeight(); y++)
      {
         String str = "";
         for(int x = 0; x < getWidth(); x++)
         {
            str += getCell(x, y);
         }
         vect.add(str);
      }
      return vect;  
   }
   
   public void generateRooms()
   {
      baseRoomTemplate = new RoomTemplate[width][height];
      rolledRoomTemplate = new RoomTemplate[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(isCorridor(getCell(x, y)))
            baseRoomTemplate[x][y] = corridorTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         else
         {
            if(getCell(x, y) == OPEN_ROOM)
               baseRoomTemplate[x][y] = openTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
            else if(getCell(x, y) == CLOSED_ROOM)
               baseRoomTemplate[x][y] = closedTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
            else if(getCell(x, y) == STARTING_ROOM)
               baseRoomTemplate[x][y] = startTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
            else if(GameEngine.randomBoolean())
               baseRoomTemplate[x][y] = openTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
            else
               baseRoomTemplate[x][y] = closedTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         }
         baseRoomTemplate[x][y].rotateUntilMatches(passArray[x][y]);
         rolledRoomTemplate[x][y] = null;
         addCorridorDoors(x, y);
      }
      setObstacles();
      process();
      validateButtons();
   }
   
   public void setPassArray()
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
         setPassArrayCell(x, y);
      }
   }
   
   // add a door to each corrider
   private void addCorridorDoors(int x, int y)
   {
      if(isCorridor(getCell(x, y)))
      {
         int w = baseRoomTemplate[x][y].getWidth();
         int h = baseRoomTemplate[x][y].getHeight();
         if(passArray[x][y][NORTH] && !isCorridor(getCell(x, y - 1)))
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(i, 0, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(w / 2, 0, TEMPLATE_DOOR);
         }
         if(passArray[x][y][SOUTH] && !isCorridor(getCell(x, y + 1)))
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(i, h - 1, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(w / 2, h - 1, TEMPLATE_DOOR);
         }
         if(passArray[x][y][EAST] && !isCorridor(getCell(x + 1, y)))
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(w - 1, i, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(w - 1, h / 2, TEMPLATE_DOOR);
         }
         if(passArray[x][y][WEST] && !isCorridor(getCell(x - 1, y)))
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(0, i, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(0, h / 2, TEMPLATE_DOOR);
         }
      }
   }
   
   private boolean isGuaranteedPassable(char c)
   {
      return c == OPEN_ROOM || c == CLOSED_ROOM || c == INCLUSIVE_CORRIDOR || c == STARTING_ROOM;
   }
   
   private boolean isCorridor(char c)
   {
      return c == INCLUSIVE_CORRIDOR || c == EXCLUSIVE_CORRIDOR;
   }
   
   public boolean isCorridor(int x, int y)
   {
      return isCorridor(getCell(x, y));
   }
   
   private void setPassArrayCell(int x, int y)
   {
      char thisCell = getCell(x, y);
      char eastCell = OOB_ROOM;
      char southCell = OOB_ROOM;
      if(isInBounds(x + 1, y))
         eastCell = getCell(x + 1, y);
      if(isInBounds(x, y + 1))
         southCell = getCell(x, y + 1);
      
      // clear
      if(thisCell == OPEN_ROOM || thisCell == CLOSED_ROOM || thisCell == STARTING_ROOM)
      {
         if(isGuaranteedPassable(eastCell))
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(isGuaranteedPassable(southCell))
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
      
      // inclusive corridor
      if(thisCell == INCLUSIVE_CORRIDOR)
      {
         if(isGuaranteedPassable(eastCell)|| eastCell == EXCLUSIVE_CORRIDOR)
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(isGuaranteedPassable(southCell) || southCell == EXCLUSIVE_CORRIDOR)
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
      
      // exclusive corridor
      if(thisCell == EXCLUSIVE_CORRIDOR)
      {
         if(eastCell == EXCLUSIVE_CORRIDOR || eastCell == INCLUSIVE_CORRIDOR)
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(southCell == EXCLUSIVE_CORRIDOR || southCell == INCLUSIVE_CORRIDOR)
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
      {
         newMap[x][y] = OOB_ROOM;
      }
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
      if(a == TEMPLATE_DOOR || b == TEMPLATE_DOOR)
         return TEMPLATE_DOOR;
      if(a == TEMPLATE_WALL || b == TEMPLATE_WALL)
         return TEMPLATE_WALL;
      if(a == TEMPLATE_CLEAR || b == TEMPLATE_CLEAR)
         return TEMPLATE_CLEAR;
      if(a == TEMPLATE_VACUUM || b == TEMPLATE_VACUUM)
         return TEMPLATE_VACUUM;
      return TEMPLATE_OOB;
   }
   
   public void print()
   {
      char[][] charMap = getTileMap();
      for(int y = 0; y < charMap[0].length; y++)
      {
         for(int x = 0; x < charMap.length; x++)
         {
            if(charMap[x][y] == TEMPLATE_OOB)
               System.out.print(" ");
            else
               System.out.print(charMap[x][y] + "");
         }
         System.out.println("");
      }
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
   
   private boolean[][] getFloodMap(Coord start)
   {
      boolean[][] boolMap = new boolean[width * 3][height * 3];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         int xTranslation = (3 * x) + 1;
         int yTranslation = (3 * y) + 1;
         if(getCell(x, y) != OOB_ROOM)
            boolMap[xTranslation][yTranslation] = true;
         if(passArray[x][y][NORTH])
            boolMap[xTranslation][yTranslation - 1] = true;
         if(passArray[x][y][SOUTH])
            boolMap[xTranslation][yTranslation + 1] = true;
         if(passArray[x][y][EAST])
            boolMap[xTranslation + 1][yTranslation] = true;
         if(passArray[x][y][WEST])
            boolMap[xTranslation - 1][yTranslation] = true;
      }
      return FloodFill.fill(boolMap, (start.x * 3) + 1, (start.y * 3) + 1);
   }
   
   public void cleanUpStruts()
   {
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(isStrut(x, y))
         {
            tileMap[x][y] = TEMPLATE_OOB;
         }
      }
   }
   
   private boolean isStrut(int xOrigin, int yOrigin)
   {
      for(int x = Math.max(0, xOrigin - 1); x < Math.min(tileMap.length, xOrigin + 2); x++)
      for(int y = Math.max(0, yOrigin - 1); y < Math.min(tileMap[0].length, yOrigin + 2); y++)
      {
         if(tileMap[x][y] != TEMPLATE_WALL && tileMap[x][y] != TEMPLATE_OOB)
            return false;
      }
      return true;
   }
   
   @Override
   public char getCell(int x, int y)
   {
      if(isInBounds(x, y))
         return super.getCell(x, y);
      return OOB_ROOM;
   }
   
   // returns a zone template that includes at least one of each RoomTemplate
   public static ZoneTemplate getDemo()
   {
      Vector<String> v = new Vector<String>();
      v.add("#...#");
      v.add("#...#");
      v.add("##C##");
      v.add("++c++");
      v.add("++C++");
      v.add("++c++");
      v.add("##C##");
      v.add("#...#");
      v.add("#.S.#");
      return new ZoneTemplate(v);
   }
   
   public static void main(String[] args)
   {
      ZoneTemplate zt = ZoneTemplate.getDemo();
      zt.print();
      
      Vector<String> strList = zt.serialize();
      for(String str : strList)
         System.out.println(str);
   }

}