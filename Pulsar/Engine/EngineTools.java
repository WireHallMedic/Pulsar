package Pulsar.Engine;

import WidlerSuite.*;

public class EngineTools
{
   public static final double SHOTGUN_SPRAY_ARC = 0.523599; // 30 degrees
   public static final double SHOTGUN_SPRAY_ARC_INCREMENT = SHOTGUN_SPRAY_ARC / 4.0;
   
   public static double[] getShotgunSprayAngles(double theta)
   {
      double[] angleArr = new double[5];
      angleArr[0] = theta - (SHOTGUN_SPRAY_ARC_INCREMENT * 2);
      angleArr[1] = theta - SHOTGUN_SPRAY_ARC_INCREMENT;
      angleArr[2] = theta;
      angleArr[3] = theta + SHOTGUN_SPRAY_ARC_INCREMENT;
      angleArr[4] = theta + (SHOTGUN_SPRAY_ARC_INCREMENT * 2);
      return angleArr;
   }
   
   public static Coord[] getShotgunSprayTargets(Coord origin, Coord target)
   {
      Vect straight = new Vect(origin, target);
      double[] angleArr = getShotgunSprayAngles(straight.angle);
      Coord[] coordArr = new Coord[5];
      for(int i = 0; i < coordArr.length; i++)
      {
         Vect v = new Vect(angleArr[i], 9);
         coordArr[i] = new Coord(v);
         coordArr[i].add(origin);
      }
      return coordArr;
   }
}