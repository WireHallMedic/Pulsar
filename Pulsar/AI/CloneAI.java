package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class CloneAI extends BasicAI
{
   public CloneAI(Actor s)
   {
      super(s);
      team = Team.PLAYER;
   }
   
   public void plan()
   {
      Actor a = getNearestVisibleEnemy();
      if(a != null && EngineTools.isAdjacent(self.getMapLoc(), a.getMapLoc()))
      {
         self.setCurHealth(-1);
      }
      setPendingAction(ActorAction.DELAY);
      setPendingTarget(self.getMapLoc());
   }
}