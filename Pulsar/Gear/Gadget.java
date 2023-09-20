package Pulsar.Gear;

import Pulsar.Engine.*;
import Pulsar.Actor.*;
import Pulsar.AI.*;
import WidlerSuite.*;
import java.util.*;

public class Gadget extends GearObj implements GearConstants, ActorConstants, AIConstants
{
	private int maxUses;
	private int remainingUses;
	private StatusEffectType statusEffectType;
   private String shortName;
   private boolean targetsSelf;
   private Weapon weaponEffect;
   private GadgetSpecialEffect specialEffect;
   private String description;
   private StatusEffect passiveStatusEffect;
   private boolean passiveOnly;
   private boolean placeAdjacent;


	public int getMaxUses(){return maxUses;}
	public int getRemainingUses(){return remainingUses;}
	public StatusEffectType getStatusEffectType(){return statusEffectType;}
   public String getShortName(){return shortName;}
   public boolean getTargetsSelf(){return targetsSelf;}
   public Weapon getWeaponEffect(){return weaponEffect;}
   public GadgetSpecialEffect getSpecialEffect(){return specialEffect;}
   public String getDescription(){return description;}
   public StatusEffect getPassiveStatusEffect(){return passiveStatusEffect;}
   public boolean hasStatusEffect(){return statusEffectType != null;}
   public boolean hasWeaponEffect(){return weaponEffect != null;}
   public boolean hasSpecialEffect(){return specialEffect != null;}
   public boolean hasPassiveStatusEffect(){return passiveStatusEffect != null;}
   public boolean isPassiveOnly(){return passiveOnly;}
   public boolean getPlaceAdjacent(){return placeAdjacent;}


	public void setMaxUses(int m){maxUses = m;}
	public void setRemainingUses(int r){remainingUses = r;}
	public void setStatusEffectType(StatusEffectType s){statusEffectType = s;}
   public void setShortName(String sn){shortName = sn;}
   public void setTargetsSelf(boolean ts){targetsSelf = ts;}
   public void setWeaponEffect(Weapon we){weaponEffect = we;}
   public void setSpecialEffect(GadgetSpecialEffect se){specialEffect = se;}
   public void setDescription(String d){description = d;}
   public void setPassiveStatusEffect(StatusEffect se){passiveStatusEffect = se;}
   public void setPassiveOnly(boolean po){passiveOnly = po;}
   public void setPlaceAdjacent(boolean pa){placeAdjacent = pa;}
   


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
      passiveStatusEffect = null;
      fullyCharge();
      passiveOnly = false;
      placeAdjacent = false;
   }
   
   public String getSummary()
   {
      if(passiveOnly)
         return getName();
      return getName() + " [" + getRemainingUses() + "]";
   }
   
   public String getShortSummary()
   {
      if(passiveOnly)
         return getShortName();
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
         if(getWeaponEffect().hasWeaponTag(WeaponTag.BLAST))
            target = GameEngine.getDetonationLoc(user.getMapLoc(), target);
         else 
            target = GameEngine.getActualTarget(user.getMapLoc(), target);
         Combat.resolveAttack(user, target, getWeaponEffect());
      }
      if(hasSpecialEffect())
      {
         if(getPlaceAdjacent())
         {
            target = EngineTools.getTileTowards(user.getMapLoc(), target);
         }
         if(getSpecialEffect() == GadgetSpecialEffect.HOLOCLONE)
         {
            Actor clone = ActorFactory.getHoloclone();
            target = GameEngine.getActualTarget(user.getMapLoc(), target);
            clone.setAllLocs(GameEngine.getClosestEmptyTile(target));
            clone.setAlertness(Alertness.ALERT);
            GameEngine.add(clone);
         }
         if(getSpecialEffect() == GadgetSpecialEffect.TURRET)
         {
            Actor turret = ActorFactory.getTurret();
            target = GameEngine.getActualTarget(user.getMapLoc(), target);
            turret.setAllLocs(GameEngine.getClosestEmptyTile(target));
            turret.getAI().setTeam(Team.PLAYER);
            turret.setAlertness(Alertness.ALERT);
            GameEngine.add(turret);
         }
         if(getSpecialEffect() == GadgetSpecialEffect.COMBAT_DRONE)
         {
            Actor drone = ActorFactory.getDrone();
            target = GameEngine.getActualTarget(user.getMapLoc(), target);
            drone.setAllLocs(GameEngine.getClosestEmptyTile(target));
            drone.getAI().setTeam(Team.PLAYER);
            drone.getAI().setLeader(user);
            drone.setAlertness(Alertness.ALERT);
            GameEngine.add(drone);
         }
         if(getSpecialEffect() == GadgetSpecialEffect.NAPALM)
         {
            for(int x = -1; x < 2; x++)
            for(int y = -1; y < 2; y++)
            {
               if(getWeaponEffect().hasWeaponTag(WeaponTag.BLAST))
                  target = GameEngine.getDetonationLoc(user.getMapLoc(), target);
               else 
                  target = GameEngine.getActualTarget(user.getMapLoc(), target);
               GameEngine.getZoneMap().tryToIgnite(target.x + x, target.y + y);
            }
         }
      }
   }
}