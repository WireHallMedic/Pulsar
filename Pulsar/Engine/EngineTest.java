package Pulsar.Engine;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import WidlerSuite.*;


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
      assertEquals("Damage applied as expected", 17, defender.getCurHealth());
   }

   
   @Test public void combatTestArmor() 
   {
      Actor attacker = getTestActor();
      Actor defender = getTestActorWithArmor();
      
      Combat.resolveAttackAgainstActor(attacker, defender, attacker.getWeapon());
      assertEquals("Armor stops damage that gets through shields", 19, defender.getCurHealth());
      defender.getArmor().setDamageReduction(5);
      Combat.resolveAttackAgainstActor(attacker, defender, attacker.getWeapon());
      assertEquals("Minimum non-shielded damage is 1 per hit", 16, defender.getCurHealth());
   }
   
   @Test public void testButtonActionParsing()
   {
      assertEquals("Getting TOGGLE by string works", ButtonAction.TOGGLE, ButtonAction.parse("Toggle"));
      assertEquals("Getting FLOOD_WATER by string works", ButtonAction.FLOOD_WATER, ButtonAction.parse("FLOOD_WATER"));
      assertEquals("Getting FLOOD_ACID by string works", ButtonAction.FLOOD_ACID, ButtonAction.parse("flood_acid"));
      assertEquals("Getting ButtonAction with bad string returns null", null, ButtonAction.parse("Blarg"));
   }
   
   @Test public void testCoordParsing()
   {
      Coord exemplar = new Coord(1, -5);
      assertTrue("Generating brackets, valid numbers", exemplar.equals(EngineTools.parseCoord("[1, -5]")));
      assertTrue("Generating no brackets, valid numbers", exemplar.equals(EngineTools.parseCoord("1, -5")));
      assertThrows(java.lang.NumberFormatException.class, () -> {EngineTools.parseCoord("1.3, 4");});
      assertThrows(java.lang.ArrayIndexOutOfBoundsException.class, () -> {EngineTools.parseCoord("pants");});
   }
   
   @Test public void testButtonTriggerParsing()
   {
      ButtonTrigger trigger = new ButtonTrigger();
      Coord exemplar = new Coord(1, 3);
      assertEquals("Initial buttonAction is null.", null, trigger.getButtonAction());
      assertEquals("Initial targetList is empty.", 0, trigger.getTargetList().size());
      assertEquals("Initial intensity is 1.", 1, trigger.getIntensity());
      assertEquals("Initial callerReps is 1.", 1, trigger.getCallerReps());
      
      trigger.parse("BUTTON_ACTION:toggle");
      assertEquals("Setting buttonAction works", ButtonAction.TOGGLE, trigger.getButtonAction());
      
      trigger.parse("BUTTON_target:1,3");
      assertTrue("Adding target works.", trigger.getTargetList().elementAt(0).equals(exemplar));
      
      exemplar = new Coord(3, 1);
      trigger.parse("BUTTON_TARGET : 3 , 1 ");
      assertTrue("Adding multiple targets works.", trigger.getTargetList().elementAt(1).equals(exemplar));
      
      trigger.parse("  BUTTON_INTENSITY : 5");
      assertEquals("Setting intensity works.", 5, trigger.getIntensity());
      
      trigger.parse("BUTTON_REPS : 5 ");
      assertEquals("Setting reps works.", 5, trigger.getCallerReps());
   }
}
