package Pulsar.GUI;

import WidlerSuite.*;
import Pulsar.Actor.*;

public class MovementScriptFactory implements GUIConstants
{
   private static int getXStep(int originX, int targetX)
   {
      return (int)(targetX - originX);
   }
   
   private static int getYStep(int originY, int targetY)
   {
      return (int)(targetY - originY);
   }
   
   
   public static MovementScript getWalkingScript(Actor actor, Coord target)
   {
      UnboundTile ut = actor.getSprite();
      int xStep = getXStep(ut.getXLoc(), target.x);
      int yStep = getXStep(ut.getYLoc(), target.y);
      MovementScript ms = new MovementScript(ut);
      for(int i = 0; i < 10; i++)
      {
         ms.setOffset(i, .1 * xStep, .1 * yStep);
      }
      ms.setNonlocksTargetOnEnd(true);
      return ms;
   }
}