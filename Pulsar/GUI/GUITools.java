package Pulsar.GUI;

import WidlerSuite.*;
import Pulsar.Engine.*;

public class GUITools implements GUIConstants, WSFontConstants
{
   private static TileAnimationManager animationManager = null;
   
   public static TileAnimationManager getAnimationManager(){return animationManager;}
   
   public static void setAnimationManager(TileAnimationManager am){animationManager = am;}
   
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
   
   public static int[] getBar(int cur, int max, int length)
   {
      cur = Math.max(cur, 0);
      cur = Math.min(cur, max);
      int[] iconArray = new int[length];
      // early exits to avoid int math imprecision
      if(cur == max)
      {
         for(int i = 0; i < length; i++)
            iconArray[i] = FULL_BLOCK_TILE;
         return iconArray;
      }
      if(cur == 0)
      {
         for(int i = 0; i < length; i++)
            iconArray[i] = ' ';
         return iconArray;
      }
      
      int calcValue = (cur * (length * 8)) / max;
      calcValue = Math.max(calcValue, 1);
      for(int i = 0; i < length; i++)
      {
         switch(calcValue)
         {
            case 0  : iconArray[i] = ' '; break;
            case 1  : iconArray[i] = BLOCK_ONE_EIGHTH_TILE; calcValue = 0; break;
            case 2  : iconArray[i] = BLOCK_TWO_EIGHTHS_TILE; calcValue = 0; break;
            case 3  : iconArray[i] = BLOCK_THREE_EIGHTHS_TILE; calcValue = 0; break;
            case 4  : iconArray[i] = BLOCK_FOUR_EIGHTHS_TILE; calcValue = 0; break;
            case 5  : iconArray[i] = BLOCK_FIVE_EIGHTHS_TILE; calcValue = 0; break;
            case 6  : iconArray[i] = BLOCK_SIX_EIGHTHS_TILE; calcValue = 0; break;
            case 7  : iconArray[i] = BLOCK_SEVEN_EIGHTHS_TILE; calcValue = 0; break;
            default : iconArray[i] = FULL_BLOCK_TILE; calcValue -= 8; break;
         }
      }
      return iconArray;
   }
   
   public static String initToSec(int init)
   {
      String sec = "";
      switch(init)
      {
         case 0 : sec = "0"; break;
         case 1 : sec = ".5"; break;
         default : sec += (init / 2);
            if(init % 2 == 1)
               sec += ".5";
      }
      return sec + "s";
   }
   
   public static String doubleToPercent(double d)
   {
      int percent = (int)(100 * d);
      return percent + "%";
   }
   
   public static String centerString(String input, int width)
   {
      int spacerWidth = (width - input.length()) / 2;
      String spacer = "";
      for(int i = 0; i < spacerWidth; i++)
         spacer += " ";
      return spacer + input + spacer;
   }
   
   public static String getAirPressureString(int airPressure)
   {
      String str = "Error";
      switch(airPressure)
      {
         case 5 : str = "Good"; break;
         case 4 : str = "Fair"; break;
         case 3 : str = "Poor"; break;
         case 2 : str = "Low"; break;
         case 1 : str = "Very Low"; break;
         case 0 : str = "None"; break;
      }
      return str;
   }
}