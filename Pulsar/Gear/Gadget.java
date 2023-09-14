package Pulsar.Gear;

import Pulsar.Engine.*;
import Pulsar.Actor.*;
import WidlerSuite.*;

public class Gadget extends GearObj implements GearConstants, ActorConstants
{
	private int maxUses;
	private int remainingUses;
	private StatusEffectType statusEffectType;
   private String shortName;


	public int getMaxUses(){return maxUses;}
	public int getRemainingUses(){return remainingUses;}
	public StatusEffectType getStatusEffectType(){return statusEffectType;}
   public String getShortName(){return shortName;}


	public void setMaxUses(int m){maxUses = m;}
	public void setRemainingUses(int r){remainingUses = r;}
	public void setStatusEffectType(StatusEffectType s){statusEffectType = s;}
   public void setShortName(String sn){shortName = sn;}
   
   public boolean hasStatusEffect(){return statusEffectType != null;}


   public Gadget()
   {
      super(GADGET_ICON, "Unknown Gadget");
      shortName = "Unknown G.";
      maxUses = DEFAULT_GADGET_USES;
      statusEffectType = null;
      fullyCharge();
   }
   
   public String getSummary()
   {
      return getName() + " [" + getRemainingUses() + "]";
   }
   
   public String getShortSummary()
   {
      return getShortName() + "[" + getRemainingUses() + "]";
   }
   
   public void fullyCharge()
   {
      remainingUses = maxUses;
   }
   
   public void discharge()
   {
      remainingUses--;
   }
   
   public boolean canUse()
   {
      return getRemainingUses() > 0;
   }
   
   public void use(Coord target)
   {
      if(hasStatusEffect())
      {
         Actor a = GameEngine.getActorAt(target);
         if(a != null)
            a.add(StatusEffectFactory.getEffect(statusEffectType));
      }
      else
      {
      
      }
   }
}