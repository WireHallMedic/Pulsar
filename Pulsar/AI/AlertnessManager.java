/*
A simple state machine for handling alertness
*/

package Pulsar.AI;

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
      System.out.println(self + " recovers from surprise");
      self.setAlertness(Alertness.ALERT);
   }
   
   public void update(boolean canSeeEnemy)
   {
      if(self.getAlertness() == Alertness.INACTIVE)
      {
         if(canSeeEnemy || isAwareOfEnemies())
         {
            self.setAlertness(Alertness.SURPRISED);
            System.out.println(self + " moves from inactive to surprised");
         }
         else if(friendsAreWorried())
         {
            self.setAlertness(Alertness.CAUTIOUS);
            System.out.println(self + " moves from inactive to cautious");
         }
      }
      if(self.getAlertness() == Alertness.RELAXED)
      {
         if(canSeeEnemy || isAwareOfEnemies())
         {
            self.setAlertness(Alertness.SURPRISED);
            System.out.println(self + " moves from relaxed to surprised");
         }
         else if(friendsAreWorried())
         {
            self.setAlertness(Alertness.CAUTIOUS);
            System.out.println(self + " moves from relaxed to cautious");
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
            System.out.println(self + " moves from cautious to alert");
         }
      }
      if(self.getAlertness() == Alertness.ALERT)
      {
         if(!canSeeEnemy && !isAwareOfEnemies())
         {
            self.setAlertness(Alertness.CAUTIOUS);
            System.out.println(self + " moves from alert to cautious");
         }
      }
   }
}