package Pulsar.Actor;

import WidlerSuite.*;
import Pulsar.Engine.*;
import Pulsar.Gear.*;
import Pulsar.Zone.*;
import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;
import java.util.*;


public class Actor implements ActorConstants, GUIConstants, AIConstants, EngineConstants, GearConstants
{
   protected String name;
	protected Coord mapLoc;
	protected UnboundTile sprite;
   protected int turnEnergy;
   protected BasicAI ai;
   protected ActorMemory memory;
   protected int visionRange;
   protected Shield shield;
   protected Weapon weapon;
   protected Armor armor;
   protected int curHealth;
   protected int maxHealth;
   protected Color bloodColor;
   protected Weapon unarmed;
   protected boolean startOfTurnPerformed;
   protected boolean flying;
   protected ActionSpeed attackSpeed;
   protected ActionSpeed moveSpeed;
   protected ActionSpeed interactSpeed;
   protected Alertness alertness;
   protected AlertnessManager alertnessManager;
   protected Vector<StatusEffect> tempStatusEffectList;
   protected DeathEffect deathEffect;
   protected boolean biological;
   protected boolean mechanical;
   protected boolean needsAir;
   protected boolean onFire;
   protected boolean forcedMovementSinceLastTurn;


   public String getName(){return name;}
	public Coord getMapLoc(){return new Coord(mapLoc);}
	public UnboundTile getSprite(){return sprite;}
   public int getTurnEnergy(){return turnEnergy;}
   public BasicAI getAI(){return ai;}
   public ActorMemory getMemory(){return memory;}
   public int getBaseVisionRange(){return visionRange;}
   public Shield getShield(){return shield;}
   public Armor getArmor(){return armor;}
   public int getCurHealth(){return curHealth;}
   public int getMaxHealth(){return maxHealth;}
   public Color getBloodColor(){return bloodColor;}
   public Weapon getUnarmedAttack(){return unarmed;}
   public boolean isFlying(){return flying;}
   public ActionSpeed getBaseMoveSpeed(){return moveSpeed;}
   public ActionSpeed getBaseAttackSpeed(){return attackSpeed;}
   public ActionSpeed getBaseInteractSpeed(){return interactSpeed;}
   public Alertness getAlertness(){return alertness;}
   public AlertnessManager getAlertnessManager(){return alertnessManager;}
   public Vector<StatusEffect> getTempStatusEffectList(){return tempStatusEffectList;}
   public DeathEffect getDeathEffect(){return deathEffect;}
   public boolean isBiological(){return biological;}
   public boolean isMechanical(){return mechanical;}
   public boolean isOnFire(){return onFire;}
   public boolean didForcedMovement(){return forcedMovementSinceLastTurn;}


   public void setName(String n){name = n;}
	public void setMapLoc(Coord m){setMapLoc(m.x, m.y);}
	public void setMapLoc(int x, int y){mapLoc = new Coord(x, y);}
	public void setSprite(UnboundTile s){sprite = s;}
   public void setTurnEnergy(int te){turnEnergy = te;}
   public void setAI(BasicAI newAI){ai = newAI;}
   public void setMemory(ActorMemory m){memory = m;}
   public void setVisionRange(int vr){visionRange = vr;}
   public void setShield(Shield s){shield = s;}
   public void setWeapon(Weapon w){weapon = w;}
   public void setArmor(Armor a){armor = a;}
   public void setCurHealth(int h){curHealth = h;}
   public void setMaxHealth(int h){maxHealth = h;}
   public void setBloodColor(Color c){bloodColor = c;}
   public void setUnarmedAttack(Weapon w){unarmed = w;}
   public void setFlying(boolean f){flying = f;}
   public void setAttackSpeed(ActionSpeed as){attackSpeed = as;}
   public void setMoveSpeed(ActionSpeed ms){moveSpeed = ms;}
   public void setInteractSpeed(ActionSpeed is){interactSpeed = is;}
   public void setAlertness(Alertness a){alertness = a;}
   public void setAlertnessManager(AlertnessManager am){alertnessManager = am;}
   public void setTempStatusEffectList(Vector<StatusEffect> list){tempStatusEffectList = list; burningCheck();}
   public void setDeathEffect(DeathEffect de){deathEffect = de;}
   public void setBiological(boolean b){biological = b;}
   public void setMechanical(boolean m){mechanical = m;}
   public void setNeedsAir(boolean na){needsAir = na;}
   public void setOnFire(boolean of){onFire = of;}
   public void setDidForcedMovement(boolean dfm){forcedMovementSinceLastTurn = dfm;}


