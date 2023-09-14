package Pulsar.AI;

public interface AIConstants
{
   public static final int DEFAULT_MEMORY_DURATION = 16;
   public static final int PATHFINDING_MAP_RADIUS = 10;
   
   public enum ActorAction
   {
      STEP,
      DELAY,
      CONTEXT_SENSITIVE,
      TOGGLE,
      ATTACK,
      UNARMED_ATTACK,
      USE,
      SWITCH_WEAPONS,
      GADGET_0,
      GADGET_1,
      GADGET_2,
      GADGET_3,
      GADGET_4;
      
      public boolean isGadgetAction()
      {
         return this == GADGET_0 ||
                this == GADGET_1 ||
                this == GADGET_2 ||
                this == GADGET_3 ||
                this == GADGET_4;
      }
      
      public int getGadgetIndex()
      {
         if(isGadgetAction())
            return this.ordinal() - GADGET_0.ordinal();
         return -1;
      }
   }
   
   public enum Team
   {
      PLAYER,
      VILLAIN,
      NEUTRAL;
      
      public boolean isHostile(Team that)
      {
         if(this == that)
            return false;
         if(this == PLAYER && that == VILLAIN)
            return true;
         if(this == VILLAIN)
            return true;
         if(this == NEUTRAL && that == VILLAIN)
            return true;
         return false;
      }
      
      public boolean isFriendly(Team that)
      {
         if(this == that)
            return true;
         if(this == PLAYER && that == NEUTRAL)
            return true;
         if(this == NEUTRAL && that == PLAYER)
            return true;
         return false;
      }
   }
   
   public enum Alertness
   {
      INACTIVE    ("Inactive"),
      RELAXED     ("Relaxed"),
      SURPRISED   ("Surprised"),
      CAUTIOUS    ("Cautious"),
      ALERT       ("Alert");
      
      public String name;
      
      private Alertness(String n)
      {
         name = n;
      }
   }
}