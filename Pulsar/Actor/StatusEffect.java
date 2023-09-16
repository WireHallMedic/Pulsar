package Pulsar.Actor;

import Pulsar.Gear.*;

public class StatusEffect implements GearConstants
{		
   private String name;
	private boolean expires;
	private int remainingDuration;
	private boolean stacks;
	private boolean negative;
	private String descriptor;
	private int moveSpeed;
	private int attackSpeed;
	private int interactSpeed;
	private int ongoingDamage;
	private GearConstants.DamageType damageType;
	private int visionRange;
   private boolean needsAir;


	public String getName(){return name;}
	public boolean getExpires(){return expires;}
	public int getRemainingDuration(){return remainingDuration;}
	public boolean getStacks(){return stacks;}
	public boolean isNegative(){return negative;}
	public String getDescriptor(){return descriptor;}
	public int getMoveSpeed(){return moveSpeed;}
	public int getAttackSpeed(){return attackSpeed;}
	public int getInteractSpeed(){return interactSpeed;}
	public int getOngoingDamage(){return ongoingDamage;}
	public GearConstants.DamageType getDamageType(){return damageType;}
	public int getVisionRange(){return visionRange;}
   public boolean getNeedsAir(){return needsAir;}


	public void setName(String n){name = n;}
	public void setExpires(boolean e){expires = e;}
	public void setRemainingDuration(int r){remainingDuration = r;}
	public void setStacks(boolean s){stacks = s;}
	public void setNegative(boolean n){negative = n;}
	public void setDescriptor(String d){descriptor = d;}
	public void setMoveSpeed(int m){moveSpeed = m;}
	public void setAttackSpeed(int a){attackSpeed = a;}
	public void setInteractSpeed(int i){interactSpeed = i;}
	public void setOngoingDamage(int o){ongoingDamage = o;}
	public void setDamageType(GearConstants.DamageType d){damageType = d;}
	public void setVisionRange(int v){visionRange = v;}
   public void setNeedsAir(boolean na){needsAir = na;}



   public StatusEffect()
   {
      this("Unknown Status Effect", 10);
   }
   
   public StatusEffect(String n, int dur)
   {
      name = n;
      remainingDuration = dur;
      expires = true;
      stacks = false;
      negative = false;
      if(remainingDuration == -1)
         expires = false;
      descriptor = "affect by an unknown status effect";
      moveSpeed = 0;
   	attackSpeed = 0;
   	interactSpeed = 0;
   	ongoingDamage = 0;
   	damageType = null;
   	visionRange = 0;
      needsAir = true;
   }
   
   public boolean shouldCombine(StatusEffect that)
   {
      return this.name.equals(that.name) && !stacks;
   }
   
   public void increment()
   {
      if(expires)
         remainingDuration--;
   }
   
   public boolean isExpired()
   {
      return remainingDuration < 1 && expires;
   }
   
   public void combine(StatusEffect that)
   {
      remainingDuration = Math.max(this.remainingDuration, that.remainingDuration);
   }
   
   // anything that does ongoing thermal damage is a burning effect
   public boolean isBurningEffect()
   {
      return getOngoingDamage() > 0 && getDamageType() == GearConstants.DamageType.THERMAL;
   }
}