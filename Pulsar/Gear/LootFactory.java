package Pulsar.Gear;

import Pulsar.Engine.*;
import java.util.*;

public class LootFactory implements GearConstants
{
   public static LootType rollLootType()
   {
      WeightedRandomizer table = new WeightedRandomizer(LootType.values());
      return (LootType)table.roll();
   }
   
   public static LootType rollNonCreditLootType()
   {
      LootType[] typeList = new LootType[LootType.values().length - 1];
      int i = 0;
      for(LootType type : LootType.values())
      {
         if(type == LootType.CREDITS)
            continue;
         typeList[i] = type;
         i++;
      }
      WeightedRandomizer table = new WeightedRandomizer(typeList);
      return (LootType)table.roll();
   }
   
   public static LootRarity rollLootRarity()
   {
      WeightedRandomizer table = new WeightedRandomizer(LootRarity.values());
      return (LootRarity)table.roll();
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
      
      System.out.println("Non-Credit Rolls:");
      results = new int[LootType.values().length];
      for(int i = 0; i < maxReps; i++)
         results[rollNonCreditLootType().ordinal()]++;
      for(int i = 0; i < LootType.values().length; i++)
      {
         double percent = results[i] * 100.0 / maxReps;
         System.out.println(LootType.values()[i] + ": " + percent + "%");
      }
      
      for(int i = 0; i < 20; i++)
         System.out.println("" + Credits.generate().getAmount());
   }
}