   public Actor(int icon){this(icon, "Unknown Actor");}
   public Actor(int icon, String n)
   {
      name = n;
      mapLoc = new Coord(-1, -1);
      createSprite(icon, DEFAULT_ACTOR_FG_COLOR.getRGB(), DEFAULT_ACTOR_BG_COLOR.getRGB());
      turnEnergy = FULLY_CHARGED - ActionSpeed.SLOW.timeCost;
      ai = new BasicAI(this);
      memory = new ActorMemory(this);
      visionRange = DEFAULT_VISION_RANGE;
      setShield(null);
      setWeapon(null);
      setCurHealth(20);
      setMaxHealth(20);
      setBloodColor(GUIConstants.HUMAN_BLOOD);
      unarmed = WeaponFactory.getBasicWeapon(GearConstants.WeaponType.MELEE);
      startOfTurnPerformed = false;
      flying = false;
      attackSpeed = ActionSpeed.STANDARD;
      moveSpeed = ActionSpeed.STANDARD;
      interactSpeed = ActionSpeed.STANDARD;
      alertness = Alertness.RELAXED;
      alertnessManager = new AlertnessManager(this);
      tempStatusEffectList = new Vector<StatusEffect>();
      deathEffect = null;
      biological = true;
      mechanical = false;
      needsAir = true;
      onFire = false;
      forcedMovementSinceLastTurn = false;
   }
   
   public String getInspectionString()
   {
      String outStr = "";
      Vector<String> strVector = new Vector<String>();
      if(isFlying())
         strVector.add("Flying");
      if(getNeedsAir() && GameEngine.getZoneMap().getTile(getMapLoc()).getAirPressure() == 0)
         strVector.add("Suffocating");
      for(StatusEffect se : getAllStatusEffects())
         strVector.add(se.getName());
      for(String str : strVector)
      {
         outStr += ", " + str;
      }
      if(outStr.length() > 2)
         outStr = outStr.substring(2);
      return outStr;
   }
   
   public String getMood(Actor that)
   {
      String str = "";
      if(getAI().isHostile(that))
         str = "Hostile, ";
      else if(getAI().isFriendly(that))
         str = "Friendly, ";
      else
         str = "Indifferent, ";
      str += alertness.name;
      return str;
   }
   
   
   public void setColor(Color c)
   {
      createSprite(getSprite().getIconIndex(), c.getRGB(), DEFAULT_ACTOR_BG_COLOR.getRGB());
   }
   
   public void reconcileSprite()
   {
      sprite.setXLoc(mapLoc.x);
      sprite.setYLoc(mapLoc.y);
      sprite.setXOffset(0.0);
      sprite.setYOffset(0.0);
   }
   
   @Override
   public String toString()
   {
      return getName();
   }
   
   public void createSprite(int icon, int fgColor, int bgColor)
   {
      sprite = generateUnboundTile(icon, fgColor, bgColor);
      sprite.setAffectedByAge(false);
   }
   
   public UnboundTile generateUnboundTile(int icon, int fgColor, int bgColor)
   {
      return GUIConstants.SQUARE_TILE_PALETTE.getUnboundTile(icon, fgColor, bgColor, OuterPanel.getSizeMultiplier(), UnboundTile.CIRCLE_BACKGROUND);
   }
   
   // location stuff
   ///////////////////////////////////////////////////////////////////////////////
   
   public void setAllLocs(Coord l){setAllLocs(l.x, l.y);}
   public void setAllLocs(int x, int y)
   {
      setMapLoc(x, y);
      setSpriteLoc(x, y);
   }
   
   public void setSpriteLoc(Coord l){setSpriteLoc(l.x, l.y);}
   public void setSpriteLoc(int x, int y)
   {
      sprite.setXLoc(x);
      sprite.setYLoc(y);
      sprite.setXOffset(0.0);
      sprite.setYOffset(0.0);
   }
   
   public int getSpriteXLoc()
   {
      return sprite.getXLoc();
   }
   
