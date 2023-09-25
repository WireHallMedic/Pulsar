package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;

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
   
   public void validate()
   {
      if(getWidth() != REQUIRED_WIDTH || getWidth() != REQUIRED_HEIGHT)
            throw new java.lang.Error("ObstacleTemplate has invalid size.");
   }
}
