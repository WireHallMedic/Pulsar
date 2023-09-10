package Pulsar.Gear;

public class ShieldFactory implements GearConstants
{
   public static Shield getBasicShield()
   {
      Shield s = new Shield();
      s.setName("Basic Shield");
      return s;
   }
   
   public static Shield getHeavyShield()
   {
      Shield s = new Shield();
      s.setName("Heavy Shield");
      s.setMaxCharge(DEFAULT_SHIELD_MAX_CHARGE * 3 / 2);
      s.setChargeDelay(DEFAULT_SHIELD_CHARGE_DELAY * 3 / 2);
      s.fullyCharge();
      return s;
   }
   
   public static Shield getFastShield()
   {
      Shield s = new Shield();
      s.setName("Fastcharge Shield");
      s.setMaxCharge(DEFAULT_SHIELD_MAX_CHARGE * 3 / 4);
      s.setChargeDelay(DEFAULT_SHIELD_CHARGE_DELAY / 2);
      s.setChargeRate(DEFAULT_SHIELD_CHARGE_RATE * 2);
      s.fullyCharge();
      return s;
   }
}