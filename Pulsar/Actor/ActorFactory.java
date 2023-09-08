package Pulsar.Actor;

import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;
import Pulsar.Gear.*;

public class ActorFactory implements ActorConstants, GearConstants
{
   public static Player getPlayer()
   {
      Player a = new Player();
      a.setAI(new PlayerAI(a));
      Weapon weapon = new Weapon();
      weapon.setAttacks(1);
     // weapon.addWeaponTag(WeaponTag.MELEE);
      a.setWeapon(weapon);
      a.setShield(new Shield());
      a.setCurHealth(32);
      a.setMaxHealth(32);
      return a;
   }
}