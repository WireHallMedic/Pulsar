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
      a.setPrimaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.SHOTGUN));
      a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.BEAM));
      WeaponFactory.setElementAndStatusEffect(a.getPrimaryWeapon(), DamageType.CRYO);
      a.setShield(ShieldFactory.getBasicShield());
      a.setArmor(ArmorFactory.getScoutArmor());
 //     a.setArmor(ArmorFactory.getStandardArmor());
  //    a.setArmor(ArmorFactory.getAssaultArmor());
      a.setMaxHealth(50);
      a.fullyHeal();
      a.setAlertness(Alertness.CAUTIOUS);
 //     a.addGadget(GadgetFactory.getAdrenalInjector());
      a.addGadget(GadgetFactory.getGrenades());
      a.addGadget(GadgetFactory.getNapalmGrenades());
 //     a.addGadget(GadgetFactory.getCryoGrenades());
 //     a.addGadget(GadgetFactory.getHoloclone());
 //     a.addGadget(GadgetFactory.getTurret());
      a.addGadget(GadgetFactory.getCombatDrone());
 //     a.addGadget(GadgetFactory.getMotionSensor());
 //     a.addGadget(GadgetFactory.getAirSupply());
 //     a.addGadget(GadgetFactory.getQuickCharger());
 
 
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
   
   public static Actor getPirate(PirateType type)
   {
      switch(type)
      {
         case PIRATE_DRONE    : return getPirateDrone();
         case PIRATE_GRUNT    : return getPirateGrunt();
         case PIRATE_COMMANDO : return getPirateCommando();
         case PIRATE_OFFICER  : return getPirateOfficer();
      }
      return null;
   }
      
   
   public static void populateWithAliens(Zone z, Coord playerLoc){populateWithAliens(z, playerLoc, MEDIUM_DENSITY);}
   public static void populateWithAliens(Zone z, Coord playerLoc, double density)
   {
      int totalCreatures = (int)(z.getMap().countOpenTiles() * density);
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
         if(type == AlienType.ALIEN_LARVA)
         {
            int count = GameEngine.randomInt(5, 8);
            i--;
            for(int j = 0; j < count; j++)
            {
               Coord c2 = z.getClosestEmptyTile(c);
               if(c2 != null)
               {
                  Actor e = getAlien(type);
                  e.setAllLocs(c2);
                  z.add(e);
                  i++;
               }
            }
         }
         else
         {
            Actor e = getAlien(type);
            e.setAllLocs(c);
            z.add(e);
         }
      }
   }
      
   
   public static void populateWithPirates(Zone z, Coord playerLoc){populateWithPirates(z, playerLoc, LOW_DENSITY);}
   public static void populateWithPirates(Zone z, Coord playerLoc, double density)
   {
      int totalCreatures = (int)(z.getMap().countOpenTiles() * density);
      WeightedRandomizer table = new WeightedRandomizer(PirateType.values());
      for(int i = 0; i < totalCreatures; i++)
      {
         PirateType type = (PirateType)table.roll();
         Coord c = new Coord(-1, -1);
         while(!z.getMap().getTile(c).isLowPassable() || z.isActorAt(c) || EngineTools.getDistanceTo(playerLoc, c) <= 10)
         {
            c.x = GameEngine.randomInt(1, z.getMap().getWidth() - 1);
            c.y = GameEngine.randomInt(1, z.getMap().getHeight() - 1);
         }
         if(type == PirateType.PIRATE_COMMANDO || type == PirateType.PIRATE_OFFICER)
         {
            Actor boss = getPirate(type);
            Actor d1 = getPirate(PirateType.PIRATE_DRONE);
            Actor d2 = getPirate(PirateType.PIRATE_DRONE);
            d1.getAI().setLeader(boss);
            d2.getAI().setLeader(boss);
            Coord c2 = z.getClosestEmptyTile(c);
            if(c2 != null)
            {
               boss.setAllLocs(c2);
               z.add(boss);
            }
            c2 = z.getClosestEmptyTile(c);
            if(c2 != null)
            {
               d1.setAllLocs(c2);
               z.add(d1);
               i++;
            }
            c2 = z.getClosestEmptyTile(c);
            if(c2 != null)
            {
               d2.setAllLocs(c2);
               z.add(d2);
               i++;
            }
         }
         else
         {
            Actor e = getPirate(type);
            e.setAllLocs(c);
            z.add(e);
         }
      }
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
      a.getAI().setUsesDoors(false);
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
      a.getAI().setUsesDoors(false);
      a.getAI().setAvoidsHazards(false);
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
   
   public static Actor getVictim()
   {
      Actor a = new Actor('v');
      a.setName("Victim");
      a.setAI(new VictimAI(a));
      a.setMaxHealth(20);
      a.fullyHeal();
      a.setAlertness(Alertness.ALERT);
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
      a.getAI().setUsesDoors(false);
      a.getAI().setTeam(AIConstants.Team.NEUTRAL);
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.MELEE));
      a.setMaxHealth(20);
      a.fullyHeal();
      return a;
   }
   
   
   // mercenaries
   ////////////////////////////////////////////////////////////////////////////
   
   
   public static Actor getPirateGrunt()
   {
      Actor a = new Actor('g');
      a.setName("Grunt");
      a.setAI(new StandardAI(a));
      a.setColor(HUMAN_FLESH);
      a.setWeapon(WeaponFactory.getPistol());
      a.setShield(ShieldFactory.getBasicShield());
      a.setMaxHealth(20);
      a.fullyHeal();
      return a;
   }
   
   public static Actor getPirateDrone()
   {
      Actor a = getDrone();
      a.setName("Drone");
      return a;
   }
   
   public static Actor getPirateCommando()
   {
      Actor a = getPirateGrunt();
      a.setName("Commando");
      a.setIconIndex('c');
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.SHOTGUN));
      a.setMaxHealth(a.getMaxHealth() * 3 / 2);
      a.fullyHeal();
      return a;
   }
   
   public static Actor getPirateOfficer()
   {
      Actor a = getDrone();
      a.setName("Officer");
      a.setWeapon(WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE));
      a.setMaxHealth(a.getMaxHealth() * 2);
      a.fullyHeal();
      return a;
   }
   
   // aliens
   ////////////////////////////////////////////////////////////////////////////
   
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
      a.setIconIndex('h');
      a.setName("Alien Hunter");
      a.setWeapon(WeaponFactory.getAlienSpit());
      return a;
   }
   
   public static Actor getAlienSoldier()
   {
      Actor a = getAlienWorker();
      a.setIconIndex('s');
      a.setName("Alien Soldier");
      a.setMaxHealth(a.getMaxHealth() * 2);
      a.fullyHeal();
      return a;
   }
   
   public static Actor getAlienLarva()
   {
      Actor a = getAlienWorker();
      a.getAI().setUsesDoors(false);
      a.setIconIndex('l');
      a.setName("Alien Larva");
      a.setMaxHealth(a.getMaxHealth() / 2);
      a.fullyHeal();
      return a;
   }
}