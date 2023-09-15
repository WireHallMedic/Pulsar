package Pulsar.Actor;

public interface ActorConstants
{
   public static final int FULLY_CHARGED = 10;
   
   public static final int DEFAULT_VISION_RANGE = 7;
   
   public static final int STATUS_EFFECT_SHORT_DURATION = 10;
   public static final int STATUS_EFFECT_MEDIUM_DURATION = 20;
   public static final int STATUS_EFFECT_LONG_DURATION = 40;
   
   // player-specific constants
   public static final int DEFAULT_MAX_GADGETS = 3;
   
   public enum DeathEffect
   {
      EXPLODE;
   }
   
   
   public enum StatusEffectType
   {
      BURNING     ("Burning"),
      FROZEN      ("Frozen"),
      BLIND       ("Blind"),
      EAGLE_EYE   ("Eagle Eye"),
      HASTE       ("Haste");
      
      public String name;
      
      private StatusEffectType(String n)
      {
         name = n;
      }
   }
}