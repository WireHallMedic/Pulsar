package Pulsar.Zone;

import Pulsar.GUI.*;

public class Fire extends MapTile implements GUIConstants
{
   private MapTile originalTile;
   
   public MapTile getOriginalTile(){return originalTile;}
   
   public void setOriginalTile(MapTile tile){originalTile = tile;}
   
   
   public Fire(MapTile original)
   {
      super('^', FIRE_COLOR.getRGB(), original.getBGColor(), "Fire", true, true, true);
      setAndBurnOriginalTile(original);
   }
   
   public void setAndBurnOriginalTile(MapTile tile)
   {
      tile.setFGColor(ASH_COLOR.getRGB());
      tile.setName("Burned " + tile.getName());
      setOriginalTile(tile);
   }
}