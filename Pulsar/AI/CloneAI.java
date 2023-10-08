package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import Pulsar.GUI.*;

public class CloneAI extends BasicAI implements GUIConstants
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
   
   @Override
   public void plan()
   {
      setPendingAction(ActorAction.DELAY);
      setPendingTarget(self.getMapLoc());
   }
   
   @Override
   public void act()
   { 
      if(countsDown)
      {
         turnsRemaining--;
         if(turnsRemaining == 0)
         {
            self.discharge(self.getMoveSpeed().timeCost);
            clearPlan();
            self.killNoCorpse();
            return;
         }
         char countdownChar = (char)('0' + turnsRemaining);
         VisualEffectFactory.addFloatEffect(self, countdownChar, ORANGE, 1.0);
      }
      super.act();
   }
}