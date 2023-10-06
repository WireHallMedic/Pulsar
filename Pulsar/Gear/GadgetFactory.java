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
      switch(type)
      {
         case ADRENAL_INJECTOR   : return getAdrenalInjector();
         case GRENADES           : return getGrenades();
         case NAPALM_GRENADES    : return getNapalmGrenades();
         case CRYO_GRENADES      : return getCryoGrenades();
         case HOLOCLONE          : return getHoloclone();
         case TURRET             : return getTurret();
         case COMBAT_DRONE       : return getCombatDrone();
         case MOTION_SENSOR      : return getMotionSensor();
         case AIR_SUPPLY         : return getAirSupply();
         case QUICK_CHARGER      : return getQuickCharger();
         default                 : return null;
      }
   }
   
   public static Gadget getAdrenalInjector()
   {
      Gadget g = new Gadget();
      g.setName("Adrenal Injector");
      g.setShortName("Adrenal Inj");
      g.setStatusEffectType(StatusEffectType.HASTE);
      g.setTargetsSelf(true);
      g.setDescription("Temporarily increases your speed. Speed can be limited by heavy armor.");
      return g;
   }
   
   public static Gadget getGrenades()
   {
      Gadget g = new Gadget();
      g.setName("Grenades");
      g.setShortName("Grenades");
      g.setWeaponEffect(WeaponFactory.getExplodingBarrel());
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it.");
      return g;
   }
   
   public static Gadget getNapalmGrenades()
   {
      Gadget g = new Gadget();
      g.setName("Napalm Grenades");
      g.setShortName("Napalm Grenades");
      g.setWeaponEffect(WeaponFactory.getNapalm());
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it, igniting the area.");
      g.setSpecialEffect(GadgetSpecialEffect.NAPALM);
      return g;
   }
   
   public static Gadget getCryoGrenades()
   {
      Gadget g = new Gadget();
      g.setName("Cryo Grenades");
      g.setShortName("Cryo Grenades");
      g.setWeaponEffect(WeaponFactory.getCryoExplosion());
      g.setTargetsSelf(false);
      g.setDescription("Explodes where you throw it, freezing terrain and creatures in the area.");
      return g;
   }
   
   public static Gadget getHoloclone()
   {
      Gadget g = new Gadget();
      g.setName("Exploding Holoclone");
      g.setShortName("Holoclone");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.HOLOCLONE);
      g.setDescription("Deploys a clone of you, which explodes on death.");
      g.setPlaceAdjacent(true);
      return g;
   }
   
   public static Gadget getTurret()
   {
      Gadget g = new Gadget();
      g.setName("Portable Turret");
      g.setShortName("Turret");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.TURRET);
      g.setDescription("Depolys a stationary gun turret.");
      g.setPlaceAdjacent(true);
      return g;
   }
   
   public static Gadget getCombatDrone()
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
      return g;
   }
   
   public static Gadget getMotionSensor()
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
   
   public static Gadget getAirSupply()
   {
      Gadget g = new Gadget();
      g.setName("Portable Air Supply");
      g.setShortName("Air Supply");
      g.setPassiveOnly(true);
      g.setPassiveStatusEffect(StatusEffectFactory.getEffect(StatusEffectType.AIR_SUPPLY));
      g.setDescription("Provides a continuous supply of air, allowing you to ignore vacuum.");
      return g;
   }
   
   public static Gadget getQuickCharger()
   {
      Gadget g = new Gadget();
      g.setName("Quick Charger");
      g.setShortName("Quick Charger");
      g.setTargetsSelf(true);
      g.setSpecialEffect(GadgetSpecialEffect.RECHARGE);
      g.setDescription("Immedeatly fully charges your shield and weapons.");
      return g;
   }
}