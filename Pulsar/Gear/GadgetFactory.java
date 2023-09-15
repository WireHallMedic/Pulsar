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
   
   public static Gadget getHoloclone()
   {
      Gadget g = new Gadget();
      g.setName("Exploding Holoclone");
      g.setShortName("Holoclone");
      g.setTargetsSelf(false);
      g.setSpecialEffect(GadgetSpecialEffect.HOLOCLONE);
      g.setDescription("Create an exploding clone where you throw it.");
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
      return g;
   }
}