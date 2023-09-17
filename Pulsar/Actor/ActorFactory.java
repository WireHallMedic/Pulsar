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
  //    a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.SHOTGUN));
      a.setShield(ShieldFactory.getBasicShield());
      a.setArmor(ArmorFactory.getStandardArmor());
//      a.setArmor(ArmorFactory.getAssaultArmor());
      a.setMaxHealth(50);
      a.fullyHeal();
      a.setAlertness(Alertness.CAUTIOUS);
 //     a.getArmor().setDamageReduction(100);
    //  a.addGadget(GadgetFactory.getAdrenalInjector());
  //    a.addGadget(GadgetFactory.getTurret());
      a.addGadget(GadgetFactory.getNapalmGrenades());
    //  a.addGadget(GadgetFactory.getMotionSensor());
      a.addGadget(GadgetFactory.getAirSupply());
      a.addGadget(GadgetFactory.getHoloclone());
      return a;
   }
   
   public static Actor getHoloclone()
   {
      Actor a = new Actor('@');
      a.setName("Holoclone");
      a.setAI(new CloneAI(a));
      a.setMaxHealth(20);
      a.fullyHeal();
      a.setDeathEffect(DeathEffect.EXPLODE);
      a.setTurnEnergy(FULLY_CHARGED);
      a.setMechanical(true);
      a.setBiological(false);
      a.setBloodColor(ROBOT_BLOOD);
      a.setColor(ROBOT_FLESH);
      a.setNeedsAir(false);
      return a;
   }
   
   public static Actor getTurret()
   {
      Actor a = new Actor('t');
      a.setName("Turret");
      a.setAI(new TurretAI(a));
      a.setMaxHealth(20);
      a.fullyHeal();
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE));
      a.setShield(ShieldFactory.getBasicShield());
      a.setDeathEffect(DeathEffect.EXPLODE);
      a.setTurnEnergy(FULLY_CHARGED);
      a.setMechanical(true);
      a.setBiological(false);
      a.setBloodColor(ROBOT_BLOOD);
      a.setColor(ROBOT_FLESH);
      a.setNeedsAir(false);
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
      a.setBloodColor(ALIEN_COLOR);
      a.setColor(ALIEN_FLESH);
      a.setAI(new StandardEnemyAI(a));
      a.setMaxHealth(20);
      a.fullyHeal();
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
   
   public static Actor getAlienSoldier()
   {
      Actor a = getAlienWorker();
      a.getSprite().setIconIndex('s');
      a.setName("Alien Soldier");
      a.setMaxHealth(a.getMaxHealth() * 2);
      a.fullyHeal();
      return a;
   }
   
   public static Actor getAlienLarva()
   {
      Actor a = getAlienWorker();
      a.getSprite().setIconIndex('l');
      a.setName("Alien Larva");
      a.setMaxHealth(a.getMaxHealth() / 2);
      a.fullyHeal();
      return a;
   }
}