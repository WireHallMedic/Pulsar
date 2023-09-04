package Pulsar.GUI;

import WidlerSuite.*;
import Pulsar.Engine.*;

public class GUITools implements GUIConstants
{

   public static int getXStep(int originX, int targetX)
   {
      return (int)(targetX - originX);
   }
   
   public static int getYStep(int originY, int targetY)
   {
      return (int)(targetY - originY);
   }
   
   public static int getXDirectionTo(int originX, int targetX)
   {
      if(originX > targetX)
         return -1;
      if(originX < targetX)
         return 1;
      return 0;
   }
   
   public static int getYDirectionTo(int originY, int targetY)
   {
      if(originY > targetY)
         return -1;
      if(originY < targetY)
         return 1;
      return 0;
   }
   
   public static double getXSpeedTowards(Coord origin, Coord target)
   {
      Vect vector = new Vect(origin, target);
      vector.magnitude = 1000.0;
      Coord normalizedCoord = new Coord(vector);
      return normalizedCoord.x / 1000.0;
   }
   
   public static double getYSpeedTowards(Coord origin, Coord target)
   {
      Vect vector = new Vect(origin, target);
      vector.magnitude = 1000.0;
      Coord normalizedCoord = new Coord(vector);
      return normalizedCoord.y / 1000.0;
   }
   
   public static double modifyByAmount(double base, double modifier)
   {
      double amt = base - modifier;
      amt += GameEngine.random() * (2 * modifier);
      return amt;
   }
}