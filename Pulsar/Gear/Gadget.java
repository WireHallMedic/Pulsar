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
   private boolean targetsSelf;
   private Weapon weaponEffect;
   private GadgetSpecialEffect specialEffect;
   private String description;


	public int getMaxUses(){return maxUses;}
	public int getRemainingUses(){return remainingUses;}
	public StatusEffectType getStatusEffectType(){return statusEffectType;}
   public String getShortName(){return shortName;}
   public boolean getTargetsSelf(){return targetsSelf;}
   public Weapon getWeaponEffect(){return weaponEffect;}
   public GadgetSpecialEffect getSpecialEffect(){return specialEffect;}
   public String getDescription(){return description;}
   public boolean hasStatusEffect(){return statusEffectType != null;}
   public boolean hasWeaponEffect(){return weaponEffect != null;}
   public boolean hasSpecialEffect(){return specialEffect != null;}


	public void setMaxUses(int m){maxUses = m;}
	public void setRemainingUses(int r){remainingUses = r;}
	public void setStatusEffectType(StatusEffectType s){statusEffectType = s;}
   public void setShortName(String sn){shortName = sn;}
   public void setTargetsSelf(boolean ts){targetsSelf = ts;}
   public void setWeaponEffect(Weapon we){weaponEffect = we;}
   public void setSpecialEffect(GadgetSpecialEffect se){specialEffect = se;}
   public void setDescription(String d){description = d;}
   


   public Gadget()
   {
      super(GADGET_ICON, "Unknown Gadget");
      shortName = "Unknown G.";
      maxUses = DEFAULT_GADGET_USES;
      statusEffectType = null;
      targetsSelf = false;
      weaponEffect = null;
      specialEffect = null;
      description = "No description.";
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
   
   public void use(Actor user, Coord target)
   {
      if(hasStatusEffect())
      {
         Actor a = GameEngine.getActorAt(target);
         if(a != null)
            a.add(StatusEffectFactory.getEffect(statusEffectType));
      }
      if(hasWeaponEffect())
      {
         Combat.resolveAttack(user, target, getWeaponEffect());
      }
      if(hasSpecialEffect())
      {
         if(getSpecialEffect() == GadgetSpecialEffect.HOLOCLONE)
         {
            Actor clone = ActorFactory.getHoloclone();
            clone.setAllLocs(GameEngine.getClosestEmptyTile(target));
            GameEngine.add(clone);
         }
      }
   }
}