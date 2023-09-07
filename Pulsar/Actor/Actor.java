package Pulsar.Actor;

import WidlerSuite.*;
import Pulsar.Engine.*;
import Pulsar.Gear.*;
import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;


public class Actor implements ActorConstants, GUIConstants
{
   private String name;
	private Coord mapLoc;
	private UnboundTile sprite;
   private int turnEnergy;
   private BasicAI ai;
   private int visionRange;
   private Shield shield;
   private Weapon weapon;
   private int curHealth;
   private int maxHealth;


   public String getName(){return name;}
	public Coord getMapLoc(){return new Coord(mapLoc);}
	public UnboundTile getSprite(){return sprite;}
   public int getTurnEnergy(){return turnEnergy;}
   public BasicAI getAI(){return ai;}
   public int getVisionRange(){return visionRange;}
   public Shield getShield(){return shield;}
   public Weapon getWeapon(){return weapon;}
   public int getCurHealth(){return curHealth;}
   public int getMaxHealth(){return maxHealth;}


   public void setName(String n){name = n;}
	public void setMapLoc(Coord m){setMapLoc(m.x, m.y);}
	public void setMapLoc(int x, int y){mapLoc = new Coord(x, y);}
	public void setSprite(UnboundTile s){sprite = s;}
   public void setTurnEnergy(int te){turnEnergy = te;}
   public void setAI(BasicAI newAI){ai = newAI;}
   public void setVisionRange(int vr){visionRange = vr;}
   public void setShield(Shield s){shield = s;}
   public void setWeapon(Weapon w){weapon = w;}
   public void setCurHealth(int h){curHealth = h;}
   public void setMaxHealth(int h){maxHealth = h;}


   public Actor(int icon){this(icon, "Unknown Actor");}
   public Actor(int icon, String n)
   {
      name = n;
      mapLoc = new Coord(-1, -1);
      createSprite(icon, DEFAULT_ACTOR_FG_COLOR.getRGB(), DEFAULT_ACTOR_BG_COLOR.getRGB());
      turnEnergy = 0;
      ai = new BasicAI(this);
      visionRange = DEFAULT_VISION_RANGE;
      setShield(null);
      setWeapon(null);
      setCurHealth(20);
      setMaxHealth(20);
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
      sprite = GUIConstants.SQUARE_TILE_PALETTE.getUnboundTile(
         icon, fgColor, bgColor, OuterPanel.getSizeMultiplier(), UnboundTile.CIRCLE_BACKGROUND);
      sprite.setAffectedByAge(false);
   }
   
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
      return GameEngine.getZoneMap().getTile(x, y).isLowPassable() && !GameEngine.isActorAt(x, y);
   }
   
   public boolean canSee(int x, int y){return canSee(new Coord(x, y));}
   public boolean canSee(Actor target){return canSee(target.getMapLoc());}
   public boolean canSee(Coord target)
   {
      return WSTools.getAngbandMetric(getMapLoc(), target) <= getVisionRange() &&
         GameEngine.getZoneMap().canSee(getMapLoc(), target, getVisionRange());
   }
   
   // gear methods
   ////////////////////////////////////////////////////////////////////
   public boolean hasShield()
   {
      return getShield() != null;
   }
   
   public boolean hasWeapon()
   {
      return getWeapon() != null;
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
   
   // health methods
   ////////////////////////////////////////////////////////////////////
   public void fullyHeal()
   {
      setCurHealth(getMaxHealth());
   }
   
   public void applyDamage(int damage, GearConstants.DamageType damageType)
   {
      if(hasShield())
      {
         boolean shieldUpBefore = shield.hasCharge();
         damage = shield.applyDamage(damage, damageType);
         if(GameEngine.playerCanSee(this))
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
      else
      { // handled in the else because otherwise applyDamageToHealth will do it.
         if(GameEngine.getPlayer() == this)
         {
            PlayerPanel.updatePlayerPanel();
         }
      }
   }
   
   public void applyDamageToHealth(int damage)
   {
      curHealth -= damage;
      
      if(GameEngine.getPlayer() == this)
      {
         PlayerPanel.updatePlayerPanel();
      }
   }
   
   public boolean isDead()
   {
      return curHealth <= 0;
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
      if(!(isReadyToAct()))
      {
         turnEnergy++;
         if(hasShield())
            getShield().charge();
         if(hasWeapon())
            getWeapon().charge();
      }
   }
   
   public void discharge(int cost)
   {
      turnEnergy -= cost;
      ai.clearPlan();
   }
   
   public void plan()
   {
      ai.plan();
   }
   
   public boolean hasPlan()
   {
      return ai.hasPlan();
   }
   
   public void act()
   {
      ai.act();
      discharge(NORMAL_ACTION_COST);
   }
   
}