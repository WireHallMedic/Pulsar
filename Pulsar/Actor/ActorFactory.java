package Pulsar.Actor;

import Pulsar.GUI.*;
import Pulsar.AI.*;
import java.awt.*;
import Pulsar.Gear.*;
import Pulsar.Zone.*;
import Pulsar.Engine.*;
import WidlerSuite.*;
import java.util.*;

public class ActorFactory implements ActorConstants, GearConstants, AIConstants, GUIConstants
{
   
   public static final double POPULATE_CHANCE_CORRIDOR = 0.50;
   public static final double POPULATE_CHANCE_OPEN     = 0.90;
   public static final double POPULATE_CHANCE_CLOSED   = 0.90;
   public static final double POPULATE_CHANCE_BOSS     = 1.00;
   
   private static boolean[][] enemyPlacableMap;
   private static boolean[][] allTrueMap;  // for spiralsearching for placable locations
   
   
   public static Player getPlayer()
   {
      Player a = new Player();
      a.setAI(new PlayerAI(a));
      //Weapon weapon = WeaponFactory.getBasicWeapon(WeaponType.BATTLE_RIFLE);
      //a.setWeapon(weapon);
      a.setPrimaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.SHOTGUN));
 //     a.setSecondaryWeapon(WeaponFactory.getBasicWeapon(WeaponType.PLASMA));
 //     WeaponFactory.setElementAndStatusEffect(a.getPrimaryWeapon(), DamageType.THERMAL);
      a.setShield(ShieldFactory.getBasicShield());
      a.setArmor(ArmorFactory.getScoutArmor());
 //     a.setArmor(ArmorFactory.getEngineerArmor());
  //    a.setArmor(ArmorFactory.getAssaultArmor());
      a.setMaxHealth(50);
      a.fullyHeal();
      a.setAlertness(Alertness.CAUTIOUS);
 //     a.addGadget(GadgetFactory.getAdrenalInjector());
 //     a.addGadget(GadgetFactory.getGrenades());
      a.addGadget(GadgetFactory.getNapalmGrenades());
 //     a.addGadget(GadgetFactory.getCryoGrenades());
 //     a.addGadget(GadgetFactory.getHoloclone(LootRarity.RARE));
 //     a.addGadget(GadgetFactory.getTurret());
 //     a.addGadget(GadgetFactory.getCombatDrone());
 //     a.addGadget(GadgetFactory.getLoveBomb());
 //     a.addGadget(GadgetFactory.getBreachingCharge());
 //     a.addGadget(GadgetFactory.getMotionSensor());
 //     a.addGadget(GadgetFactory.getAirSupply());
 //     a.addGadget(GadgetFactory.getEMPGrenades());
  //    a.addGadget(GadgetFactory.getQuickCharger());
 //     a.addGadget(GadgetFactory.getAcidBomb());
  
      a.getInventory().add(WeaponFactory.getBasicWeapon(WeaponType.SLUG_RIFLE));
 
 
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
         case ALIEN_QUEEN     : return getAlienQueen();
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
   
   
   public static Actor generate(EnemyType e)
   {
      if(e instanceof AlienType)
         return getAlien((AlienType)e);
      if(e instanceof PirateType)
         return getPirate((PirateType)e);
      throw new java.lang.Error("Unknown EnemyType: " + e);
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
            i--;
            int count = GameEngine.randomInt(5, 8);
            for(int j = 0; j < count; j++)
            {
               add(z, getAlien(type), c);
               i++;
            }
         }
         else
         {
            add(z, getAlien(type), c);
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
            
            add(z, boss, c);
            add(z, d1, c);
            add(z, d2, c);
            i += 2;
         }
         else
         {
            add(z, getPirate(type), c);
         }
      }
   }
   
   public static void populateWithAliens(Zone zone, ZoneBuilder zoneBuilder)
   {
      populateByRoom(zone, zoneBuilder, AlienType.values());
   }
   
   public static void populateWithPirates(Zone zone, ZoneBuilder zoneBuilder)
   {
      populateByRoom(zone, zoneBuilder, PirateType.values());
   }
   
   public static boolean add(Zone zone, Actor actor, Coord target)
   {
      target = zone.getClosestEmptyTile(target);
      if(target != null)
      {
         actor.setAllLocs(target);
         zone.add(actor);
         return true;
      }
      return false;
   }
   
   public static boolean add(Zone zone, Vector<Actor> actorList, Coord target)
   {
      for(Actor actor : actorList)
      {
         if(!add(zone, actor, target))
            return false;
      }
      return true;
   }
   
   public static void setEnemyPlacableMap(Zone zone, ZoneBuilder zoneBuilder)
   {
      ZoneMap map = zone.getMap();
      Coord startLoc = map.getExit();
      int w = map.getWidth();
      int h = map.getHeight();
      
      enemyPlacableMap = new boolean[w][w];
      allTrueMap = new boolean[w][w];
      
      // set all low passable true
      for(int x = 0; x < w; x++)
      for(int y = 0; y < h; y++)
      {
         enemyPlacableMap[x][y] = map.getTile(x, y).isLowPassable();
         allTrueMap[x][y] = true;
      }
      
      // mask off closets
      for(Closet closet : zoneBuilder.getClosetList())
      {
         for(int x = closet.getOrigin().x; x < closet.getOrigin().x + closet.getSize().x; x++)
         for(int y = closet.getOrigin().y; y < closet.getOrigin().y + closet.getSize().y; y++)
            enemyPlacableMap[x][y] = false;
      }
      
      // mask off starting area
      for(int x = startLoc.x - 10; x < startLoc.x + 11; x++)
      for(int y = startLoc.y - 1; y < startLoc.y + 11; y++)
      {
         if(map.isInBounds(x, y))
            enemyPlacableMap[x][y] = false;
      }
   }
   
   public static boolean populateNearPoint(Zone zone, Coord c, EnemyType enemyType){return populateNearPoint(zone, c.x, c.y, enemyType);}
   public static boolean populateNearPoint(Zone zone, int xTarget, int yTarget, EnemyType enemyType)
   {
      SpiralSearch search = new SpiralSearch(allTrueMap, xTarget, yTarget);
      Coord c = search.getNext();
      while(c != null && enemyPlacableMap[c.x][c.y] == false)
         c = search.getNext();
      if(c == null)
      {
         System.out.println("Unable to place");
         return false;
      }
      int count = GameEngine.randomInt(enemyType.getMinGroupSize(), enemyType.getMaxGroupSize() + 1);
      for(int i = 0; i < count; i++)
         add(zone, generate(enemyType), c);
      if(enemyType.getMinionCount() > 0)
      {
         EnemyType minionType = enemyType.getMinion();
         for(int i = 0; i < enemyType.getMinionCount(); i++)
            add(zone, generate(minionType), c);
      }
      return true;
   }
   
   public static void populateByRoom(Zone zone, ZoneBuilder zoneBuilder, EnemyType[] enemyTable)
   {
      char[][] roomMap = zoneBuilder.getCellMap();
      int w = roomMap.length;
      int h = roomMap[0].length;
      int roomWidth = zoneBuilder.getRoomWidth() - 1;
      int roomHeight = zoneBuilder.getRoomHeight() - 1;
      setEnemyPlacableMap(zone, zoneBuilder);
      WeightedRandomizer table = new WeightedRandomizer(enemyTable);
      for(int x = 0; x < w; x++)
      for(int y = 0; y < h; y++)
      {
         int xTarget = (x * roomWidth) + (roomWidth / 2);
         int yTarget = (y * roomHeight) + (roomHeight / 2);
         switch(roomMap[x][y])
         {
            case ZoneBuilder.OPEN_ROOM :
               if(GameEngine.random() <= POPULATE_CHANCE_OPEN)
               {
                  populateNearPoint(zone, xTarget, yTarget, (EnemyType)table.roll());
               }
               break;
            case ZoneBuilder.CLOSED_ROOM :
               if(GameEngine.random() <= POPULATE_CHANCE_CLOSED)
               {
                  populateNearPoint(zone, xTarget, yTarget, (EnemyType)table.roll());
               }
               break;
            case ZoneBuilder.CORRIDOR :
               if(GameEngine.random() <= POPULATE_CHANCE_CORRIDOR)
               {
                  populateNearPoint(zone, xTarget, yTarget, (EnemyType)table.roll());
               }
               break;
            case ZoneBuilder.OOB_ROOM :
               break;
            case ZoneBuilder.STARTING_ROOM :
               break;
         }
      }
      // add another one to the boss room
      int xTarget = (zoneBuilder.getBossRoom().x * roomWidth) + (roomWidth / 2);
      int yTarget = (zoneBuilder.getBossRoom().y * roomHeight) + (roomHeight / 2);
      {
         populateNearPoint(zone, xTarget, yTarget, (EnemyType)table.roll());
      }
   }

   
   public static void populateFromSpawnPoints(Zone zone, ZoneBuilder zoneBuilder, EnemyType[] enemyTable)
   {
      WeightedRandomizer table = new WeightedRandomizer(enemyTable);
      Vector<SpawnPoint> bossRoomList = new Vector<SpawnPoint>();
      for(SpawnPoint spawnPoint : zoneBuilder.getSpawnPointList())
      {
         if(spawnPoint.isBoss())
         {
            bossRoomList.add(spawnPoint);
         }
         else
         {
            switch(spawnPoint.getRoom())
            {
               case ZoneBuilder.OPEN_ROOM :
                  if(GameEngine.random() <= POPULATE_CHANCE_OPEN)
                  {
                     populateNearPoint(zone, spawnPoint.getLoc(), (EnemyType)table.roll());
                  }
                  break;
               case ZoneBuilder.CLOSED_ROOM :
                  if(GameEngine.random() <= POPULATE_CHANCE_CLOSED)
                  {
                     populateNearPoint(zone, spawnPoint.getLoc(), (EnemyType)table.roll());
                  }
                  break;
               case ZoneBuilder.CORRIDOR :
                  if(GameEngine.random() <= POPULATE_CHANCE_CORRIDOR)
                  {
                     populateNearPoint(zone, spawnPoint.getLoc(), (EnemyType)table.roll());
                  }
                  break;
               case ZoneBuilder.OOB_ROOM :
                  break;
               case ZoneBuilder.STARTING_ROOM :
                  break;
            }
         }
         if(bossRoomList.size() > 0)
         {
            int bossIndex = GameEngine.randomInt(0, bossRoomList.size() + 1);
            for(int i = 0; i < bossRoomList.size(); i++)
            {
               if(i == bossIndex)
                  populateNearPoint(zone, spawnPoint.getLoc(), enemyTable[0].getBoss());
               else
                  populateNearPoint(zone, spawnPoint.getLoc(), (EnemyType)table.roll());
            }
         }
      }
      /*
      // add another one to the boss room
      int xTarget = (zoneBuilder.getBossRoom().x * roomWidth) + (roomWidth / 2);
      int yTarget = (zoneBuilder.getBossRoom().y * roomHeight) + (roomHeight / 2);
      {
         populateNearPoint(zone, xTarget, yTarget, (EnemyType)table.roll());
      }*/
   }
   
   public static Actor getHoloclone()
   {
      Actor a = new Actor('@');
      a.setName("Holoclone");
      a.setAI(new CloneAI(a));
      a.setMaxHealth(40);
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
   
   public static Actor getBreachingCharge()
   {
      Actor a = new Actor('0');
      a.setColor(ORANGE);
      a.setName("Breaching Charge");
      CloneAI ai = new CloneAI(a);
      ai.setCountsDown(true);
      ai.setTeam(Team.NONE);
      a.setAI(ai);
      a.setMaxHealth(50);
      a.fullyHeal();
      a.setDeathEffect(DeathEffect.BREACH);
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
   
   // this enemy is lame, make them cool
   public static Actor getAlienQueen()
   {
      Actor a = getAlienWorker();
      a.setIconIndex('s');
      a.setName("Alien Queen");
      a.setMaxHealth(a.getMaxHealth() * 4);
      a.fullyHeal();
      return a;
   }
   
   
   
   public static void main(String[] args)
   {
      ZoneBuilder zoneBuilder = new ZoneBuilder();
      ZoneMap map = ZoneMapFactory.buildFromTemplates(zoneBuilder, ZoneConstants.TileType.VACUUM);
      Zone zone = new Zone("Test Map", -1, map, zoneBuilder);
      GameEngine.setCurZone(zone);
      Player p = ActorFactory.getPlayer();
      ActorFactory.populateWithPirates(zone, zoneBuilder);
      char[][] outMap = new char[map.getWidth()][map.getHeight()];
      for(int x = 0; x < outMap.length; x++)
      for(int y = 0; y < outMap[0].length; y++)
      {
         MapTile tile = map.getTile(x, y);
         if(tile.isLowPassable())
            outMap[x][y] = '.';
         else if (tile instanceof Door)
            outMap[x][y] = '/';
         else
            outMap[x][y] = '#';
      }
      
      for(Actor a : GameEngine.getActorList())
      {
         outMap[a.getMapLoc().x][a.getMapLoc().y] = 'X';
      }
      
      for(int y = 0; y < outMap[0].length; y++)
      {
         for(int x = 0; x < outMap.length; x++)
         {
            System.out.print("" + outMap[x][y]);
         }
         System.out.println();
      }
   }
}