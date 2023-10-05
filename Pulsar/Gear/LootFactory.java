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
      int maxReps = 10000;
      int[] results = new int[LootType.values().length];
      for(int i = 0; i < maxReps; i++)
         results[rollLootType().ordinal()]++;
      for(int i = 0; i < LootType.values().length; i++)
      {
         double percent = results[i] * 100.0 / maxReps;
         System.out.println(LootType.values()[i] + ": " + percent + "%");
      }
   }
}