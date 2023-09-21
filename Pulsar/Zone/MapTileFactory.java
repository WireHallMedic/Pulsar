package Pulsar.Zone;

import Pulsar.GUI.*;
import java.awt.*;

public class MapTileFactory implements ZoneConstants, GUIConstants
{
   public static MapTile getTile(MapTile original)
   {
      if(original instanceof Vacuum)
         return new Vacuum();
      return new MapTile(original);
   }
   
   public static MapTile getTile(TileType baseType)
   {
      return getTile(baseType, DEFAULT_TILE_FG_COLOR, DEFAULT_TILE_BG_COLOR);
   }
   
   public static MapTile getTile(TileType baseType, Color fgColor, Color bgColor)
   {
      if(baseType == TileType.WATER)
         return getWater();
      if(baseType == TileType.ACID)
         return getAcid();
      if(baseType == TileType.TERMINAL)
         return getTerminal();
      if(baseType == TileType.DOOR)
         return getDoor();
      MapTile tile = new MapTile(baseType.iconIndex, fgColor.getRGB(), bgColor.getRGB(), baseType.name, 
                                 baseType.lowPassable, baseType.highPassable, baseType.transparent);
      if(baseType == TileType.NULL || baseType == TileType.CLEAR)
         tile.setDurability(Durability.UNBREAKABLE);
      if(baseType == TileType.RUBBLE)
         tile.setSlowsMovement(true);
      return tile;
   }
   
   public static MapTile getDoor()
   {
      Door door = new Door(DEFAULT_TILE_FG_COLOR.getRGB(), DEFAULT_TILE_BG_COLOR.getRGB(), "Door");
      return door;
   }
   
   public static MapTile getButton()
   {
      Button button = new Button(DEFAULT_TILE_FG_COLOR.getRGB(), DEFAULT_TILE_BG_COLOR.getRGB());
      return button;
   }
   
   public static MapTile getVacuum()
   {
      return new Vacuum();
   }
   
   public static Acid getAcid()
   {
      return new Acid();
   }
   
   public static MapTile getBroken(MapTile originalTile)
   {
      MapTile brokenTile = getTile(TileType.RUBBLE);
      brokenTile.setFGColor(originalTile.getFGColor());
      brokenTile.setBGColor(originalTile.getBGColor());
      return brokenTile;
   }
   
   public static MapTile getCrate()
   {
      MapTile tile = getTile(TileType.HIGH_WALL);
      tile.setName("Crate");
      tile.setFGColor(BROWN.getRGB());
      tile.setDurability(Durability.FRAGILE);
      return tile;
   }
   
   public static MapTile getBarrel()
   {
      MapTile tile = getTile(TileType.LOW_WALL);
      tile.setIconIndex('0');
      tile.setName("Barrel");
      tile.setFGColor(LIGHT_GREY.getRGB());
      tile.setDurability(Durability.FRAGILE);
      return tile;
   }
   
   public static MapTile getExplodingBarrel()
   {
      MapTile tile = getBarrel();
      tile.setName("Exploding Barrel");
      tile.setFGColor(RED.getRGB());
      tile.setOnDestructionEffect(OnDestructionEffect.EXPLOSION);
      return tile;
   }
   
   public static MapTile getWaterBarrel()
   {
      MapTile tile = getBarrel();
      tile.setName("Water Barrel");
      tile.setFGColor(LIGHT_BLUE.getRGB());
      tile.setOnDestructionEffect(OnDestructionEffect.FLOOD_WATER);
      return tile;
   }
   
   public static MapTile getWater()
   {
      TileType waterType = TileType.WATER;
      GradientTile tile = new GradientTile(waterType.iconIndex, DEFAULT_TILE_FG_COLOR.getRGB(), DEFAULT_TILE_BG_COLOR.getRGB(), 
                                           "Water", waterType.lowPassable, waterType.highPassable, waterType.transparent);
      tile.setGradient(WATER_COLOR_GRADIENT);
      tile.setDurability(Durability.UNBREAKABLE);
      tile.setPulseType(GradientTile.BACKGROUND);
      tile.setPulseSpeed(GradientTile.SLOW);
      tile.setLiquid(true);
      tile.setSlowsMovement(true);
      return tile;
   }
   
   public static MapTile getTerminal()
   {
      Terminal terminal = new Terminal(TERMINAL_FG_COLOR.getRGB(), DEFAULT_TILE_BG_COLOR.getRGB());
      return terminal;
   }
}