package Pulsar.Gear;

import WidlerSuite.*;
import Pulsar.GUI.*;
import java.awt.*;

public abstract class GearObj implements GearConstants, GUIConstants
{
   private String name;
   private int iconIndex;
	private Color fgColor;
   private String description;


   public String getName(){return name;}
	public int getIconIndex(){return iconIndex;}
	public Color getFGColor(){return fgColor;}
   public String getDescription(){return description;}


   public void setName(String n){name = n;}
	public void setIconIndex(int i){iconIndex = i;}
	public void setFGColor(Color f){fgColor = f;}
   public void setDescription(String d){description = d;}
   
   public GearObj(int ii, String n)
   {
      name = n;
      iconIndex = ii;
      fgColor = DEFAULT_TILE_FG_COLOR;
      description = "<No Description>";
   }

   @Override
   public String toString()
   {
      return name;
   }
   
   abstract public String getSummary();
}