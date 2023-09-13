package Pulsar.Engine;

import Pulsar.GUI.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import WidlerSuite.*;
import java.util.*;
import java.awt.*;

public class Combat implements GUIConstants, GearConstants
{
   public static MainGameFGPanel getMapPanel(){return GameEngine.getMapPanel();}
   
   public static void resolveAttack(Actor attacker, Coord initialTarget, Weapon weapon)
   {
      Vector<Coord> targetList = getActualTargets(attacker.getMapLoc(), initialTarget, weapon);
      Vector<Actor> targetActorList = new Vector<Actor>();
      // attacker animation
      if(GameEngine.playerCanSee(attacker))
      {
         MovementScript msa = MovementScriptFactory.getAttackAnimation(attacker, initialTarget);
         getMapPanel().addLocking(msa);
      }
      for(Coord target : targetList)
      {
         Actor defender = GameEngine.getActorAt(target);
         if(defender != null)
         {
            resolveAttackAgainstActor(attacker, defender, weapon);
            targetActorList.add(defender);
         }
         else
         {
            if(GameEngine.playerCanSee(target))
            {
               boolean shouldGenerateRicochette = true;
               // melee attacks only generate ricochettes on !highPassable
               if(weapon.hasWeaponTag(GearConstants.WeaponTag.MELEE) &&
                  GameEngine.getZoneMap().getTile(target).isHighPassable())
                  shouldGenerateRicochette = false;
               // blasts don't generate ricochettes
               if(weapon.hasWeaponTag(GearConstants.WeaponTag.BLAST))
                  shouldGenerateRicochette = false;
               if(shouldGenerateRicochette)
               {
                  int delay = weapon.hasWeaponTag(GearConstants.WeaponTag.MELEE) ? MELEE_ATTACK_HIT_DELAY : 0;
                  Color c = new Color(GameEngine.getZoneMap().getTile(target).getFGColor());
                  VisualEffectFactory.createRicochette(target, attacker.getMapLoc(), c, delay);
               }
            }
            if(tileDestructionCheck(target, weapon))
            {
               GameEngine.getZoneMap().breakTile(target);
            }
         }
      }
      // blasts generate explosions
      if(weapon.hasWeaponTag(GearConstants.WeaponTag.BLAST))
      {
         VisualEffectFactory.createExplosion(targetList.elementAt(4));
      }
      // combat messages
      if(targetActorList.size() > 0 && involvesPlayer(attacker, targetActorList))
      {
         addCombatMessage(attacker, targetActorList, weapon);
      }
      weapon.discharge();
   }
   
   public static void nonWeaponExplosion(int x, int y){nonWeaponExplosion(new Coord(x, y));}
   public static void nonWeaponExplosion(Coord c)
   {
      Vector<Coord> targetList = getActualTargets(c, c, WeaponFactory.EXPLODING_BARREL);
      for(Coord target : targetList)
      {
         Actor defender = GameEngine.getActorAt(target);
         if(defender != null)
         {
            resolveAttackAgainstActor(c, defender, WeaponFactory.EXPLODING_BARREL);
         }
         if(tileDestructionCheck(target, WeaponFactory.EXPLODING_BARREL))
         {
            GameEngine.getZoneMap().breakTile(target);
         }
      }
      VisualEffectFactory.createExplosion(c);
   }
   
   private static Vector<Coord> getActualTargets(Coord origin, Coord initialTarget, Weapon weapon)
   {
      Vector<Coord> targetList = new Vector<Coord>();
      // self
      if(origin.equals(initialTarget))
      {
         targetList.add(origin);
         if(!weapon.hasWeaponTag(GearConstants.WeaponTag.BLAST))
            return targetList;
      }
      // shotgun targets
      if(weapon.hasWeaponTag(GearConstants.WeaponTag.SPREAD))
      {
         return getShotgunTargets(origin, initialTarget);
      }
      // blast
      if(weapon.hasWeaponTag(GearConstants.WeaponTag.BLAST))
      {
         return getBlastTargets(origin, initialTarget);
      }
      // melee
      if(weapon.hasWeaponTag(GearConstants.WeaponTag.MELEE))
      {
         targetList.add(EngineTools.getTileTowards(origin, initialTarget));
         return targetList;
      }
      // something in the way for a line
      Vector<Coord> lineOfFire = StraightLine.findLine(origin, initialTarget, StraightLine.REMOVE_ORIGIN);
      for(Coord c : lineOfFire)
      {
         if(GameEngine.blocksShooting(c))
         {
            targetList.add(c);
            return targetList;
         }
      }
      // all clear single target
      targetList.add(initialTarget);
      return targetList;
   }
   
