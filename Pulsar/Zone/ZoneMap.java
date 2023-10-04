package Pulsar.Zone;

import Pulsar.GUI.*;
import Pulsar.Gear.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import WidlerSuite.*;
import java.util.*;

public class ZoneMap implements ZoneConstants, GUIConstants
{
	private int width;
	private int height;
	private MapTile[][] tileArray;
   private boolean[][] transparencyMap;
   private boolean[][] lowPassMap;
   private boolean[][] highPassMap;
   private GearObj[][] gearMap;
   private Corpse[][] corpseMap;
	private MapTile oobTile;
   private ShadowFoVRect fov;
   private Vector<Coord> vacuumList;
   private Vector<Coord> breachList;
   private Vector<Coord> fireList;
   private Vector<Coord> iceList;
   private Vector<Coord> automaticDoorList;
   private boolean alternatingTurns;
   private Vector<ButtonTrigger> buttonTriggerList;
   private Vector<DelayedButtonTrigger> delayedButtonTriggerList;
   private Coord exit;


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public MapTile[][] getTileArray(){return tileArray;}
	public MapTile getOOBTile(){return oobTile;}
   public boolean[][] getTransparencyMap(){return transparencyMap;}
   public ShadowFoVRect getFoV(){return fov;}
   public Vector<Coord> getVacuumList(){return vacuumList;}
   public Vector<Coord> getBreachList(){return breachList;}
   public Vector<Coord> getFireList(){return fireList;}
   public Vector<Coord> getIceList(){return iceList;}
   public Vector<Coord> getAutomaticDoorList(){return automaticDoorList;}
   public Vector<ButtonTrigger> getButtonTriggerList(){return buttonTriggerList;}
   public Coord getStartingLoc(){return new Coord(exit);}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setTileArray(MapTile[][] t){tileArray = t;}
	public void setOOBTile(MapTile o){oobTile = o;}
   public void setFoV(ShadowFoVRect newFoV){fov = newFoV;}
   public void setVacuumList(Vector<Coord> vl){vacuumList = vl;}
   public void setBreachList(Vector<Coord> bl){breachList = bl;}
   public void setFireList(Vector<Coord> fl){fireList = fl;}
   public void setIceList(Vector<Coord> il){iceList = il;}
   public void setAutomaticDoorList(Vector<Coord> adl){automaticDoorList = adl;}
   public void setButtonTriggerList(Vector<ButtonTrigger> btl){buttonTriggerList = btl;}
   public void setExit(int x, int y){exit = new Coord(x, y);}
   public void setExit(Coord c){setExit(c.x, c.y);}


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
         setTile(x, y, MapTileFactory.getTile(defaultTile));
      oobTile = MapTileFactory.getTile(TileType.NULL);
      fov = new ShadowFoVRect(transparencyMap);
      vacuumList = new Vector<Coord>();
      breachList = new Vector<Coord>();
      fireList = new Vector<Coord>();
      iceList = new Vector<Coord>();
      buttonTriggerList = new Vector<ButtonTrigger>();
      delayedButtonTriggerList = new Vector<DelayedButtonTrigger>();
      automaticDoorList = new Vector<Coord>();
      exit = null;
      refreshCorpseMap();
      refreshGearMap();
   }
   
   public void takeTurn()
   {
      alternatingTurns = !alternatingTurns;
      adjustAir();
      extinguishCheck();
      thawCheck();
      checkAutomaticDoors();
      processDelayedButtonTriggers();
   }
   
   public void postProcessing()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(tileArray[x][y] instanceof Vacuum)
            vacuumList.add(new Coord(x, y));
         if(tileArray[x][y] instanceof Door)
         {
            Door d = (Door)tileArray[x][y];
            if(d.isAutomatic())
               registerAutomaticDoor(x, y);
         }
      }
   }
   
   public int countOpenTiles()
   {
      int count = 0;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(getTile(x, y).isLowPassable())
            count++;
      }
      return count;
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
                  case FLOOD_WATER : flood(c, trigger.getIntensity(), TileType.WATER); break;
                  case FLOOD_ACID : flood(c, trigger.getIntensity(), TileType.ACID); break;
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
      return MapTileFactory.getTile(oobTile);
   }
   
   public void breakTile(Coord c){breakTile(c.x, c.y);}
   public void breakTile(int x, int y)
   {
      MapTile mt = getTile(x, y);
      if(getTile(x, y) instanceof Ice)
      {
         thaw(x, y);
         mt = getTile(x, y);
      }
      MapTile newTile = MapTileFactory.getBroken(mt);
      if(mt.isHighPassable())
         newTile.setAirPressure(mt.getAirPressure());
      else
         newTile.setAirPressure(0);
      setTile(x, y, newTile);
      if(mt.hasOnDestructionEffect())
         GameEngine.doDestructionEffect(x, y, mt);
      breachCheck(x, y);
   }
   
   // return a passability map accounting for movement type, door usage, and pathfinding radius
   public boolean[][] getPassMap(Actor a, boolean noHazards)
   {
      int radius = a.getAI().getPathfindingRadius();
      boolean[][] passMap = new boolean[radius + radius + 1][radius + radius + 1];
      for(int x = 0; x < (radius * 2) + 1; x++)
      for(int y = 0; y < (radius * 2) + 1; y++)
      {
         Coord realLoc = new Coord(x + a.getMapLoc().x - radius, y + a.getMapLoc().y - radius);
         if(a.getAI().getUsesDoors() && getTile(realLoc) instanceof Door)
            passMap[x][y] = true;
         else if(noHazards && a.isHazard(realLoc))
            passMap[x][y] = false;
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
   
   public void flood(Coord c, int reps, TileType type){flood(c.x, c.y, reps, type);}
   public void flood(int x, int y, int reps, TileType type)
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
            setTile(loc, MapTileFactory.getTile(type));
            if(corpseMap[loc.x][loc.y] != null)
            {
               if(getTile(loc) instanceof Acid)
                  corpseMap[loc.x][loc.y] = null;
               else
                  corpseMap[loc.x][loc.y].setColor(getTile(loc).getFGColor());
            }
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
      if(isInBounds(x, y) &&
         !(tileArray[x][y] instanceof Fire) &&
         !(tileArray[x][y] instanceof Acid))
         corpseMap[x][y] = corpse;
   }
   
   public Corpse getCorpseAt(Coord loc){return getCorpseAt(loc.x, loc.y);}
   public Corpse getCorpseAt(int x, int y)
   {
      if(isInBounds(x, y))
         return corpseMap[x][y];
      return null;
   }

   
   // gear stuff
   ////////////////////////////////////////////////////////////////////////////
   
   private void refreshGearMap()
   {
      gearMap = new GearObj[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         gearMap[x][y] = null;
   }
   
   public boolean isGearAt(Coord c){return isGearAt(c.x, c.y);}
   public boolean isGearAt(int x, int y)
   {
      if(isInBounds(x, y))
         return gearMap[x][y] != null;
      return false;
   }
   
   public boolean canDropGearAt(Coord c){return canDropGearAt(c.x, c.y);}
   public boolean canDropGearAt(int x, int y)
   {
      if(isInBounds(x, y))
      {
         MapTile tile = getTile(x, y);
         if(tile.isLowPassable() && 
            tile instanceof Door == false &&
            !isGearAt(x, y))
            return true;
      }
      return false;
   }
   
   public Coord getNearestGearLocation(int x, int y){return getNearestGearLocation(new Coord(x, y));}
   public Coord getNearestGearLocation(Coord c)
   {
      if(canDropGearAt(c))
         return c;
      
      SpiralSearch search = new SpiralSearch(lowPassMap, c);
      Coord target = search.getNext();
      while(target != null)
      {
         if(canDropGearAt(target))
            return target;
         target = search.getNext();
      }
      return null;
   }
   
   public void dropGear(Coord c, GearObj gear){dropGear(c.x, c.y, gear);}
   public void dropGear(int x, int y, GearObj gear)
   {
      Coord loc = getNearestGearLocation(x, y);
      if(loc != null)
         setGearAt(loc, gear);
   }
   
   public void setGearAt(Coord loc, GearObj gear){setGearAt(loc.x, loc.y, gear);}
   public void setGearAt(int x, int y, GearObj gear)
   {
      if(isInBounds(x, y) &&
         !(tileArray[x][y] instanceof Acid))
         gearMap[x][y] = gear;
   }
   
   public GearObj getGearAt(Coord loc){return getGearAt(loc.x, loc.y);}
   public GearObj getGearAt(int x, int y)
   {
      if(isInBounds(x, y))
         return gearMap[x][y];
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
   
   
   // delayed button presses; button invocations staggered over turns
   ///////////////////////////////////////////////////////////////////////
   public void processDelayedButtonTriggers()
   {
      for(int i = 0; i < delayedButtonTriggerList.size(); i++)
      {
         DelayedButtonTrigger trigger = delayedButtonTriggerList.elementAt(i);
         trigger.increment();
         if(trigger.isReady())
         {
            buttonPressed(trigger.triggerIndex);
            delayedButtonTriggerList.removeElementAt(i);
            i--;
         }
      }
   }
   
   public void addDelayedButtonEffect(int delay, Button button)
   {
      delayedButtonTriggerList.add(new DelayedButtonTrigger(delay, button));
   }
   
   private class DelayedButtonTrigger
   {
      public int delay;
      public int triggerIndex;
      
      public DelayedButtonTrigger(int d, Button button)
      {
         delay = d;
         triggerIndex = button.getTriggerIndex();
      }
      
      public void increment()
      {
         delay--;
      }
      
      public boolean isReady()
      {
         return delay <= 0;
      }
   }
   
   
   // automatic doors
   ////////////////////////////////////////////////////////
   public void registerAutomaticDoor(Coord c){registerAutomaticDoor(c.x, c.y);}
   public void registerAutomaticDoor(int x, int y)
   {
      automaticDoorList.add(new Coord(x, y));
   }
   
   public void unregisterAutomaticDoor(int x, int y){unregisterAutomaticDoor(new Coord(x, y));}
   public void unregisterAutomaticDoor(Coord c)
   {
      for(int i = 0; i < automaticDoorList.size(); i++)
      {
         if(automaticDoorList.elementAt(i).equals(c))
         {
            automaticDoorList.removeElementAt(i);
            i--;
         }
      }
   }
   
   private void checkAutomaticDoors()
   {
      // memoize actor locations so that we only go through the list once
      boolean[][] actorLocMap = new boolean[getWidth()][getHeight()];
      for(int i = 0; i < GameEngine.getActorList().size(); i++)
      {
         Coord c = GameEngine.getActorList().elementAt(i).getMapLoc();
         if(isInBounds(c))
            actorLocMap[c.x][c.y] = true;
      }
      for(int i = 0; i < automaticDoorList.size(); i++)
      {
         if(getTile(automaticDoorList.elementAt(i)) instanceof Door)
            checkAutomaticDoor(automaticDoorList.elementAt(i), actorLocMap);
         else
         {
            automaticDoorList.removeElementAt(i);
            i--;
         }
      }
   }
   
   // most doors automatically close if they have no air and no adjacent actors
   private void checkAutomaticDoor(Coord c, boolean[][] actorLocMap)
   {
      if(getTile(c) instanceof Door)
      {
         Door door = (Door)getTile(c);
         if(!door.isLocked() && door.isOpen() && door.getAirPressure() == 0)
         {
            if(okayToCloseDoor(c, actorLocMap))
            {
               tryToToggle(c);
            }
         }
      }
   }
   
   private boolean okayToCloseDoor(Coord c, boolean[][] actorLocMap)
   {
      for(int x = -1; x < 2; x++)
      for(int y = -1; y < 2; y++)
      {
         if(actorLocMap[c.x + x][c.y + y])
            return false;
      }
      return true;
   }
   
   
   // testing method
   //////////////////////////////////////////////
   public void print()
   {
      for(int y = 0; y < height; y++)
      {
         for(int x = 0; x < width; x++)
         {
            System.out.print("" + (char)(getTile(x, y).getIconIndex()));
         }
         System.out.println("");
      }
   }
}