package Pulsar.Gear;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class ArmorFactory implements GearConstants, ActorConstants
{
   public enum ArmorType implements WeightedRandomizable
   {
      BASIC_ARMOR    (30),
      SCOUT_ARMOR    (10),
      ENGINEER_ARMOR (10),
      ASSAULT_ARMOR  (10);
      
      private ArmorType(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
   }
   
   public enum ArmorUpgrade implements WeightedRandomizable
   {
      REINFORCED     (10), // + armor
      TACTICAL       (10), // + gear
      ENVIRONMENTAL  (10), // air supply
      VIGILANT       (10), // motion sensor
      TO_DO_1  (0),
      TO_DO_2  (0),
      TO_DO_3  (0),
      TO_DO_4  (0),
      TO_DO_5  (0),
      TO_DO_6  (0);
      
      private ArmorUpgrade(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
      
      public boolean canApply(Armor armor)
      {
         switch(this)
         {
            case REINFORCED      : break;
            case TACTICAL        : break;
            case ENVIRONMENTAL   : if(armor.hasStatusEffect() &&
                                      armor.getStatusEffect().getNeedsAir() == false)
                                      return false;
                                   break;
            case VIGILANT        : if(armor.hasGadgetEffect() &&
                                      armor.getGadgetEffect() == GadgetSpecialEffect.MOTION_SENSOR)
                                      return false;
                                   break;
         }
         return true;
      }
   }
   
   public static Armor generate()
   {
      return generateByRarity(LootFactory.rollLootRarity());
   }
   
   public static Armor generateByRarity(LootRarity rarity)
   {
      
      WeightedRandomizer table = new WeightedRandomizer(ArmorType.values());
      ArmorType type = (ArmorType)table.roll();
      switch(type)
      {
         case BASIC_ARMOR     : return getBasicArmor();
         case SCOUT_ARMOR     : return getScoutArmor();
         case ENGINEER_ARMOR  : return getEngineerArmor();
         case ASSAULT_ARMOR   : return getAssaultArmor();
         default              : return null;
      }
   }
   
   public static Armor getBasicArmor()
   {
      Armor a = new Armor();
      a.setName("Basic Armor");
      a.setDamageReduction(2);
	   a.setSpeedCap(ActionSpeed.FAST);
      return a;
   }
   
   public static Armor getScoutArmor()
   {
      Armor a = getBasicArmor();
      a.setName("Scout Armor");
      a.setDamageReduction(0);
      a.setGadgetEffect(GadgetSpecialEffect.MOTION_SENSOR);
      return a;
   }
   
   public static Armor getEngineerArmor()
   {
      Armor a = getBasicArmor();
      a.setName("Engineer Armor");
      a.setInventorySpace(2);
      return a;
   }
   
   public static Armor getAssaultArmor()
   {
      Armor a = getBasicArmor();
      a.setName("Assault Armor");
      a.setDamageReduction(4);
	   a.setSpeedCap(ActionSpeed.STANDARD);
      a.setStatusEffect(StatusEffectFactory.getEffect(StatusEffectType.AIR_SUPPLY));
      return a;
   }
   
   public static void main(String[] args)
   {
      Armor a = getBasicArmor();
      System.out.println(a.getName() + ": " + a.getSummary());
      a = getScoutArmor();
      System.out.println(a.getName() + ": " + a.getSummary());
      a = getEngineerArmor();
      System.out.println(a.getName() + ": " + a.getSummary());
      a = getAssaultArmor();
      System.out.println(a.getName() + ": " + a.getSummary());
   }
}