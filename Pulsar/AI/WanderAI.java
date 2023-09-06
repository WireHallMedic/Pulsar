/*
50% of the time steps in a random direction
*/

package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class WanderAI extends BasicAI
{
   private static final Coord[] stepList = getStepList();
   
   public WanderAI(Actor s)
   {
      super(s);
   }
   
   // 50% chance to wait
   // else step in a random direction if able
   // else wait
   public void plan()
   {
      if(GameEngine.randomInt(0, 2) == 0)
      {
         setPendingAction(ActorAction.DELAY);
         setPendingTarget(self.getMapLoc());
      }
      else
      {
         Coord stepDir = stepList[GameEngine.randomInt(0, 8)];
         Coord target = new Coord(stepDir.x + self.getMapLoc().x, stepDir.y + self.getMapLoc().y);
         if(self.canStep(target))
         {
            setPendingAction(ActorAction.STEP);
            setPendingTarget(target);
         }
         else // can't step
         {
            setPendingAction(ActorAction.DELAY);
            setPendingTarget(self.getMapLoc());
         }
      }
   }
   
   public static Coord[] getStepList()
   {
      Coord[] list = new Coord[8];
      list[0] = new Coord(0, 1);
      list[1] = new Coord(0, -1);
      list[2] = new Coord(1, 0);
      list[3] = new Coord(-1, 0);
      list[4] = new Coord(1, 1);
      list[5] = new Coord(-1, 1);
      list[6] = new Coord(1, -1);
      list[7] = new Coord(-1, -1);
      return list;
   }
}