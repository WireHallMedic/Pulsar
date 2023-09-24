package Pulsar.Zone;

import java.util.*;

public class ObstacleTemplate extends MapTemplate implements ZoneConstants
{
   public static final int REQUIRED_WIDTH = 5;
   public static final int REQUIRED_HEIGHT = 5;
   
   public ObstacleTemplate(Vector<String> input)
   {
      super(input);
      validate();
   }
   
   public ObstacleTemplate(ObstacleTemplate that)
   {
      super(that);
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
