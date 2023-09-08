package Pulsar.Engine;

import Pulsar.GUI.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import WidlerSuite.*;
import java.util.*;
import java.awt.*;

public class Combat implements GUIConstants
{
   public static MainGameFGPanel getMapPanel(){return GameEngine.getMapPanel();}
   
   public static void resolveAttack(Actor attacker, Coord target)
   {
      target = getActualTarget(attacker.getMapLoc(), target);
      // attacker animation
      if(GameEngine.playerCanSee(attacker))
      {
         MovementScript msa = MovementScriptFactory.getAttackAnimation(attacker, target);
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
      
      attacker.getWeapon().discharge();
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
      int delay = attacker.getWeapon().isMelee() ? MELEE_ATTACK_HIT_DELAY : 0;
      
      for(int i = 0; i < damageArray.length; i++)
      {
         defender.applyDamage(damageArray[i], attacker.getWeapon().getDamageType(), false);
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
         damage[i] += (int)(GameEngine.random() * weapon.getVariableDamage());
      }
      return damage;
   }
}