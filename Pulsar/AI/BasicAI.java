package Pulsar.AI;

import Pulsar.Actor.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import Pulsar.Engine.*;
import Pulsar.Zone.*;
import java.awt.*;

public class BasicAI implements AIConstants
{
	private Actor self;
	private ActorAction pendingActorAction;
	private Coord pendingTarget;
	private ActorAction previousActorAction;
	private Coord previousTarget;


	public Actor getSelf(){return self;}
	public ActorAction getPendingAction(){return pendingActorAction;}
	public Coord getPendingTarget(){return new Coord(pendingTarget);}
	public ActorAction getPreviousActorAction(){return previousActorAction;}
	public Coord getPreviousTarget(){return new Coord(previousTarget);}


	public void setSelf(Actor s){self = s;}
	public void setPendingAction(ActorAction p){pendingActorAction = p;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}


   public BasicAI(Actor s)
   {
      self = s;
      previousTarget = null;
      previousActorAction = null;
      clearPlan();
   }
   
   // override in child classes
   public void plan()
   {
      setPendingAction(ActorAction.DELAY);
      setPendingTarget(self.getMapLoc());
   }
   
   public boolean hasPlan()
   {
      return pendingActorAction != null && pendingTarget != null;
   }
   
   public void clearPlan()
   {
      pendingActorAction = null;
      pendingTarget = null;
   }
   
   public void act()
   {
      if(pendingActorAction == ActorAction.STEP)
         doStep();
      if(pendingActorAction == ActorAction.TOGGLE)
         doToggle();
   }
   
   public void shiftPendingToPrevious()
   {
      previousTarget = pendingTarget;
      previousActorAction = pendingActorAction;
      clearPlan();
   }
   
   public void interpertContext()
   {
      if(self.getMapLoc().equals(pendingTarget))
      {
         setPendingAction(ActorAction.DELAY);
      }
      else if(self.canStep(pendingTarget))
      {
         setPendingAction(ActorAction.STEP);
      }
      else if(GameEngine.getZoneMap().getTile(pendingTarget) instanceof ToggleTile)
      {
         setPendingAction(ActorAction.TOGGLE);
      }
      // no possible action
      else if(GameEngine.isActorAt(pendingTarget))
      {
         MessagePanel.addMessage("There's a dude there.");
         clearPlan();
      }
      // no possible action
      else
      {
         MessagePanel.addMessage("That's a wall.");
         clearPlan();
      }
      
   }
   
   // carrying out the actions
   //////////////////////////////////////////////////////////////////
   private void doStep()
   {
      MovementScript ms = MovementScriptFactory.getWalkingScript(self, pendingTarget);
      self.setMapLoc(pendingTarget);
      GameEngine.addLocking(ms);
   }
   
   private void doToggle()
   {
      MapTile tile = GameEngine.getZoneMap().getTile(pendingTarget);
      if(tile instanceof ToggleTile)
      {
         ToggleTile tTile = (ToggleTile)tile;
         tTile.toggle();
         GameEngine.getZoneMap().updateFoV();
         GameEngine.getPlayer().updateFoV();
      }
   }
}