package Pulsar.Zone;

import Pulsar.GUI.*;
import Pulsar.Engine.*;

public class Fire extends GradientTile implements GUIConstants, ZoneConstants
{
   private MapTile originalTile;
   private int duration;
   
   public void setOriginalTile(MapTile tile){originalTile = tile;}
   public void setDuration(int d){duration = d;}
   
   public int getDuration(){return duration;}
   
   
   public Fire(MapTile original)
   {
      super(TileType.FIRE.iconIndex, original.getFGColor(), original.getBGColor(), TileType.FIRE.name, 
            TileType.FIRE.lowPassable, TileType.FIRE.highPassable, TileType.FIRE.transparent);
      setAndBurnOriginalTile(original);
      setAirPressure(original.getAirPressure());
      setGradient(FIRE_COLOR_GRADIENT);
      setDurability(Durability.UNBREAKABLE);
      duration = BASE_FIRE_DURATION + GameEngine.randomInt(0, RANDOM_FIRE_DURATION + 1);
   }
   
   public void setAndBurnOriginalTile(MapTile tile)
   {
      tile.setFGColor(ASH_COLOR.getRGB());
      tile.setName("Burned " + tile.getName());
      setOriginalTile(tile);
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
      return duration - EXTINGUISH_ARRAY[getAirPressure()] <= 0;
   }
   
}