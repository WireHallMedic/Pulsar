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
      REINFORCED     (30, "Reinforced", "R"), // + armor
      TACTICAL       (10, "Tactical", "T"), // + gear
      ENVIRONMENTAL  (10, "Environmental", "E"), // air supply
      VIGILANT       (10, "Vigilant", "V"), // motion sensor
      TO_DO_1  (0, "To Do", "X"),
      TO_DO_2  (0, "To Do", "X"),
      TO_DO_3  (0, "To Do", "X"),
      TO_DO_4  (0, "To Do", "X"),
      TO_DO_5  (0, "To Do", "X"),
      TO_DO_6  (0, "To Do", "X");
      
      private ArmorUpgrade(int w, String n, String sn)
      {
         weight = w;
         name = n;
         shortName = sn;
      }
      
      private int weight;
      private String name;
      private String shortName;
      
      public int getWeight(){return weight;}
      public String getName(){return name;}
      public String getShortName(){return shortName;}
      
      public boolean canApply(Armor armor)
      {
         switch(this)
         {
            case REINFORCED      : break;
            case TACTICAL        : break;
            case ENVIRONMENTAL   : if(armor.hasStatusEffect())
                                      return false;
                                   break;
            case VIGILANT        : if(armor.hasGadgetEffect())
                                      return false;
                                   break;
         }
         return true;
      }
   }
   
   public static void addUpgrade(Armor armor, ArmorUpgrade upgrade)
   {
      switch(upgrade)
      {
         case REINFORCED      : armor.setDamageReduction(armor.getDamageReduction() + 1);
                                break;
         case TACTICAL        : armor.setInventorySpace(armor.getInventorySpace() + 1);
                                break;
         case ENVIRONMENTAL   : armor.setStatusEffect(StatusEffectFactory.getEffect(StatusEffectType.AIR_SUPPLY));
                                break;
         case VIGILANT        : armor.setGadgetEffect(GadgetSpecialEffect.MOTION_SENSOR);
                                break;
      }
      armor.setName(upgrade.getName() + " " + armor.getName());
   }
   
   public static Armor generate()
   {
      return generateByRarity(LootFactory.rollLootRarity());
   }
   
   public static Armor generateByRarity(LootRarity rarity)
   {
      WeightedRandomizer table = new WeightedRandomizer(ArmorType.values());
      ArmorType type = (ArmorType)table.roll();
      Armor armor = getBasicArmor();
      int upgrades = 0;
      switch(type)
      {
         case SCOUT_ARMOR     : armor = getScoutArmor(); break;
         case ENGINEER_ARMOR  : armor = getEngineerArmor(); break;
         case ASSAULT_ARMOR   : armor = getAssaultArmor(); break;
      }
      if(rarity == LootRarity.UNCOMMON)
         upgrades = 1;
      if(rarity == LootRarity.RARE)
         upgrades = 2;
      for(int i = 0; i < upgrades; i++)
      {
         table = new WeightedRandomizer(ArmorUpgrade.values());
         ArmorUpgrade upgrade = (ArmorUpgrade)table.roll();
         while(!upgrade.canApply(armor))
            upgrade = (ArmorUpgrade)table.roll();
         addUpgrade(armor, upgrade);
      }
      return armor;
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
      for(int i = 0; i < 20; i++)
         System.out.println(generate().getName());
   }
}