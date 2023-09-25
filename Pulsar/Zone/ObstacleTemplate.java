package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class ObstacleTemplate extends MapTemplate implements ZoneConstants
{
   public static final int REQUIRED_WIDTH = 5;
   public static final int REQUIRED_HEIGHT = 5;
   
   private ButtonTrigger buttonTrigger;
   
   public void setButtonTrigger(ButtonTrigger t){buttonTrigger = t;}

   public ButtonTrigger getButtonTrigger(){return buttonTrigger;}
   public boolean hasButtonTrigger(){return buttonTrigger != null;}
   
   public ObstacleTemplate(Vector<String> input)
   {
      super(input);
      buttonTrigger = null;
      validate();
   }
   
   public ObstacleTemplate(ObstacleTemplate that)
   {
      super(that);
      if(that.hasButtonTrigger())
      {
         this.buttonTrigger = new ButtonTrigger(that.buttonTrigger);
      }
   }
   
   @Override
   public void rotate()
   {
      if(hasButtonTrigger())
         buttonTrigger.shiftFromRotation(REQUIRED_WIDTH, REQUIRED_HEIGHT);
      super.rotate();
   }
   
   @Override
   public void mirrorX()
   {
      if(hasButtonTrigger())
         buttonTrigger.shiftFromMirrorX(REQUIRED_WIDTH, REQUIRED_HEIGHT);
      super.mirrorX();
   }
   
   @Override
   public void mirrorY()
   {
      if(hasButtonTrigger())
         buttonTrigger.shiftFromMirrorY(REQUIRED_WIDTH, REQUIRED_HEIGHT);
      super.mirrorY();
   }
      
   public void print()
   {
      for(int y = 0; y < height; y++)
      {
         for(int x = 0; x < width; x++)
         {
            System.out.print(getCell(x, y));
         }
         System.out.println("");
      }
   }
   
   public boolean validate()
   {
      if(getWidth() != REQUIRED_WIDTH || getHeight() != REQUIRED_HEIGHT)
            throw new java.lang.Error("ObstacleTemplate has invalid size.");
      return true;
   }
   
   // buttonTriggers aren't set at obstacleTemplate generation, so the manager validates them after
   public boolean validateButtons()
   {
      int buttonsFound = 0;
      for(int x = 0; x < REQUIRED_WIDTH; x++)
      for(int y = 0; y < REQUIRED_HEIGHT; y++)
      {
         if(getCell(x, y) == TEMPLATE_BUTTON)
         {
            buttonsFound++;
            if(buttonTrigger == null)
            {
               throw new java.lang.Error("ObstacleTemplate has button with no trigger.");
            }
            if(buttonTrigger.getCallerLoc() == null)
            {
               throw new java.lang.Error("ObstacleTemplate has ButtonTrigger with no callerLoc.");
            }
            Coord c = new Coord(x, y);
            if(!buttonTrigger.getCallerLoc().equals(c))
            {
               throw new java.lang.Error("ObstacleTemplate has ButtonTrigger with bad callerLoc: expected " + c + ", found " + buttonTrigger.getCallerLoc() + ".");
            }
         }
      }
      if(buttonsFound == 0 && buttonTrigger != null)
         throw new java.lang.Error("ObstacleTemplate has ButtonTrigger with no corresponding button.");
      return true;
   }
}
