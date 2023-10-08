package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class CloneAI extends BasicAI
{
	private boolean countsDown;
	private int turnsRemaining;


	public boolean isCountsDown(){return countsDown;}
	public int getTurnsRemaining(){return turnsRemaining;}


	public void setCountsDown(boolean c){countsDown = c;}
	public void setTurnsRemaining(int t){turnsRemaining = t;}


   public CloneAI(Actor s)
   {
      super(s);
      team = Team.PLAYER;
      self.setAlertness(Alertness.ALERT);
      countsDown = false;
      turnsRemaining = 4;
   }
   
   public void plan()
   {
      setPendingAction(ActorAction.DELAY);
      setPendingTarget(self.getMapLoc());
      
      if(countsDown)
      {
         turnsRemaining--;
         if(turnsRemaining == 0)
            self.kill();
      }
   }
}