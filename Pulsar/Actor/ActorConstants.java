package Pulsar.Actor;

public interface ActorConstants
{
   public static final int FULLY_CHARGED = 10;
   
   public static final int DEFAULT_VISION_RANGE = 7;
   
   public static final int STATUS_EFFECT_SHORT_DURATION = 10;
   public static final int STATUS_EFFECT_MEDIUM_DURATION = 20;
   public static final int STATUS_EFFECT_LONG_DURATION = 40;
   
   
   public enum StatusEffectType
   {
      BURNING,
      FROZEN,
      BLIND;
   }
}