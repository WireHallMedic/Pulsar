package Pulsar.Actor;

import Pulsar.AI.*;
import Pulsar.GUI.*;
import WidlerSuite.*;
import Pulsar.Engine.*;
import Pulsar.Zone.*;
import Pulsar.Gear.*;

public class Player extends Actor implements GUIConstants
{
   private boolean[][] visibilityMap;
   private Coord visibilityMapCorner;
   private Weapon secondaryWeapon;
   private boolean usingPrimaryWeapon;
   
   private static final int VISIBILITY_MAP_WIDTH = MAP_WIDTH_TILES + 2;
   private static final int VISIBILITY_MAP_HEIGHT = MAP_HEIGHT_TILES + 2;
   
   public Player()
   {
      super('@', "Player");
      setAI(new PlayerAI(this));
      getSprite().setFGColor(PLAYER_COLOR.getRGB());
      visibilityMap = null;
      visibilityMapCorner = null;
      secondaryWeapon = null;
      usingPrimaryWeapon = true;
   }
   
   @Override
   public void charge()
   {
      if(!isReadyToAct())
      {
         if(hasSecondaryWeapon())
            secondaryWeapon.charge();
      }
      super.charge();
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
   
   public int[] getAltWeaponBar(int length)
   {
      return GUITools.getBar(getAltWeapon().getCurCharge(), getAltWeapon().getMaxCharge(), length);
   }
   
   public void updateFoV()
   {
      ZoneMap map = GameEngine.getZoneMap();
      if(map == null)
         return;
      ShadowFoVRect fov = map.getFoV();
      if(fov == null)
         return;
      fov.calcFoV(getMapLoc().x, getMapLoc().y, getVisionRange());
      visibilityMapCorner = new Coord(getMapLoc().x - (VISIBILITY_MAP_WIDTH / 2), 
                                       getMapLoc().y - (VISIBILITY_MAP_HEIGHT / 2));
      visibilityMap = fov.getArray(visibilityMapCorner.x, visibilityMapCorner.y, VISIBILITY_MAP_WIDTH, VISIBILITY_MAP_HEIGHT);
   }
   
   @Override
	public void setMapLoc(Coord m){setMapLoc(m.x, m.y);}
   @Override
	public void setMapLoc(int x, int y)
   {
      super.setMapLoc(x, y);
      updateFoV();
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
      if(WSTools.getAngbandMetric(getMapLoc(), target) > (double)getVisionRange())
         return false;
      int shiftedX = target.x - visibilityMapCorner.x;
      int shiftedY = target.y - visibilityMapCorner.y;
      return visibilityMap[shiftedX][shiftedY];
   }
}