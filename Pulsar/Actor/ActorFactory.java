package Pulsar.Actor;

import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;
import Pulsar.Gear.*;

public class ActorFactory implements ActorConstants, GearConstants, AIConstants
{
   public static Player getPlayer()
   {
      Player a = new Player();
      a.setAI(new PlayerAI(a));
      Weapon weapon = WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE);
     // weapon.addWeaponTag(WeaponTag.MELEE);
      a.setWeapon(weapon);
      //a.setPrimaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.PLASMA));
      a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.SHOTGUN));
      a.setShield(ShieldFactory.getBasicShield());
      a.setMaxHealth(50000);
      a.fullyHeal();
      a.setAlertness(Alertness.CAUTIOUS);
      return a;
   }
   
   public static Actor getTestEnemy()
   {
      Actor a = new Actor('e');
      a.setName("Test Enemy");
      a.setAI(new StandardEnemyAI(a));
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE));
      a.setMaxHealth(20);
      a.fullyHeal();
      return a;
   }
   
   public static Actor getMeleeTestEnemy()
   {
      Actor a = getTestEnemy();
      a.setName("Melee Test Enemy");
      a.setAI(new StandardEnemyAI(a));
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.MELEE));
      return a;
   }
   
   public static Actor getWanderTestEnemy()
   {
      Actor a = getTestEnemy();
      a.setName("Wander Test Enemy");
      a.setAI(new WanderAI(a));
      a.setAlertness(Alertness.INACTIVE);
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.MELEE));
      return a;
   }
   
   public static Actor getGoat()
   {
      Actor a = new Actor('g');
      a.setName("Goat");
      a.setAI(new WanderAI(a));
      a.getAI().setTeam(AIConstants.Team.NEUTRAL);
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.MELEE));
      a.setMaxHealth(20);
      a.fullyHeal();
      return a;
   }
}