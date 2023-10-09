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
      EXPLODE, BREACH;
   }
   
   
   public enum StatusEffectType
   {
      BURNING       ("Burning"),
      FROZEN        ("Frozen"),
      BLIND         ("Blind"),
      EAGLE_EYE     ("Eagle Eye"),
      HASTE         ("Haste"),
      AIR_SUPPLY    ("Air Supply"),
      CHARMED       ("Charmed"),
      DISRUPTED     ("Disrupted");
      
      public String name;
      
      private StatusEffectType(String n)
      {
         name = n;
      }
   }
   
   public abstract interface EnemyType extends WeightedRandomizable
   {
      public int getMinGroupSize();
      public int getMaxGroupSize();
      public abstract EnemyType getMinion();
   }
   
   public enum AlienType implements EnemyType
   {
      ALIEN_LARVA       (5,  4, 8),
      ALIEN_WORKER      (20, 3, 5),
      ALIEN_SOLDIER     (5,  1, 2),
      ALIEN_HUNTER      (5,  1, 3),
      ALIEN_QUEEN       (0,  1, 1);
      
      private AlienType(int w, int min, int max)
      {
         weight = w;
         minGroupSize = min;
         maxGroupSize = max;
      }
      
      private int weight;
      private int minGroupSize;
      private int maxGroupSize;
      
      public int getWeight(){return weight;}
      public int getMinGroupSize(){return minGroupSize;}
      public int getMaxGroupSize(){return maxGroupSize;}
      
      public EnemyType getMinion(){return ALIEN_WORKER;}
   }
   
   public enum PirateType implements EnemyType
   {
      PIRATE_GRUNT       (30, 3, 5),
      PIRATE_COMMANDO    (10, 1, 2),
      PIRATE_DRONE       (0,  1, 1),
      PIRATE_OFFICER     (5,  1, 1);
      
      private PirateType(int w, int min, int max)
      {
         weight = w;
         minGroupSize = min;
         maxGroupSize = max;
      }
      
      private int weight;
      private int minGroupSize;
      private int maxGroupSize;
      
      public int getWeight(){return weight;}
      public int getMinGroupSize(){return minGroupSize;}
      public int getMaxGroupSize(){return maxGroupSize;}
      
      public EnemyType getMinion(){return PIRATE_DRONE;}
   }
}