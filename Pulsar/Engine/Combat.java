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
      if(targetActorList.size() > 0 && involvesPlayer(attacker, targetActorList))
      {
         addCombatMessage(attacker, targetActorList, weapon);
      }
      weapon.discharge();
   }
   
   private static Vector<Coord> getActualTargets(Coord origin, Coord initialTarget, Weapon weapon)
   {
      Vector<Coord> targetList = new Vector<Coord>();
      // self
      if(origin.equals(initialTarget))
      {
         targetList.add(origin);
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
   
   public static void resolveAttackAgainstActor(Actor attacker, Actor defender, Weapon weapon)
   {
      boolean hasShieldBefore = defender.shieldIsUp();
      int startingHealth = defender.getCurHealth();
      int[] damageArray = rollDamage(weapon);
      int delay = weapon.isMelee() ? MELEE_ATTACK_HIT_DELAY : 0;
      
      for(int i = 0; i < damageArray.length; i++)
      {
         defender.applyDamage(damageArray[i], weapon.getDamageType(), false);
      }
      
      if(GameEngine.playerCanSee(defender))
      {
         // target impact
         MovementScript msd = MovementScriptFactory.getImpactScript(defender, attacker.getMapLoc(), delay);
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
         // blood spray
         if(defender.getCurHealth() < startingHealth)
         {
            VisualEffectFactory.createSpray(defender.getMapLoc(), attacker.getMapLoc(), defender.getBloodColor(), delay);
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
         // target all actors plus first blocking tile or last tile if no blocking
         for(int i = 0; i < line.size(); i++)
         {
            if(i == line.size() - 1 || // last tile in line
               GameEngine.blocksShooting(line.elementAt(i)) || // tile blocks line of effect
               EngineTools.getDistanceTo(origin, line.elementAt(i)) >= SHOTGUN_MAX_RANGE) // tile is max range
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
               if(!GameEngine.getZoneMap().getTile(line.elementAt(i)).isHighPassable() || EngineTools.getDistanceTo(origin, line.elementAt(i)) >= SHOTGUN_MAX_RANGE)
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
      String targetString = "multiple targets";
      if(targetActorList.size() == 1)
         targetString = targetActorList.elementAt(0).getName();
      MessagePanel.addMessage(attacker.getName() + " " + weapon.getHitDescriptor() + " " + targetString + "!");
   }
}