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
   public static final int MAX_CLOSET_SIZE = 16;
   
   public static final int NORTH = 0;
   public static final int EAST = 1;
   public static final int SOUTH = 2;
   public static final int WEST = 3;
   
   public static final char TEMPLATE_VACUUM = 'V';
   public static final char TEMPLATE_CLEAR = '.';
   public static final char TEMPLATE_WALL = '#';
   public static final char TEMPLATE_DOOR = '/';
   public static final char TEMPLATE_OOB = 'X';
   public static final char TEMPLATE_BARREL = 'b';
   public static final char TEMPLATE_WATER_BARREL = 'w';
   public static final char TEMPLATE_EXPLODING_BARREL = 'e';
   public static final char TEMPLATE_CRATE = 'c';
   public static final char TEMPLATE_LOOT_CRATE = '$';
   public static final char TEMPLATE_TABLE = '=';
   public static final char TEMPLATE_RUBBLE = ',';
   public static final char TEMPLATE_WATER = '~';
   public static final char TEMPLATE_ACID = 'a';
   public static final char TEMPLATE_BUTTON = '!';
   public static final char TEMPLATE_WINDOW = ':';
   public static final char TEMPLATE_EXIT = '>';
   public static final char TEMPLATE_TERMINAL = 'T';
   public static final char TEMPLATE_SPAWN_POINT = 'S';
   
   public static final char TEMPLATE_OBSTACLE = '?';
      
   public enum TileType
   {
      CLEAR       (SMALL_BULLET_TILE, "Clear", true, true, true),
      HIGH_WALL   ('#', "Wall", false, false, false),
      LOW_WALL    ('=', "Low Wall", false, true, true),
      WINDOW      (':', "Window", false, false, true),
      RUBBLE      (',', "Rubble", true, true, true),
      WATER       ('~', "Water", true, true, true),
      ACID        ('~', "Acid", true, true, true),
      FIRE        ('^', "Fire", true, true, true),
      ICE         ('-', "Ice", true, true, true),
      DOOR        ('|', "Door", false, false, false),
      TERMINAL    (CAPITAL_OMEGA_TILE, "Terminal", false, true, true),
      CRATE       ('#', "Crate", false, false, false),
      VACUUM      (' ', "Vacuum", false, false, true),
      EXIT        (DIAMOND_TILE, "Exit", true, true, true),
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
      STANDARD, FRAGILE, REINFORCED, UNBREAKABLE;
      
      public boolean getBreaks(boolean heavyDamage)
      {
         if(heavyDamage)
         {
            switch(this)
            {
               case FRAGILE:     return true;
               case STANDARD:    return true;
               case REINFORCED:  return false;
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
               case REINFORCED:  return false;
               case UNBREAKABLE: return false;
            }
         }
         return false;
      }
   }
   
   public enum OnDestructionEffect
   {
      EXPLOSION, FLOOD_WATER, FLOOD_ACID;
      
      public boolean isFloodEffect()
      {
         if(this == FLOOD_WATER ||
            this == FLOOD_ACID)
            return true;
         return false;
      }
   }
   
   // an array keyed to air pressure
   public int[] EXTINGUISH_ARRAY = {1000, 10, 5, 0, 0, 0};
   
   
}