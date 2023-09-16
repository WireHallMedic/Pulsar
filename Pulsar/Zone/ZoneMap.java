package Pulsar.Zone;

import Pulsar.GUI.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import WidlerSuite.*;
import java.util.*;

public class ZoneMap implements ZoneConstants, GUIConstants
{
	private int width;
	private int height;
	private MapTile[][] tileArray;
	private MapTile oobTile;
   private boolean[][] transparencyMap;
   private ShadowFoVRect fov;
   private boolean[][] lowPassMap;
   private boolean[][] highPassMap;
   private Vector<Coord> vacuumList;
   private Vector<Coord> breachList;
   private boolean alternatingTurns;


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public MapTile[][] getTileArray(){return tileArray;}
	public MapTile getOobTile(){return oobTile;}
   public boolean[][] getTransparencyMap(){return transparencyMap;}
   public ShadowFoVRect getFoV(){return fov;}
   public Vector<Coord> getVacuumList(){return vacuumList;}
   public Vector<Coord> getBreachList(){return breachList;}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setTileArray(MapTile[][] t){tileArray = t;}
	public void setOobTile(MapTile o){oobTile = o;}
   public void setFoV(ShadowFoVRect newFoV){fov = newFoV;}
   public void setVacuumList(Vector<Coord> vl){vacuumList = vl;}
   public void setBreachList(Vector<Coord> bl){breachList = bl;}


   public ZoneMap(int w, int h, MapTile defaultTile)
   {
      width = w;
      height = h;
      tileArray = new MapTile[width][height];
      transparencyMap = new boolean[width][height];
      lowPassMap = new boolean[width][height];
      highPassMap = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         setTile(x, y, new MapTile(defaultTile));
      oobTile = MapTileFactory.getTile(TileType.NULL);
      fov = new ShadowFoVRect(transparencyMap);
      vacuumList = new Vector<Coord>();
      breachList = new Vector<Coord>();
   }
   
   public void takeTurn()
   {
      alternatingTurns = !alternatingTurns;
      adjustAir();
   }
   
