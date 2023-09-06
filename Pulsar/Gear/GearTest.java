package Pulsar.Gear;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class GearTest implements GearConstants
{
   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }
   
   public Shield getTestShield()
   {
      Shield shield = new Shield();
   	shield.setMaxCharge(10);
   	shield.setChargeDelay(10);
   	shield.setChargeRate(1);
      shield.fullyCharge();
      return shield;
   }
   
   public Weapon getTestWeapon()
   {
      Weapon weapon = new Weapon();
	   weapon.setMaxCharge(20);
	   weapon.setChargeCost(5);
	   weapon.setChargeRate(1);
      weapon.fullyCharge();
      return weapon;
   }


   
   @Test public void shieldTest() 
   {
      Shield shield = getTestShield();
      int remainder = shield.applyDamage(6, DamageType.KINETIC);
      Assert.assertEquals("remainder for Kinetic damage, shield holds", remainder, 0);
      Assert.assertEquals("curCharge for Kinetic damage, shield holds", shield.getCurCharge(), 4);
      
      shield = getTestShield();
      remainder = shield.applyDamage(12, DamageType.KINETIC);
      Assert.assertEquals("remainder for Kinetic damage, shield breaks", remainder, 2);
      Assert.assertEquals("curCharge for Kinetic damage, shield breaks", shield.getCurCharge(), 0);
      
      shield = getTestShield();
      remainder = shield.applyDamage(3, DamageType.ELECTRO);
      Assert.assertEquals("remainder for Electro damage, shield holds", remainder, 0);
      Assert.assertEquals("curCharge for Electro damage, shield holds", shield.getCurCharge(), 4);
      
      shield = getTestShield();
      remainder = shield.applyDamage(6, DamageType.ELECTRO);
      Assert.assertEquals("remainder for Electro damage, shield breaks", remainder, 2);
      Assert.assertEquals("curCharge for Electro damage, shield breaks", shield.getCurCharge(), 0);
      
      for(int i = 0; i < 5; i++)
         shield.charge();
      Assert.assertEquals("Recharging, before recharge begins", shield.getCurCharge(), 0);
      for(int i = 0; i < 6; i++)
         shield.charge();
      Assert.assertEquals("Recharging, mid recharge", shield.getCurCharge(), 1);
      for(int i = 0; i < 10; i++)
         shield.charge();
      Assert.assertEquals("Recharging, post recharge", shield.getCurCharge(), 10);
   }

   
   @Test public void creditTest() 
   {
      Credits creditA = new Credits(10);
      Credits creditB = new Credits(5);
      creditA.add(creditB);
      Assert.assertEquals("Calling object gets value", creditA.getAmount(), 15);
      Assert.assertEquals("Argument object loses value", creditB.getAmount(), 0);
   }

   
   @Test public void weaponTest() 
   {
      Weapon weapon = getTestWeapon();
      weapon.fire();
      Assert.assertEquals("Firing uses charge", weapon.getCurCharge(), 15);
      weapon.charge();
      Assert.assertEquals("Recharges",  weapon.getCurCharge(), 16);
      for(int i = 0; i < 5; i++)
         weapon.charge();
      Assert.assertEquals("Does not overcharge",  weapon.getCurCharge(), 20);
      weapon.setCurCharge(0);
      boolean fired = weapon.fire();
      Assert.assertFalse("Does not fire when discharged",  fired);
      
   }
}
