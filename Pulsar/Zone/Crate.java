package Pulsar.Zone;

import Pulsar.Gear.*;

public class Crate extends MapTile
{
   private double lootChance;
   private boolean nonCreditLoot;
   
   public Crate(int fg, int bg)
   {
      super(TileType.CRATE.iconIndex, fg, bg, "Crate", TileType.CRATE.lowPassable, 
         TileType.CRATE.highPassable, TileType.CRATE.transparent);
      setDurability(Durability.FRAGILE);
      lootChance = .1;
      nonCreditLoot = false;
   }
}
