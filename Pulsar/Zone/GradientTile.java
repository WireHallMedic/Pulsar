package Pulsar.Zone;

import Pulsar.GUI.*;
import WidlerSuite.*;
import java.awt.*;

public class GradientTile extends MapTile implements ZoneConstants, GUIConstants
{
   public static final int SLOW = 1;
   public static final int MEDIUM = 2;
   public static final int FAST = 3;
   public static final boolean FOREGROUND = true;
   public static final boolean BACKGROUND = false;
   
   private Color[] gradient;
   private int pulseSpeed;
   private boolean pulseType;
   
   public Color[] getGradient(){return gradient;}
   public int getPulseSpeed(){return pulseSpeed;}
   public boolean getPulseType(){return pulseType;}
   
   public void setGradient(Color[] g){gradient = g;}
   public void setPulseSpeed(int ps){pulseSpeed = ps;}
   public void setPulseType(boolean pt){pulseType = pt;}
   
   public GradientTile(int i, int fg, int bg, String n, boolean lp, boolean hp, boolean t)
   {   
      super(i, fg, bg, n, lp, hp, t);
      gradient = null;
      pulseSpeed = MEDIUM;
      pulseType = FOREGROUND;
   }
   
   @Override
   public int getFGColor()
   {
      if(pulseType == FOREGROUND)
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
      else
         return super.getFGColor();
   }
   
   @Override
   public int getBGColor()
   {
      if(pulseType == BACKGROUND)
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
         return DEFAULT_TILE_BG_COLOR.getRGB();
      }
      else
         return super.getBGColor();
   }
}