package Pulsar.AI;

import Pulsar.Actor.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import Pulsar.Engine.*;
import Pulsar.Zone.*;
import Pulsar.Gear.*;
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
   
   public boolean canAttack()
   {
      return self.getWeapon().canFire();
   }
   
   public void act()
   {
      switch(pendingAction)
      {
         case STEP :             doStep(); break;
         case TOGGLE :           doToggle(); break;
         case DELAY :            doDelay(); break;
         case ATTACK :           doAttack(); break;
         case SWITCH_WEAPONS :   doWeaponSwitch(); break;
         case UNARMED_ATTACK :   doUnarmedAttack(); break;
      }
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
      // actor
      else if(GameEngine.isActorAt(pendingTarget))
      {
         // enemy, unarmed attack
         if(isHostile(GameEngine.getActorAt(pendingTarget)))
         {
            setPendingAction(ActorAction.UNARMED_ATTACK);
         }
         else
         // friendly, no action
         {
            MessagePanel.addMessage("There's a dude there.");
            clearPlan();
         }
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
      if(EngineTools.isAdjacent(target, self.getMapLoc()))
         return target;
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
   
   // find nearest tile the target does not have line of effect to
   protected Coord getNearestSafeTile(Coord target)
   {
      SpiralSearch search = new SpiralSearch(GameEngine.getZoneMap().getLowPassMap(), self.getMapLoc());
      Coord searchLoc = search.getNext();
      while(searchLoc != null)
      {
         if(!GameEngine.hasClearShotIgnoring(target, searchLoc, self.getMapLoc()))
            return searchLoc;
         searchLoc = search.getNext();
      }
      return null;
   }
   
   // find nearest tile from which to attack the target
   protected Coord getNearestAttackTile(Actor target, Weapon weapon){return getNearestAttackTile(target.getMapLoc(), weapon);}
   protected Coord getNearestAttackTile(Coord target, Weapon weapon)
   {
      if(weapon.isMelee())
      {
         return getStepTowards(target);
      }
      SpiralSearch search = new SpiralSearch(GameEngine.getZoneMap().getLowPassMap(), self.getMapLoc());
      Coord searchLoc = search.getNext();
      while(searchLoc != null)
      {
         if(GameEngine.hasClearShotIgnoring(searchLoc, target, self.getMapLoc()) &&
            !GameEngine.isActorAt(searchLoc))
         {
            return searchLoc;
         }
         searchLoc = search.getNext();
      }
      return null;
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
      Combat.resolveAttack(self, pendingTarget, self.getWeapon());
   }
   
   private void doUnarmedAttack()
   {
      lastActorTargeted = GameEngine.getActorAt(pendingTarget);
      Combat.resolveAttack(self, pendingTarget, self.getUnarmedAttack());
   }
   
   private void doWeaponSwitch()
   {
      if(self instanceof Player)
      {
         Player pSelf = (Player)self;
         pSelf.switchWeapons();
      }
   }
   
   
   // planning methods
   //////////////////////////////////////////////////////////////////
   
   // attack if able, move to get clear shot if not
   protected void planToAttack(Actor target, Weapon weapon)
   {
      // adjacent to enemy
      if(EngineTools.isAdjacent(self, target))
      {
         setPendingTarget(target.getMapLoc());
         setPendingAction(ActorAction.ATTACK);
         return;
      }
      // not adjacent
      else
      {
         // has clear shot with ranged weapon
         if(GameEngine.hasClearShot(self, target) && !weapon.isMelee())
         {
            setPendingTarget(target.getMapLoc());
            setPendingAction(ActorAction.ATTACK);
            return;
         }
         else
         // move towards
         {
            Coord nearestAttackTile = getNearestAttackTile(target, weapon);
            if(nearestAttackTile != null)
            {
               Coord stepLoc = getStepTowards(nearestAttackTile);
               if(stepLoc != null)
               {
                  setPendingTarget(stepLoc);
                  setPendingAction(ActorAction.STEP);
                  return;
               }
            }
         }
      }
   }
   
   // move towards the nearest tile that target does not have line of effect to
   protected void planMoveToCover(Actor target)
   {
      Coord safeLoc = getNearestSafeTile(target.getMapLoc());
      if(safeLoc != null)
      {
         Coord stepTowards = getStepTowards(safeLoc);
         if(stepTowards != null)
         {
            setPendingTarget(stepTowards);
            setPendingAction(ActorAction.STEP);
            return;
         }
      }
   }
}