   public void postProcessing()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(tileArray[x][y] instanceof Vacuum)
            vacuumList.add(new Coord(x, y));
      }
   }
   
   private void breachCheck(Coord c){breachCheck(c.x, c.y);}
   private void breachCheck(int x, int y)
   {
      if(!getTile(x, y).isLowPassable() && !getTile(x, y).isHighPassable())
      {
         removeBreach(x, y);
      }
      else
      {
      if(getTile(x + 1, y) instanceof Vacuum ||
         getTile(x - 1, y) instanceof Vacuum ||
         getTile(x, y + 1) instanceof Vacuum ||
         getTile(x, y - 1) instanceof Vacuum)
         addBreach(x, y);
      }
   }
   
   private void addBreach(int x, int y){addBreach(new Coord(x, y));}
   private void addBreach(Coord c)
   {
      for(Coord existing : breachList)
      {
         if(existing.equals(c))
            return;
      }
      breachList.add(c);
   }
   
   private void removeBreach(int x, int y){removeBreach(new Coord(x, y));}
   private void removeBreach(Coord c)
   {
      for(int i = 0; i < breachList.size(); i++)
      {
         if(breachList.elementAt(i).equals(c))
         {
            breachList.removeElementAt(i);
            return;
         }
      }
      
   }
   
   public void updateFoV()
   {
      for(int x = 0; x < tileArray.length; x++)
      for(int y = 0; y < tileArray[0].length; y++)
         transparencyMap[x][y] = tileArray[x][y].isTransparent();
      fov.reset(transparencyMap);
   }
   
   public void update(int x, int y){update(new Coord(x, y));}
   public void update(Coord c)
   {
      transparencyMap[c.x][c.y] = getTile(c).isTransparent();
      lowPassMap[c.x][c.y] = getTile(c).isLowPassable();
      highPassMap[c.x][c.y] = getTile(c).isHighPassable();
      fov.reset(transparencyMap);
      breachCheck(c);
   }
   
   // quickly calculate if origin can see target
   public boolean canSee(Coord origin, Coord target, int radius)
   {
      fov.calcCone(origin, radius, target);
      return fov.isVisible(target);
   }
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && 
             y >= 0 && 
             x < width && 
             y < height;
   }
   
   public void setTile(int x, int y, MapTile t)
   {
      if(isInBounds(x, y))
      {
         tileArray[x][y] = t;
         transparencyMap[x][y] = t.isTransparent();
         lowPassMap[x][y] = t.isLowPassable();
         highPassMap[x][y] = t.isHighPassable();
      }
   }
   
   public MapTile getTile(Coord c){return getTile(c.x, c.y);}
   public MapTile getTile(int x, int y)
   {
      if(isInBounds(x, y))
         return tileArray[x][y];
      return new MapTile(oobTile);
   }
   
   public void breakTile(Coord c){breakTile(c.x, c.y);}
   public void breakTile(int x, int y)
   {
      MapTile mt = getTile(x, y);
      setTile(x, y, MapTileFactory.getBroken(mt));
      if(mt.hasOnDestructionEffect())
         GameEngine.doDestructionEffect(x, y, mt);
      breachCheck(x, y);
   }
   
   // return a passability map accounting for movement type, door usage, and pathfinding radius
   public boolean[][] getPassMap(Actor a)
   {
      int radius = a.getAI().getPathfindingRadius();
      boolean[][] passMap = new boolean[radius + radius + 1][radius + radius + 1];
      for(int x = 0; x < (radius * 2) + 1; x++)
      for(int y = 0; y < (radius * 2) + 1; y++)
      {
         Coord realLoc = new Coord(x + a.getMapLoc().x - radius, y + a.getMapLoc().y - radius);
         if(a.getAI().getUsesDoors() && getTile(realLoc) instanceof Door)
            passMap[x][y] = true;
         else
            passMap[x][y] = isPassable(a, realLoc);
      }
      return passMap;
   }
   
   public Coord getPassMapInset(Actor a)
   {
      Coord inset = new Coord(a.getMapLoc());
      inset.x -= a.getAI().getPathfindingRadius();
      inset.y -= a.getAI().getPathfindingRadius();
      return inset;
   }
   
   public boolean isPassable(Actor a, int x, int y){return isPassable(a, new Coord(x, y));}
   public boolean isPassable(Actor a, Coord c)
   {
      if(a.isFlying())
         return getTile(c.x, c.y).isHighPassable();
      return getTile(c.x, c.y).isLowPassable();
   }
   
   
   public boolean[][] getHighPassMap()
   {
      boolean[][] newMap = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         newMap[x][y] = lowPassMap[x][y];
      return newMap;
   }
   
   
   public boolean[][] getLowPassMap()
   {
      boolean[][] newMap = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         newMap[x][y] = lowPassMap[x][y];
      return newMap;
   }
   
   // get a portion of the low pass map for performance
   public boolean[][] getLowPassMapPortion(Coord origin, int radius)
   {
      boolean[][] passMap = new boolean[radius + radius + 1][radius + radius + 1];
      for(int x = 0; x < (radius * 2) + 1; x++)
      for(int y = 0; y < (radius * 2) + 1; y++)
      {
         Coord realLoc = new Coord(x + origin.x - radius, y + origin.y - radius);
         passMap[x][y] = getTile(realLoc).isLowPassable();
      }
      return passMap;
   }
   
   public void adjustAir()
   {
      boolean[][] depressurizationMap = new boolean[width][height];
      boolean doPullTowardsVacuum = false;
      
      for(Coord c : breachList)
      {
         boolean[][] curMap = FloodFill.fill(getHighPassMap(), c.x, c.y);
         for(int x = 0; x < width; x++)
         for(int y = 0; y < height; y++)
            depressurizationMap[x][y] = depressurizationMap[x][y] || curMap[x][y];
      }
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(depressurizationMap[x][y])
         {
            if(tileArray[x][y].getAirPressure() > 0)
               doPullTowardsVacuum = true;
            tileArray[x][y].depressurize();
         }
         else if(alternatingTurns)
         {
            if(tileArray[x][y].isHighPassable() && tileArray[x][y].getAirPressure() < FULL_AIR_PRESSURE && tileArray[x][y] instanceof Vacuum == false)
               tileArray[x][y].pressurize();
         }
      }
      if(doPullTowardsVacuum)
      {
         GameEngine.pullTowardsVacuum(depressurizationMap, breachList, getHighPassMap());
      }
   }
}