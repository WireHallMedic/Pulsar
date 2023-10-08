package Pulsar.Zone;

import Pulsar.Gear.*;

public class Crate extends MapTile
{
   public static final double BASE_LOOT_CHANCE = .3;
   
	private double lootChance;       // probability of loot
	private boolean nonCreditLoot;   // all loot is non-credit
   private int numberOfRolls;       // how many times to check probability


	public double getLootChance(){return lootChance;}
	public boolean isNonCreditLoot(){return nonCreditLoot;}
   public int getNumberOfRolls(){return numberOfRolls;}


	public void setLootChance(double l){lootChance = l;}
	public void setNonCreditLoot(boolean n){nonCreditLoot = n;}
   public void setNumberOfRolls(int n){numberOfRolls = n;}

   
   public Crate(int fg, int bg)
   {
      super(TileType.CRATE.iconIndex, fg, bg, "Crate", TileType.CRATE.lowPassable, 
         TileType.CRATE.highPassable, TileType.CRATE.transparent);
      setDurability(Durability.FRAGILE);
      lootChance = BASE_LOOT_CHANCE;
      nonCreditLoot = false;
      numberOfRolls = 2;
   }
}
