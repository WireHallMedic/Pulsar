package Pulsar.Gear;

import Pulsar.Engine.*;
import Pulsar.Actor.*;

public class Armor extends GearObj implements GearConstants, ActorConstants
{	
	private int damageReduction;
	private ActionSpeed speedCap;
   private StatusEffect statusEffect;


	public int getDamageReduction(){return damageReduction;}
	public ActionSpeed getSpeedCap(){return speedCap;}
   public StatusEffect getStatusEffect(){return statusEffect;}
   public boolean hasStatusEffect(){return statusEffect != null;}


	public void setDamageReduction(int d){damageReduction = d;}
	public void setSpeedCap(ActionSpeed s){speedCap = s;}
   public void setStatusEffect(StatusEffect se){statusEffect = se;}


   public Armor()
   {
      super(ARMOR_ICON, "Unknown Armor");
      damageReduction = 0;
      speedCap = ActionSpeed.STANDARD;
      statusEffect = null;
   }
   
   public String getSummary()
   {
      String str =  "Damage Reduction: " + damageReduction + ", Max Move Speed: " + speedCap.name;
      if(hasStatusEffect())
         str += ", " + getStatusEffectString();
      return str;
   }
   
   public String getStatusEffectString()
   {
      if(!hasStatusEffect())
         return "";
      else
         return getStatusEffect().getName();
   }

}