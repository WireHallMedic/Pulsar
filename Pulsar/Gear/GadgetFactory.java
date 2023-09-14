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
      g.setWeaponEffect(null);
      g.setTargetsSelf(true);
      return g;
   }
   
   public static Gadget getGrenades()
   {
      Gadget g = new Gadget();
      g.setName("Grenades");
      g.setShortName("Grenades");
      g.setStatusEffectType(null);
      g.setWeaponEffect(WeaponFactory.getExplodingBarrel());
      g.setTargetsSelf(false);
      return g;
   }
}