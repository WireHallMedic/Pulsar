package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class ZoneBuilder extends MapTemplate implements ZoneConstants
{
   public static final char OPEN_ROOM = TEMPLATE_CLEAR;
   public static final char CLOSED_ROOM = '+';
   public static final char OOB_ROOM = TEMPLATE_OOB;
   public static final char CORRIDOR = 'C';
   public static final char STARTING_ROOM = 'S';
   
   public static final int SMALL = 0;
   public static final int MEDIUM = 1;
   public static final int LARGE = 2;

   
   private RoomTemplateManager openTemplateManager;
   private RoomTemplateManager closedTemplateManager;
   private RoomTemplateManager corridorTemplateManager;
   private RoomTemplateManager startTemplateManager;
   private boolean[][][] passArray;
   private boolean[][] criticalPath;
   private RoomTemplate[][] baseRoomTemplate;
   private RoomTemplate[][] rolledRoomTemplate;
   private char[][] tileMap;
   private Vector<ButtonTrigger> triggerList;
   private int roomWidth;
   private int roomHeight;
   private Coord startRoom;
   private Coord bossRoom;
   private Vector<Closet> closetList;
   private Vector<SpawnPoint> spawnPointList;
   private int size;
   
   public boolean[] getPassArray(int x, int y){return passArray[x][y];}
   public RoomTemplate getBaseRoomTemplate(int x, int y){return baseRoomTemplate[x][y];}
   public RoomTemplate getRolledRoomTemplate(int x, int y){return rolledRoomTemplate[x][y];}
   public char[][] getTileMap(){return tileMap;}
   public Vector<ButtonTrigger> getTriggerList(){return triggerList;}
   public int getRoomWidth(){return roomWidth;}
   public int getRoomHeight(){return roomHeight;}
   public Coord getStartRoom(){return startRoom;}
   public Coord getBossRoom(){return bossRoom;}
   public Vector<Closet> getClosetList(){return closetList;}
   public Vector<SpawnPoint> getSpawnPointList(){return spawnPointList;}
   
   public ZoneBuilder(int s, RoomTemplateManager openTM, RoomTemplateManager closedTM, RoomTemplateManager corridorTM,
                      RoomTemplateManager startTM)
   {
      super(getDefaultVector(s));
      size = s;
      openTemplateManager = openTM;
      closedTemplateManager = closedTM;
      corridorTemplateManager = corridorTM;
      startTemplateManager = startTM;
      if(openTemplateManager == null)
         openTemplateManager = new RoomTemplateManager("Open Room Templates.txt");
      if(closedTemplateManager == null)
         closedTemplateManager = new RoomTemplateManager("Closed Room Templates.txt");
      if(corridorTemplateManager == null)
         corridorTemplateManager = new RoomTemplateManager("Corridor Templates.txt");
      if(startTemplateManager == null)
         startTemplateManager = new RoomTemplateManager("Starting Room Templates.txt");
      RoomTemplate rt = openTemplateManager.random(RoomTemplate.RoomTemplateType.BLOCK);
      roomWidth = rt.getWidth();
      roomHeight = rt.getHeight();
      triggerList = new Vector<ButtonTrigger>();
      initializePassArray();
      setCriticalPath();
      setRoomTypes();
      addNonCriticalRooms();
      generateRooms();
      setClosetList();
      cleanUpStruts();
      ZoneDecorator.decorate(this);
      //addCrates(60);
      //addBossCrates(5);
   }
   
   public ZoneBuilder(ZoneBuilder that)
   {
      this(that.size, that.openTemplateManager, that.closedTemplateManager, 
           that.corridorTemplateManager, that.startTemplateManager);
   }
   
   public ZoneBuilder()
   {
      this(SMALL, null, null, null, null);
   }
   
   public ZoneBuilder(int s)
   {
      this(s, null, null, null, null);
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
   
   private void setCriticalPath()
   {
      criticalPath = new boolean[width][height];
      int xLoc = 0;
      int yLoc = GameEngine.randomInt(0, 5);
      boolean canGoUp;
      boolean canGoDown;
      startRoom = new Coord(xLoc, yLoc);
      criticalPath[startRoom.x][startRoom.y] = true;
      boolean continueF = true;
      while(continueF)
      {
         int oldX = xLoc;
         int oldY = yLoc;
         canGoUp = yLoc > 0 && !criticalPath[xLoc][yLoc - 1];
         canGoDown = yLoc < height - 1 && !criticalPath[xLoc][yLoc + 1];
         if(canGoUp && canGoDown)
         {
            switch(GameEngine.randomInt(0, 3))
            {
               case 0 : yLoc++; break;
               case 1 : yLoc--; break;
               case 2 : xLoc++; break;
            }
         }
         else if(canGoUp)
         {
            switch(GameEngine.randomInt(0, 3))
            {
               case 0 : 
               case 1 : yLoc--; break;
               case 2 : xLoc++; break;
            }
         }
         else if(canGoDown)
         {
            switch(GameEngine.randomInt(0, 3))
            {
               case 0 : 
               case 1 : yLoc++; break;
               case 2 : xLoc++; break;
            }
         }
         else
         {
            xLoc++;
         }
         if(xLoc == criticalPath.length)
         {
            bossRoom = new Coord(xLoc - 1, yLoc);
            continueF = false;
         }
         else
         {
            criticalPath[xLoc][yLoc] = true;
            addPassage(oldX, oldY, xLoc, yLoc);
         }
      }
   }
      
   public void initializePassArray()
   {
      passArray = new boolean[width][height][4];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         boolean[] defaultArr = {false, false, false, false};
      }
   }
   
   public void addPassage(int x1, int y1, int x2, int y2)
   {
      if(x1 < x2)
      {
         passArray[x1][y1][EAST] = true;
         passArray[x2][y2][WEST] = true;
      }
      if(x1 > x2)
      {
         passArray[x1][y1][WEST] = true;
         passArray[x2][y2][EAST] = true;
      }
      if(y1 < y2)
      {
         passArray[x1][y1][SOUTH] = true;
         passArray[x2][y2][NORTH] = true;
      }
      if(y1 > y2)
      {
         passArray[x1][y1][NORTH] = true;
         passArray[x2][y2][SOUTH] = true;
      }
   }
   
   private void addNonCriticalRooms()
   {
      boolean continueF = true;
      boolean[][] onPath = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         onPath[x][y] = criticalPath[x][y];
      while(continueF)
      {
         continueF = false;
         for(int x = 0; x < width; x++)
         for(int y = 0; y < height; y++)
         {
            if(onPath[x][y])
            {
               Vector<Coord> unconnectedRooms = getAdjoiningRooms(x, y, onPath);
               if(unconnectedRooms.size() > 0)
               {
                  Coord newConnection = unconnectedRooms.elementAt(GameEngine.randomInt(0, unconnectedRooms.size()));
                  onPath[newConnection.x][newConnection.y] = true;
                  addPassage(x, y, newConnection.x, newConnection.y);
                  continueF = true;
               }
            }
         }
      }
   }
   
   private void setRoomTypes()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(criticalPath[x][y])
         {
            switch(GameEngine.randomInt(0, 3))
            {
               case 0 : setCell(x, y, CORRIDOR); break;
               default: setCell(x, y, OPEN_ROOM); 
            }
         }
         else
         {
            switch(GameEngine.randomInt(0, 5))
            {
               case 0 : setCell(x, y, CORRIDOR); break;
               case 1 : 
               case 2 : setCell(x, y, CLOSED_ROOM); break;
               default: setCell(x, y, OPEN_ROOM); 
            }
         }
      }
      setCell(startRoom.x, startRoom.y, STARTING_ROOM);
      setCell(bossRoom.x, bossRoom.y, OPEN_ROOM);
   }
   
   
   private Vector<Coord> getAdjoiningRooms(int x, int y, boolean[][] onPath)
   {
      int[][] adjList = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
      int xProspect;
      int yProspect;
      Vector<Coord> adjRoomList = new Vector<Coord>();
      // add all adjacent rooms that are not already added and exist
      for(int i = 0; i < 4; i++)
      {
         xProspect = adjList[i][0] + x;
         yProspect = adjList[i][1] + y;
         if(xProspect >= 0 &&
            xProspect < width &&
            yProspect >= 0 &&
            yProspect < height &&
            !onPath[xProspect][yProspect])
         {
            adjRoomList.add(new Coord(xProspect, yProspect));
         }
      }
      return adjRoomList;
   }
   

   public void generateRooms()
   {
      baseRoomTemplate = new RoomTemplate[width][height];
      rolledRoomTemplate = new RoomTemplate[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(getCell(x, y) == CORRIDOR)
            baseRoomTemplate[x][y] = corridorTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         else if(getCell(x, y) == OPEN_ROOM)
            baseRoomTemplate[x][y] = openTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         else if(getCell(x, y) == CLOSED_ROOM)
            baseRoomTemplate[x][y] = closedTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         else if(getCell(x, y) == STARTING_ROOM)
            baseRoomTemplate[x][y] = startTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         else if(GameEngine.randomBoolean())
            baseRoomTemplate[x][y] = openTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         else
            baseRoomTemplate[x][y] = closedTemplateManager.random(RoomTemplate.determineType(passArray[x][y]));
         baseRoomTemplate[x][y].rotateUntilMatches(passArray[x][y]);
         rolledRoomTemplate[x][y] = null;
         addCorridorDoors(x, y);
      }
      resolveProbTiles();
      process();
      registerSpawnPoints();
      validateButtons();
   }

   
   // add a door to each corrider
   private void addCorridorDoors(int x, int y)
   {
      if(getCell(x, y) == CORRIDOR)
      {
         int w = baseRoomTemplate[x][y].getWidth();
         int h = baseRoomTemplate[x][y].getHeight();
         if(passArray[x][y][NORTH] && getCell(x, y - 1) != CORRIDOR)
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(i, 0, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(w / 2, 0, TEMPLATE_DOOR);
         }
         if(passArray[x][y][SOUTH] && getCell(x, y + 1) != CORRIDOR)
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(i, h - 1, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(w / 2, h - 1, TEMPLATE_DOOR);
         }
         if(passArray[x][y][EAST] && getCell(x + 1, y) != CORRIDOR)
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(w - 1, i, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(w - 1, h / 2, TEMPLATE_DOOR);
         }
         if(passArray[x][y][WEST] && getCell(x - 1, y) != CORRIDOR)
         {
            for(int i = 0; i < w; i++)
               baseRoomTemplate[x][y].setCell(0, i, TEMPLATE_WALL);
            baseRoomTemplate[x][y].setCell(0, h / 2, TEMPLATE_DOOR);
         }
      }
   }

   public void resolveProbTiles()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         rolledRoomTemplate[x][y] = baseRoomTemplate[x][y].resolveProbTiles();
      }
   }
   
   public void process()
   {
      // clear lists
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
      placeRoom(newMap, 0, 0, rolledRoomTemplate[0][0]);
      // place all rooms at y = 0 (except 0, 0)
      for(int x = 1; x < width; x++)
         placeRoom(newMap, x * (roomWidth - 1), 0, rolledRoomTemplate[x][0]);
      // place all rooms at x = 0 (except 0, 0)
      for(int y = 1; y < height; y++)
         placeRoom(newMap, 0, y * (roomHeight - 1), rolledRoomTemplate[0][y]);
      // place all other rooms
      for(int x = 1; x < width; x++)
      for(int y = 1; y < height; y++)
         placeRoom(newMap, x * (roomWidth - 1), y * (roomHeight - 1), rolledRoomTemplate[x][y]);
      tileMap = newMap;
   }
   
   private void registerSpawnPoints()
   {
      spawnPointList = new Vector<SpawnPoint>();
      // non-border cells
      for(int x = 0; x < tileMap.length; x++)
      for(int y = 0; y < tileMap[0].length; y++)
      {
         if(tileMap[x][y] == TEMPLATE_SPAWN_POINT)
         {
            SpawnPoint sp = new SpawnPoint(x, y, getCell(x / (roomWidth - 1), y / (roomHeight - 1)));
            if(getBossRoom().equals(x / (roomWidth - 1), y / (roomHeight - 1)))
            {
               sp.setBoss(true);
            }
            spawnPointList.add(sp);
         }
      }
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
   
   public boolean isAdjacentToDoor(int x, int y)
   {
      // must not be adjacent to a door
      return tileMap[x - 1][y] == TEMPLATE_DOOR || tileMap[x + 1][y] == TEMPLATE_DOOR ||
             tileMap[x][y - 1] == TEMPLATE_DOOR || tileMap[x][y + 1] == TEMPLATE_DOOR;
   }
      
   // crates can be place on clear, not adjacent to a door, and not with two opposite non-clear tiles adjacent
   public boolean canPlaceCrate(int x, int y)
   {
      // must be on clear
      if(tileMap[x][y] != TEMPLATE_CLEAR)
         return false;
      
      // must not be adjacent to a door
      if(isAdjacentToDoor(x, y))
         return false;
      
      // must not block a choke point
      if((tileMap[x - 1][y] != TEMPLATE_CLEAR && tileMap[x + 1][y] != TEMPLATE_CLEAR) ||
         (tileMap[x][y - 1] != TEMPLATE_CLEAR && tileMap[x][y + 1] != TEMPLATE_CLEAR))
         return false;
         
      return true;
   }
   
   public boolean isCorridor(int x, int y)
   {
      return getCell(x, y) == CORRIDOR;
   }
   
   @Override
   public char getCell(int x, int y)
   {
      if(isInBounds(x, y))
         return super.getCell(x, y);
      return OOB_ROOM;
   }
   
   public boolean inInRoom(int pointX, int pointY, int roomX, int roomY)
   {
      int roomMinX = roomX * (getRoomWidth() - 1);
      int roomMinY = roomY * (getRoomHeight() - 1);
      int roomMaxX = roomMinX + getRoomWidth();
      int roomMaxY = roomMinY + getRoomHeight();
      return pointX >= roomMinX && pointX < roomMaxX &&
             pointY >= roomMinY && pointY < roomMinY;
   }
   
   
   private void setClosetList()
   {
      closetList = new Vector<Closet>();
      ZoneDivisor zd = new ZoneDivisor(this);
      closetList = zd.getClosetList();
   }
   
   public void shift(int x, int y){shift(new Coord(x, y));}
   public void shift(Coord c)
   {
      for(ButtonTrigger trigger : triggerList)
         trigger.shift(c);
      for(Closet closet : closetList)
         closet.shift(c);
      for(SpawnPoint spawnPoint : spawnPointList)
         spawnPoint.shift(c);
   }
   
   
   public static Vector<String> getDefaultVector(int size)
   {
      int w = 5;
      int h = 5;
      if(size == 1)
         w = 6;
      if(size == 2)
         w = 7;
      Vector<String> v = new Vector<String>();
      for(int y = 0; y < h; y++)
      {
         String str = "";
         for(int x = 0; x < w; x++)
         {
            str += ".";
         }
         v.add(str);
      }
      return v;
   }
   
   public static void main(String[] args)
   {
      ZoneBuilder zb = new ZoneBuilder(LARGE);
      zb.print();
   }

}