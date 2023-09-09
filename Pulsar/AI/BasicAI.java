package Pulsar.AI;

import Pulsar.Actor.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import Pulsar.Engine.*;
import Pulsar.Zone.*;
import java.awt.*;
import java.util.*;

public class BasicAI implements AIConstants
{
	protected Actor self;
	protected ActorAction pendingAction;
	protected Coord pendingTarget;
	protected ActorAction previousAction;
	protected Coord previousTarget;
   protected Actor lastActorTargeted;
   protected Team team;


	public Actor getSelf(){return self;}
	public ActorAction getPendingAction(){return pendingAction;}
	public Coord getPendingTarget(){if(pendingTarget == null) return null; return new Coord(pendingTarget);}
	public ActorAction getPreviousAction(){return previousAction;}
	public Coord getPreviousTarget(){if(pendingTarget == null) return null; return new Coord(previousTarget);}
   public Actor getLastActorTargeted(){return lastActorTargeted;}
   public Team getTeam(){return team;}


	public void setSelf(Actor s){self = s;}
	public void setPendingAction(ActorAction p){pendingAction = p;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}
   public void setLastActorTargeted(Actor a){lastActorTargeted = a;}
   public void setTeam(Team t){team = t;}


   public BasicAI(Actor s)
   {
      self = s;
      previousTarget = null;
      previousAction = null;
      lastActorTargeted = null;
      team = Team.VILLAIN;
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
      return pendingAction != null && pendingTarget != null;
   }
   
   public void clearPlan()
   {
      pendingAction = null;
      pendingTarget = null;
   }
   
   public boolean isHostile(Actor that)
   {
      return getTeam().isHostile(that.getAI().getTeam());
   }
   
   public Actor getNearestVisibleEnemy()
   {
      Actor curActor = null;
      int curDist = 10000;
      for(Actor prospect : GameEngine.getActorList())
      {
         if(isHostile(prospect) && self.canSee(prospect))
         {
            int prospectDist = EngineTools.getDistanceTo(self, prospect);
            if(prospectDist < curDist)
            {
               curDist = prospectDist;
               curActor = prospect;
            }
         }
      }
      return curActor;
   }
   
   public void act()
   {
      if(pendingAction == ActorAction.STEP)
         doStep();
      if(pendingAction == ActorAction.TOGGLE)
         doToggle();
      if(pendingAction == ActorAction.DELAY)
         doDelay();
      if(pendingAction == ActorAction.ATTACK)
         doAttack();
      if(pendingAction == ActorAction.SWITCH_WEAPONS)
         doWeaponSwitch();
      shiftPendingToPrevious();
   }
   
   public void shiftPendingToPrevious()
   {
      previousTarget = pendingTarget;
      previousAction = pendingAction;
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
   
   protected Coord getStepTowards(Coord target)
   {
      AStar pathing = new AStar();
      boolean[][] passMap = GameEngine.getZoneMap().getLowPassMap();
      for(Actor a : GameEngine.getActorList())
      {
         passMap[a.getMapLoc().x][a.getMapLoc().y] = false;
      }
      passMap[self.getMapLoc().x][self.getMapLoc().y] = true;
      passMap[target.x][target.y] = GameEngine.getZoneMap().getTile(target).isLowPassable();
      Vector<Coord> path = pathing.path(passMap, self.getMapLoc(), target);
      if(path.size() == 0)
      {
         return null;
      }
      return path.elementAt(0);
   }
   
   // carrying out the actions
   //////////////////////////////////////////////////////////////////
   private void doStep()
   {
      if(GameEngine.playerCanSee(pendingTarget) || GameEngine.playerCanSee(self))
      {
         MovementScript ms = MovementScriptFactory.getWalkingScript(self, pendingTarget);
         self.setMapLoc(pendingTarget);
         GameEngine.addLocking(ms);
      }
      else
      {
         self.setAllLocs(pendingTarget);
      }
   }
   
   private void doToggle()
   {
      MapTile tile = GameEngine.getZoneMap().getTile(pendingTarget);
      if(tile instanceof ToggleTile)
      {
         ToggleTile tTile = (ToggleTile)tile;
         tTile.toggle();
         GameEngine.getZoneMap().update(pendingTarget);
         GameEngine.getPlayer().updateFoV();
      }
   }
   
   private void doDelay()
   {
      
   }
   
   private void doAttack()
   {
      lastActorTargeted = GameEngine.getActorAt(pendingTarget);
      Combat.resolveAttack(self, pendingTarget);
   }
   
   private void doWeaponSwitch()
   {
      if(self instanceof Player)
      {
         Player pSelf = (Player)self;
         pSelf.switchWeapons();
      }
   }
}