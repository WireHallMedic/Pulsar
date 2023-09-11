package Pulsar.Gear;

import Pulsar.Engine.*;
import Pulsar.Actor.*;

public class Armor extends GearObj implements GearConstants, ActorConstants
{	
	private int damageReduction;
	private int speedCap;


	public int getDamageReduction(){return damageReduction;}
	public int getSpeedCap(){return speedCap;}


	public void setDamageReduction(int d){damageReduction = d;}
	public void setSpeedCap(int s){speedCap = s;}


   public Armor()
   {
      super(ARMOR_ICON, "Unknown Armor");
      damageReduction = 0;
      speedCap = FAST_ACTION_COST;
   }
   
   public String getSummary()
   {
      return "Damage Reduction: " + damageReduction + ", Max Move Speed: " + EngineTools.getSpeedString(speedCap);
   }

}