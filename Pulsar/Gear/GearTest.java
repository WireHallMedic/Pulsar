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
   
   public Shield getDefaultShield()
   {
      Shield shield = new Shield();
   	shield.setMaxCharge(10);
   	shield.setChargeDelay(10);
   	shield.setChargeRate(1);
      shield.fullyCharge();
      return shield;
   }


   /** A test that always fails. **/
   @Test public void shieldTest() 
   {
      Shield shield = getDefaultShield();
      int remainder = shield.applyDamage(6, DamageType.KINETIC);
      Assert.assertEquals("remainder for Kinetic damage, shield holds", remainder, 0);
      Assert.assertEquals("curCharge for Kinetic damage, shield holds", shield.getCurCharge(), 4);
      
      shield = getDefaultShield();
      remainder = shield.applyDamage(12, DamageType.KINETIC);
      Assert.assertEquals("remainder for Kinetic damage, shield breaks", remainder, 2);
      Assert.assertEquals("curCharge for Kinetic damage, shield breaks", shield.getCurCharge(), 0);
      
      shield = getDefaultShield();
      remainder = shield.applyDamage(3, DamageType.ELECTRO);
      Assert.assertEquals("remainder for Electro damage, shield holds", remainder, 0);
      Assert.assertEquals("curCharge for Electro damage, shield holds", shield.getCurCharge(), 4);
      
      shield = getDefaultShield();
      remainder = shield.applyDamage(6, DamageType.ELECTRO);
      Assert.assertEquals("remainder for Electro damage, shield breaks", remainder, 2);
      Assert.assertEquals("curCharge for Electro damage, shield breaks", shield.getCurCharge(), 0);
      
      for(int i = 0; i < 5; i++)
         shield.increment();
      Assert.assertEquals("Recharging, before recharge begins", shield.getCurCharge(), 0);
      for(int i = 0; i < 6; i++)
         shield.increment();
      Assert.assertEquals("Recharging, mid recharge", shield.getCurCharge(), 1);
      for(int i = 0; i < 10; i++)
         shield.increment();
      Assert.assertEquals("Recharging, post recharge", shield.getCurCharge(), 10);
   }
}
