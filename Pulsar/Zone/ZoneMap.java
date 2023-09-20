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
   private Vector<Coord> fireList;
   private Vector<Coord> iceList;
   private boolean alternatingTurns;
   private Vector<ButtonTrigger> buttonTriggerList;
   private Corpse[][] corpseMap;


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public MapTile[][] getTileArray(){return tileArray;}
	public MapTile getOobTile(){return oobTile;}
   public boolean[][] getTransparencyMap(){return transparencyMap;}
   public ShadowFoVRect getFoV(){return fov;}
   public Vector<Coord> getVacuumList(){return vacuumList;}
   public Vector<Coord> getBreachList(){return breachList;}
   public Vector<Coord> getFireList(){return fireList;}
   public Vector<Coord> getIceList(){return iceList;}
   public Vector<ButtonTrigger> getButtonTriggerList(){return buttonTriggerList;}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setTileArray(MapTile[][] t){tileArray = t;}
	public void setOobTile(MapTile o){oobTile = o;}
   public void setFoV(ShadowFoVRect newFoV){fov = newFoV;}
   public void setVacuumList(Vector<Coord> vl){vacuumList = vl;}
   public void setBreachList(Vector<Coord> bl){breachList = bl;}
   public void setFireList(Vector<Coord> fl){fireList = fl;}
   public void setIceList(Vector<Coord> il){iceList = il;}
   public void setButtonTriggerList(Vector<ButtonTrigger> btl){buttonTriggerList = btl;}


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
      fireList = new Vector<Coord>();
      iceList = new Vector<Coord>();
      buttonTriggerList = new Vector<ButtonTrigger>();
      refreshCorpseMap();
   }
   
   public void takeTurn()
   {
      alternatingTurns = !alternatingTurns;
      adjustAir();
      extinguishCheck();
      thawCheck();
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
   
   public void add(ButtonTrigger buttonTrigger)
   {
      buttonTriggerList.add(buttonTrigger);
   }
   
   public void buttonPressed(int triggerIndex)
   {
      boolean foundF = false;
      for(ButtonTrigger trigger : buttonTriggerList)
      {
         if(trigger.getTriggerIndex() == triggerIndex)
         {
            // do the thing
            for(Coord c : trigger.getTargetList())
            {
               switch(trigger.getButtonAction())
               {
                  case TOGGLE : tryToToggle(c); break;
                  default :     System.out.println("Error: ButtonTrigger with no action.");
               }
            }
            foundF = true;
         }
      }
      if(!foundF)
         System.out.println("Error: did not find triggerIndex " + triggerIndex);
   }
   
   public void tryToToggle(Coord c)
   {
      MapTile tile = getTile(c);
      if(tile instanceof ToggleTile)
      {
         // using a button to open a door ignores locked status
         if(tile instanceof Door)
         {
            Door door = (Door)tile;
            boolean originallyLocked = door.isLocked();
            door.setLocked(false);
            door.toggle();
            door.setLocked(originallyLocked);
         }
         else
         {
            ToggleTile tTile = (ToggleTile)tile;
            tTile.toggle();
         }
         update(c);
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
      boolean wasHighPassable = highPassMap[c.x][c.y];
      transparencyMap[c.x][c.y] = getTile(c).isTransparent();
      lowPassMap[c.x][c.y] = getTile(c).isLowPassable();
      highPassMap[c.x][c.y] = getTile(c).isHighPassable();
      fov.reset(transparencyMap);
      breachCheck(c);
      // opening a door between two zero-air tiles sets door to zero air
      if(!wasHighPassable && highPassMap[c.x][c.y])
      {
         int pressure = tileArray[c.x][c.y].getAirPressure();
         if(tileArray[c.x + 1][c.y].getAirPressure() < pressure)
            pressure = tileArray[c.x + 1][c.y].getAirPressure();
         if(tileArray[c.x - 1][c.y].getAirPressure() < pressure)
            pressure = tileArray[c.x - 1][c.y].getAirPressure();
         if(tileArray[c.x][c.y + 1].getAirPressure() < pressure)
            pressure = tileArray[c.x][c.y + 1].getAirPressure();
         if(tileArray[c.x][c.y - 1].getAirPressure() < pressure)
            pressure = tileArray[c.x][c.y - 1].getAirPressure();
         tileArray[c.x][c.y].setAirPressure(pressure);
      }
   }
   
   // quickly calculate if origin can see target
   public boolean canSee(Coord origin, Coord target, int radius)
   {
      fov.calcCone(origin, radius, target);
      return fov.isVisible(target);
   }
   
   public boolean isInBounds(Coord c){return isInBounds(c.x, c.y);}
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && 
             y >= 0 && 
             x < width && 
             y < height;
   }
   
   public void setTile(Coord c, MapTile t){setTile(c.x, c.y, t);}
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
      MapTile newTile = MapTileFactory.getBroken(mt);
      if(mt.isHighPassable())
         newTile.setAirPressure(mt.getAirPressure());
      else
         newTile.setAirPressure(0);
      if(getTile(x, y) instanceof Ice)
      {System.out.println("Thawing");
         thaw(x, y);}
      setTile(x, y, newTile);
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
         newMap[x][y] = highPassMap[x][y];
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
   
   public void flood(Coord c, int reps){flood(c.x, c.y, reps);}
   public void flood(int x, int y, int reps)
   {
      if(!isInBounds(x, y))
         return;
      SpiralSearch search = new SpiralSearch(lowPassMap, x, y);
      Coord loc = new Coord(x, y);
      for(int i = 0; i < reps; i++)
      {
         while(loc != null && !isFloodable(loc))
            loc = search.getNext();
         if(loc != null)
         {
            extinguish(loc);
            setTile(loc, MapTileFactory.getWater());
         }
      }
   }
   
   public boolean isFloodable(int x, int y){return isFloodable(new Coord(x, y));}
   public boolean isFloodable(Coord c)
   {
      MapTile tile = getTile(c);
      if(tile.isLiquid())
         return false;
      if(tile instanceof Door)
         return false;
      if(!tile.isLowPassable())
         return false;
      return true;
   }
   
   
   // breach stuff
   //////////////////////////////////////////////////////////////////
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
   
   // corpse stuff
   ////////////////////////////////////////////////////////////////////////////
   
   private void refreshCorpseMap()
   {
      corpseMap = new Corpse[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         corpseMap[x][y] = null;
   }
   
   public boolean isCorpseAt(Coord c){return isCorpseAt(c.x, c.y);}
   public boolean isCorpseAt(int x, int y)
   {
      if(isInBounds(x, y))
         return corpseMap[x][y] != null;
      return false;
   }
   
   public boolean canDropCorpseAt(Coord c){return canDropCorpseAt(c.x, c.y);}
   public boolean canDropCorpseAt(int x, int y)
   {
      if(isInBounds(x, y))
      {
         MapTile tile = getTile(x, y);
         if(tile.isLowPassable() && 
            tile instanceof Fire == false &&
            tile instanceof Door == false &&
            !isCorpseAt(x, y))
            return true;
      }
      return false;
   }
   
   public Coord getNearestCorpseLocation(int x, int y){return getNearestCorpseLocation(new Coord(x, y));}
   public Coord getNearestCorpseLocation(Coord c)
   {
      if(canDropCorpseAt(c))
         return c;
      SpiralSearch search = new SpiralSearch(lowPassMap, c);
      Coord target = search.getNext();
      while(target != null)
      {
         if(canDropCorpseAt(target))
            return target;
         target = search.getNext();
      }
      return null;
   }
   
   public void dropCorpse(Actor a)
   {
      Coord loc = getNearestCorpseLocation(a.getMapLoc());
      if(loc != null)
         setCorpseAt(loc, new Corpse(a));
   }
   
   public void setCorpseAt(Coord loc, Corpse corpse){setCorpseAt(loc.x, loc.y, corpse);}
   public void setCorpseAt(int x, int y, Corpse corpse)
   {
      if(isInBounds(x, y))
         corpseMap[x][y] = corpse;
   }
   
   public Corpse getCorpseAt(Coord loc){return getCorpseAt(loc.x, loc.y);}
   public Corpse getCorpseAt(int x, int y)
   {
      if(isInBounds(x, y))
         return corpseMap[x][y];
      return null;
   }
   
   
   // fire stuff
   ////////////////////////////////////////////////////////////////////////////
   public void tryToIgnite(int x, int y){tryToIgnite(new Coord(x, y));}
   public void tryToIgnite(Coord c)
   {
      if(getTile(c).isIgnitable())
         ignite(c);
      else if(getTile(c) instanceof Ice)
         thaw(c);
   }
   
   public void ignite(int x, int y){ignite(new Coord(x, y));}
   public void ignite(Coord c)
   {
      MapTile fire = new Fire(getTile(c));
      setTile(c, fire);
      if(getTile(c) instanceof Fire)
      {
         fireList.add(c);
         setCorpseAt(c, null);
      }
   }
   
   public void extinguish(int x, int y){extinguish(new Coord(x, y));}
   public void extinguish(Coord c)
   {
      MapTile tile = getTile(c);
      if(tile instanceof Fire)
      {
         Fire fireTile = (Fire)tile;
         setTile(c, fireTile.getOriginalTile());
         if(!(getTile(c) instanceof Fire))
         {
            for(int i = 0; i < fireList.size(); i++)
            {
               if(fireList.elementAt(i).equals(c))
               {
                  fireList.removeElementAt(i);
               }
            }
         }
      }
   }
   
   private void extinguishCheck()
   {
      for(int i = 0; i < fireList.size(); i++)
      {
         Coord loc = fireList.elementAt(i);
         Fire tile = (Fire)getTile(loc);
         tile.increment();
         if(tile.isExpired())
            extinguish(loc);
      }
   }
   
   // ice stuff
   ////////////////////////////////////////////////////////////////////////////
   public void tryToFreeze(int x, int y){tryToFreeze(new Coord(x, y));}
   public void tryToFreeze(Coord c)
   {
      if(getTile(c) instanceof Vacuum || !isInBounds(c))
         return;
      if(getTile(c) instanceof Fire)
         extinguish(c);
      else
         freeze(c);
   }
   
   public void freeze(int x, int y){freeze(new Coord(x, y));}
   public void freeze(Coord c)
   {
      if(getTile(c) instanceof Ice)
      {
         Ice ice = (Ice)getTile(c);
         ice.refreshDuration();
         return;
      }
      Ice ice = new Ice(getTile(c));
      setTile(c, ice);
      iceList.add(c);
      if(isCorpseAt(c))
         getCorpseAt(c).setColor(getTile(c).getFGColor());
   }
   
   public void thaw(int x, int y){thaw(new Coord(x, y));}
   public void thaw(Coord c)
   {
      MapTile tile = getTile(c);
      if(tile instanceof Ice)
      {
         Ice iceTile = (Ice)tile;
         setTile(c, iceTile.getOriginalTile());
         if(isCorpseAt(c))
            getCorpseAt(c).setColor(getTile(c).getFGColor());
         
         for(int i = 0; i < iceList.size(); i++)
         {
            if(iceList.elementAt(i).equals(c))
            {
               iceList.removeElementAt(i);
            }
         }
      }
   }
   
   private void thawCheck()
   {
      for(int i = 0; i < iceList.size(); i++)
      {
         Coord loc = iceList.elementAt(i);
         Ice tile = (Ice)getTile(loc);
         tile.increment();
         if(tile.isExpired())
            thaw(loc);
      }
   }

}