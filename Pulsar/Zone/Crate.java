package Pulsar.Zone;

import Pulsar.Gear.*;

public class Crate extends MapTile
{
	private double lootChance;
	private boolean nonCreditLoot;


	public double getLootChance(){return lootChance;}
	public boolean isNonCreditLoot(){return nonCreditLoot;}


	public void setLootChance(double l){lootChance = l;}
	public void setNonCreditLoot(boolean n){nonCreditLoot = n;}

   
   public Crate(int fg, int bg)
   {
      super(TileType.CRATE.iconIndex, fg, bg, "Crate", TileType.CRATE.lowPassable, 
         TileType.CRATE.highPassable, TileType.CRATE.transparent);
      setDurability(Durability.FRAGILE);
      lootChance = .5;
      nonCreditLoot = false;
   }
}