   public int getSpriteYLoc()
   {
      return sprite.getYLoc();
   }
   
   public double getSpriteXOffset()
   {
      return sprite.getXOffset();
   }
   
   public double getSpriteYOffset()
   {
      return sprite.getYOffset();
   }
   
   public boolean canStep(Coord c){return canStep(c.x, c.y);}
   public boolean canStep(int x, int y)
   {
      if(GameEngine.isActorAt(x, y))
         return false;
      MapTile tile = GameEngine.getZoneMap().getTile(x, y);
      if(isFlying() && tile.isHighPassable())
         return true;
      if(tile.isLowPassable())
         return true;
      return false;
   }
   
   public boolean canSee(int x, int y){return canSee(new Coord(x, y));}
   public boolean canSee(Actor target){return canSee(target.getMapLoc());}
   public boolean canSee(Coord target)
   {
      return WSTools.getAngbandMetric(getMapLoc(), target) <= getVisionRange() &&
         GameEngine.getZoneMap().canSee(getMapLoc(), target, getVisionRange() + 1);
   }
   
   // team stuff
   ///////////////////////////////////////////////////////////////////////////////
   
   public boolean isHostile(Actor that)
   {
      return this.getAI().isHostile(that);
   }
   
   public boolean isFriendly(Actor that)
   {
      return this.getAI().isFriendly(that);
   }
   
   // gear methods
   ////////////////////////////////////////////////////////////////////
   public boolean shieldIsUp()
   {
      return hasShield() && getShield().hasCharge();
   }
   
   public boolean hasShield()
   {
      return getShield() != null;
   }
   
   public boolean hasWeapon()
   {
      return weapon != null;
   }
   
   public boolean hasArmor()
   {
      return armor != null;
   }
   
   public int getDamageReduction()
   {
      if(hasArmor())
         return armor.getDamageReduction();
      return 0;
   }
   
   public Weapon getWeapon()
   {
      if(hasWeapon())
         return weapon;
      return unarmed;
   }
   
   public int[] getShieldBar(int length)
   {
      if(hasShield())
      {
         return GUITools.getBar(getShield().getCurCharge(), getShield().getMaxCharge(), length);
      }
      else
      {
         return GUITools.getBar(0, 20, length);
      }
   }
   
   public int[] getWeaponBar(int length)
   {
      return GUITools.getBar(getWeapon().getCurCharge(), getWeapon().getMaxCharge(), length);
   }
   
   
   // health methods
   ////////////////////////////////////////////////////////////////////
   public void fullyHeal()
   {
      setCurHealth(getMaxHealth());
   }
   
   public void applyDamage(int damage, DamageType damageType, boolean doAnimation)
   {
      // double damage being electrified when in water
      if(damageType == DamageType.ELECTRO && GameEngine.getZoneMap().getTile(getMapLoc()).isLiquid())
         damage *= 2;
      if(hasShield())
      {
         boolean shieldUpBefore = shield.hasCharge();
         damage = shield.applyDamage(damage, damageType);
         // hits that get through shield do min 1 damage
         if(shield.getCurCharge() == 0)
            damage = Math.max(1, damage - getDamageReduction());
         if(doAnimation && GameEngine.playerCanSee(this))
         {
            if(shieldUpBefore)
            {
               VisualEffectFactory.createShieldFlicker(this);
               if(!shield.hasCharge())
               {
                  VisualEffectFactory.createShieldBreak(this);
               }
            }
         }
      }
      if(damage > 0)
      {
         applyDamageToHealth(damage);
      }
      setDidForcedMovement(true);
   }
   
   public void applyDamageToHealth(int damage)
   {
      curHealth -= damage;
      if(isDead())
         GameEngine.registerDeadActor(this);
   }
   
   public boolean isDead()
   {
      return curHealth <= 0;
   }
   
   public void kill()
   {
      applyDamageToHealth(curHealth + 1);
   }
   
   public void doDeathEffect()
   {
      if(getDeathEffect() == DeathEffect.EXPLODE)
         Combat.nonWeaponExplosion(getMapLoc());
   }
   
   public int[] getHealthBar(int length)
   {
      return GUITools.getBar(getCurHealth(), getMaxHealth(), length);
   }
   
