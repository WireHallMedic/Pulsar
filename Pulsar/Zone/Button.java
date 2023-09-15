package Pulsar.Zone;

import WidlerSuite.*;

public class Button extends MapTile implements ToggleTile, WSFontConstants
{
   public static final boolean UNPRESSED = true;
   public static final boolean PRESSED = false;
   
   private boolean pressed = false;
   
   
   public Button(int fg, int bg, String n)
   {
      super('x', fg, bg, n, false, true, true);
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
      pressed = !pressed;
   }
}