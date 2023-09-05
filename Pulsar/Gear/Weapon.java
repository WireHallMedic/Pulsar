package Pulsar.Gear;

public class Weapon extends GearObj implements GearConstants
{	
	private int baseDamage;
	private int variableDamage;
	private int attacks;
	private DamageType damageType;
	private boolean melee;
	private boolean heavy;


	public int getBaseDamage(){return baseDamage;}
	public int getVariableDamage(){return variableDamage;}
	public int getAttacks(){return attacks;}
	public DamageType getDamageType(){return damageType;}
	public boolean isMelee(){return melee;}
	public boolean isHeavy(){return heavy;}


	public void setBaseDamage(int b){baseDamage = b;}
	public void setVariableDamage(int v){variableDamage = v;}
	public void setAttacks(int a){attacks = a;}
	public void setDamageType(DamageType d){damageType = d;}
	public void setMelee(boolean m){melee = m;}
	public void setHeavy(boolean h){heavy = h;}


   public Weapon()
   {
      super(WEAPON_ICON, "Unknown Weapon");
      baseDamage = 5;
      variableDamage = 3;
      attacks = 3;
      damageType = DamageType.KINETIC;
      heavy = false;
      melee = false;
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
      if(heavy)
         tagString += ", Heavy";
      if(melee)
         tagString += ", Melee";
      tagString += "]";
      return tagString;
   }
}