   // AI/turn methods
   ////////////////////////////////////////////////////////////////////
   public boolean isReadyToAct()
   {
      return turnEnergy >= FULLY_CHARGED;
   }
   
   public void charge()
   {
      if(!isReadyToAct())
      {
         turnEnergy++;
         if(hasShield())
            shield.charge();
         if(weapon != null)
            weapon.charge();
         memory.increment();
         incrementStatusEffects();
      }
   }
   
   public void discharge(int cost)
   {
      turnEnergy -= cost;
   }
   
   public void plan()
   {
      if(!startOfTurnPerformed)
         startOfTurn();
      if(isSurprised())
      {
         ai.setPendingAction(ActorAction.DELAY);
         ai.setPendingTarget(getMapLoc());
         getAlertnessManager().recoverFromSurprise();
      }
      else if(isInactive())
      {
         ai.setPendingAction(ActorAction.DELAY);
         ai.setPendingTarget(getMapLoc());
      }
      else
      {
         ai.plan();
      }
   }
   
   protected void startOfTurn()
   {
      updateMemory();
      if(ai.hasLeader() && ai.getLeader().isDead())
         ai.setLeader(null);
      startOfTurnPerformed = true;
      setDidForcedMovement(false);
   }
   
   protected void endOfTurn()
   {
      updateMemory();
      if(GameEngine.getZoneMap().getTile(getMapLoc()).isLiquid() && isOnFire())
         extinguish();
      applyOngoingDamage();
      if(GameEngine.getZoneMap().getTile(getMapLoc()).getAirPressure() == 0)
         suffocate();
      if(GameEngine.getZoneMap().getTile(getMapLoc()) instanceof Fire)
         catchFireCheck();
      startOfTurnPerformed = false;
   }
   
   public boolean hasPlan()
   {
      return ai.hasPlan();
   }
   
   public boolean isSurprised()
   {
      return getAlertness() == Alertness.SURPRISED;
   }
   
   public boolean isInactive()
   {
      return getAlertness() == Alertness.INACTIVE;
   }
   
   public void act()
   {
      ai.act();
      endOfTurn();
   }
   
   public void updateMemory()
   {
      boolean enemySighted = false;
      Actor a;
      for(int i = 0; i < GameEngine.getActorList().size(); i++)
      {
         a = GameEngine.getActorList().elementAt(i);
         if(canSee(a) && a != this)
         {
            memory.noteActor(a);
            if(a.isHostile(this))
               enemySighted = true;
         }
      }
      alertnessManager.update(enemySighted);
   }
   
   public void suffocate()
   {
      if(!getNeedsAir())
         return;
      if(this == GameEngine.getPlayer())
         MessagePanel.addMessage("You are suffocating!");
      applyDamageToHealth(SUFFOCATION_DAMAGE);
      if(GameEngine.playerCanSee(getMapLoc()) && isDead())
         MessagePanel.addMessage(getName() + " suffocates!");
   }
   
   // status effect stuff
   ///////////////////////////////////////////////////////////////////////////////
   private void incrementStatusEffects()
   {
   
      if(isOnFire() && GameEngine.getZoneMap().getTile(getMapLoc()).getAirPressure() == 0)
         extinguish();
      for(int i = 0; i < tempStatusEffectList.size(); i++)
      {
         StatusEffect se = tempStatusEffectList.elementAt(i);
         se.increment();
         if(se.isExpired())
         {
            tempStatusEffectList.removeElementAt(i);
            burningCheck();
            i--;
         }
      }
   }
   
   public void add(StatusEffect se)
   {
      if(isDead())
         return;
      if(GameEngine.playerCanSee(this))
      {
         Color color = TERMINAL_FG_COLOR;
         if(se.isNegative())
            color = WARNING_COLOR;
         String beginning = "The " + getName() + " is ";
         if(GameEngine.getPlayer() == this)
            beginning = "You are ";
         MessagePanel.addMessage(beginning + se.getDescriptor() + "!", color);
      }
      boolean needToAdd = true;
      for(int i = 0; i < tempStatusEffectList.size(); i++)
      {
         if(tempStatusEffectList.elementAt(i).shouldCombine(se))
         {
            needToAdd = false;
            tempStatusEffectList.elementAt(i).combine(se);
            break;
         }
      }
      if(needToAdd)
      {
         tempStatusEffectList.add(se);
         if(se.isBurningEffect())
            onFire = true;
      }
   }
   
