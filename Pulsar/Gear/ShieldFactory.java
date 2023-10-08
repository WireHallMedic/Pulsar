package Pulsar.Gear;

import Pulsar.Engine.*;

public class ShieldFactory implements GearConstants
{
   public enum ShieldType implements WeightedRandomizable
   {
      BASIC       (20),
      FASTCHARGE  (5),
      HEAVY       (5);
      
      private ShieldType(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
   }
   
   public static Shield generate()
   {
      return generate(LootFactory.rollLootRarity());
   }
   
   public static Shield generate(LootRarity rarity)
   {
      
      WeightedRandomizer table = new WeightedRandomizer(ShieldType.values());
      ShieldType type = (ShieldType)table.roll();
      switch(type)
      {
         case BASIC        : return getBasicShield();
         case FASTCHARGE   : return getFastShield();
         case HEAVY        : return getHeavyShield();
         default           : return null;
      }
   }
   
   public static Shield getBasicShield()
   {
      Shield s = new Shield();
      s.setName("Basic Shield");
      return s;
   }
   
   public static Shield getHeavyShield()
   {
      Shield s = new Shield();
      s.setName("Heavy Shield");
      s.setMaxCharge(DEFAULT_SHIELD_MAX_CHARGE * 3 / 2);
      s.setChargeDelay(DEFAULT_SHIELD_CHARGE_DELAY * 3 / 2);
      s.fullyCharge();
      return s;
   }
   
   public static Shield getFastShield()
   {
      Shield s = new Shield();
      s.setName("Fastcharge Shield");
      s.setMaxCharge(DEFAULT_SHIELD_MAX_CHARGE * 3 / 4);
      s.setChargeDelay(DEFAULT_SHIELD_CHARGE_DELAY / 2);
      s.setChargeRate(DEFAULT_SHIELD_CHARGE_RATE * 2);
      s.fullyCharge();
      return s;
   }
}