package Pulsar.Gear;

import java.awt.*;
import java.util.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import Pulsar.Engine.*;

public interface GearConstants extends WSFontConstants, GUIConstants
{
   public static final int WEAPON_ICON = '}';
   public static final int SHIELD_ICON = ')';
   public static final int ARMOR_ICON = '&';
   public static final int GADGET_ICON = '+';
   public static final int CREDIT_ICON = CENT_TILE;
   
   public static final int DEFAULT_MAX_INVENTORY = 5;
   
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
   
   public enum WeaponType implements WeightedRandomizable
   {
      DEFAULT        ("Unknown Weapon", 0, false),
      MELEE          ("Melee", 0, false),
      BATTLE_RIFLE   ("Battle Rifle", 20, false),
      SHOTGUN        ("Shotgun", 20, false),
      SLUG_RIFLE     ("Slug Rifle", 20, false),
      BEAM           ("Beam Cannon", 5, true),
      PLASMA         ("Plasma Launcher", 5, true);
      
      public String name;
      private int weight;
      private boolean uncommon;
      
      public int getWeight(){return weight;}
      public boolean isUncommon(){return uncommon;}
      
      private WeaponType(String n, int w, boolean u)
      {
         name = n;
         weight = w;
         uncommon = u;
      }
      
      private static WeaponType[] getListByRarity(boolean uncommon)
      {
         Vector<WeaponType> typeList = new Vector<WeaponType>();
         for(WeaponType type : WeaponType.values())
         {
            if(uncommon == type.isUncommon() && type.getWeight() > 0)
               typeList.add(type);
         }
         WeaponType[] typeArray = new WeaponType[typeList.size()];
         for(int i = 0; i < typeArray.length; i++)
            typeArray[i] = typeList.elementAt(i);
         return typeArray;
      }
      
      public static WeaponType[] getCommonList()
      {
         return getListByRarity(false);
      }
      
      public static WeaponType[] getUncommonList()
      {
         return getListByRarity(true);
      }
   }
   
   public enum GadgetSpecialEffect
   {
      HOLOCLONE         ("Holoclone"),
      BREACH            ("Breach"),
      TURRET            ("Turret"),
      COMBAT_DRONE      ("Combat Drone"),
      NAPALM            ("Napalm"),
      MOTION_SENSOR     ("Motion Sensor"),
      CHARM             ("Charm"),
      RECHARGE          ("Quick Charger"),
      ACID_BOMB         ("Acid Bomb");
      
      public String name;
      
      private GadgetSpecialEffect(String n)
      {
         name = n;
      }
   }
   
   public enum LootType implements WeightedRandomizable
   {
      CREDITS  (135),
      WEAPON   (10),
      GADGET   (10),
      ARMOR    (5),
      SHIELD   (5);
      
      private LootType(int w)
      {
         weight = w;
      }
      
      private int weight;
      
      public int getWeight(){return weight;}
   }
   
   public enum LootRarity implements WeightedRandomizable
   {
      COMMON   (80, COMMON_GEAR_COLOR),
      UNCOMMON (17, UNCOMMON_GEAR_COLOR),
      RARE     (3, RARE_GEAR_COLOR);
      
      private LootRarity(int w, Color c)
      {
         weight = w;
         color = c;
      }
      
      private int weight;
      private Color color;
      
      public int getWeight(){return weight;}
      public Color getColor(){return color;}
   }
}