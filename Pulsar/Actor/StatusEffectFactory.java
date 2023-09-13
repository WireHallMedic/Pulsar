package Pulsar.Actor;

import Pulsar.Gear.*;

public class StatusEffectFactory implements ActorConstants, GearConstants
{
   public static StatusEffect getEffect(StatusEffectType type)
   {
      StatusEffect se = new StatusEffect();
      switch(type)
      {
         case BURNING :    se.setName("Burning");
                           se.setRemainingDuration(STATUS_EFFECT_SHORT_DURATION);
	                        se.setExpires(true);
	                        se.setStacks(false);
                           se.setNegative(true);
                           se.setDescriptor("burning");
                           se.setOngoingDamage(3);
	                        se.setDamageType(DamageType.THERMAL);
                           break;
         case FROZEN :     se.setName("Frozen");
                           se.setRemainingDuration(STATUS_EFFECT_SHORT_DURATION);
	                        se.setExpires(true);
	                        se.setStacks(false);
                           se.setNegative(true);
                           se.setDescriptor("frozen");
                           se.setMoveSpeed(-1);
                        	se.setAttackSpeed(-1);
                        	se.setInteractSpeed(-1);
                           break;
         case BLIND :      se.setName("Blind");
                           se.setRemainingDuration(STATUS_EFFECT_SHORT_DURATION);
	                        se.setExpires(true);
	                        se.setStacks(false);
                           se.setNegative(true);
                           se.setDescriptor("blinded");
                           se.setVisionRange(-10);
                           break;
      }
      return se;
   }
}