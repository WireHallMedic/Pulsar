package Pulsar.Engine;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Pulsar.Actor.*;
import Pulsar.Gear.*;


public class EngineTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }
   
   
   public Actor getTestActor()
   {
      Shield shield = new Shield();
      shield.setMaxCharge(12);
      shield.fullyCharge();
      Weapon weapon = new Weapon();
      weapon.setBaseDamage(5);
      weapon.setVariableDamage(0);
      weapon.setAttacks(3);
      weapon.setDamageType(GearConstants.DamageType.KINETIC);
      Actor actor = new Actor('t');
      actor.setWeapon(weapon);
      actor.setShield(shield);
      actor.setMaxHealth(20);
      actor.fullyHeal();
      return actor;
   }


   
   @Test public void combatTest() {
      Actor attacker = getTestActor();
      Actor defender = getTestActor();
      
      Combat.resolveAttackAgainstActor(attacker, defender);
      Assert.assertEquals("Damage applied as expected", defender.getCurHealth(), 17);
   }
}