   public static void resolveAttackAgainstActor(Actor attacker, Actor defender, Weapon weapon){resolveAttackAgainstActor(attacker.getMapLoc(), defender, weapon);}
   public static void resolveAttackAgainstActor(Coord origin, Actor defender, Weapon weapon)
   {
      boolean hasShieldBefore = defender.shieldIsUp();
      int startingHealth = defender.getCurHealth();
      int[] damageArray = rollDamage(weapon);
      int delay = weapon.isMelee() ? MELEE_ATTACK_HIT_DELAY : 0;
      
      for(int i = 0; i < damageArray.length; i++)
      {
         defender.applyDamage(damageArray[i], weapon.getDamageType(), false);
      }
      
      if(weapon.hasWeaponTag(WeaponTag.KNOCKBACK))
         doKnockback(defender, origin, 1);
      
      if(GameEngine.playerCanSee(defender))
      {
         // target impact
         MovementScript msd = MovementScriptFactory.getImpactScript(defender, origin, delay);
         getMapPanel().addLocking(msd);
         
         // shield flicker
         if(hasShieldBefore)
         {
            VisualEffectFactory.createShieldFlicker(defender, delay);
            // shield break
            if(!defender.shieldIsUp())
            {
               VisualEffectFactory.createShieldBreak(defender, delay);
            }
         }
         // blood spray and blood drip
         if(defender.getCurHealth() < startingHealth)
         {
            VisualEffectFactory.createSpray(defender.getMapLoc(), origin, defender.getBloodColor(), delay);
            GameEngine.getZoneMap().getTile(defender.getMapLoc()).setFGColor(defender.getBloodColor().getRGB());
         }
      }
   }
   
   public static int[] rollDamage(Weapon weapon)
   {
      int[] damage = new int[weapon.getAttacks()];
      for(int i = 0; i < damage.length; i++)
      {
         damage[i] = weapon.getBaseDamage();
         damage[i] += (int)(GameEngine.random() * (weapon.getVariableDamage() + 1));
      }
      return damage;
   }
   
   public static boolean tileDestructionCheck(Coord target, Weapon weapon)
   {
      double breakChance = GameEngine.getZoneMap().getTile(target).getDurability().getBreakChance(weapon.isHeavy());
      return GameEngine.random() <= breakChance;
   }
   
   // shotguns target and are stopped by: high impassable
   // shotguns target and are not stopped by: actors, low impassable
   public static Vector<Coord> getShotgunTargets(Coord origin, Coord target)
   {
      Vector<Coord> finalTargetList = new Vector<Coord>();
      if(origin.equals(target))
      {
         finalTargetList.add(origin);
         return finalTargetList;
      }
      Vector<Coord> targetList = EngineTools.getShotgunSprayLine(origin, target);
      for(Coord prospect : targetList)
      {
         Vector<Coord> line = StraightLine.findLine(origin, prospect, StraightLine.REMOVE_ORIGIN);
         for(int i = 0; i < line.size(); i++)
         {
            if(EngineTools.getDistanceTo(origin, line.elementAt(i)) <= SHOTGUN_MAX_RANGE && // in range
               (GameEngine.blocksShooting(line.elementAt(i)) || // tile blocks line of effect
               !GameEngine.getZoneMap().getTile(line.elementAt(i)).isLowPassable())) // is not low passable
            {
               // ignore if duplicate
               boolean addF = true;
               for(int j = 0; j < finalTargetList.size(); j++)
               {
                  if(finalTargetList.elementAt(j).equals(line.elementAt(i)))
                  {
                     addF = false;
                     break;
                  }
               }
               // otherwise add
               if(addF)
                  finalTargetList.add(line.elementAt(i));
               // don't search farther if hits a wall or max range
               if(!GameEngine.getZoneMap().getTile(line.elementAt(i)).isHighPassable())
                  break;
            }
         }
      }
      return finalTargetList;
   }
   
   public static Vector<Coord> getBlastTargets(Coord origin, Coord target)
   {
      Coord detonationLoc = GameEngine.getDetonationLoc(origin, target);
      Vector<Coord> blastList = new Vector<Coord>();
      for(int x = -1; x < 2; x++)
      for(int y = -1; y < 2; y++)
      {
         blastList.add(new Coord(detonationLoc.x +x, detonationLoc.y + y));
      }
      return blastList;
   }
   
   public static boolean involvesPlayer(Actor attacker, Vector<Actor> targetActorList)
   {
      if(attacker == GameEngine.getPlayer())
         return true;
      for(Actor a : targetActorList)
         if(a == GameEngine.getPlayer())
            return true;
      return false;
   }
   
   public static void addCombatMessage(Actor attacker, Vector<Actor> targetActorList, Weapon weapon)
   {
      int deathsCaused = 0;
      for(Actor a : targetActorList)
      {
         if(a.isDead())
         {
            deathsCaused++;
         }
      }
      String targetString = "multiple targets";
      if(targetActorList.size() == 1)
      {
         targetString = targetActorList.elementAt(0).getName();
      }
      if(deathsCaused > 0)
      {
         if(targetActorList.size() == 1)
            targetString += ", killing them";
         else
            targetString += ", killing " + deathsCaused;
      }
      MessagePanel.addMessage(attacker.getName() + " " + weapon.getHitDescriptor() + " " + targetString + "!");
   }
   
   public static void doKnockback(Actor actor, Coord source, int knockback)
   {
      if(knockback <= 0)
         return;
      Vect kbVect = new Vect(source, actor.getMapLoc());
      kbVect.magnitude = (double)knockback;
      Coord targetTile = new Coord(kbVect);
      targetTile.x += actor.getMapLoc().x;
      targetTile.y += actor.getMapLoc().y;
      Vector<Coord> knockbackPath = StraightLine.findLine(actor.getMapLoc(), targetTile, StraightLine.REMOVE_ORIGIN);
      targetTile = null;
      for(Coord tile : knockbackPath)
      {
         if(!actor.canStep(tile))
            break;
         targetTile = tile;
      }
      if(targetTile == null)
         return;
      MovementScript ms = MovementScriptFactory.getKnockbackScript(actor, targetTile);
      GameEngine.getMapPanel().addLocking(ms);
      actor.setMapLoc(targetTile);
   }
}