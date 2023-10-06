package Pulsar.Actor;

import Pulsar.AI.*;
import Pulsar.GUI.*;
import WidlerSuite.*;
import Pulsar.Engine.*;
import Pulsar.Zone.*;
import Pulsar.Gear.*;
import java.util.*;
import java.awt.*;

public class Player extends Actor implements GUIConstants, GearConstants
{
   private static final int VISIBILITY_MAP_WIDTH = MAP_WIDTH_TILES + 2;
   private static final int VISIBILITY_MAP_HEIGHT = MAP_HEIGHT_TILES + 2;
   
   private boolean[][] visibilityMap;
   private char[][] lastSeen;
   private Coord visibilityMapCorner;
   private Weapon secondaryWeapon;
   private boolean usingPrimaryWeapon;
   private Vector<Gadget> gadgetList;
   private int maxGadgets;
   
   public Vector<Gadget> getGadgetList(){return gadgetList;}
   public int getMaxGadgets(){return maxGadgets;}
   
   public void setGadgetList(Vector<Gadget> gl){gadgetList = gl;}
   public void setMaxGadgets(int mg){maxGadgets = mg;}
   
   
   public Player()
   {
      super('@', "Player");
      setAI(new PlayerAI(this));
      getSprite().setFGColor(PLAYER_COLOR.getRGB());
      visibilityMap = null;
      visibilityMapCorner = null;
      secondaryWeapon = null;
      usingPrimaryWeapon = true;
      gadgetList = new Vector<Gadget>();
      maxGadgets = DEFAULT_MAX_GADGETS;
   }
   
   @Override
   public void charge()
   {
      if(!isReadyToAct())
      {
         if(secondaryWeapon != null)
            secondaryWeapon.charge();
      }
      super.charge();
   }
   
   @Override
   public String getMood(Actor that)
   {
      return "This is you";
   }
   
   // weapon stuff
   public void setPrimaryWeapon(Weapon w){weapon = w;}
   public void setSecondaryWeapon(Weapon w){secondaryWeapon = w;}
   public Weapon getPrimaryWeapon(){return weapon;}
   public Weapon getSecondaryWeapon(){return secondaryWeapon;}
   public boolean hasSecondaryWeapon(){return secondaryWeapon != null;}
   
   @Override
   public void setWeapon(Weapon w)
   {
      if(usingPrimaryWeapon)
         setPrimaryWeapon(w);
      else
         setSecondaryWeapon(w);
   }
   
   public void switchWeapons()
   {
      usingPrimaryWeapon = !usingPrimaryWeapon;
   }
   
   @Override
   public boolean hasWeapon()
   {
      if(usingPrimaryWeapon)
         return weapon != null;
      return secondaryWeapon != null;
   }
   
   @Override
   public Weapon getWeapon()
   {
      if(usingPrimaryWeapon && weapon != null)
         return weapon;
      if(!usingPrimaryWeapon && secondaryWeapon != null)
         return secondaryWeapon;
      return unarmed;
   }
   
   public Weapon getAltWeapon()
   {
      if(!usingPrimaryWeapon && weapon != null)
         return weapon;
      if(usingPrimaryWeapon && secondaryWeapon != null)
         return secondaryWeapon;
      return unarmed;
   }
   
   public boolean hasAltWeapon()
   {
      return getAltWeapon() != unarmed;
   }
   
   public int[] getAltWeaponBar(int length)
   {
      return GUITools.getBar(getAltWeapon().getCurCharge(), getAltWeapon().getMaxCharge(), length);
   }
   
   // inventory manipulation
   ///////////////////////////////////////////////////
   public void unequip(GearObj obj)
   {
      if(obj == null)
         return;
      unequipItem(obj);
      getInventory().add(obj);
      getAI().setPendingAction(ActorAction.INVENTORY_ACTION);
      getAI().setPendingTarget(getMapLoc());
   }
   
   public void equip(GearObj obj, int slot)
   {
   
   }
   
   public void drop(GearObj obj)
   {
   
   }
   
