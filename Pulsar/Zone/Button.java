package Pulsar.Zone;

import WidlerSuite.*;
import Pulsar.Engine.*;

public class Button extends MapTile implements ToggleTile, WSFontConstants
{
   public static final boolean UNPRESSED = true;
   public static final boolean PRESSED = false;
   
   private boolean pressed;
   private int triggerIndex;
   private boolean oneUse;
   private int repetitions;
   
   public int getTriggerIndex(){return triggerIndex;}
   public boolean isPressed(){return pressed;}
   public boolean isOneUse(){return oneUse;}
   public int getRepetitions(){return repetitions;}
   
   public void setTriggerIndex(int ti){triggerIndex = ti;}
   public void setPressed(boolean p){pressed = p;}
   public void setOneUse(boolean ou){oneUse = ou;}
   public void setRepetitions(int r){repetitions = r;}
   
   
   public Button(int fg, int bg)
   {
      super('x', fg, bg, "Button", false, true, true);
      triggerIndex = EngineTools.getUniqueNumber();
      oneUse = true;
      pressed = false;
      repetitions = 1;
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
      if(oneUse)
      {
         if(!pressed)
         {
            GameEngine.buttonPressed(this);
         }
         pressed = true;
      }
      else
      {
         GameEngine.buttonPressed(this);
         pressed = !pressed;
      }
   }
}