package Pulsar.Gear;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class WeaponFactory implements GearConstants, ActorConstants
{
   public static Weapon EXPLODING_BARREL = getExplodingBarrel();
   
   public enum WeaponUpgrade implements WeightedRandomizable
   {
      CRYO           (10, "Cryo", "C"),
      THERMAL        (10, "Thermal", "T"),
      ELECTRO        (10, "Electro", "E"),
      HIGH_POWER     (10, "High-Powered", "HP"),
      FAST_CHARGE    (10, "Fast-Charge", "FC"),
      HIGH_CAPACITY  (10, "High Capacity", "HC"),
      VULCAN         (10, "Vulcan", "V"),
      TO_DO_1        (0, "Vulcan", "V"),
      TO_DO_2        (0, "Vulcan", "V"),
      TO_DO_3        (0, "Vulcan", "V");
      
      private WeaponUpgrade(int w, String n, String sn)
      {
         weight = w;
         name = n;
         shortName = sn;
      }
      
      private int weight;
      private String name;
      private String shortName;
      
      public int getWeight(){return weight;}
      public String getName(){return name;}
      public String getShortName(){return shortName;}
      
      public boolean canApply(Weapon weapon)
      {
         switch(this)
         {
            case CRYO            : if(weapon.getDamageType() != DamageType.KINETIC)
                                      return false;
                                   break;
            case THERMAL         : if(weapon.getDamageType() != DamageType.KINETIC)
                                      return false;
                                   break;
            case ELECTRO         : if(weapon.getDamageType() != DamageType.KINETIC)
                                      return false;
                                   break;
            case HIGH_POWER      : break;
            case FAST_CHARGE     : break;
            case HIGH_CAPACITY   : break;
            case VULCAN          : if(weapon.getAttacks() < 2)
                                      return false;
                                   break;
         }
         return true;
      }
   }
   
   public static Weapon getBasicWeapon(WeaponType baseType)
   {
      Weapon w = new Weapon(baseType.name);
      // set default values
      w.setBaseDamage(DEFAULT_BASE_DAMAGE);
      w.setVariableDamage(DEFAULT_VARIABLE_DAMAGE);
      w.setAttacks(1);
      w.setDamageType(DamageType.KINETIC);
      w.setMaxCharge(BASE_WEAPON_CHARGE_COST * 5);
      w.setChargeCost(BASE_WEAPON_CHARGE_COST);
      w.setChargeRate(BASE_WEAPON_CHARGE_RATE);
      switch(baseType)
      {
         case SLUG_RIFLE        :
            w.setBaseDamage(DEFAULT_BASE_DAMAGE * 2);
            w.addWeaponTag(WeaponTag.KNOCKBACK);
            w.setHitDescriptor("shoots");
            break;
         case BATTLE_RIFLE :
            w.setAttacks(3);
            w.setHitDescriptor("shoots");
            break;
         case AUTORIFLE    : 
            w.setBaseDamage(DEFAULT_BASE_DAMAGE / 2);
            w.setAttacks(5);
            w.setHitDescriptor("shoots");
            break;
         case SHOTGUN      :
            w.setChargeCost(BASE_WEAPON_CHARGE_COST * 5 / 4);
            w.setBaseDamage(DEFAULT_BASE_DAMAGE / 2);
            w.setVariableDamage(DEFAULT_VARIABLE_DAMAGE / 2);
            w.setAttacks(5);
            w.addWeaponTag(WeaponTag.SPREAD);
            w.setHitDescriptor("blasts");
            break;
         case MELEE        : 
            w.setName("Unarmed");
            w.setBaseDamage(DEFAULT_BASE_DAMAGE / 2);
            w.setChargeCost(0);
            w.addWeaponTag(WeaponTag.MELEE);
            break;
         case PLASMA       :
            w.setBaseDamage(DEFAULT_BASE_DAMAGE * 4);
            w.setChargeCost(BASE_WEAPON_CHARGE_COST * 2);
            w.setMaxCharge(w.getChargeCost() * 3);
            w.addWeaponTag(WeaponTag.BLAST);
            w.addWeaponTag(WeaponTag.HEAVY);
            w.addWeaponTag(WeaponTag.KNOCKBACK);
            w.setHitDescriptor("blasts");
            break;
         case BEAM       :
            w.setBaseDamage(DEFAULT_BASE_DAMAGE * 4);
            w.setChargeCost(BASE_WEAPON_CHARGE_COST * 2);
            w.setMaxCharge(w.getChargeCost() * 3);
            w.addWeaponTag(WeaponTag.BEAM);
            w.addWeaponTag(WeaponTag.HEAVY);
            w.addWeaponTag(WeaponTag.KNOCKBACK);
            w.setHitDescriptor("blasts");
            break;
      }
      
      w.fullyCharge();
      return w;
   }
   
   // a worse battle rifle
   public static Weapon getCarbine()
   {
      Weapon w = getBasicWeapon(WeaponType.BATTLE_RIFLE);
      w.setAttacks(2);
      w.setName("Carbine");
      return w;
   }
   
   // basic weapon for enemies
   public static Weapon getPistol()
   {
      Weapon w = getBasicWeapon(WeaponType.BATTLE_RIFLE);
      w.setAttacks(1);
      w.setName("Pistol");
      return w;
   }
   
   public static Weapon getAlienClaws()
   {
      Weapon w = getBasicWeapon(WeaponType.MELEE);
      w.setName("Claws");
      w.setBaseDamage(DEFAULT_BASE_DAMAGE);
      w.setHitDescriptor("claws");
      return w;
   }
   
   public static Weapon getAlienSpit()
   {
      Weapon w = getBasicWeapon(WeaponType.DEFAULT);
      w.setName("Flame Spit");
      w.setBaseDamage(DEFAULT_BASE_DAMAGE);
      w.setChargeCost(0);
      w.setDamageType(DamageType.THERMAL);
      w.setStatusEffectChance(.25);
      w.setStatusEffectType(StatusEffectType.BLIND);
      w.setHitDescriptor("spits burning goo on");
      return w;
   }
   
   // used for environmental explosions
   public static Weapon getExplodingBarrel()
   {
      Weapon w = getBasicWeapon(WeaponType.PLASMA);
      w.setName("Exploding Barrel");
      w.addWeaponTag(WeaponTag.KNOCKBACK);
      w.setChargeCost(0);
      return w;
   }
   
   public static Weapon getHarmlessExplosion()
   {
      Weapon w = getExplodingBarrel();
      w.setBaseDamage(0);
      w.setVariableDamage(0);
      w.removeWeaponTag(WeaponTag.HEAVY);
      w.removeWeaponTag(WeaponTag.KNOCKBACK);
      return w;
   }
   
   public static Weapon getGrenadeExplosion(){return getGrenadeExplosion(0);}
   public static Weapon getGrenadeExplosion(int power)
   {
      Weapon w = getExplodingBarrel();
      w.setName("Grenade");
      switch(power)
      {
         case 0 :
            break;
         case 1 :
            w.setBaseDamage((int)(w.getBaseDamage() * 1.5));
            break;
         case 2 :
            w.setBaseDamage(w.getBaseDamage() * 2);
            break;
         default : throw new java.lang.Error("Out of bounds power level: " + power);
      }
      return w;
   }
   
   public static Weapon getCryoExplosion(){return getCryoExplosion(0);}
   public static Weapon getCryoExplosion(int power)
   {
      Weapon w = getExplodingBarrel();
      w.setName("Exploding Cryo Barrel");
      w.setDamageType(DamageType.CRYO);
      w.setStatusEffectType(StatusEffectType.FROZEN);
      w.setStatusEffectChance(1.0);
      switch(power)
      {
         case 0 :
            w.removeWeaponTag(WeaponTag.KNOCKBACK);
            w.removeWeaponTag(WeaponTag.HEAVY);
            break;
         case 1 :
            w.removeWeaponTag(WeaponTag.KNOCKBACK);
            w.removeWeaponTag(WeaponTag.HEAVY);
            w.setBaseDamage((int)(w.getBaseDamage() * 1.5));
            break;
         case 2 :
            w.setBaseDamage(w.getBaseDamage() * 2);
            break;
         default : throw new java.lang.Error("Out of bounds power level: " + power);
      }
      return w;
   }
   
   public static Weapon getEMPExplosion(){return getEMPExplosion(0);}
   public static Weapon getEMPExplosion(int power)
   {
      Weapon w = getExplodingBarrel();
      w.setName(" EMP");
      w.setStatusEffectType(StatusEffectType.DISRUPTED);
      w.setDamageType(DamageType.ELECTRO);
      w.setStatusEffectChance(1.0);
      switch(power)
      {
         case 0 :
            w.setBaseDamage(0);
   	      w.setVariableDamage(0);
            w.removeWeaponTag(WeaponTag.KNOCKBACK);
            w.removeWeaponTag(WeaponTag.HEAVY);
            break;
         case 1 :
            w.removeWeaponTag(WeaponTag.KNOCKBACK);
            w.removeWeaponTag(WeaponTag.HEAVY);
            break;
         case 2 :
            w.setBaseDamage((int)(w.getBaseDamage() * 1.5));
            break;
         default : throw new java.lang.Error("Out of bounds power level: " + power);
      }
      return w;
   }
   
   public static Weapon getNapalm(){return getNapalm(0);}
   public static Weapon getNapalm(int power)
   {
      Weapon w = getExplodingBarrel();
      w.setName("Napalm");
      w.setDamageType(DamageType.THERMAL);
      w.setStatusEffectType(StatusEffectType.BURNING);
      w.setStatusEffectChance(1.0);
      w.setChargeCost(0);
      switch(power)
      {
         case 0 :
            w.removeWeaponTag(WeaponTag.KNOCKBACK);
            w.removeWeaponTag(WeaponTag.HEAVY);
            break;
         case 1 :
            w.removeWeaponTag(WeaponTag.KNOCKBACK);
            w.removeWeaponTag(WeaponTag.HEAVY);
            w.setBaseDamage((int)(w.getBaseDamage() * 1.5));
            break;
         case 2 :
            w.setBaseDamage(w.getBaseDamage() * 2);
            break;
         default : throw new java.lang.Error("Out of bounds power level: " + power);
      }
      return w;
   }
   
   public static void applyRandomUpgrade(Weapon weapon)
   {
      WeightedRandomizer table = new WeightedRandomizer(WeaponUpgrade.values());
      WeaponUpgrade upgrade = (WeaponUpgrade)table.roll();
      while(!upgrade.canApply(weapon))
         upgrade = (WeaponUpgrade)table.roll();
      addUpgrade(weapon, upgrade);
   }
   
   public static void addUpgrade(Weapon weapon, WeaponUpgrade upgrade)
   {
      switch(upgrade)
      {
         case CRYO            : setElementAndStatusEffect(weapon, DamageType.CRYO);
                                break;
         case THERMAL         : setElementAndStatusEffect(weapon, DamageType.THERMAL);
                                break;
         case ELECTRO         : setElementAndStatusEffect(weapon, DamageType.ELECTRO);
                                break;
         case HIGH_POWER      : weapon.setBaseDamage(weapon.getBaseDamage() * 3 / 2);
                                break;
         case FAST_CHARGE     : weapon.setChargeRate(weapon.getChargeRate() * 2);
                                break;
         case HIGH_CAPACITY   : weapon.setMaxCharge(weapon.getMaxCharge() * 3 / 2);
                                break;
         case VULCAN          : weapon.setAttacks(weapon.getAttacks() * 3 / 2);
                                break;
      }
      weapon.setName(upgrade.getName() + " " + weapon.getName());
   }
   
   public static void setElementAndStatusEffect(Weapon w, DamageType type)
   {
      w.setDamageType(type);
      switch(type)
      {
         case KINETIC : w.addWeaponTag(WeaponTag.KNOCKBACK); 
                        break;
         case THERMAL : w.setStatusEffectType(StatusEffectType.BURNING); 
                        w.setStatusEffectChance(.25);
                        break;
         case CRYO    : w.setStatusEffectType(StatusEffectType.FROZEN); 
                        w.setStatusEffectChance(.25);
                        break;
         case ELECTRO : break;
      }
   }
   
   public static Weapon generate()
   {
      return generate(LootFactory.rollLootRarity());
   }
   
   public static Weapon generate(LootRarity rarity)
   {
      Weapon weapon = null;
      int upgrades = 0;
      if(rarity == LootRarity.COMMON)
      {
         WeightedRandomizer table = new WeightedRandomizer(WeaponType.getCommonList());
         weapon = getBasicWeapon((WeaponType)table.roll());
      }
      else
      {
         boolean uncommonWeapon = GameEngine.randomBoolean();
         WeightedRandomizer table = new WeightedRandomizer(WeaponType.getCommonList());
         if(uncommonWeapon)
            table = new WeightedRandomizer(WeaponType.getUncommonList());
         weapon = getBasicWeapon((WeaponType)table.roll());
         if(!uncommonWeapon)
            upgrades++;
      }
      if(rarity == LootRarity.RARE)
         upgrades++;
      for(int i = 0; i < upgrades; i++)
      {
         applyRandomUpgrade(weapon);
      }
      weapon.setColorByRarity(rarity);
      return weapon;
   }
   
   public static void main(String[] args)
   {
      for(int i = 0; i < 20; i++)
      {
         System.out.println(generate().getName());
      }
   }
}