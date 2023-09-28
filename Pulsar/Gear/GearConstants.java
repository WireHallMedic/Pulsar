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
   
   // weapon defaults
   public static final int DEFAULT_BASE_DAMAGE = 6;
   public static final int DEFAULT_VARIABLE_DAMAGE = 2;
   public static final int BASE_WEAPON_CHARGE_COST = 24;
   public static final int BASE_WEAPON_CHARGE_RATE = 4;
   public static final int SHOTGUN_MAX_RANGE = 8;
   public static final int BEAM_MAX_RANGE = 8;
   
   // shield defaults
   public static final int DEFAULT_SHIELD_MAX_CHARGE = 20;
   public static final int DEFAULT_SHIELD_CHARGE_RATE = 1;
   public static final int DEFAULT_SHIELD_CHARGE_DELAY = 10;
   
   // gadget defaults
   public static final int DEFAULT_GADGET_USES = 3;

   
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
      BEAM         ("Beam"),        // hits actors and low-impassable tiles until it hits a high-impassable or 10 tiles
      KNOCKBACK    ("Knockback"),   // pushes targets hit
      HEAVY        ("Heavy");       // damages terrain
      
      public String name;
      
      private WeaponTag(String n)
      {
         name = n;
      }
   }
   
   public enum WeaponType
   {
      DEFAULT        ("Unknown Weapon"),
      SLUG_RIFLE     ("Slug Rifle"),
      BATTLE_RIFLE   ("Battle Rifle"),
      AUTORIFLE      ("Autorifle"),
      SHOTGUN        ("Shotgun"),
      MELEE          ("Melee"),
      BEAM           ("Beam Cannon"),
      PLASMA         ("Plasma Launcher");
      
      public String name;
      
      private WeaponType(String n)
      {
         name = n;
      }
   }
   
   public enum GadgetSpecialEffect
   {
      HOLOCLONE         ("Holoclone"),
      TURRET            ("Turret"),
      COMBAT_DRONE      ("Combat Drone"),
      NAPALM            ("Napalm"),
      MOTION_SENSOR     ("Motion Sensor"),
      RECHARGE          ("Quick Charger");
      
      public String name;
      
      private GadgetSpecialEffect(String n)
      {
         name = n;
      }
   }
}