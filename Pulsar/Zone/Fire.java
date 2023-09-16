package Pulsar.Zone;

import Pulsar.GUI.*;

public class Fire extends GradientTile implements GUIConstants
{
   private MapTile originalTile;
   
   public void setOriginalTile(MapTile tile){originalTile = tile;}
   
   
   public Fire(MapTile original)
   {
      super('^', original.getBGColor(), "Fire", true, true, true);
      setAndBurnOriginalTile(original);
      setAirPressure(original.getAirPressure());
      setGradient(FIRE_COLOR_GRADIENT);
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
   
}