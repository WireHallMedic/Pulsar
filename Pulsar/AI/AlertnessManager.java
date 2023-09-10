/*
A simple state machine for handling alertness
*/

package Pulsar.AI;

import Pulsar.GUI.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.util.*;

public class AlertnessManager implements AIConstants
{	
   private Actor self;


	public Actor getSelf(){return self;}


	public void setSelf(Actor s){self = s;}

   public AlertnessManager(Actor s)
   {
      self = s;
   }
   
   private boolean isAwareOfEnemies()
   {
      return self.getMemory().getEnemyList().size() > 0;
   }
   
   private boolean friendsAreWorried()
   {
      Vector<Actor> friendList = self.getMemory().getFriends();
      for(Actor a : friendList)
      {
         if(a.getAlertness() == Alertness.ALERT && self.canSee(a))
            return true;
      }
      return false;
   }
   
   public void recoverFromSurprise()
   {
      self.setAlertness(Alertness.ALERT);
   }
   
   public void becomeSurprised()
   {
      self.setAlertness(Alertness.SURPRISED);
      if(GameEngine.playerCanSee(self))
         VisualEffectFactory.addSurpriseIndicator(self);
   }
   
   public void update(boolean canSeeEnemy)
   {
      if(self.getAlertness() == Alertness.INACTIVE)
      {
         if(canSeeEnemy || isAwareOfEnemies())
         {
            becomeSurprised();
         }
         else if(friendsAreWorried())
         {
            self.setAlertness(Alertness.CAUTIOUS);
         }
      }
      if(self.getAlertness() == Alertness.RELAXED)
      {
         if(canSeeEnemy || isAwareOfEnemies())
         {
            becomeSurprised();
         }
         else if(friendsAreWorried())
         {
            self.setAlertness(Alertness.CAUTIOUS);
         }
      }
      if(self.getAlertness() == Alertness.SURPRISED)
      {
         ;
      }
      if(self.getAlertness() == Alertness.CAUTIOUS)
      {
         if(canSeeEnemy || isAwareOfEnemies())
         {
            self.setAlertness(Alertness.ALERT);
         }
      }
      if(self.getAlertness() == Alertness.ALERT)
      {
         if(!canSeeEnemy && !isAwareOfEnemies())
         {
            self.setAlertness(Alertness.CAUTIOUS);
         }
      }
   }
}