package Pulsar.AI;

public interface AIConstants
{
   public enum ActorAction
   {
      STEP,
      DELAY,
      CONTEXT_SENSITIVE,
      TOGGLE,
      ATTACK,
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
         if(this == PLAYER)
         {
            switch(that)
            {
               case VILLAIN : return true;
               case NEUTRAL : return false;
            }
         }
         if(this == VILLAIN)
         {
            return true;
         }
         if(this == NEUTRAL)
         {
            return false;
         }
         return false;
      }
   }
}