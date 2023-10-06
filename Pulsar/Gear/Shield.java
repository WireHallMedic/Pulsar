package Pulsar.Gear;

import Pulsar.GUI.*;
import Pulsar.Actor.*;

public class Shield extends GearObj implements GearConstants, Chargable
{
	private int curCharge;
	private int maxCharge;
	private int chargeDelay;
	private int chargeRate;
	private int timeSinceHit;
   private StatusEffect statusEffect;
   private GadgetSpecialEffect gadgetEffect;


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
   public void setStatusEffect(StatusEffect se){statusEffect = se;}
   public void setGadgetEffect(GadgetSpecialEffect ge){gadgetEffect = ge;}

   public Shield()
   {
      super(SHIELD_ICON, "Unknown Shield");
      maxCharge = DEFAULT_SHIELD_MAX_CHARGE;
      chargeDelay = DEFAULT_SHIELD_CHARGE_DELAY;
      chargeRate = DEFAULT_SHIELD_CHARGE_RATE;
      timeSinceHit = 0;
      fullyCharge();
      statusEffect = null;
      gadgetEffect = null;
   }
   
   // shield status effects only work when the shield has charge
   public StatusEffect getStatusEffect()
   {
      if(curCharge > 0)
         return statusEffect;
      return null;
   }
   
   // same for gadget effects
   public GadgetSpecialEffect getGadgetEffect()
   {
      if(curCharge > 0)
         return gadgetEffect;
      return null;
   }
   
   public void fullyCharge()
   {
      curCharge = maxCharge;
   }
   
   public void fullyDischarge()
   {
      curCharge = 0;
   }
   
   public String getSummary()
   {
      return getChargeSummary() + ", " + getShortSummary();
   }
   
   public String getChargeSummary()
   {
      return getCurCharge() + "/" + getMaxCharge();
   }
   
   public String getShortSummary()
   {
      String str = "Charge Delay: " + GUITools.initToSec(getChargeDelay()) + 
                   ", Charge Rate: " + getChargeRate();
      boolean suffix = false;
      if(statusEffect != null)
      {
         str += ", " + statusEffect.getName();
         suffix = true;
      }
      if(gadgetEffect != null)
      {
         str += ", " + gadgetEffect.name;
         suffix = true;
      }
      if(suffix)
         str += " when charged.";
      return str;
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