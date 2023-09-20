package Pulsar.Zone;

import Pulsar.GUI.*;
import Pulsar.Engine.*;

public class Ice extends MapTile implements GUIConstants, ZoneConstants
{
   private MapTile originalTile;
   private int duration;
   
   public void setOriginalTile(MapTile tile){originalTile = tile;}
   public void setDuration(int d){duration = d;}
   
   public int getDuration(){return duration;}
   
   
   public Ice(MapTile original)
   {
      super(TileType.ICE.iconIndex, LIGHT_BLUE.getRGB(), WHITE.getRGB(), "Ice", true, true, true);
      originalTile = original;
      setAirPressure(original.getAirPressure());
      refreshDuration();
   }
   
   public void refreshDuration()
   {
      duration = BASE_ICE_DURATION + GameEngine.randomInt(0, RANDOM_ICE_DURATION + 1);
   }
   
   public MapTile getOriginalTile()
   {
      originalTile.setAirPressure(getAirPressure());
      return originalTile;
   }
   
   public void increment()
   {
      duration--;
   }
   
   public boolean isExpired()
   {
      return duration <= 0;
   }
   
}