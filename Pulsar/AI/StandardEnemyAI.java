/*
This AI will try to:
if weapon has charge:
   attack enemy
   move to a tile where it can attack enemy
if weapon has no charge
   move to cover
   attack player in melee
   move to melee with player
*/

package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class StandardEnemyAI extends WanderAI implements AIConstants
{
   public StandardEnemyAI(Actor s)
   {
      super(s);
   }
   
   @Override
   public void plan()
   {
      Actor nearestEnemy = getNearestVisibleEnemy();
      // no visible enemy
      if(nearestEnemy == null)
      {
         super.plan();
         return;
      }
      // has visible enemy
      else
      {
         // adjacent to enemy
         if(EngineTools.areAdjacent(self, nearestEnemy))
         {
            setPendingTarget(nearestEnemy.getMapLoc());
            setPendingAction(ActorAction.ATTACK);
            return;
         }
         // not adjacent
         else
         {
            // has clear shot
            if(GameEngine.hasClearShot(self, nearestEnemy))
            {
               setPendingTarget(nearestEnemy.getMapLoc());
               setPendingAction(ActorAction.ATTACK);
               return;
            }
            else
            {
               Coord stepLoc = getStepTowards(nearestEnemy.getMapLoc());
               if(stepLoc != null)
               {
                  setPendingTarget(stepLoc);
                  setPendingAction(ActorAction.STEP);
                  return;
               }
            }
         }
      }
      // default
      setPendingTarget(self.getMapLoc());
      setPendingAction(ActorAction.DELAY);
   }
}