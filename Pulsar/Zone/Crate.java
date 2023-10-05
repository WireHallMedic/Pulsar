package Pulsar.Zone;

import Pulsar.Gear.*;

public class Crate extends MapTile
{
   private double lootChance;
   private boolean nonCreditLoot;
   
   public Crate(int fg, int bg)
   {
      super(TileType.HIGH_WALL.iconIndex, fg, bg, "Crate", TileType.HIGH_WALL.lowPassable, 
         TileType.HIGH_WALL.highPassable, TileType.HIGH_WALL.transparent);
      setDurability(Durability.FRAGILE);
      lootChance = .1;
      nonCreditLoot = false;
   }
}
