package Pulsar.Gear;

import java.awt.*;
import WidlerSuite.*;

public interface GearConstants extends WSFontConstants
{
   public static final int WEAPON_ICON = '}';
   public static final int SHIELD_ICON = ')';
   public static final int ARMOR_ICON = '&';
   public static final int GADGET_ICON = '+';
   public static final int CREDIT_ICON = CENT_TILE;
   
   public static final Color DEFAULT_WEAPON_COLOR = Color.WHITE;
   
   public enum DamageType
   {
      KINETIC  ("Kinetic"),      // basic damage type
      THERMAL  ("Thermal"),      // good against organics, bad against inorganics, causes fires
      CRYO     ("Cryo"),         // slows, freezes things, puts out fires
      ELECTRO  ("Electro");      // good against inorganics and shields, interacts with liquids
      
      public String name;
      
      private DamageType(String n)
      {
         name = n;
      }
   }
   
   public enum WeaponTag
   {
      SPREAD       ("Spread"),      // fires in a spread like a shotgun
      MELEE        ("Melee"),       // attacks against adjacent actors only
      BLAST        ("Blast"),       // 1-tile radius explosion
      LARGE_BLAST  ("Large Blast"), // 2-tile radius explosion
      HEAVY        ("Heavy");       // damages terrain
      
      public String name;
      
      private WeaponTag(String n)
      {
         name = n;
      }
   }
}