package Pulsar.Zone;

import Pulsar.GUI.*;

public class Acid extends GradientTile implements ZoneConstants, GUIConstants
{
   public Acid()
   {
      super(TileType.ACID.iconIndex, DEFAULT_TILE_FG_COLOR.getRGB(), DEFAULT_TILE_BG_COLOR.getRGB(), TileType.ACID.name,
            TileType.ACID.lowPassable, TileType.ACID.highPassable, TileType.ACID.transparent);
      setGradient(ACID_COLOR_GRADIENT);
      setDurability(Durability.UNBREAKABLE);
      setPulseType(GradientTile.BACKGROUND);
      setPulseSpeed(GradientTile.SLOW);
      setLiquid(true);
      setSlowsMovement(true);
   }
}