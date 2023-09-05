package Pulsar.Gear;

import java.util.*;

public class Weapon extends GearObj implements GearConstants
{	
	private int baseDamage;
	private int variableDamage;
	private int attacks;
	private DamageType damageType;
   private Vector<WeaponTag> weaponTags;


	public int getBaseDamage(){return baseDamage;}
	public int getVariableDamage(){return variableDamage;}
	public int getAttacks(){return attacks;}
	public DamageType getDamageType(){return damageType;}
   public Vector<WeaponTag> getWeaponTags(){return weaponTags;}


	public void setBaseDamage(int b){baseDamage = b;}
	public void setVariableDamage(int v){variableDamage = v;}
	public void setAttacks(int a){attacks = a;}
	public void setDamageType(DamageType d){damageType = d;}
   public void setWeaponTags(Vector<WeaponTag> wt){weaponTags = wt;}


   public Weapon()
   {
      super(WEAPON_ICON, "Unknown Weapon");
      baseDamage = 5;
      variableDamage = 3;
      attacks = 3;
      damageType = DamageType.KINETIC;
      weaponTags = new Vector<WeaponTag>();
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