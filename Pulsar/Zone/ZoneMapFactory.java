package Pulsar.Zone;

public class ZoneMapFactory implements ZoneConstants
{
   public static ZoneMap getTestMap()
   {
      int w = 15;
      int h = 15;
      ZoneMap m = new ZoneMap(w, h, MapTileFactory.getTile(TileType.CLEAR));
      addBorder(m, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(5, 5, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(5, 6, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(6, 5, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(6, 6, MapTileFactory.getTile(TileType.HIGH_WALL));
      m.setTile(5, 7, MapTileFactory.getDoor());
      
      return m;
   }
   
   public static void addBorder(ZoneMap map, MapTile borderTile)
   {
      for(int x = 0; x < map.getWidth(); x++)
      {
         map.setTile(x, 0, new MapTile(borderTile));
         map.setTile(x, map.getHeight() - 1, new MapTile(borderTile));
      }
      for(int y = 0; y < map.getHeight(); y++)
      {
         map.setTile(0, y, new MapTile(borderTile));
         map.setTile(map.getWidth() - 1, y, new MapTile(borderTile));
      }
   }
}