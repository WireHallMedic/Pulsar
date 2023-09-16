package Pulsar.Zone;

import Pulsar.GUI.*;
import WidlerSuite.*;
import java.awt.*;

public class GradientTile extends MapTile implements ZoneConstants, GUIConstants
{
   public static final int SLOW = 1;
   public static final int MEDIUM = 2;
   public static final int FAST = 3;
   
   private Color[] gradient;
   private int pulseSpeed;
   
   public Color[] getGradient(){return gradient;}
   public int getPulseSpeed(){return pulseSpeed;}
   
   public void setGradient(Color[] g){gradient = g;}
   public void setPulseSpeed(int ps){pulseSpeed = ps;}
   
   public GradientTile(int i, int bg, String n, boolean lp, boolean hp, boolean t)
   {   
      super(i, DEFAULT_TILE_FG_COLOR.getRGB(), bg, n, lp, hp, t);
      gradient = null;
      pulseSpeed = MEDIUM;
   }
   
   @Override
   public int getFGColor()
   {
      TileAnimationManager animationManager = GUITools.getAnimationManager();
      if(gradient != null && animationManager != null)
      {
         switch(pulseSpeed)
         {
            case SLOW:   return gradient[animationManager.slowPulse()].getRGB();
            case MEDIUM: return gradient[animationManager.mediumPulse()].getRGB();
            case FAST:   return gradient[animationManager.fastPulse()].getRGB();
         }
      }
      return DEFAULT_TILE_FG_COLOR.getRGB();
   }
}