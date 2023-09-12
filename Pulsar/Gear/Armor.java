package Pulsar.Gear;

import Pulsar.Engine.*;
import Pulsar.Actor.*;

public class Armor extends GearObj implements GearConstants, ActorConstants
{	
	private int damageReduction;
	private ActionSpeed speedCap;


	public int getDamageReduction(){return damageReduction;}
	public ActionSpeed getSpeedCap(){return speedCap;}


	public void setDamageReduction(int d){damageReduction = d;}
	public void setSpeedCap(ActionSpeed s){speedCap = s;}


   public Armor()
   {
      super(ARMOR_ICON, "Unknown Armor");
      damageReduction = 0;
      speedCap = ActionSpeed.STANDARD;
   }
   
   public String getSummary()
   {
      return "Damage Reduction: " + damageReduction + ", Max Move Speed: " + speedCap.name;
   }

}