   private void unequipItem(GearObj obj)
   {
      if(weapon == obj)
         weapon = null;
      if(secondaryWeapon == obj)
         secondaryWeapon = null;
      if(armor == obj)
         armor = null;
      if(shield == obj)
         shield = null;
      for(int i = 0; i < gadgetList.size(); i++)
         if(gadgetList.elementAt(i) == obj)
            gadgetList.removeElementAt(i);
   }
   
   public void updateFoV()
   {
      ZoneMap map = GameEngine.getZoneMap();
      if(map == null)
         return;
      ShadowFoVRect fov = map.getFoV();
      if(fov == null)
         return;
      fov.calcFoV(getMapLoc().x, getMapLoc().y, getVisionRange() + 1);
      visibilityMapCorner = new Coord(getMapLoc().x - (VISIBILITY_MAP_WIDTH / 2), 
                                       getMapLoc().y - (VISIBILITY_MAP_HEIGHT / 2));
      visibilityMap = fov.getArray(visibilityMapCorner.x, visibilityMapCorner.y, VISIBILITY_MAP_WIDTH, VISIBILITY_MAP_HEIGHT);
      for(int x = 0; x < visibilityMap.length; x++)
      for(int y = 0; y < visibilityMap[0].length; y++)
      {
         if(canSee(visibilityMapCorner.x + x, visibilityMapCorner.y + y))
            setLastSeen(visibilityMapCorner.x + x, visibilityMapCorner.y + y, (char)GameEngine.getZoneMap().getTile(visibilityMapCorner.x + x, visibilityMapCorner.y + y).getIconIndex());
      }
   }
   
   @Override
   protected void startOfTurn()
   {
      super.startOfTurn();
      updateFoV();
   }
   
   @Override
   protected void endOfTurn()
   {
     super.endOfTurn();
     updateFoV();
   }
   
   @Override
   public void add(StatusEffect se)
   {
      super.add(se);
      if(se.getVisionRange() != 0)
         updateFoV();
   }
   
   @Override
	public void setMapLoc(Coord m){setMapLoc(m.x, m.y);}
   @Override
	public void setMapLoc(int x, int y)
   {
      super.setMapLoc(x, y);
      updateFoV();
   }
   
   @Override
   public void fullyRefresh()
   {
      super.fullyRefresh();
      if(hasAltWeapon())
         getAltWeapon().fullyCharge();
      for(Gadget g : gadgetList)
      {
         g.fullyCharge();
      }
   }
      
   
   // players memoize an area, rather than calculating FoV with every call
   @Override
   public boolean canSee(int x, int y){return canSee(new Coord(x, y));}
   @Override
   public boolean canSee(Actor target){return canSee(target.getMapLoc());}
   @Override
   public boolean canSee(Coord target)
   {
      if(visibilityMapCorner == null || visibilityMap == null)
      {
         updateFoV();
      }
      if(visibilityMapCorner == null || visibilityMap == null)
      {
         return false;
      }
      if(WSTools.getAngbandMetric(getMapLoc(), target) > getVisionRange())
         return false;
      int shiftedX = target.x - visibilityMapCorner.x;
      int shiftedY = target.y - visibilityMapCorner.y;
      return visibilityMap[shiftedX][shiftedY];
   }
   
   public void resetLastSeen()
   {
      lastSeen = new char[GameEngine.getZoneMap().getWidth()][GameEngine.getZoneMap().getHeight()];
      for(int x = 0; x < lastSeen.length; x++)
      for(int y = 0; y < lastSeen[0].length; y++)
         lastSeen[x][y] = ' ';
   }
   
   public void setLastSeen(int x, int y, char c)
   {
      if(lastSeen == null)
         resetLastSeen();
      if(GameEngine.getZoneMap().isInBounds(x, y))
         lastSeen[x][y] = c;
   }
   
   public char getLastSeen(int x, int y)
   {
      if(lastSeen == null)
         resetLastSeen();
      if(GameEngine.getZoneMap().isInBounds(x, y))
         return lastSeen[x][y];
      return ' ';
   }
   
