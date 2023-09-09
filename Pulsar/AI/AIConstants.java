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
      SWITCH_WEAPONS;
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
}