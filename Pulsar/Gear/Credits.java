package Pulsar.Gear;

import Pulsar.GUI.*;
import Pulsar.Engine.*;

public class Credits extends GearObj implements GearConstants, GUIConstants
{
   public static final int COMMON_MIN = 10;
   public static final int COMMON_MAX = 50;
   public static final int UNCOMMON_MULTIPLIER = 5;
   public static final int RARE_MULTIPLIER = 20;
   
   private int amount;
   
   public int getAmount(){return amount;}
   
   public void setAmount(int amt){amount = amt;}
   
   public Credits(int amt)
   {
      super(CREDIT_ICON, "Credits");
      setColor(GOLD);
      amount = amt;
   }
   
   @Override
   public String getName()
   {
      return getSummary();
   }
   
   public void add(Credits that)
   {
      this.adjustAmount(that.getAmount());
      that.setAmount(0);
   }
   
   public void adjustAmount(int val)
   {
      amount += val;
   }
   
   public String getSummary()
   {
      return getAmount() + " " + super.getName();
   }
   
   
   // factory methods
   public static Credits generate(LootRarity rarity)
   {
      int min = COMMON_MIN;
      int max = COMMON_MAX;
      if(rarity == LootRarity.UNCOMMON)
      {
         min *= UNCOMMON_MULTIPLIER;
         max *= UNCOMMON_MULTIPLIER;
      }
      if(rarity == LootRarity.RARE)
      {
         min *= RARE_MULTIPLIER;
         max *= RARE_MULTIPLIER;
      }
      int value = GameEngine.randomInt(min, max);
      return new Credits(value);
   }
   
   public static Credits generate()
   {
      return generate(LootFactory.rollLootRarity());
   }
}