package Pulsar.Zone;

import Pulsar.GUI.*;
import WidlerSuite.*;

public class ZoneMap implements ZoneConstants, GUIConstants
{
	private int width;
	private int height;
	private MapTile[][] tileArray;
	private MapTile oobTile;
   private boolean[][] transparencyMap;
   private ShadowFoVRect fov;


	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public MapTile[][] getTileArray(){return tileArray;}
	public MapTile getOobTile(){return oobTile;}
   public boolean[][] getTransparencyMap(){return transparencyMap;}
   public ShadowFoVRect getFoV(){return fov;}


	public void setWidth(int w){width = w;}
	public void setHeight(int h){height = h;}
	public void setTileArray(MapTile[][] t){tileArray = t;}
	public void setOobTile(MapTile o){oobTile = o;}
   public void setFoV(ShadowFoVRect newFoV){fov = newFoV;}


   public ZoneMap(int w, int h, MapTile defaultTile)
   {
      width = w;
      height = h;
      tileArray = new MapTile[width][height];
      transparencyMap = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         setTile(x, y, new MapTile(defaultTile));
      oobTile = MapTileFactory.getTile(TILE_TYPE.NULL);
      fov = new ShadowFoVRect(transparencyMap);
   }
   
   public void updateFoV()
   {
      fov.reset(transparencyMap);
   }
   
   // quickly calculate if origin can see target
   public boolean canSee(Coord origin, Coord target, int radius)
   {
      fov.calcCone(origin, radius, target);
      return fov.isVisible(target);
   }
   
   public void setPlayerViewArea(Coord loc, int radius)
   {
   
   }
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && 
             y >= 0 && 
             x < width && 
             y < height;
   }
   
   public void setTile(int x, int y, MapTile t)
   {
      if(isInBounds(x, y))
      {
         tileArray[x][y] = t;
         transparencyMap[x][y] = t.isTransparent();
      }
   }
   
   public MapTile getTile(int x, int y)
   {
      if(isInBounds(x, y))
         return tileArray[x][y];
      return new MapTile(oobTile);
   }
   
}