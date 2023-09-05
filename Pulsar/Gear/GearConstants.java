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
      KINETIC  ("Kinetic"), 
      THERMAL  ("Thermal"), 
      CRYO     ("Cryo"), 
      ELECTRO  ("Electro");
      
      public String name;
      
      private DamageType(String n)
      {
         name = n;
      }
   }
   
   public enum WeaponTag
   {
      SPREAD       ("Spread"),
      MELEE        ("Melee"),
      BLAST        ("Blast"),
      LARGE_BLAST  ("Large Blast"),
      HEAVY        ("Heavy");
      
      public String name;
      
      private WeaponTag(String n)
      {
         name = n;
      }
   }
}