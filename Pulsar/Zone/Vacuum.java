package Pulsar.Zone;

import Pulsar.GUI.*;

public class Vacuum extends MapTile implements ZoneConstants, GUIConstants
{
   @Override
   public Durability getDurability(){return Durability.UNBREAKABLE;}
   @Override
   public int getAirPressure(){return 0;}
   
   public Vacuum()
   {
      super(' ', DEFAULT_TILE_FG_COLOR.getRGB(), BLACK.getRGB(), "Space", false, false, true);
   }
}