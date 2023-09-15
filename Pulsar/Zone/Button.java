package Pulsar.Zone;

import WidlerSuite.*;
import Pulsar.Engine.*;

public class Button extends MapTile implements ToggleTile, WSFontConstants
{
   public static final boolean UNPRESSED = true;
   public static final boolean PRESSED = false;
   
   private boolean pressed = false;
   private int triggerIndex;
   
   public int getTriggerIndex(){return triggerIndex;}
   public boolean isPressed(){return pressed;}
   
   public void setTriggerIndex(int ti){triggerIndex = ti;}
   public void setPressed(boolean p){pressed = p;}
   
   
   public Button(int fg, int bg, int ti)
   {
      super('x', fg, bg, "Button", false, true, true);
      triggerIndex = ti;
   }

	public int getIconIndex()
   {
      if(pressed)
         return RING_TILE;
      return DOT_TILE;
   }
   
	public String getName()
   {
      String suffix = " (Pressed)";
      if(pressed)
         suffix = " (Unpressed)";
      return super.getName() + suffix;
   }
   
   
   public void toggle()
   {
      if(!pressed)
      {
         GameEngine.buttonPressed(triggerIndex);
      }
      pressed = true;
   }
}