package Pulsar.AI;

public interface AIConstants
{
   public static final int DEFAULT_MEMORY_DURATION = 16;
   public static final int PATHFINDING_MAP_RADIUS = 10;
   
   public enum ActorAction
   {
      STEP                 (true),
      DELAY                (true),
      CONTEXT_SENSITIVE    (false),
      TOGGLE               (true),
      ATTACK               (false),
      UNARMED_ATTACK       (false),
      USE                  (false),
      GRAB                 (false),
      SWITCH_WEAPONS       (true),
      GADGET_0             (false),
      GADGET_1             (false),
      GADGET_2             (false),
      GADGET_3             (false),
      GADGET_4             (false);
      
      public boolean canUseDuringAnimationLock;
      
      private ActorAction(boolean cu)
      {
         canUseDuringAnimationLock = cu;
      }
      
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
      ALIEN,
      NEUTRAL;
      
      public boolean isHostile(Team that)
      {
         if(this == that)
            return false;
         if(this == PLAYER)
         {
            if(that == VILLAIN) return true;
            if(that == ALIEN) return true;
            if(that == NEUTRAL) return false;
         }
         if(this == VILLAIN)
         {
            if(that == PLAYER) return true;
            if(that == ALIEN) return true;
            if(that == NEUTRAL) return false;
         }
         if(this == ALIEN)
         {
            if(that == VILLAIN) return true;
            if(that == PLAYER) return true;
            if(that == NEUTRAL) return true;
         }
         if(this == NEUTRAL)
         {
            if(that == VILLAIN) return true;
            if(that == ALIEN) return true;
            if(that == PLAYER) return false;
         }
         return false;
      }
      
      public boolean isFriendly(Team that)
      {
         if(this == that)
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