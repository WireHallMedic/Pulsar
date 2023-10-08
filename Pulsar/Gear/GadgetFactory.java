package Pulsar.Gear;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class GadgetFactory implements ActorConstants, GearConstants
{
   public enum GadgetType implements WeightedRandomizable
   {
      ADRENAL_INJECTOR  (10),
      GRENADES          (10),
      NAPALM_GRENADES   (10),
      CRYO_GRENADES     (10),
      EMP_GRENADES      (10),
      HOLOCLONE         (10),
      TURRET            (10),
      COMBAT_DRONE      (10),
      MOTION_SENSOR     (10),
      AIR_SUPPLY        (10),
      QUICK_CHARGER     (10);
      
      private GadgetType(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
   }
   
   public static Gadget generate()
   {
      return generateByRarity(LootFactory.rollLootRarity());
   }
   
   public static Gadget generateByRarity(LootRarity rarity)
   {
      WeightedRandomizer table = new WeightedRandomizer(GadgetType.values());
      GadgetType type = (GadgetType)table.roll();
      return generateByType(type, rarity);
   }
   
   public static Gadget generateByType(GadgetType type){return generateByType(type, LootRarity.COMMON);}
   public static Gadget generateByType(GadgetType type, LootRarity rarity)
   {
      switch(type)
      {
         case ADRENAL_INJECTOR   : return getAdrenalInjector(rarity);
         case GRENADES           : return getGrenades(rarity);
         case NAPALM_GRENADES    : return getNapalmGrenades(rarity);
         case CRYO_GRENADES      : return getCryoGrenades(rarity);
         case EMP_GRENADES       : return getEMPGrenades(rarity);
         case HOLOCLONE          : return getHoloclone(rarity);
         case TURRET             : return getTurret(rarity);
         case COMBAT_DRONE       : return getCombatDrone(rarity);
         case MOTION_SENSOR      : return getMotionSensor(rarity);
         case AIR_SUPPLY         : return getAirSupply(rarity);
         case QUICK_CHARGER      : return getQuickCharger(rarity);
         default                 : return null;
      }
   }
   
   public static Gadget getAdrenalInjector(){return getAdrenalInjector(LootRarity.COMMON);}
   public static Gadget getAdrenalInjector(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Adrenal Injector");
      g.setShortName("Adrenal Inj");
      g.setStatusEffectType(StatusEffectType.HASTE);
      g.setTargetsSelf(true);
      g.setDescription("Temporarily increases your speed. Speed can be limited by heavy armor.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setStatusEffectDuration(2.0);
         g.setDescription(g.getDescription() + " Extended duration.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setStatusEffectDuration(3.0);
         g.setDescription(g.getDescription() + " Greatly extended duration.");
      }
      return g;
   }
   
   public static Gadget getGrenades(){return getGrenades(LootRarity.COMMON);}
   public static Gadget getGrenades(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Grenades");
      g.setShortName("Grenades");
      g.setWeaponEffect(WeaponFactory.getExplodingBarrel());
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.getWeaponEffect().setBaseDamage((int)(g.getWeaponEffect().getBaseDamage() * 1.5));
         g.setDescription(g.getDescription() + " Improved damage.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.getWeaponEffect().setBaseDamage((int)(g.getWeaponEffect().getBaseDamage() * 2.0));
         g.setDescription(g.getDescription() + " Greatly improved damage.");
      }
      return g;
   }
   
   public static Gadget getNapalmGrenades(){return getNapalmGrenades(LootRarity.COMMON);}
   public static Gadget getNapalmGrenades(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Napalm Grenades");
      g.setShortName("Napalm Grenades");
      g.setWeaponEffect(WeaponFactory.getNapalm());
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it, igniting the area.");
      g.setSpecialEffect(GadgetSpecialEffect.NAPALM);
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.getWeaponEffect().setBaseDamage((int)(g.getWeaponEffect().getBaseDamage() * 1.5));
         g.setDescription(g.getDescription() + " Improved damage.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.getWeaponEffect().setBaseDamage((int)(g.getWeaponEffect().getBaseDamage() * 2.0));
         g.setDescription(g.getDescription() + " Greatly improved damage.");
      }
      return g;
   }
   
   public static Gadget getCryoGrenades(){return getCryoGrenades(LootRarity.COMMON);}
   public static Gadget getCryoGrenades(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Cryo Grenades");
      g.setShortName("Cryo Grenades");
      g.setWeaponEffect(WeaponFactory.getCryoExplosion());
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it, freezing terrain and creatures in the area.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.getWeaponEffect().setBaseDamage((int)(g.getWeaponEffect().getBaseDamage() * 1.5));
         g.setDescription(g.getDescription() + " Improved damage.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.getWeaponEffect().setBaseDamage((int)(g.getWeaponEffect().getBaseDamage() * 2.0));
         g.setDescription(g.getDescription() + " Greatly improved damage.");
      }
      return g;
   }
   
   public static Gadget getEMPGrenades(){return getEMPGrenades(LootRarity.COMMON);}
   public static Gadget getEMPGrenades(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("EMP Grenades");
      g.setShortName("EMP Grenades");
      g.setWeaponEffect(WeaponFactory.getEMPExplosion(rarity.ordinal()));
      g.setTargetsSelf(false);
      g.setDescription("Explodes, disrupting shields and mechanical foes.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setDescription(g.getDescription() + " Also deals damage.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setDescription(g.getDescription() + " Also deals improved damage.");
      }
      return g;
   }
   
   public static Gadget getHoloclone(){return getHoloclone(LootRarity.COMMON);}
   public static Gadget getHoloclone(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Exploding Holoclone");
      g.setShortName("Holoclone");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.HOLOCLONE);
      g.setDescription("Deploys a clone of you, which explodes on death.");
      g.setPlaceAdjacent(true);
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setIntensity(2);
         g.setDescription("Deploys two clones of you, which explodes on death.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setIntensity(3);
         g.setDescription("Deploys three clones of you, which explodes on death. That's a lot of exploding clones!");
      }
      return g;
   }
   
   public static Gadget getTurret(){return getTurret(LootRarity.COMMON);}
   public static Gadget getTurret(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Portable Turret");
      g.setShortName("Turret");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.TURRET);
      g.setDescription("Depolys a stationary gun turret.");
      g.setPlaceAdjacent(true);
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setIntensity(2);
         g.setDescription("Depolys a stationary gun turret with a random gun upgrade.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setIntensity(3);
         g.setDescription("Depolys a stationary gun turret with three random gun upgrades.");
      }
      return g;
   }
   
   public static Gadget getCombatDrone(){return getCombatDrone(LootRarity.COMMON);}
   public static Gadget getCombatDrone(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Combat Drone");
      g.setShortName("C. Drone");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.COMBAT_DRONE);
      g.setDescription("Depolys a combat drone, who follows you and fires on enemies.");
      g.setPlaceAdjacent(true);
      g.setMaxUses(1);
      g.fullyCharge();
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setIntensity(2);
         g.setDescription("Depolys a combat drone, who follows you and fires on enemies. The drone has a random gun upgrade.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setIntensity(3);
         g.setDescription("Depolys a combat drone, who follows you and fires on enemies. The drone has three random gun upgrades.");
      }
      return g;
   }
   
   public static Gadget getMotionSensor(){return getMotionSensor(LootRarity.COMMON);}
   public static Gadget getMotionSensor(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Motion Sensor");
      g.setShortName("Motion Sensor");
      g.setTargetsSelf(false);
      g.setPassiveOnly(true);
      g.setSpecialEffect(GadgetSpecialEffect.MOTION_SENSOR);
      g.setDescription("Detects nearby movement, even through walls.");
      return g;
   }
   
   public static Gadget getAirSupply(){return getAirSupply(LootRarity.COMMON);}
   public static Gadget getAirSupply(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Portable Air Supply");
      g.setShortName("Air Supply");
      g.setPassiveOnly(true);
      g.setPassiveStatusEffect(StatusEffectFactory.getEffect(StatusEffectType.AIR_SUPPLY));
      g.setDescription("Provides a continuous supply of air, allowing you to ignore vacuum.");
      return g;
   }
   
   public static Gadget getQuickCharger(){return getQuickCharger(LootRarity.COMMON);}
   public static Gadget getQuickCharger(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Quick Charger");
      g.setShortName("Quick Charger");
      g.setTargetsSelf(true);
      g.setSpecialEffect(GadgetSpecialEffect.RECHARGE);
      g.setDescription("Immedeatly fully charges your shield and weapons.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setIntensity(2);
         g.setDescription("Immedeatly overcharges your shield and weapons to 150%.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setIntensity(3);
         g.setDescription("Immedeatly overcharges your shield and weapons to 200%.");
      }
      return g;
   }
}