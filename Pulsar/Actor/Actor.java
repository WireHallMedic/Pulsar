package Pulsar.Actor;

import WidlerSuite.*;
import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;


public class Actor implements ActorConstants
{
	private Coord mapLoc;
	private UnboundTile sprite;
   private int turnEnergy;
   private BasicAI ai;


	public Coord getMapLoc(){return new Coord(mapLoc);}
	public UnboundTile getSprite(){return sprite;}
   public int getTurnEnergy(){return turnEnergy;}
   public BasicAI getAI(){return ai;}


	public void setMapLoc(Coord m){setMapLoc(m.x, m.y);}
	public void setMapLoc(int x, int y){mapLoc = new Coord(x, y);}
	public void setSprite(UnboundTile s){sprite = s;}
   public void setTurnEnergy(int te){turnEnergy = te;}
   public void setAI(BasicAI newAI){ai = newAI;}


   public Actor(int icon)
   {
      mapLoc = new Coord(-1, -1);
      createSprite(icon, Color.WHITE.getRGB(), Color.GRAY.getRGB());
      turnEnergy = 0;
      ai = new BasicAI(this);
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
   
   public int getSpriteXLoc()
   {
      return sprite.getXLoc();
   }
   
   public int getSpriteYLoc()
   {
      return sprite.getYLoc();
   }
   
   public double getSpriteXOffset()
   {
      return sprite.getXOffset();
   }
   
   public double getSpriteYOffset()
   {
      return sprite.getYOffset();
   }
   
   // AI/turn methods
   ////////////////////////////////////////////////////////////////////
   public boolean isReadyToAct()
   {
      return turnEnergy >= FULLY_CHARGED;
   }
   
   public void charge()
   {
      if(!(isReadyToAct()))
         turnEnergy++;
   }
   
   public void discharge(int cost)
   {
      turnEnergy -= cost;
      ai.clearPlan();
   }
   
   public void plan()
   {
      ai.plan();
   }
   
   public boolean hasPlan()
   {
      return ai.hasPlan();
   }
   
   public void act()
   {
      ai.act();
      discharge(NORMAL_ACTION_COST);
      ai.shiftPendingToPrevious();
   }
   
}