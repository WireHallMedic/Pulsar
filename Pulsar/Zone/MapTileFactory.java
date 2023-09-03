package Pulsar.Zone;

import Pulsar.GUI.*;
import java.awt.*;

public class MapTileFactory implements ZoneConstants, GUIConstants
{
   public static MapTile getTile(TILE_TYPE baseType, Color fgColor, Color bgColor)
   {
      MapTile tile = new MapTile(baseType.iconIndex, fgColor.getRGB(), bgColor.getRGB(), baseType.name, 
                                 baseType.lowPassable, baseType.highPassable, baseType.transparent);
      return tile;
   }
}