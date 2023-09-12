package Pulsar.Actor;

public enum ActionSpeed
{
   SLOW          ("Slow", 4),
   STANDARD      ("Standard", 2),
   FAST          ("Fast", 1),
   INSTANTANEOUS ("Instantaneous", 0);
   
   public String name;
   public int timeCost;
   
   private ActionSpeed(String n, int tc)
   {
      name = n;
      timeCost = tc;
   }
   
   public ActionSpeed faster()
   {
      switch(this)
      {
         case SLOW :       return STANDARD;
         case STANDARD :   return FAST;
      }
      return this;
   }
   
   public ActionSpeed slower()
   {
      switch(this)
      {
         case FAST :       return STANDARD;
         case STANDARD :   return SLOW;
      }
      return this;
   }
}