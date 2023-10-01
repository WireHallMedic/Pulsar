package Pulsar.Gear;

import Pulsar.Actor.*;

public class ArmorFactory implements GearConstants, ActorConstants
{
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