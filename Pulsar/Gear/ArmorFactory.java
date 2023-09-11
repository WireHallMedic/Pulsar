package Pulsar.Gear;

import Pulsar.Actor.*;

public class ArmorFactory implements GearConstants
{
   public static Armor getScoutArmor()
   {
      Armor a = new Armor();
      a.setName("Scout Armor");
      a.setDamageReduction(0);
	   a.setSpeedCap(ActorConstants.FAST_ACTION_COST);
      return a;
   }
   
   public static Armor getStandardArmor()
   {
      Armor a = new Armor();
      a.setName("Standard Armor");
      a.setDamageReduction(2);
	   a.setSpeedCap(ActorConstants.NORMAL_ACTION_COST);
      return a;
   }
   
   public static Armor getAssaultArmor()
   {
      Armor a = new Armor();
      a.setName("Assault Armor");
      a.setDamageReduction(4);
	   a.setSpeedCap(ActorConstants.SLOW_ACTION_COST);
      return a;
   }
}