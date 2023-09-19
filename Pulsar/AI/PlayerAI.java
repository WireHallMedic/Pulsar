package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;

public class PlayerAI extends BasicAI
{
   public PlayerAI(Actor s)
   {
      super(s);
      team = Team.PLAYER;
   }
   
   public void plan()
   {
      ;
   }
   
//    @Override
//    public void act()
//    {
//       super.act();
//       GameEngine.getPlayer().updateFoV();
//    }
}