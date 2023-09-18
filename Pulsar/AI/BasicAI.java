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
   protected int pathfindingRadius;
   protected boolean usesDoors;
   protected Actor leader;
   protected int followDistance;


	public Actor getSelf(){return self;}
	public ActorAction getPendingAction(){return pendingAction;}
	public Coord getPendingTarget(){if(pendingTarget == null) return null; return new Coord(pendingTarget);}
	public ActorAction getPreviousAction(){return previousAction;}
	public Coord getPreviousTarget(){if(pendingTarget == null) return null; return new Coord(previousTarget);}
   public Actor getLastActorTargeted(){return lastActorTargeted;}
   public Team getTeam(){return team;}
   public int getPathfindingRadius(){return pathfindingRadius;}
   public boolean getUsesDoors(){return usesDoors;}
   public Actor getLeader(){return leader;}
   public int getFollowDistance(){return followDistance;}
   public boolean hasLeader(){return leader != null;}


	public void setSelf(Actor s){self = s;}
	public void setPendingAction(ActorAction p){pendingAction = p;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}
   public void setLastActorTargeted(Actor a){lastActorTargeted = a;}
   public void setTeam(Team t){team = t;}
   public void setPathfindingRadius(int pr){pathfindingRadius = pr;}
   public void setUsesDoors(boolean ud){usesDoors = ud;}
   public void setLeader(Actor a){leader = a;}
   public void setFollowDistance(int fd){followDistance = fd;}


   public BasicAI(Actor s)
   {
      self = s;
      previousTarget = null;
      previousAction = null;
      lastActorTargeted = null;
      team = Team.VILLAIN;
      pathfindingRadius = PATHFINDING_MAP_RADIUS;
      usesDoors = true;
      leader = null;
      followDistance = 5;
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
   
   public boolean isFriendly(Actor that)
   {
      return getTeam().isFriendly(that.getAI().getTeam());
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
      if(pendingAction.isGadgetAction())
         doGadgetAction(pendingAction.getGadgetIndex());
      shiftPendingToPrevious();
      clearPlan();
      GameEngine.getPlayer().updateFoV();
   }
   
   protected void shiftPendingToPrevious()
   {
      previousTarget = pendingTarget;
      previousAction = pendingAction;
   }
   
   protected void setStepTowards(Coord target)
   {
      setPendingTarget(target);
      if(getUsesDoors() && GameEngine.getZoneMap().getTile(target) instanceof Door && !GameEngine.getZoneMap().isPassable(self, target))
         setPendingAction(ActorAction.TOGGLE);
      else
         setPendingAction(ActorAction.STEP);
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
         if(unlockedCheck(pendingTarget))
            setPendingAction(ActorAction.TOGGLE);
         else
         {
            MessagePanel.addMessage("It's locked.");
            clearPlan();
         }
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
   
   public void interpertUse()
   {
      // toggle tile
      if(GameEngine.getZoneMap().getTile(pendingTarget) instanceof ToggleTile)
      {
         if(unlockedCheck(pendingTarget))
            setPendingAction(ActorAction.TOGGLE);
         else
         {
            MessagePanel.addMessage("It's locked.");
            clearPlan();
         }
      } 
      else
      // no possible action
      {
         MessagePanel.addMessage("Nothing to use there.");
         clearPlan();
      }
      
   }
   
   protected boolean unlockedCheck(Coord target)
   {
      if(GameEngine.getZoneMap().getTile(target) instanceof Door)
      {
         Door door = (Door)GameEngine.getZoneMap().getTile(target);
         return !door.isLocked();
      }
      return true;
   }
   
   protected Coord getStepTowards(Coord target)
   {
      if(EngineTools.isAdjacent(target, self.getMapLoc()))
      {
         if(GameEngine.isActorAt(target))
            return null;
         return target;
      }
      AStar pathing = new AStar();
      // get passability map
      boolean[][] passMap = GameEngine.getZoneMap().getPassMap(self);
      Coord passMapInset = GameEngine.getZoneMap().getPassMapInset(self);
      // modify stuff for reduced size
      Coord adjustedSelfLoc = self.getMapLoc();
      adjustedSelfLoc.subtract(passMapInset);
      Coord adjustedTarget = new Coord(target);
      adjustedTarget.subtract(passMapInset);
      // check target is in search area
      if(adjustedTarget.x < 0 ||
         adjustedTarget.y < 0 ||
         adjustedTarget.x >= passMap.length ||
         adjustedTarget.y >= passMap[0].length)
      {
         return null;
      }
      // mark actor tiles as impassable
      for(Actor a : GameEngine.getActorList())
      {
         Coord actorLoc = a.getMapLoc();
         actorLoc.subtract(passMapInset);
         if(actorLoc.x < 0 ||
            actorLoc.y < 0 ||
            actorLoc.x >= passMap.length ||
            actorLoc.y >= passMap[0].length)
         {
            continue;
         }
         passMap[actorLoc.x][actorLoc.y] = false;
      }
      // mark origin and target as passable
      passMap[adjustedSelfLoc.x][adjustedSelfLoc.y] = true;
      passMap[adjustedTarget.x][adjustedTarget.y] = GameEngine.getZoneMap().getTile(target).isLowPassable();
      Vector<Coord> path = pathing.path(passMap, adjustedSelfLoc, adjustedTarget);
      if(path.size() == 0)
      {
         return null;
      }
      Coord stepLoc = path.elementAt(0);
      stepLoc.add(passMapInset);
      return stepLoc;
   }
   
   // find nearest tile with no line of effect to any visible enemies
   protected Coord getNearestSafeTile()
   {
      Vector<Actor> enemyList = GameEngine.getVisibleEnemies(self);
      boolean[][] passMap = GameEngine.getZoneMap().getPassMap(self);
      Coord passMapInset = GameEngine.getZoneMap().getPassMapInset(self);
      // modify stuff for reduced size
      Coord adjustedSelfLoc = self.getMapLoc();
      adjustedSelfLoc.subtract(passMapInset);
      
      SpiralSearch search = new SpiralSearch(passMap, adjustedSelfLoc);
      Coord searchLoc = search.getNext();
      while(searchLoc != null)
      {
         boolean safeF = true;
         searchLoc.add(passMapInset);
         for(Actor enemy : enemyList)
         {
            if(GameEngine.hasClearShotIgnoring(enemy.getMapLoc(), searchLoc, self.getMapLoc()))
            {
               safeF = false;
               break;
            }
         }
         if(safeF && !GameEngine.isActorAt(searchLoc))
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
      boolean[][] passMap = GameEngine.getZoneMap().getPassMap(self);
      Coord passMapInset = GameEngine.getZoneMap().getPassMapInset(self);
      // modify stuff for reduced size
      Coord adjustedSelfLoc = self.getMapLoc();
      adjustedSelfLoc.subtract(passMapInset);
      Coord adjustedTarget = new Coord(target);
      adjustedTarget.subtract(passMapInset);
      SpiralSearch search = new SpiralSearch(passMap, adjustedSelfLoc);
      Coord searchLoc = search.getNext();
      while(searchLoc != null)
      {
         searchLoc.add(passMapInset);
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
         GameEngine.addLocking(ms);
         self.setMapLoc(pendingTarget);
      }
      else
      {
         self.setAllLocs(pendingTarget);
      }
      self.discharge(self.getMoveSpeed().timeCost);
   }
   
   private void doToggle()
   {
      MapTile tile = GameEngine.getZoneMap().getTile(pendingTarget);
      if(tile instanceof ToggleTile)
      {
         ToggleTile tTile = (ToggleTile)tile;
         tTile.toggle();
         GameEngine.getZoneMap().update(pendingTarget);
      }
      self.discharge(self.getInteractSpeed().timeCost);
   }
   
   private void doDelay()
   {
      self.discharge(Math.min(self.getMoveSpeed().timeCost,
                     Math.min(self.getAttackSpeed().timeCost,
                              self.getInteractSpeed().timeCost)));
   }
   
   private void doAttack()
   {
      lastActorTargeted = GameEngine.getActorAt(pendingTarget);
      Combat.resolveAttack(self, pendingTarget, self.getWeapon());
      self.discharge(self.getAttackSpeed().timeCost);
   }
   
   private void doUnarmedAttack()
   {
      lastActorTargeted = GameEngine.getActorAt(pendingTarget);
      Combat.resolveAttack(self, pendingTarget, self.getUnarmedAttack());
      self.discharge(self.getAttackSpeed().timeCost);
   }
   
   private void doWeaponSwitch()
   {
      if(self instanceof Player)
      {
         Player pSelf = (Player)self;
         pSelf.switchWeapons();
         self.discharge(self.getInteractSpeed().timeCost);
      }
   }
   
   private void doGadgetAction(int index)
   {
      if(self instanceof Player)
      {
         Player pSelf = (Player)self;
         Gadget gadget = pSelf.getGadget(index);
         // no gadget found, should never happen
         if(gadget == null)
         {
            MessagePanel.addMessage("No gadget to use");
            clearPlan();
         }
         // out of charges
         else if(!gadget.canUse())
         {
            MessagePanel.addMessage(gadget.getName() + " is out of charges");
            clearPlan();
         }
         else
         {
            gadget.use(self, pendingTarget);
            self.discharge(self.getInteractSpeed().timeCost);
            gadget.discharge();
         }
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
                  setStepTowards(stepLoc);
                  return;
               }
            }
         }
      }
   }
   
   // move towards the nearest tile that target does not have line of effect to
   protected void planMoveToCover()
   {
      Coord safeLoc = getNearestSafeTile();
      if(safeLoc != null)
      {
         Coord stepTowards = getStepTowards(safeLoc);
         if(stepTowards != null)
         {
            setStepTowards(stepTowards);
            return;
         }
      }
   }
}