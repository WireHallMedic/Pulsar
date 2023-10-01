package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.util.*;
import WidlerSuite.*;

public class VictimAI extends BasicAI implements AIConstants
{
   
   public VictimAI(Actor a)
   {
      super(a);
      setTeam(AIConstants.Team.PLAYER);
   }
   
   public void tryToAddLeader()
   {
      for(Actor a : GameEngine.getActorList())
         if(a.getAI().getTeam() == getTeam() && a != self && self.canSee(a))
         {
            setLeader(a);
            self.getAlertnessManager().becomeSurprised();
         }
   }
   
   
   @Override
   public void plan()
   {
      if(getLeader() == null)
         tryToAddLeader();
      
      // too far from leader
      if(hasLeader() && EngineTools.getDistanceTo(getLeader().getMapLoc(), self.getMapLoc()) > getFollowDistance())
      {
         Coord stepTowards = getStepTowards(getLeader().getMapLoc());
         // can path to last seen
         if(stepTowards != null)
         {
            setStepTowards(stepTowards);
            return;
         }
      }
      
      Actor nearestEnemy = getNearestVisibleEnemy();
      // has visible enemy
      if(nearestEnemy != null)
      {
         // hide from enemies
         planMoveToCover();
         if(hasPlan())
            return;
      }
      else
      // no known enemies
      {
        // too close to leader
        if(hasLeader() && EngineTools.getDistanceTo(getLeader().getMapLoc(), self.getMapLoc()) == 1)
        {
           Coord stepAway = getStepAwayFrom(getLeader().getMapLoc());
           // can path to last seen
           if(stepAway != null)
           {
              setStepTowards(stepAway);
              return;
           }
        }
         super.plan();
         return;
      }
      
      // default
      setPendingTarget(self.getMapLoc());
      setPendingAction(ActorAction.DELAY);
   }

}