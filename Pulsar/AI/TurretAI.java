package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class TurretAI extends BasicAI
{
   public TurretAI(Actor s)
   {
      super(s);
      self.setAlertness(Alertness.ALERT);
   }
   
   public void plan()
   {
      Actor target = getNearestVisibleEnemy();
      // no visible enemy
      if(target != null)
      {
         System.out.println("Has target.");
         // weapon is charged
         if(canAttack() && GameEngine.hasClearShot(self, target))
         {
            setPendingTarget(target.getMapLoc());
            setPendingAction(ActorAction.ATTACK);
            return;
         }
         else
         {
         
         System.out.println("No clear shot.");
         }
      }
      // default
      setPendingTarget(self.getMapLoc());
      setPendingAction(ActorAction.DELAY);

   }
}