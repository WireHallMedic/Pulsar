package Pulsar.Gear;

import java.util.*;

public class Weapon extends GearObj implements GearConstants, Chargable
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


	public int getBaseDamage(){return baseDamage;}
	public int getVariableDamage(){return variableDamage;}
	public int getAttacks(){return attacks;}
	public DamageType getDamageType(){return damageType;}
	public Vector<WeaponTag> getWeaponTags(){return weaponTags;}
	public int getCurCharge(){return curCharge;}
	public int getMaxCharge(){return maxCharge;}
	public int getChargeCost(){return chargeCost;}
	public int getChargeRate(){return chargeRate;}


	public void setBaseDamage(int b){baseDamage = b;}
	public void setVariableDamage(int v){variableDamage = v;}
	public void setAttacks(int a){attacks = a;}
	public void setDamageType(DamageType d){damageType = d;}
	public void setWeaponTags(Vector<WeaponTag> w){weaponTags = w;}
	public void setCurCharge(int c){curCharge = c;}
	public void setMaxCharge(int m){maxCharge = m;}
	public void setChargeCost(int c){chargeCost = c;}
	public void setChargeRate(int r){chargeRate = r;}


   public Weapon()
   {
      super(WEAPON_ICON, "Unknown Weapon");
      baseDamage = 5;
      variableDamage = 3;
      attacks = 3;
      damageType = DamageType.KINETIC;
      weaponTags = new Vector<WeaponTag>();
      int curCharge = 20;
      int maxCharge = 20;
      chargeCost = 4;
      chargeRate = 1;
   }
   
   public void fullyCharge()
   {
      curCharge = maxCharge;
   }
   
   public void charge()
   {
      if(curCharge < maxCharge)
         curCharge++;
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
   
   
   public String getSummary()
   {
      if(attacks == 1)
         return getSingleShotSummary();
      return getMultipleShotSummary();
   }
   
   private String getDamagePerShotString()
   {
      return baseDamage + "-" + (baseDamage + variableDamage - 1);
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
}