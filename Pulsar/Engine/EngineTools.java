package Pulsar.Engine;

import WidlerSuite.*;
import java.util.*;
import Pulsar.Actor.*;

public class EngineTools
{
   public static final double SHOTGUN_SPRAY_ARC = 0.523599; // 30 degrees
   public static final double SHOTGUN_SPRAY_ARC_INCREMENT = SHOTGUN_SPRAY_ARC / 2.0;

   
   public static Vector<Coord> getShotgunSprayTargets(Coord origin, Coord target)
   {
      Vect straight = new Vect(origin, target);
      double maxAngle = straight.angle + SHOTGUN_SPRAY_ARC_INCREMENT;
      double minAngle = straight.angle - SHOTGUN_SPRAY_ARC_INCREMENT;
      Coord maxCoord = new Coord(new Vect(maxAngle, 10));
      Coord minCoord = new Coord(new Vect(minAngle, 10));
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
}