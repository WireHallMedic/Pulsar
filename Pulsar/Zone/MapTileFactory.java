package Pulsar.Zone;

import Pulsar.GUI.*;
import java.awt.*;

public class MapTileFactory implements ZoneConstants, GUIConstants
{
   public static MapTile getTile(TileType baseType)
   {
      return getTile(baseType, DEFAULT_TILE_FG_COLOR, DEFAULT_TILE_BG_COLOR);
   }
   
   public static MapTile getTile(TileType baseType, Color fgColor, Color bgColor)
   {
      MapTile tile = new MapTile(baseType.iconIndex, fgColor.getRGB(), bgColor.getRGB(), baseType.name, 
                                 baseType.lowPassable, baseType.highPassable, baseType.transparent);
      if(baseType == TileType.NULL)
         tile.setDurability(Durability.UNBREAKABLE);
      return tile;
   }
   
   public static MapTile getDoor()
   {
      Door door = new Door(Color.WHITE.getRGB(), BG_COLOR.getRGB(), "Door");
      return door;
   }
   
   public static MapTile getBroken(MapTile originalTile)
   {
      MapTile brokenTile = getTile(TileType.RUBBLE);
      brokenTile.setFGColor(originalTile.getFGColor());
      brokenTile.setBGColor(originalTile.getBGColor());
      return brokenTile;
   }
}