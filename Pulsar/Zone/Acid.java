package Pulsar.Zone;

import Pulsar.GUI.*;

public class Acid extends GradientTile implements ZoneConstants, GUIConstants
{
   public Acid()
   {
      super(TileType.WATER.iconIndex, DEFAULT_TILE_FG_COLOR.getRGB(), DEFAULT_TILE_BG_COLOR.getRGB(), "Acid",
            TileType.WATER.lowPassable, TileType.WATER.highPassable, TileType.WATER.transparent);
      setGradient(ACID_COLOR_GRADIENT);
      setDurability(Durability.UNBREAKABLE);
      setPulseType(GradientTile.BACKGROUND);
      setPulseSpeed(GradientTile.SLOW);
      setLiquid(true);
      setSlowsMovement(true);
   }
}