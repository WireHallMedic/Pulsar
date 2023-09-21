package Pulsar.Zone;

import WidlerSuite.*;

public interface ZoneConstants extends WSFontConstants
{
   public static final int FULL_AIR_PRESSURE = 5;
   public static final int BASE_FIRE_DURATION = 20;
   public static final int RANDOM_FIRE_DURATION = 10;
   public static final int BASE_ICE_DURATION = 30;
   public static final int RANDOM_ICE_DURATION = 10;
   public static final int FIRE_DAMAGE = 3;
   public static final int ACID_DAMAGE = 5;
   
   public enum TileType
   {
      CLEAR       (SMALL_BULLET_TILE, "Clear", true, true, true),
      HIGH_WALL   ('#', "Wall", false, false, false),
      LOW_WALL    ('=', "Low Wall", false, true, true),
      WINDOW      (':', "Window", false, false, true),
      RUBBLE      (',', "Rubble", true, true, true),
      WATER       ('~', "Water", true, true, true),
      ICE         ('-', "Ice", true, true, true),
      TERMINAL    (CAPITAL_OMEGA_TILE, "Terminal", false, true, true),
      NULL        (' ', "", false, false, false);
      
      public int iconIndex;
      public String name;
      public boolean lowPassable;
      public boolean highPassable;
      public boolean transparent;
      
      private TileType(int i, String n, boolean lp, boolean hp, boolean t)
      {
         iconIndex = i;
         name = n;
         lowPassable = lp;
         highPassable = hp;
         transparent = t;
      }
   }
   
   public enum Durability
   {
      STANDARD, FRAGILE, UNBREAKABLE;
      
      public boolean getBreaks(boolean heavyDamage)
      {
         if(heavyDamage)
         {
            switch(this)
            {
               case FRAGILE:     return true;
               case STANDARD:    return true;
               case UNBREAKABLE: return false;
            }
         }
         else
         // not heavy damage
         {
            switch(this)
            {
               case FRAGILE:     return true;
               case STANDARD:    return false;
               case UNBREAKABLE: return false;
            }
         }
         return false;
      }
   }
   
   public enum OnDestructionEffect
   {
      EXPLOSION, FLOOD, FIRE;
   }
   
   // an array keyed to air pressure
   public int[] EXTINGUISH_ARRAY = {1000, 10, 5, 0, 0, 0};
}