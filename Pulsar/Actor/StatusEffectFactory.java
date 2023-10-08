package Pulsar.Actor;

import Pulsar.Gear.*;
import Pulsar.Zone.*;

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
                           se.setOngoingDamage(ZoneConstants.FIRE_DAMAGE);
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
         case DISRUPTED :  se.setName("Disrupted");
                           se.setRemainingDuration(STATUS_EFFECT_SHORT_DURATION);
	                        se.setExpires(true);
	                        se.setStacks(false);
                           se.setNegative(true);
                           se.setDescriptor("disrupted");
                           se.setDisrupts(true);
                           break;
         case CHARMED :    se.setName("Charmed");
                           se.setRemainingDuration(STATUS_EFFECT_LONG_DURATION);
	                        se.setExpires(true);
	                        se.setStacks(false);
                           se.setNegative(true);
                           se.setDescriptor("charmed");
                           se.setCharms(true);
                           break;
         case EAGLE_EYE  : se.setName("Eagle Eye");
                           se.setRemainingDuration(STATUS_EFFECT_LONG_DURATION);
	                        se.setExpires(true);
	                        se.setStacks(false);
                           se.setNegative(false);
                           se.setDescriptor("clear of vision");
                           se.setVisionRange(10);
                           break;
         case HASTE :      se.setName("Hasted");
                           se.setRemainingDuration(STATUS_EFFECT_LONG_DURATION);
	                        se.setExpires(true);
	                        se.setStacks(false);
                           se.setNegative(false);
                           se.setDescriptor("hasted");
                           se.setMoveSpeed(1);
                        	se.setAttackSpeed(1);
                        	se.setInteractSpeed(1);
                           break;
         case AIR_SUPPLY : se.setName("Air Supply");
                           se.setNeedsAir(false);
                           break;
      }
      return se;
   }
}