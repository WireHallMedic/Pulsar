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
   protected Team tempTeam;
   protected int pathfindingRadius;
   protected boolean usesDoors;
   protected boolean avoidsHazards;
   protected Actor leader;
   protected Actor tempLeader;
   protected int followDistance;


	public Actor getSelf(){return self;}
	public ActorAction getPendingAction(){return pendingAction;}
	public Coord getPendingTarget(){if(pendingTarget == null) return null; return new Coord(pendingTarget);}
	public ActorAction getPreviousAction(){return previousAction;}
	public Coord getPreviousTarget(){if(pendingTarget == null) return null; return new Coord(previousTarget);}
   public Actor getLastActorTargeted(){return lastActorTargeted;}
   public int getPathfindingRadius(){return pathfindingRadius;}
   public boolean getUsesDoors(){return usesDoors;}
   public boolean getAvoidsHazards(){return avoidsHazards;}
   public int getFollowDistance(){return followDistance;}
   public boolean hasLeader(){return getLeader() != null;}
   
   
	public void setSelf(Actor s){self = s;}
	public void setPendingAction(ActorAction p){pendingAction = p;}
	public void setPendingTarget(Coord p){setPendingTarget(p.x, p.y);}
	public void setPendingTarget(int x, int y){pendingTarget = new Coord(x, y);}
   public void setLastActorTargeted(Actor a){lastActorTargeted = a;}
   public void setTeam(Team t){team = t;}
   public void setPathfindingRadius(int pr){pathfindingRadius = pr;}
   public void setUsesDoors(boolean ud){usesDoors = ud;}
   public void setAvoidsHazards(boolean ah){avoidsHazards = ah;}
   public void setLeader(Actor a){leader = a;}
   public void setFollowDistance(int fd){followDistance = fd;}
   public void setTempTeam(Team t){tempTeam = t;}
   public void setTempLeader(Actor l){tempLeader = l;}


   public BasicAI(Actor s)
   {
      self = s;
      previousTarget = null;
      previousAction = null;
      lastActorTargeted = null;
      team = Team.VILLAIN;
      tempTeam = null;
      pathfindingRadius = PATHFINDING_MAP_RADIUS;
      usesDoors = true;
      avoidsHazards = true;
      leader = null;
      tempLeader = null;
      followDistance = 4;
      clearPlan();
   }
   

   public Team getTeam()
   {
      if(tempTeam != null)
         return tempTeam;
      return team;
   }
   
   public Actor getLeader()
   {
      if(tempLeader != null)
         return tempLeader;
      return leader;
   }
   
   // override in child classes
   public void plan()
   {
      setPendingAction(ActorAction.DELAY);
      setPendingTarget(self.getMapLoc());
   }
   
   public boolean isLeaderOf(Actor that)
   {
      return that.getAI().isFollowerOf(self);
   }
   
   public boolean isFollowerOf(Actor that)
   {
      return that == getLeader();
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
         case GRAB :             doGrab(); break;
         case INVENTORY_ACTION : doManipulateInventory(); break;
      }
      if(pendingAction.isGadgetAction())
         doGadgetAction(pendingAction.getGadgetIndex());
      shiftPendingToPrevious();
      clearPlan();
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
      // actor
      else if(GameEngine.isActorAt(pendingTarget))
      {
         Actor that = GameEngine.getActorAt(pendingTarget);
         // enemy, unarmed attack
         if(isHostile(that))
         {
            setPendingAction(ActorAction.UNARMED_ATTACK);
         }
         // push follower
         else if(that.getAI().isFollowerOf(self) && self.canStepIgnoringActors(pendingTarget))
         {
            setPendingAction(ActorAction.STEP);
         }
         // friendly, no action
         else
         {
            MessagePanel.addMessage("There's a dude there.");
            clearPlan();
         }
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
      // melee a crate
      else if(GameEngine.getZoneMap().getTile(pendingTarget) instanceof Crate)
      {
         setPendingAction(ActorAction.UNARMED_ATTACK);
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
   
   protected Coord getStepTowards(Coord target){return getStepTowards(target, getAvoidsHazards());}
   protected Coord getStepTowards(Coord target, boolean noHazards)
   {
      if(EngineTools.isAdjacent(target, self.getMapLoc()))
      {
         if(GameEngine.isActorAt(target))
            return null;
         return target;
      }
      AStar pathing = new AStar();
      // get passability map
      boolean[][] passMap = GameEngine.getZoneMap().getPassMap(self, noHazards);
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
         // if no non-hazardous path, try with hazards
         if(noHazards)
            return getStepTowards(target, false);
         return null;
      }
      Coord stepLoc = path.elementAt(0);
      stepLoc.add(passMapInset);
      return stepLoc;
   }
   
   protected Coord getStepAwayFrom(Actor a){return getStepAwayFrom(a.getMapLoc());}
   protected Coord getStepAwayFrom(Coord target)
   {
      Coord bestLoc = null;
      int highestDist = 0;
      for(int x = -1; x < 2; x++)
      for(int y = -1; y < 2; y++)
      {
         if(x == 0 && y == 0)
            continue;
         Coord prospect = new Coord(x, y);
         prospect.add(self.getMapLoc());
         if(self.canStep(prospect) && !(getAvoidsHazards() && self.isHazard(prospect)))
         {
            int dist = EngineTools.getDistanceTo(prospect, target);
            if(dist > highestDist)
            {
               bestLoc = prospect;
               highestDist = dist;
            }
         }
      }
      return bestLoc;
   }
   
   // find nearest tile with no line of effect to any visible enemies
   protected Coord getNearestSafeTile()
   {
      Vector<Actor> enemyList = GameEngine.getVisibleEnemies(self);
      boolean[][] passMap = GameEngine.getZoneMap().getPassMap(self, getAvoidsHazards());
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
      boolean[][] passMap = GameEngine.getZoneMap().getPassMap(self, getAvoidsHazards());
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
      // push follower on tile
      Actor that = GameEngine.getActorAt(pendingTarget);
      if(that != null && that.getAI().isFollowerOf(self))
      {
         that.getAI().setPendingAction(ActorAction.STEP);
         that.getAI().setPendingTarget(self.getMapLoc());
         that.getAI().doStep();
         that.getAI().clearPlan();
      }
      else if(that != null && !that.getAI().isLeaderOf(self))
      {
         System.out.println("Actor " + self.getName() + " stepping into occupied tile " + pendingTarget);
      }
      
      // slide on ice
      if(GameEngine.getZoneMap().getTile(pendingTarget) instanceof Ice && !self.isFlying())
      {
         Coord newTarget = new Coord(pendingTarget.x - self.getMapLoc().x, pendingTarget.y - self.getMapLoc().y);
         newTarget.add(pendingTarget);
         if(self.canStep(newTarget))
            pendingTarget = newTarget;
      }
      if(GameEngine.playerCanSee(pendingTarget) || GameEngine.playerCanSee(self))
      {
         MovementScript ms = MovementScriptFactory.getWalkingScript(self, pendingTarget);
         GameEngine.addLocking(ms, self);
         self.setMapLoc(pendingTarget);
      }
      else
      {
         self.setAllLocs(pendingTarget);
      }
      // movement is slowed on some tiles
      if(!self.isFlying() && GameEngine.getZoneMap().getTile(pendingTarget).slowsMovement())
         self.discharge(self.getMoveSpeed().slower().timeCost);
      else
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
   
   public void doGrab(){doGrab(true);}
   public void doGrab(boolean spendCharge)
   {
      boolean pickupF = false;
      // nothing at location
      if(!GameEngine.getZoneMap().isGearAt(self.getMapLoc()))
      {
         MessagePanel.addMessage("Nothing to pick up here.");
      }
      // credits
      else if(GameEngine.getZoneMap().getGearAt(self.getMapLoc()) instanceof Credits)
      {
         pickupF = true;
      }
      // anything else
      else
      {
         // inventory is full
         if(self.getInventory().isFull())
         {
            MessagePanel.addMessage("Your inventory is full.");
         }
         else
         {
            pickupF = true;
         }
      }
      if(pickupF)
      {
         GearObj gear = GameEngine.getZoneMap().getGearAt(self.getMapLoc());
         GameEngine.getZoneMap().setGearAt(self.getMapLoc(), null);
         // must be before pickup, as adding credits to inventory sets original amt to 0
         if(self instanceof Player)
         {
            String particle = "a ";
            if(gear instanceof Credits)
               particle = "";
            MessagePanel.addMessage("You pick up " + particle + gear.getName() + ".");
         }
         if(GameEngine.playerCanSee(self.getMapLoc()))
         {
            VisualEffectFactory.addPickupIndicator(self, gear);
         }
         self.getInventory().add(gear);
         if(spendCharge)
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
            MessagePanel.addMessage(self.getName() + " uses a " + gadget.getName() + ".");
         }
      }
   }
   
   // the actual mechanics for this are handled in the Player class
   public void doManipulateInventory()
   {
      self.discharge(self.getInteractSpeed().timeCost);
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