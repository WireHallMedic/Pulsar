package Pulsar.Engine;

public interface EngineConstants
{
   public static final int SUFFOCATION_DAMAGE = 2;
   
   public enum GameMode
   {
      STANDARD, TARGETING, LOOK, OTHER_PANEL;
   }
   
   public enum ButtonAction
   {
      TOGGLE,
      FLOOD_WATER,
      FLOOD_ACID;
      
      public static ButtonAction parse(String s)
      {
         for(ButtonAction action : ButtonAction.values())
         {
            if(action.toString().toLowerCase().equals(s.toLowerCase().trim()))
               return action;
         }
         return null;
      }
   }
   
}