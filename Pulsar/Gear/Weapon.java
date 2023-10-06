package Pulsar.Gear;

import java.util.*;
import Pulsar.Actor.*;
import Pulsar.GUI.*;

public class Weapon extends GearObj implements GearConstants, Chargable, ActorConstants
{	
   private int baseDamage;
	private int variableDamage;
	private int attacks;
	private DamageType damageType;
	private Vector<WeaponTag> weaponTags;
	private int curCharge;
	private int maxCharge;
	private int chargeCost;
	private int chargeRate;
   private String hitDescriptor;
   private double statusEffectChance;
   private StatusEffectType statusEffectType;


	public int getBaseDamage(){return baseDamage;}
	public int getVariableDamage(){return variableDamage;}
	public int getAttacks(){return attacks;}
	public DamageType getDamageType(){return damageType;}
	public Vector<WeaponTag> getWeaponTags(){return weaponTags;}
	public int getCurCharge(){return curCharge;}
	public int getMaxCharge(){return maxCharge;}
	public int getChargeCost(){return chargeCost;}
	public int getChargeRate(){return chargeRate;}
   public String getHitDescriptor(){return hitDescriptor;}
   public double getStatusEffectChance(){return statusEffectChance;}
   public StatusEffectType getStatusEffectType(){return statusEffectType;}


	public void setBaseDamage(int b){baseDamage = b;}
	public void setVariableDamage(int v){variableDamage = v;}
	public void setAttacks(int a){attacks = a;}
	public void setDamageType(DamageType d){damageType = d;}
	public void setWeaponTags(Vector<WeaponTag> w){weaponTags = w;}
	public void setCurCharge(int c){curCharge = c;}
	public void setMaxCharge(int m){maxCharge = m;}
	public void setChargeCost(int c){chargeCost = c;}
	public void setChargeRate(int r){chargeRate = r;}
   public void setHitDescriptor(String hd){hitDescriptor = hd;}
   public void setStatusEffectChance(double sec){statusEffectChance = sec;}
   public void setStatusEffectType(StatusEffectType set){statusEffectType = set;}
   
   public boolean inflictsStatusEffect(){return statusEffectChance > 0.0 && statusEffectType != null;}


   public Weapon(){this("Unknown Weapon");}
   public Weapon(String name)
   {
      super(WEAPON_ICON, name);
      baseDamage = 5;
      variableDamage = 3;
      attacks = 3;
      damageType = DamageType.KINETIC;
      weaponTags = new Vector<WeaponTag>();
      curCharge = 20;
      maxCharge = 20;
      chargeCost = 4;
      chargeRate = 1;
      hitDescriptor = "strikes";
      statusEffectChance = 0.0;
      statusEffectType = null;
   }
   
   public void fullyCharge()
   {
      curCharge = maxCharge;
   }
   
   public void fullyDischarge()
   {
      curCharge = 0;
   }
   
   public void charge()
   {
      if(curCharge < maxCharge)
         curCharge += chargeRate;
   }
   
   public boolean canFire()
   {
      return getCurCharge() >= getChargeCost();
   }
   
   public void discharge()
   {
      setCurCharge(getCurCharge() - getChargeCost());
   }
   
   public void addWeaponTag(WeaponTag tag)
   {
      if(!weaponTags.contains(tag))
         weaponTags.add(tag);
   }
   
   public void removeWeaponTag(WeaponTag tag)
   {
      weaponTags.remove(tag);
   }
   
   public boolean hasWeaponTag(WeaponTag tag)
   {
      for(WeaponTag listTag : weaponTags)
      {
         if(listTag == tag)
            return true;
      }
      return false;
   }
   
   public boolean isMelee()
   {
      return hasWeaponTag(WeaponTag.MELEE);
   }
   
   public boolean isHeavy()
   {
      return hasWeaponTag(WeaponTag.HEAVY);
   }
   
   public int getRemainingShots()
   {
      if(getChargeCost() == 0)
         return 1;
      return getCurCharge() / getChargeCost();
   }
   
   public int getMaxShots()
   {
      return getMaxCharge() / getChargeCost();
   }
   
   
   public String getSummary()
   {
      String str = "";
      if(attacks == 1)
         str = getSingleShotSummary();
      else
         str = getMultipleShotSummary();
      return str + " " + getStatusEffectString();
   }
   
   public String getAmmoCount()
   {
      if(getChargeCost() == 0)
         return (char)INFINITY_TILE + "/" + (char)INFINITY_TILE;
      return getRemainingShots() + "/" + getMaxShots();
   }
   
   public int[] getAmmoCountTiles()
   {
      int[] returnArr = new int[5];
      if(getChargeCost() == 0)
      {
         returnArr[0] = ' ';
         returnArr[1] = INFINITY_TILE;
         returnArr[2] = '/';
         returnArr[3] = ' ';
         returnArr[4] = INFINITY_TILE;
      }
      else
      {
         if(getRemainingShots() < 10)
            returnArr[0] = ' ';
         else
            returnArr[0] = (getRemainingShots() / 10) + '0';
         returnArr[1] = (getRemainingShots() % 10) + '0';
         returnArr[2] = '/';
         if(getMaxShots() < 10)
         {
            returnArr[3] = (getMaxShots() % 10) + '0';;
            returnArr[4] = ' ';
         }
         else
         {
            returnArr[3] = (getMaxShots() / 10) + '0';
            returnArr[4] = (getMaxShots() % 10) + '0';
         }
      }
      return returnArr;
   }
   
   private String getDamagePerShotString()
   {
      if(variableDamage == 0)
         return "" + baseDamage;
      return baseDamage + "-" + (baseDamage + variableDamage);
   }
   
   private String getSingleShotSummary()
   {
      return getDamagePerShotString() + " " + getTagString();
   }
   
   private String getMultipleShotSummary()
   {
      return "(" + getDamagePerShotString() + ")x" + attacks + " " + getTagString();
   }
   
   private String getTagString()
   {
      String tagString = "[" + damageType.name;
      // based on enum rather than just iterating the list for constant order
      for(WeaponTag tag : WeaponTag.values())
      {
         if(hasWeaponTag(tag))
            tagString += ", " + tag.name;
      }
      tagString += "]";
      return tagString;
   }
   
   private String getStatusEffectString()
   {
      if(getStatusEffectType() == null)
         return "";
      return GUITools.doubleToPercent(getStatusEffectChance()) + " chance to inflict " + 
             getStatusEffectType().name + ".";
   }
}