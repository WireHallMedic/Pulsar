package Pulsar.Engine;

import Pulsar.GUI.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import WidlerSuite.*;

public class Combat
{
   public static MainGameFGPanel getMapPanel(){return GameEngine.getMapPanel();}
   
   public static void resolveAttack(Actor attacker, Coord target)
   {
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
      
      attacker.getWeapon().fire();
   }
   
   public static void resolveAttackAgainstActor(Actor attacker, Actor defender)
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