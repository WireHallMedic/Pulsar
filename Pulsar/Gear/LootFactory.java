package Pulsar.Gear;

import Pulsar.Engine.*;

public class LootFactory implements GearConstants
{
   public static LootType rollLootType()
   {
      WeightedRandomizer table = new WeightedRandomizer(LootType.values());
      return (LootType)table.roll();
   }
   
   public static void main(String[] args)
   {
      System.out.println(rollLootType());
   }
}