   private Weapon getPendingWeapon()
   {
      if(ai.getPendingAction() == ActorAction.ATTACK)
         return getWeapon();
      if(ai.getPendingAction() == ActorAction.UNARMED_ATTACK)
         return getUnarmedAttack();
      if(ai.getPendingAction().isGadgetAction())
         return getGadget(ai.getPendingAction()).getWeaponEffect();
      return null;
   }
   
   public boolean pendingAttackIsBlast()
   {
      if(ai.getPendingAction() == null)
         return false;
      if(ai.getPendingAction().isGadgetAction())
      {
         // do stuff here
      }
      Weapon pendingWeapon = getPendingWeapon();
      return pendingWeapon != null && pendingWeapon.hasWeaponTag(WeaponTag.BLAST);
   }
   
   public boolean pendingAttackIsSpread()
   {
      if(ai.getPendingAction() == null)
         return false;
      Weapon pendingWeapon = getPendingWeapon();
      return pendingWeapon != null && pendingWeapon.hasWeaponTag(WeaponTag.SPREAD);
   }
   
   public boolean pendingAttackIsBeam()
   {
      if(ai.getPendingAction() == null)
         return false;
      Weapon pendingWeapon = getPendingWeapon();
      return pendingWeapon != null && pendingWeapon.hasWeaponTag(WeaponTag.BEAM);
   }
   
   public boolean pendingAttackIsMelee()
   {
      if(ai.getPendingAction() == null)
         return false;
      Weapon pendingWeapon = getPendingWeapon();
      return pendingWeapon != null && pendingWeapon.hasWeaponTag(WeaponTag.MELEE);
   }
   
   public boolean pendingAttackIsStandard()
   {
      return !(pendingAttackIsSpread() || pendingAttackIsBlast() || pendingAttackIsMelee());
   }
   
   
   // gadget stuff
   public Gadget getGadget(int index)
   {
      if(index < gadgetList.size())
         return gadgetList.elementAt(index);
      return null;
   }
   
   public Gadget getGadget(ActorAction action)
   {
      int index = action.getGadgetIndex();
      if(index == -1)
         return null;
      return getGadget(index);
   }
   
   public void removeGadget(int index)
   {
      if(index < gadgetList.size())
         gadgetList.removeElementAt(index);
   }
   
   public void addGadget(Gadget g)
   {
      if(hasRoomForGadget())
         gadgetList.add(g);
   }
   
   public boolean hasRoomForGadget()
   {
      return gadgetList.size() < getMaxGadgets();
   }
   
   public boolean hasGadgetEffect(GadgetSpecialEffect se)
   {
      for(Gadget gadget : gadgetList)
      {
         if(gadget.getSpecialEffect() == se)
            return true;
      }
      if(hasArmor() && getArmor().getGadgetEffect() == se)
         return true;
      if(hasShield() && getShield().getGadgetEffect() == se)
         return true;
      return false;
   }
   
   public Gadget getPendingGadget()
   {
      if(ai.getPendingAction().isGadgetAction())
         return getGadget(ai.getPendingAction().getGadgetIndex());
      return null;
   }
   
   public boolean hasMotionSensor()
   {
      return hasGadgetEffect(GadgetSpecialEffect.MOTION_SENSOR);
   }
   
   @Override
   public Vector<StatusEffect> getAllStatusEffects()
   {
      Vector<StatusEffect> seList = super.getAllStatusEffects();
      for(Gadget gadget : gadgetList)
      {
         if(gadget.hasPassiveStatusEffect())
            seList.add(gadget.getPassiveStatusEffect());
      }
      return seList;
   }
   
   public Color getFlashColor()
   {
      int shieldVal = 0;
      if(hasShield())
         shieldVal = getShield().getCurCharge();
      if(shieldVal == 0 && getCurHealth() < getMaxHealth())
         return HEALTH_COLOR;
      return SHIELD_COLOR;
   }

}