package Pulsar.Actor;

import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;
import Pulsar.Gear.*;
import Pulsar.Zone.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

public class ActorFactory implements ActorConstants, GearConstants, AIConstants, GUIConstants
{
   public static Player getPlayer()
   {
      Player a = new Player();
      a.setAI(new PlayerAI(a));
      //Weapon weapon = WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE);
      //a.setWeapon(weapon);
      a.setPrimaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.AUTORIFLE));
      a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.PLASMA));
      WeaponFactory.setElementAndStatusEffect(a.getPrimaryWeapon(), DamageType.KINETIC);
      a.getPrimaryWeapon().setName("The Hammer");
      a.setShield(ShieldFactory.getBasicShield());
//      a.setArmor(ArmorFactory.getScoutArmor());
 //     a.setArmor(ArmorFactory.getStandardArmor());
      a.setArmor(ArmorFactory.getAssaultArmor());
      a.setMaxHealth(50);
      a.fullyHeal();
      a.setAlertness(Alertness.CAUTIOUS);
 //     a.addGadget(GadgetFactory.getAdrenalInjector());
 //     a.addGadget(GadgetFactory.getGrenades());
      a.addGadget(GadgetFactory.getNapalmGrenades());
      a.addGadget(GadgetFactory.getCryoGrenades());
 //     a.addGadget(GadgetFactory.getHoloclone());
 //     a.addGadget(GadgetFactory.getTurret());
 //     a.addGadget(GadgetFactory.getCombatDrone());
      a.addGadget(GadgetFactory.getMotionSensor());
 //s     a.addGadget(GadgetFactory.getAirSupply());
      return a;
   }
   
   public static Actor getAlien(AlienType type)
   {
      switch(type)
      {
         case ALIEN_LARVA     : return getAlienLarva();
         case ALIEN_WORKER    : return getAlienWorker();
         case ALIEN_SOLDIER   : return getAlienSoldier();
         case ALIEN_HUNTER    : return getAlienHunter();
         case ALIEN_QUEEN     : return null;//getAlienQueen();
      }
      return null;
   }
   
   public static void populateWithAliens(Zone z, Coord playerLoc)
   {
      double density = .05;
      int totalCreatures = (int)(z.getMap().getWidth() * z.getMap().getHeight() * density);
      WeightedRandomizer table = new WeightedRandomizer(AlienType.values());
      for(int i = 0; i < totalCreatures; i++)
      {
         AlienType type = (AlienType)table.roll();
         Coord c = new Coord(-1, -1);
         while(!z.getMap().getTile(c).isLowPassable() || z.isActorAt(c) || EngineTools.getDistanceTo(playerLoc, c) <= 10)
         {
            c.x = GameEngine.randomInt(1, z.getMap().getWidth() - 1);
            c.y = GameEngine.randomInt(1, z.getMap().getHeight() - 1);
         }
         Actor e = getAlien(type);
         e.setAllLocs(c);
         z.add(e);
      }
      /*
      for(int i = 0; i < 25; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c) || EngineTools.getDistanceTo(p.getMapLoc(), c) <= 10)
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(1, map.getHeight() - 1);
         }
         Actor e = ActorFactory.getAlienWorker();
         e.setAllLocs(c);
         add(e);
      }
      
      for(int i = 0; i < 10; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c) || EngineTools.getDistanceTo(p.getMapLoc(), c) <= 10)
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(1, map.getHeight() - 1);
         }
         Actor e = ActorFactory.getAlienHunter();
         e.setAllLocs(c);
         add(e);
      }
      
      for(int i = 0; i < 10; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c) || EngineTools.getDistanceTo(p.getMapLoc(), c) <= 10)
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(1, map.getHeight() - 1);
         }
         Actor e = ActorFactory.getAlienSoldier();
         e.setAllLocs(c);
         add(e);
      }
   
      for(int i = 0; i < 5; i++)
      {
         Coord c = new Coord(-1, -1);
         while(!getZoneMap().getTile(c).isLowPassable() || isActorAt(c) || EngineTools.getDistanceTo(p.getMapLoc(), c) <= 10)
         {
            c.x = randomInt(1, map.getWidth() - 1);
            c.y = randomInt(1, map.getHeight() - 1);
         }
         for(int j = 0; j < 5; j++)
         {
            Actor e = ActorFactory.getAlienLarva();
            Coord loc = getClosestEmptyTile(c);
            if(loc != null)
            {
               e.setAllLocs(loc);
               add(e);
            }
         }
      }*/

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
   
   public static Actor getDrone()
   {
      Actor a = new Actor('d');
      a.setName("Combat Drone");
      a.setAI(new StandardAI(a));
      a.setMaxHealth(20);
      a.fullyHeal();
      a.setWeapon(WeaponFactory.getCarbine());
      a.setShield(ShieldFactory.getBasicShield());
      a.setDeathEffect(DeathEffect.EXPLODE);
      a.setTurnEnergy(FULLY_CHARGED);
      a.setMechanical(true);
      a.setBiological(false);
      a.setBloodColor(ROBOT_BLOOD);
      a.setColor(ROBOT_FLESH);
      a.setNeedsAir(false);
      a.setFlying(true);
      return a;
   }
   
   public static Actor getTestEnemy()
   {
      Actor a = new Actor('e');
      a.setName("Standard Enemy");
      a.setAI(new StandardAI(a));
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
      a.setAI(new StandardAI(a));
      a.getAI().setTeam(AIConstants.Team.ALIEN);
      a.setUnarmedAttack(WeaponFactory.getAlienClaws());
      a.setBloodColor(ALIEN_COLOR);
      a.setColor(ALIEN_FLESH);
      a.setAI(new StandardAI(a));
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