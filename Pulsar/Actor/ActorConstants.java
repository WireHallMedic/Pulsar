package Pulsar.Actor;

import Pulsar.Engine.*;

public interface ActorConstants
{
   public static final int FULLY_CHARGED = 10;
   
   public static final int DEFAULT_VISION_RANGE = 7;
   
   public static final int STATUS_EFFECT_SHORT_DURATION = 10;
   public static final int STATUS_EFFECT_MEDIUM_DURATION = 20;
   public static final int STATUS_EFFECT_LONG_DURATION = 40;
   
   // player-specific constants
   public static final int DEFAULT_MAX_GADGETS = 3;
   
   public static final double BIOLOGICAL_FIRE_CHANCE = .5;
   public static final double MECHANICAL_FIRE_CHANCE = .25;
   
   // generation constants
   public static final double HIGH_DENSITY = .05;
   public static final double MEDIUM_DENSITY = .03;
   public static final double LOW_DENSITY = .015;
   
   public enum DeathEffect
   {
      EXPLODE;
   }
   
   
   public enum StatusEffectType
   {
      BURNING       ("Burning"),
      FROZEN        ("Frozen"),
      BLIND         ("Blind"),
      EAGLE_EYE     ("Eagle Eye"),
      HASTE         ("Haste"),
      AIR_SUPPLY    ("Air Supply");
      
      public String name;
      
      private StatusEffectType(String n)
      {
         name = n;
      }
   }
   
   public enum AlienType implements WeightedRandomizable
   {
      ALIEN_LARVA       (5),
      ALIEN_WORKER      (20),
      ALIEN_SOLDIER     (5),
      ALIEN_HUNTER      (5),
      ALIEN_QUEEN       (0);
      
      private AlienType(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
   }
   
   public enum PirateType implements WeightedRandomizable
   {
      PIRATE_GRUNT       (30),
      PIRATE_COMMANDO    (10),
      PIRATE_DRONE       (0),
      PIRATE_OFFICER     (5);
      
      private PirateType(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
   }
}