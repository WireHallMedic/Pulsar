package Pulsar.Gear;

import WidlerSuite.*;
import Pulsar.GUI.*;
import java.awt.*;

public abstract class GearObj implements GearConstants, GUIConstants
{
   private int iconIndex;
	private Color fgColor;


	public int getIconIndex(){return iconIndex;}
	public Color getFGColor(){return fgColor;}


	public void setIconIndex(int i){iconIndex = i;}
	public void setFGColor(Color f){fgColor = f;}
   
   public GearObj(int ii)
   {
      iconIndex = ii;
      fgColor = DEFAULT_TILE_FG_COLOR;
   }

}