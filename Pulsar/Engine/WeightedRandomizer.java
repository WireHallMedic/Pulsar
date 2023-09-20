package Pulsar.Engine;

import java.util.*;

public class WeightedRandomizer
{
   private Vector<WeightedRandomizable> list;
   private int maxRoll;
   
   public WeightedRandomizer()
   {
      list = new Vector<WeightedRandomizable>();
      maxRoll = 0;
   }
   
   public WeightedRandomizer(Vector<WeightedRandomizable> inputList)
   {
      this();
      for(WeightedRandomizable entry : inputList)
         add(entry);
   }
   
   public WeightedRandomizer(WeightedRandomizable[] inputList)
   {
      this();
      for(WeightedRandomizable entry : inputList)
         add(entry);
   }
   
   public void add(WeightedRandomizable entry)
   {
      list.add(entry);
      maxRoll += entry.getWeight();
   }
   
   public WeightedRandomizable roll()
   {
      int roll = GameEngine.randomInt(0, maxRoll);
      for(WeightedRandomizable entry : list)
      {
         if(roll < entry.getWeight())
            return entry;
         roll -= entry.getWeight();
      }
      return null;
   }
}