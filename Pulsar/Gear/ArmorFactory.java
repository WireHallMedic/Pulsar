package Pulsar.Gear;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class ArmorFactory implements GearConstants, ActorConstants
{
   public enum ArmorType implements WeightedRandomizable
   {
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
         case SCOUT_ARMOR     : return getScoutArmor();
         case ENGINEER_ARMOR  : return getEngineerArmor();
         case ASSAULT_ARMOR   : return getAssaultArmor();
         default              : return null;
      }
   }
   public static Armor getScoutArmor()
   {
      Armor a = new Armor();
      a.setName("Scout Armor");
      a.setDamageReduction(0);
	   a.setSpeedCap(ActionSpeed.FAST);
      a.setGadgetEffect(GadgetSpecialEffect.MOTION_SENSOR);
      return a;
   }
   
   public static Armor getEngineerArmor()
   {
      Armor a = new Armor();
      a.setName("Engineer Armor");
      a.setDamageReduction(2);
	   a.setSpeedCap(ActionSpeed.FAST);
      a.setInventorySpace(2);
      return a;
   }
   
   public static Armor getAssaultArmor()
   {
      Armor a = new Armor();
      a.setName("Assault Armor");
      a.setDamageReduction(4);
	   a.setSpeedCap(ActionSpeed.STANDARD);
      a.setStatusEffect(StatusEffectFactory.getEffect(StatusEffectType.AIR_SUPPLY));
      return a;
   }
   
   public static void main(String[] args)
   {
      Armor a = getScoutArmor();
      System.out.println(a.getName() + ": " + a.getSummary());
      a = getEngineerArmor();
      System.out.println(a.getName() + ": " + a.getSummary());
      a = getAssaultArmor();
      System.out.println(a.getName() + ": " + a.getSummary());
   }
}