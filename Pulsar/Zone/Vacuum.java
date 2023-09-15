package Pulsar.Zone;

public class Vacuum extends MapTile implements ZoneConstants
{
   @Override
   public Durability getDurability(){return Durability.UNBREAKABLE;}
   @Override
   public int getAirPressure(){return -1;}
   
   public Vacuum(int i, int fg, int bg, String n, boolean lp, boolean hp, boolean t)
   {
      super(i, fg, bg, n, lp, hp, t);
   }
}