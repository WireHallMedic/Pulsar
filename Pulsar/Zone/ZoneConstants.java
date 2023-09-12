package Pulsar.Zone;

import WidlerSuite.*;

public interface ZoneConstants extends CP437
{
   public enum TileType
   {
      CLEAR       (250, "Clear", true, true, true),
      HIGH_WALL   ('#', "Wall", false, false, false),
      LOW_WALL    ('=', "Low Wall", false, true, true),
      WINDOW      (':', "Window", false, false, true),
      RUBBLE      (',', "Rubble", true, true, true),
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
      
      public double getBreakChance(boolean heavyDamage)
      {
         if(heavyDamage)
         {
            switch(this)
            {
               case STANDARD:    return .75;
               case FRAGILE:     return 1.0;
               case UNBREAKABLE: return 0.0;
            }
         }
         else
         // not heavy damage
         {
            switch(this)
            {
               case STANDARD:    return 0.0;
               case FRAGILE:     return .75;
               case UNBREAKABLE: return 0.0;
            }
         }
         return 0.0;
      }
   }
   
   public enum OnDestructionEffect
   {
      EXPLOSION, VACCUUM, FIRE;
   }
}