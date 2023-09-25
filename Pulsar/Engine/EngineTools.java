package Pulsar.Engine;

import WidlerSuite.*;
import java.util.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;

public class EngineTools implements ActorConstants, GearConstants
{
   public static final double SHOTGUN_SPRAY_ARC = 0.523599; // 30 degrees
   public static final double SHOTGUN_SPRAY_ARC_INCREMENT = SHOTGUN_SPRAY_ARC / 2.0;
   
   private static int uniqueNumber = 0;
   private static long lastTimeMark;
   
   public synchronized static int getUniqueNumber()
   {
      uniqueNumber++;
      return uniqueNumber;
   }
   
   public static void markTime(String note)
   {
      long curTime = System.currentTimeMillis();
      long difference = curTime - lastTimeMark;
      lastTimeMark = curTime;
      if(difference > 0)
         System.out.println("[" + difference + "] " + note);
   }
   
   public static Vector<Coord> getShotgunSprayLine(Coord origin, Coord target)
   {
      Vect straight = new Vect(origin, target);
      double maxAngle = straight.angle + SHOTGUN_SPRAY_ARC_INCREMENT;
      double minAngle = straight.angle - SHOTGUN_SPRAY_ARC_INCREMENT;
      Coord maxCoord = new Coord(new Vect(maxAngle, 12));
      Coord minCoord = new Coord(new Vect(minAngle, 12));
      Vector<Coord> parallelLine = StraightLine.findLine(minCoord, maxCoord);
      for(Coord c : parallelLine)
      {
         c.x += origin.x;
         c.y += origin.y;
      }
      
      return parallelLine;
   }
   
   public static Coord getTileTowards(Coord origin, Coord target)
   {
      if(origin.equals(target))
         return origin;
      return StraightLine.findLine(origin, target, StraightLine.REMOVE_ORIGIN).elementAt(0);
   }
   
   public static int getDistanceTo(Actor o, Actor t){return getDistanceTo(o.getMapLoc(), t.getMapLoc());}
   public static int getDistanceTo(Coord origin, Coord target)
   {
      return WSTools.getAngbandMetric(origin, target);
   }
   
   public static boolean isAdjacent(Actor o, Actor t){return isAdjacent(o.getMapLoc(), t.getMapLoc());}
   public static boolean isAdjacent(Coord origin, Coord target)
   {
      return getDistanceTo(origin, target) < 2;
   }
   
   public static Coord getClosest(Coord origin, Vector<Coord> prospectList)
   {
      Coord curLoc = null;
      int curDist = 10000;
      for(Coord prospect : prospectList)
      {
         if(getDistanceTo(origin, prospect) < curDist)
         {
            curDist = getDistanceTo(origin, prospect);
            curLoc = new Coord(prospect);
         }
      }
      return curLoc;
   }
   
   public static Coord parseCoord(String s)
   {
      String[] splitStr = s.split(",");
      splitStr[0] = splitStr[0].replace("[", "");
      splitStr[0] = splitStr[0].trim();
      splitStr[1] = splitStr[1].replace("]", "");
      splitStr[1] = splitStr[1].trim();
      return new Coord(Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
   }
}