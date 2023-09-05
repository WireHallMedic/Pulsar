package Pulsar.Actor;

import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;

public class ActorFactory implements ActorConstants
{
   public static Player getPlayer()
   {
      Player a = new Player();
      a.setAI(new PlayerAI(a));
      return a;
   }
}