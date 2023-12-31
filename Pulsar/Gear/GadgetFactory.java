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
      LOVE_BOMB         (10),
      QUICK_CHARGER     (10),
      BREACH_CHARGE     (10),
      ACID_BOMB         (10);
      
      private GadgetType(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
   }
   
   public static Gadget generate()
   {
      return generate(LootFactory.rollLootRarity());
   }
   
   public static Gadget generate(LootRarity rarity)
   {
      WeightedRandomizer table = new WeightedRandomizer(GadgetType.values());
      GadgetType type = (GadgetType)table.roll();
      return generate(type, rarity);
   }
   
   public static Gadget generate(GadgetType type)
   {
      return generate(type, LootRarity.COMMON);
   }
   
   
   public static Gadget generate(GadgetType type, LootRarity rarity)
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
         case LOVE_BOMB          : return getLoveBomb(rarity);
         case QUICK_CHARGER      : return getQuickCharger(rarity);
         case BREACH_CHARGE      : return getBreachingCharge(rarity);
         case ACID_BOMB          : return getAcidBomb(rarity);
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
      g.setColorByRarity(rarity);
      return g;
   }
   
   public static Gadget getGrenades(){return getGrenades(LootRarity.COMMON);}
   public static Gadget getGrenades(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Grenades");
      g.setShortName("Grenades");
      g.setWeaponEffect(WeaponFactory.getGrenadeExplosion(rarity.ordinal()));
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setDescription(g.getDescription() + " Improved damage.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setDescription(g.getDescription() + " Greatly improved damage.");
      }
      g.setColorByRarity(rarity);
      return g;
   }
   
   public static Gadget getNapalmGrenades(){return getNapalmGrenades(LootRarity.COMMON);}
   public static Gadget getNapalmGrenades(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Napalm Grenades");
      g.setShortName("Napalm Grenades");
      g.setWeaponEffect(WeaponFactory.getNapalm(rarity.ordinal()));
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it, igniting the area.");
      g.setSpecialEffect(GadgetSpecialEffect.NAPALM);
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setDescription(g.getDescription() + " Improved damage.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setDescription(g.getDescription() + " Greatly improved damage.");
      }
      g.setColorByRarity(rarity);
      return g;
   }
   
   public static Gadget getCryoGrenades(){return getCryoGrenades(LootRarity.COMMON);}
   public static Gadget getCryoGrenades(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Cryo Grenades");
      g.setShortName("Cryo Grenades");
      g.setWeaponEffect(WeaponFactory.getCryoExplosion(rarity.ordinal()));
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it, freezing terrain and creatures in the area.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setDescription(g.getDescription() + " Improved damage.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setDescription(g.getDescription() + " Greatly improved damage.");
      }
      g.setColorByRarity(rarity);
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
      g.setDescription("Disrupting shields and mechanical foes.");
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
      g.setColorByRarity(rarity);
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
      g.setColorByRarity(rarity);
      return g;
   }
   
   public static Gadget getBreachingCharge(){return getBreachingCharge(LootRarity.COMMON);}
   public static Gadget getBreachingCharge(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Breaching Charge");
      g.setShortName("B. Charge");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.BREACH);
      g.setDescription("Deploys a breaching charge, which can blast holes in bulkhead walls.");
      g.setPlaceAdjacent(true);
      g.setMaxUses(1);
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setMaxUses(2);
      }
      if(rarity == LootRarity.RARE)
      {
         g.setMaxUses(3);
      }
      g.setColorByRarity(rarity);
      g.fullyCharge();
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
      g.setColorByRarity(rarity);
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
      g.setColorByRarity(rarity);
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
      g.setColorByRarity(rarity);
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
      g.setColorByRarity(rarity);
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
      g.setColorByRarity(rarity);
      return g;
   }
   
   public static Gadget getLoveBomb(){return getLoveBomb(LootRarity.COMMON);}
   public static Gadget getLoveBomb(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Love Bombs");
      g.setShortName("Love Bombs");
      g.setWeaponEffect(WeaponFactory.getHarmlessExplosion());
      g.setIntensity(rarity.ordinal());
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it, charming the those it hits.");
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setDescription(g.getDescription() + " Improved duration.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setDescription(g.getDescription() + " Greatly improved duration.");
      }
      g.setSpecialEffect(GadgetSpecialEffect.CHARM);
      g.setColorByRarity(rarity);
      return g;
   }
   
   public static Gadget getAcidBomb(){return getAcidBomb(LootRarity.COMMON);}
   public static Gadget getAcidBomb(LootRarity rarity)
   {
      Gadget g = new Gadget();
      g.setName("Acid Bomb");
      g.setShortName("Acid Bomb");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.ACID_BOMB);
      g.setDescription("Floods an area with acid.");
      g.setIntensity((rarity.ordinal() + 1) * 6);
      if(rarity == LootRarity.UNCOMMON)
      {
         g.setName(g.getName() + " MkII");
         g.setDescription("Floods an area with quite a lot of acid.");
      }
      if(rarity == LootRarity.RARE)
      {
         g.setName(g.getName() + " MkIII");
         g.setDescription("Floods an area with, just, like a ton of acid.");
      }
      g.setColorByRarity(rarity);
      return g;
   }
}