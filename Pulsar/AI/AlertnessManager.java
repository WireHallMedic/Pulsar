/*
A simple state machine for handling alertness
*/

package Pulsar.AI;

import Pulsar.Actor.*;

public class AlertnessManager implements AIConstants
{	
   private Actor self;


	public Actor getSelf(){return self;}


	public void setSelf(Actor s){self = s;}

   public AlertnessManager(Actor s)
   {
      self = s;
   }
}