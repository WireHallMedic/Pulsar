package Pulsar.Gear;

public class Shield extends GearObj implements GearConstants, Chargable
{
	private int curCharge;
	private int maxCharge;
	private int chargeDelay;
	private int chargeRate;
	private int timeSinceHit;


	public int getCurCharge(){return curCharge;}
	public int getMaxCharge(){return maxCharge;}
	public int getChargeDelay(){return chargeDelay;}
	public int getChargeRate(){return chargeRate;}
	public int getTimeSinceHit(){return timeSinceHit;}


	public void setCurCharge(int c){curCharge = c;}
	public void setMaxCharge(int m){maxCharge = m;}
	public void setChargeDelay(int c){chargeDelay = c;}
	public void setChargeRate(int c){chargeRate = c;}
	public void setTimeSinceHit(int t){timeSinceHit = t;}

   public Shield()
   {
      super(SHIELD_ICON, "Unknown Shield");
      maxCharge = DEFAULT_SHIELD_MAX_CHARGE;
      chargeDelay = DEFAULT_SHIELD_CHARGE_DELAY;
      chargeRate = DEFAULT_SHIELD_CHARGE_RATE;
      timeSinceHit = 0;
      fullyCharge();
   }
   
   public void fullyCharge()
   {
      curCharge = maxCharge;
   }
   
   public String getSummary()
   {
      return getCurCharge() + "/" + getMaxCharge() + ", Charge Delay: " + getChargeDelay() + ", Charge Rate: " + getChargeRate();
   }
   
   public void charge()
   {
      if(curCharge < maxCharge)
      {
         if(timeSinceHit >= chargeDelay)
         {
            curCharge = Math.min(maxCharge, curCharge + chargeRate);
         }
         else
         {
            timeSinceHit++;
         }
      }
   }
   
   // returns any spillover damage
   public int applyDamage(int damage, DamageType damageType)
   {
      timeSinceHit = 0;
      if(damageType == DamageType.ELECTRO)
      {
         damage += Math.min(curCharge, damage);
      }
      int remainingDamage = 0;
      if(curCharge < damage)
      {
         remainingDamage = damage - curCharge;
         curCharge = 0;
      }
      else
      {
         curCharge = curCharge - damage;
      }
      return remainingDamage;
   }
   
   public boolean hasCharge()
   {
      return curCharge > 0;
   }
}