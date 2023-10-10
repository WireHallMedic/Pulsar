package Pulsar.Actor;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Pulsar.Gear.*;


public class ActorTest 
{

   @Test public void actionSpeedTest() 
   {
      Assert.assertEquals("Slower than slow is slow", ActionSpeed.SLOW, ActionSpeed.SLOW.slower());
      Assert.assertEquals("Slower than standard is slow", ActionSpeed.SLOW, ActionSpeed.STANDARD.slower());
      Assert.assertEquals("Slower than fast is standard", ActionSpeed.STANDARD, ActionSpeed.FAST.slower());
      Assert.assertEquals("Slower than instantaneous is instantaneous", ActionSpeed.INSTANTANEOUS, ActionSpeed.INSTANTANEOUS.slower());
      
      Assert.assertEquals("Faster than slow is standard", ActionSpeed.STANDARD, ActionSpeed.SLOW.faster());
      Assert.assertEquals("Faster than standard is fast", ActionSpeed.FAST, ActionSpeed.STANDARD.faster());
      Assert.assertEquals("Faster than fast is fast", ActionSpeed.FAST, ActionSpeed.FAST.faster());
      Assert.assertEquals("Faster than instantaneous is instantaneous", ActionSpeed.INSTANTANEOUS, ActionSpeed.INSTANTANEOUS.faster());
   }
   
   @Test public void inventoryTest()
   {
      Actor a = ActorFactory.getDrone();
      Inventory inv = a.getInventory();
      inv.setMaxSize(2);
      assertEquals("Starting inventory has no credits.", 0, inv.getCredits().getAmount());
      assertEquals("Starting inventory has no list items.", 0, inv.size());
      assertFalse("Empty inventory not full.", inv.isFull());
      assertTrue("Empty inventory has expected string.", "0 Credits".equals(inv.toString()));
      
      inv.add(new Credits(10));
      assertEquals("Adding credits adds to inventory credit.", 10, inv.getCredits().getAmount());
      assertEquals("Adding credits does not add list item.", 0, inv.size());
      assertTrue("Expected string after adding credits.", "10 Credits".equals(inv.toString()));
      
      inv.add(WeaponFactory.getPistol());
      assertEquals("Adding list item increases carried items.", 1, inv.size());
      assertFalse("Inventory below max size is not full.", inv.isFull());
      assertTrue("Expected string after adding pistol.", "10 Credits, Pistol".equals(inv.toString()));
      
      GearObj carbine = WeaponFactory.getCarbine();
      inv.add(carbine);
      assertEquals("Adding list item increases carried items.", 2, inv.size());
      assertTrue("Inventory at max size is full.", inv.isFull());
      assertFalse("Inventory at max size is not over full.", inv.isOverFull());
      assertTrue("Expected string after adding carbine.", "10 Credits, Pistol, Carbine".equals(inv.toString()));
      
      assertEquals("Taking item returns item.", carbine, inv.take(1));
      assertEquals("Taking item reduces size.", 1, inv.size());
      assertTrue("Expected string after removing carbine.", "10 Credits, Pistol".equals(inv.toString()));
      
      inv.add(WeaponFactory.getPistol());
      inv.add(WeaponFactory.getPistol());
      assertEquals("Adding list item increases carried items, even above size limit.", 3, inv.size());
      assertTrue("Inventory above max size is full.", inv.isFull());
      assertTrue("Inventory above max size is over full.", inv.isOverFull());
   }
   
   @Test public void modifiedInventoryTest()
   {
      Actor a = ActorFactory.getDrone();
      Inventory inv = a.getInventory();
      inv.setMaxSize(2);
      a.setArmor(ArmorFactory.getEngineerArmor());
      assertFalse("Empty inventory not full.", inv.isFull());
      assertTrue("Empty inventory has expected string.", "0 Credits".equals(inv.toString()));
      assertEquals("Max size includes armor mod", 4, inv.getMaxSize());
      
      inv.add(WeaponFactory.getPistol());
      assertFalse("Inventory below base max size is not full.", inv.isFull());
      assertFalse("Inventory below base max size is not over full.", inv.isOverFull());
      
      inv.add(WeaponFactory.getPistol());
      assertFalse("Inventory at max base size but below adjusted max is not full.", inv.isFull());
      assertFalse("Inventory at max base size but below adjusted max is not over full.", inv.isOverFull());
      
      inv.add(WeaponFactory.getPistol());
      assertFalse("Inventory above max base size but below adjusted max is not full.", inv.isFull());
      assertFalse("Inventory above max base size but below adjusted max is not over full.", inv.isOverFull());
      
      inv.add(WeaponFactory.getPistol());
      assertTrue("Inventory above max base size but below adjusted max is full.", inv.isFull());
      assertFalse("Inventory above max base size but below adjusted max is not over full.", inv.isOverFull());
      
      a.setArmor(null);
      assertEquals("Removing armor does not change carried items.", 4, inv.size());
      assertTrue("Inventory over adjusted max size is full.", inv.isFull());
      assertTrue("Inventory over adjusted max size is over full.", inv.isOverFull());
   }
}
