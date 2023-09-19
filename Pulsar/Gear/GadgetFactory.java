package Pulsar.Gear;

import Pulsar.Actor.*;

public class GadgetFactory implements ActorConstants, GearConstants
{
   public static Gadget getAdrenalInjector()
   {
      Gadget g = new Gadget();
      g.setName("Adrenal Injector");
      g.setShortName("Adrenal Inj");
      g.setStatusEffectType(StatusEffectType.HASTE);
      g.setTargetsSelf(true);
      g.setDescription("Increases your speed.");
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
      g.setDescription("Explodes and ignites where you throw it.");
      g.setSpecialEffect(GadgetSpecialEffect.NAPALM);
      return g;
   }
   
   public static Gadget getHoloclone()
   {
      Gadget g = new Gadget();
      g.setName("Exploding Holoclone");
      g.setShortName("Holoclone");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.HOLOCLONE);
      g.setDescription("Deploys an exploding clone.");
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
      g.setDescription("Depolys a stationary turret.");
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
      g.setDescription("Depolys a combat drone.");
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
      g.setDescription("Detects nearby movement.");
      return g;
   }
   
   public static Gadget getAirSupply()
   {
      Gadget g = new Gadget();
      g.setName("Portable Air Supply");
      g.setShortName("Air Supply");
      g.setPassiveOnly(true);
      g.setPassiveStatusEffect(StatusEffectFactory.getEffect(StatusEffectType.AIR_SUPPLY));
      g.setDescription("Provides a continuous supply of air.");
      return g;
   }
}