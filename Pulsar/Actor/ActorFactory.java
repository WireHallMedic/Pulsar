package Pulsar.Actor;

import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;
import Pulsar.Gear.*;

public class ActorFactory implements ActorConstants, GearConstants, AIConstants, GUIConstants
{
   public static Player getPlayer()
   {
      Player a = new Player();
      a.setAI(new PlayerAI(a));
      //Weapon weapon = WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE);
      //a.setWeapon(weapon);
      a.setPrimaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.SHOTGUN));
      a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.PLASMA));
   //   a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE));
      a.setShield(ShieldFactory.getBasicShield());
      a.setArmor(ArmorFactory.getStandardArmor());
      a.setMaxHealth(50);
      a.fullyHeal();
      a.setAlertness(Alertness.CAUTIOUS);
 //     a.getArmor().setDamageReduction(100);
      a.addGadget(GadgetFactory.getAdrenalInjector());
      a.addGadget(GadgetFactory.getGrenades());
      return a;
   }
   
   public static Actor getTestEnemy()
   {
      Actor a = new Actor('e');
      a.setName("Standard Enemy");
      a.setAI(new StandardEnemyAI(a));
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE));
      a.setShield(ShieldFactory.getBasicShield());
      a.setMaxHealth(20);
      a.fullyHeal();
      return a;
   }
   
   public static Actor getMeleeTestEnemy()
   {
      Actor a = getTestEnemy();
      a.setName("Melee Enemy");
      a.getSprite().setFGColor(Color.ORANGE.getRGB());
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.MELEE));
      a.getWeapon().setBaseDamage(DEFAULT_BASE_DAMAGE * 2);
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
   
   public static Actor getAlienWorker()
   {
      Actor a = new Actor('w');
      a.setName("Alien Worker");
      a.setAI(new StandardEnemyAI(a));
      a.setUnarmedAttack(WeaponFactory.getAlienClaws());
      a.setBloodColor(GUIConstants.ALIEN_COLOR);
      a.setColor(GUIConstants.ALIEN_COLOR);
      a.setAI(new StandardEnemyAI(a));
      return a;
   }
   
   public static Actor getAlienHunter()
   {
      Actor a = getAlienWorker();
      a.getSprite().setIconIndex('h');
      a.setName("Alien Hunter");
      a.setWeapon(WeaponFactory.getAlienSpit());
      return a;
   }
}