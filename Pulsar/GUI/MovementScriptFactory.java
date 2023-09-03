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
   
   private static int getXDirectionTo(int originX, int targetX)
   {
      if(originX > targetX)
         return -1;
      if(originX < targetX)
         return 1;
      return 0;
   }
   
   private static int getYDirectionTo(int originY, int targetY)
   {
      if(originY > targetY)
         return -1;
      if(originY < targetY)
         return 1;
      return 0;
   }
   
   
   public static MovementScript getWalkingScript(Actor actor, Coord target)
   {
      UnboundTile ut = actor.getSprite();
      int xStep = getXStep(ut.getXLoc(), target.x);
      int yStep = getXStep(ut.getYLoc(), target.y);
      MovementScript ms = new MovementScript(ut);
      for(int i = 0; i < 10; i++)
      {
         ms.setOffset(i, NORMAL_MOVE_SPEED * xStep, NORMAL_MOVE_SPEED * yStep);
      }
      ms.setNonlocksTargetOnEnd(true);
      return ms;
   }
   
   public static MovementScript getImpactScript(Actor actor, Coord origin, int delay)
   {
      UnboundTile ut = actor.getSprite();
      int xStep = getXDirectionTo(ut.getXLoc(), origin.x);
      int yStep = getYDirectionTo(ut.getYLoc(), origin.y);
      int frames_per_direction = 3;
      MovementScript ms = new MovementScript(ut);
      ms.setLength(delay + frames_per_direction + frames_per_direction);
      for(int i = 0; i < frames_per_direction; i++)
      {
         ms.setOffset(delay + i, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
         ms.setOffset(delay + i + frames_per_direction, NORMAL_MOVE_SPEED * xStep, NORMAL_MOVE_SPEED * yStep);
      }
      ms.setNonlocksTargetOnEnd(true);
      return ms;
   }
}