   public Vector<StatusEffect> getAllStatusEffects()
   {
      Vector<StatusEffect> seList = new Vector<StatusEffect>();
      for(StatusEffect se : tempStatusEffectList)
         seList.add(se);
      if(getArmor() != null && getArmor().getStatusEffect() != null)
         seList.add(getArmor().getStatusEffect());
      if(getShield() != null && getShield().getStatusEffect() != null)
         seList.add(getShield().getStatusEffect());
      return seList;
   }
   
   public void applyOngoingDamage()
   {
      boolean alreadyDead = false;
      for(StatusEffect se : tempStatusEffectList)
      {
         if(se.getOngoingDamage() > 0)
         {
            applyDamage(se.getOngoingDamage(), se.getDamageType(), true);
            if(isDead() && !alreadyDead)
            {
               alreadyDead = true;
               if(GameEngine.playerCanSee(getMapLoc()))
                  MessagePanel.addMessage(getName() + " has died from " + se.getName() + "!");
            }
         }
      }
   }
   
   public void burningCheck()
   {
      boolean burning = false;
      for(StatusEffect se : tempStatusEffectList)
      {
         if(se.isBurningEffect())
            burning = true;
      }
      onFire = burning;
   }
   
   public void extinguish()
   {
      for(int i = 0; i < tempStatusEffectList.size(); i++)
      {
         if(tempStatusEffectList.elementAt(i).isBurningEffect())
         {
            tempStatusEffectList.removeElementAt(i);
            i--;
         }
      }
      onFire = false;
   }
   
   public int getVisionRange()
   {
      int curVisionRange = getBaseVisionRange();
      for(StatusEffect se : getAllStatusEffects())
         curVisionRange += se.getVisionRange();
      curVisionRange = Math.min(MAP_WIDTH_TILES / 2, curVisionRange);
      return Math.max(1, curVisionRange);
   }
   
   public boolean getNeedsAir()
   {
      boolean breathes = needsAir;
      for(StatusEffect se : getAllStatusEffects())
         breathes = breathes && se.getNeedsAir();
      return breathes;
   }
   
   // modified by both status effects and armor
   public ActionSpeed getMoveSpeed()
   {
      ActionSpeed speed = getBaseMoveSpeed();
      int speedMod = 0;
      for(StatusEffect se : getAllStatusEffects())
         speedMod += se.getMoveSpeed();
      if(speedMod < 0)
         for(int i = 0; i < Math.abs(speedMod); i++)
            speed = speed.slower();
      if(speedMod > 0)
         for(int i = 0; i < speedMod; i++)
            speed = speed.faster();
      if(hasArmor() && armor.getSpeedCap().timeCost > speed.timeCost)
         return armor.getSpeedCap();
      return speed;
   }
   
   public ActionSpeed getAttackSpeed()
   {
      ActionSpeed speed = getBaseAttackSpeed();
      int speedMod = 0;
      for(StatusEffect se : getAllStatusEffects())
         speedMod += se.getAttackSpeed();
      if(speedMod < 0)
         for(int i = 0; i < Math.abs(speedMod); i++)
            speed = speed.slower();
      if(speedMod > 0)
         for(int i = 0; i < speedMod; i++)
            speed = speed.faster();
      return speed;
   }
   
   public ActionSpeed getInteractSpeed()
   {
      ActionSpeed speed = getBaseInteractSpeed();
      int speedMod = 0;
      for(StatusEffect se : getAllStatusEffects())
         speedMod += se.getInteractSpeed();
      if(speedMod < 0)
         for(int i = 0; i < Math.abs(speedMod); i++)
            speed = speed.slower();
      if(speedMod > 0)
         for(int i = 0; i < speedMod; i++)
            speed = speed.faster();
      return speed;
   }
   
   public void catchFireCheck()
   {
      double fireChance = BIOLOGICAL_FIRE_CHANCE;
      if(isMechanical())
         fireChance = MECHANICAL_FIRE_CHANCE;
      if(GameEngine.random() <= fireChance)
         add(StatusEffectFactory.getEffect(StatusEffectType.BURNING));
   }
   
}