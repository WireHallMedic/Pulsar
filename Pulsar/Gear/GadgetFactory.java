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
      return g;
   }
}