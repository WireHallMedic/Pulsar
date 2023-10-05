package Pulsar.Gear;

import Pulsar.Engine.*;
import java.util.*;

public class LootFactory implements GearConstants
{
   public static final boolean NON_CREDIT = true;
   
   public static GearObj rollLoot(){return rollLoot(false);}
   public static GearObj rollLoot(boolean nonCredit)
   {
      LootType type = rollLootType();
      if(nonCredit)
         type = rollNonCreditLootType();
      LootRarity rarity = rollLootRarity();
      GearObj loot = null;
      switch(type)
      {
         case CREDITS : loot = Credits.generateByRarity(rarity); break;
         case WEAPON : loot = WeaponFactory.generateByRarity(rarity); break;
         case GADGET : loot = GadgetFactory.generateByRarity(rarity); break;
         case ARMOR : loot = ArmorFactory.generateByRarity(rarity); break;
         case SHIELD : loot = Credits.generateByRarity(rarity); break;
         default : System.out.println("Unknown loot type: " + type); break;
      }
      
      return loot;
   }
   
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
      
      for(int i = 0; i < 20; i++)
         System.out.println("" + rollLoot(NON_CREDIT));
   }
}