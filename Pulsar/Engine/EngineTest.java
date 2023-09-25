package Pulsar.Engine;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Pulsar.Actor.*;
import Pulsar.Gear.*;


public class EngineTest implements EngineConstants
{


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
   
   public Actor getTestActorWithArmor()
   {
      Armor arm = new Armor();
      arm.setDamageReduction(2);
      Actor act = getTestActor();
      act.setArmor(arm);
      return act;
   }

   
   @Test public void combatTest() 
   {
      Actor attacker = getTestActor();
      Actor defender = getTestActor();
      
      Combat.resolveAttackAgainstActor(attacker, defender, attacker.getWeapon());
      Assert.assertEquals("Damage applied as expected", 17, defender.getCurHealth());
   }

   
   @Test public void combatTestArmor() 
   {
      Actor attacker = getTestActor();
      Actor defender = getTestActorWithArmor();
      
      Combat.resolveAttackAgainstActor(attacker, defender, attacker.getWeapon());
      Assert.assertEquals("Armor stops damage that gets through shields", 19, defender.getCurHealth());
      defender.getArmor().setDamageReduction(5);
      Combat.resolveAttackAgainstActor(attacker, defender, attacker.getWeapon());
      Assert.assertEquals("Minimum non-shielded damage is 1 per hit", 16, defender.getCurHealth());
   }
   
   @Test public void testGetButtonActionFromString()
   {
      Assert.assertEquals("Getting TOGGLE by string works", ButtonAction.TOGGLE, ButtonAction.getFromString("Toggle"));
      Assert.assertEquals("Getting FLOOD_WATER by string works", ButtonAction.FLOOD_WATER, ButtonAction.getFromString("FLOOD_WATER"));
      Assert.assertEquals("Getting FLOOD_ACID by string works", ButtonAction.FLOOD_ACID, ButtonAction.getFromString("flood_acid"));
      Assert.assertEquals("Getting ButtonAction with bad string returns null", null, ButtonAction.getFromString("Blarg"));
   }
}
