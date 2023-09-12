package Pulsar.Gear;

public class WeaponFactory implements GearConstants
{
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
            w.setHitDescriptor("blasts");
            break;
      }
      
      w.fullyCharge();
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
      Weapon w = getBasicWeapon(WeaponType.SLUG_RIFLE);
      w.setName("Acid Spit");
      w.setBaseDamage(DEFAULT_BASE_DAMAGE);
      w.setChargeCost(0);
      w.setDamageType(DamageType.THERMAL);
      w.setHitDescriptor("spits on");
      return w;
   }
}