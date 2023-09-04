package Pulsar.Actor;

import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;

public class ActorFactory implements ActorConstants
{
   public static Actor getPlayer()
   {
      Player a = new Player();
      a.setAI(new PlayerAI(a));
      return a;
   }
}