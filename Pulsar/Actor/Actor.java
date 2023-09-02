package Pulsar.Actor;

import WidlerSuite.*;
import Pulsar.GUI.*;
import java.awt.*;


public class Actor
{
	private Coord mapLoc;
	private UnboundTile sprite;


	public Coord getMapLoc(){return new Coord(mapLoc);}
	public UnboundTile getSprite(){return sprite;}


	public void setMapLoc(Coord m){setMapLoc(m.x, m.y);}
	public void setMapLoc(int x, int y){mapLoc = new Coord(x, y);}
	public void setSprite(UnboundTile s){sprite = s;}


   public Actor(int icon)
   {
      mapLoc = new Coord(-1, -1);
      createSprite(icon, Color.WHITE.getRGB(), Color.GRAY.getRGB());
   }
   
   public void createSprite(int icon, int fgColor, int bgColor)
   {
      sprite = GUIConstants.SQUARE_TILE_PALETTE.getUnboundTile(
         icon, fgColor, bgColor, OuterPanel.getSizeMultiplier(), UnboundTile.CIRCLE_BACKGROUND);
      sprite.setAffectedByAge(false);
   }
   
   public void setAllLocs(Coord l){setAllLocs(l.x, l.y);}
   public void setAllLocs(int x, int y)
   {
      setMapLoc(x, y);
      setSpriteLoc(x, y);
   }
   
   public void setSpriteLoc(Coord l){setSpriteLoc(l.x, l.y);}
   public void setSpriteLoc(int x, int y)
   {
      sprite.setXLoc(x);
      sprite.setYLoc(y);
      sprite.setXOffset(0.0);
      sprite.setYOffset(0.0);
   }
}