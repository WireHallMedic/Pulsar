package Pulsar.GUI;

import WidlerSuite.*;
import Pulsar.Actor.*;

public class MovementScriptFactory implements GUIConstants
{
   
   
   public static MovementScript getWalkingScript(Actor actor, Coord target)
   {
      UnboundTile ut = actor.getSprite();
      // use lower precision tools because we're always going exactly 1 step, allowing diagonal to be faster.
      int xStep = GUITools.getXStep(ut.getXLoc(), target.x);
      int yStep = GUITools.getYStep(ut.getYLoc(), target.y);
      MovementScript ms = new MovementScript(ut);
      int steps = (int)(1.0 / NORMAL_MOVE_SPEED);
      for(int i = 0; i < steps; i++)
      {
         ms.setOffset(i, NORMAL_MOVE_SPEED * xStep, NORMAL_MOVE_SPEED * yStep);
      }
      ms.setNonlocksTargetOnEnd(true);
      return ms;
   }
   
   public static MovementScript getImpactScript(Actor actor, Coord origin, int delay)
   {
      UnboundTile ut = actor.getSprite();
      double xStep = GUITools.getXSpeedTowards(new Coord(ut.getXLoc(), ut.getYLoc()), origin);
      double yStep = GUITools.getYSpeedTowards(new Coord(ut.getXLoc(), ut.getYLoc()), origin);
      int framesPerDirection = 3;
      MovementScript ms = new MovementScript(ut);
      ms.setLength(delay + framesPerDirection + framesPerDirection);
      for(int i = 0; i < framesPerDirection; i++)
      {
         ms.setOffset(delay + i, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
         ms.setOffset(delay + i + framesPerDirection, NORMAL_MOVE_SPEED * xStep, NORMAL_MOVE_SPEED * yStep);
      }
      ms.setNonlocksTargetOnEnd(true);
      return ms;
   }
   
   public static MovementScript getMeleeScript(Actor actor, Coord target)
   {
      UnboundTile ut = actor.getSprite();
      double xStep = GUITools.getXSpeedTowards(new Coord(ut.getXLoc(), ut.getYLoc()), target);
      double yStep = GUITools.getYSpeedTowards(new Coord(ut.getXLoc(), ut.getYLoc()), target);
      MovementScript ms = new MovementScript(ut);
      ms.setOffset(0, 0.0 - (SLOW_MOVE_SPEED * xStep), 0.0 - (SLOW_MOVE_SPEED * yStep));
      ms.setOffset(1, 0.0 - (SLOW_MOVE_SPEED * xStep), 0.0 - (SLOW_MOVE_SPEED * yStep));
      ms.setOffset(2, 0.0 - (SLOW_MOVE_SPEED * xStep), 0.0 - (SLOW_MOVE_SPEED * yStep));
      ms.setOffset(3, FAST_MOVE_SPEED * xStep, FAST_MOVE_SPEED * yStep);
      ms.setOffset(4, FAST_MOVE_SPEED * xStep, FAST_MOVE_SPEED * yStep);
      ms.setOffset(5, FAST_MOVE_SPEED * xStep, FAST_MOVE_SPEED * yStep);
      ms.setOffset(6, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
      ms.setOffset(7, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
      ms.setOffset(8, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
      ms.setOffset(9, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
      ms.setOffset(10, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
      ms.setOffset(11, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
      ms.setNonlocksTargetOnEnd(true);
      return ms;
   }
   
   public static MovementScript getShootScript(Actor actor, Coord target)
   {
      UnboundTile ut = actor.getSprite();
      double xStep = GUITools.getXSpeedTowards(target, new Coord(ut.getXLoc(), ut.getYLoc()));
      double yStep = GUITools.getYSpeedTowards(target, new Coord(ut.getXLoc(), ut.getYLoc()));
      int framesPerDirection = 2;
      MovementScript ms = new MovementScript(ut);
      ms.setLength(framesPerDirection + framesPerDirection);
      for(int i = 0; i < framesPerDirection; i++)
      {
         ms.setOffset(i, NORMAL_MOVE_SPEED * xStep, NORMAL_MOVE_SPEED * yStep);
         ms.setOffset(i + framesPerDirection, 0.0 - (NORMAL_MOVE_SPEED * xStep), 0.0 - (NORMAL_MOVE_SPEED * yStep));
      }
      ms.setNonlocksTargetOnEnd(true);
      return ms;
   }
}