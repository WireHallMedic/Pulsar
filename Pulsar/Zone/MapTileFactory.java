package Pulsar.Zone;

import Pulsar.GUI.*;
import java.awt.*;

public class MapTileFactory implements ZoneConstants, GUIConstants
{
   public static MapTile getTile(TILE_TYPE baseType)
   {
      return getTile(baseType, DEFAULT_TILE_FG_COLOR, DEFAULT_TILE_BG_COLOR);
   }
   
   public static MapTile getTile(TILE_TYPE baseType, Color fgColor, Color bgColor)
   {
      MapTile tile = new MapTile(baseType.iconIndex, fgColor.getRGB(), bgColor.getRGB(), baseType.name, 
                                 baseType.lowPassable, baseType.highPassable, baseType.transparent);
      return tile;
   }
}