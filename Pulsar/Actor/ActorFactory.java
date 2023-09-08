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
      Weapon weapon = WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE);
     // weapon.addWeaponTag(WeaponTag.MELEE);
      a.setWeapon(weapon);
      a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.SHOTGUN));
      a.setShield(new Shield());
      a.setMaxHealth(50);
      a.fullyHeal();
      return a;
   }
   
   public static Actor getTestEnemy()
   {
      Actor a = new Actor('e');
      a.setName("Test Enemy");
      a.setAI(new WanderAI(a));
      a.setMaxHealth(20);
      a.fullyHeal();
      return a;
   }
}