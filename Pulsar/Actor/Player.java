package Pulsar.Actor;

import Pulsar.AI.*;
import Pulsar.GUI.*;
import WidlerSuite.*;

public class Player extends Actor implements GUIConstants
{
   
   public Player()
   {
      super('@', "Player");
      setAI(new PlayerAI(this));
      getSprite().setFGColor(PLAYER_COLOR.getRGB());
   }
}