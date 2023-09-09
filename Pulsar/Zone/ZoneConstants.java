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
}