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
         Coord lastEnemyLoc = self.getMemory().getNearestLastKnown();
         // remember enemies, but none visible
         if(lastEnemyLoc != null)
         {
            Coord stepTowards = getStepTowards(lastEnemyLoc);
            // can path to last seen
            if(stepTowards != null)
            {
               setStepTowards(stepTowards);
               return;
            }
            // cannot path to last seen
            else
            {
               super.plan();
               return;
            }
         }
         else
         // no known enemies
         {
            super.plan();
            return;
         }
      }
      // has visible enemy
      else
      {
         // weapon is charged
         if(canAttack())
         {
            planToAttack(nearestEnemy, self.getWeapon());
            if(hasPlan())
               return;
         }
         else
         // weapon is not charged
         {
            planMoveToCover();
            if(hasPlan())
               return;
         }
      }
      // default
      setPendingTarget(self.getMapLoc());
      setPendingAction(ActorAction.DELAY);
   }
}