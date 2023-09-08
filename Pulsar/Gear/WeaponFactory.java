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
      w.setMaxCharge(100);
      w.setChargeCost(20);
      w.setChargeRate(4);
      switch(baseType)
      {
         case RIFLE        :
            w.setBaseDamage(DEFAULT_BASE_DAMAGE * 2);
            break;
         case BATTLE_RIFLE :
            w.setAttacks(3);
            break;
         case AUTORIFLE    : 
            w.setBaseDamage(DEFAULT_BASE_DAMAGE / 2);
            w.setAttacks(5);
            break;
         case SHOTGUN      :
            w.setChargeCost(25);
            w.addWeaponTag(WeaponTag.SPREAD);
            break;
         case MELEE        : 
            w.setName("Unarmed");
            w.setBaseDamage(DEFAULT_BASE_DAMAGE / 2);
            w.setChargeCost(0);
            w.addWeaponTag(WeaponTag.MELEE);
            break;
         case PLASMA       :
            w.setBaseDamage(DEFAULT_BASE_DAMAGE * 2);
            w.setChargeCost(30);
            w.addWeaponTag(WeaponTag.BLAST);
            w.addWeaponTag(WeaponTag.HEAVY);
            break;
      }
      
      w.fullyCharge();
      return w;
   }
}