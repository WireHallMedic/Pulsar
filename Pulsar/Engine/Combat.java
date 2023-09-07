package Pulsar.Engine;

import Pulsar.GUI.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import WidlerSuite.*;
import java.util.*;
import java.awt.*;

public class Combat
{
   public static MainGameFGPanel getMapPanel(){return GameEngine.getMapPanel();}
   
   public static void resolveAttack(Actor attacker, Coord target)
   {
      target = getActualTarget(attacker.getMapLoc(), target);
      // attacker animation
      if(GameEngine.playerCanSee(attacker))
      {
         MovementScript msa = MovementScriptFactory.getShootScript(attacker, target);
         getMapPanel().addLocking(msa);
      }
      
      Actor defender = GameEngine.getActorAt(target);
      if(defender != null)
      {
         resolveAttackAgainstActor(attacker, defender);
      }
      else
      {
         if(GameEngine.playerCanSee(target))
         {
            Color c = new Color(GameEngine.getZoneMap().getTile(target).getFGColor());
            VisualEffectFactory.createRicochette(target, attacker.getMapLoc(), c);
         }
      }
      
      attacker.getWeapon().fire();
   }
   
   private static Coord getActualTarget(Coord origin, Coord initialTarget)
   {
      if(origin.equals(initialTarget))
         return origin;
      Vector<Coord> lineOfFire = StraightLine.findLine(origin, initialTarget, StraightLine.REMOVE_ORIGIN);
      for(Coord c : lineOfFire)
      {
         if(GameEngine.blocksShooting(c))
            return c;
      }
      return initialTarget;
   }
   
   private static void resolveAttackAgainstActor(Actor attacker, Actor defender)
   {
      boolean hasShieldBefore = defender.shieldIsUp();
      int startingHealth = defender.getCurHealth();
      int[] damageArray = rollDamage(attacker.getWeapon());
      for(int i = 0; i < damageArray.length; i++)
      {
         defender.applyDamage(damageArray[i], attacker.getWeapon().getDamageType());
      }
      
      if(GameEngine.playerCanSee(defender))
      {
         // shield flicker
         if(hasShieldBefore)
         {
            VisualEffectFactory.createShieldFlicker(defender);
            // shield break
            if(!defender.shieldIsUp())
            {
               VisualEffectFactory.createShieldBreak(defender);
            }
         }
         // blood spray
         if(defender.getCurHealth() < startingHealth)
         {
            VisualEffectFactory.createSpray(defender.getMapLoc(), attacker.getMapLoc(), defender.getBloodColor());
         }
         // target impact
         MovementScript msd = MovementScriptFactory.getImpactScript(defender, attacker.getMapLoc(), 0);
         getMapPanel().addLocking(msd);
      }
   }
   
   public static int[] rollDamage(Weapon weapon)
   {
      int[] damage = new int[weapon.getAttacks()];
      for(int i = 0; i < damage.length; i++)
      {
         damage[i] = weapon.getBaseDamage();
         damage[i] += (int)(GameEngine.random() * weapon.getVariableDamage());
      }
      return damage;
   }
}