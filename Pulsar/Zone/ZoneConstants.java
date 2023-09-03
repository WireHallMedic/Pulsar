package Pulsar.Zone;

import WidlerSuite.*;

public interface ZoneConstants extends CP437
{
   public enum TILE_TYPE
   {
      CLEAR (MIDDLE_DOT_CHAR, "Clear", true, true, true),
      HIGH_WALL ('#', "Wall", true, true, true),
      NULL (' ', "", false, false, false);
      
      public int iconIndex;
      public String name;
      public boolean lowPassable;
      public boolean highPassable;
      public boolean transparent;
      
      private TILE_TYPE(int i, String n, boolean lp, boolean hp, boolean t)
      {
         iconIndex = i;
         name = n;
         lowPassable = lp;
         highPassable = hp;
         transparent = t;
      }
   }
}