package Pulsar.AI;

import Pulsar.Actor.*;
import WidlerSuite.*;

public class BasicAI implements AIConstants
{
	private Actor self;
	private ACTION pendingAction;
	private Coord pendingTarget;


	public Actor getSelf(){return self;}
	public ACTION getPendingAction(){return pendingAction;}
	public Coord getPendingTarget(){return new Coord(pendingTarget);}


	public void setSelf(Actor s){self = s;}
	public void setPendingAction(ACTION p){pendingAction = p;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}

   public BasicAI(Actor s)
   {
      self = s;
      pendingAction = null;
      pendingTarget = null;
   }
   
   // override in child classes
   public void plan()
   {
      setPendingAction(ACTION.DELAY);
      setPendingTarget(self.getMapLoc());
   }
   
   public boolean hasPlan()
   {
      return pendingAction != null && pendingTarget != null;
   }

}