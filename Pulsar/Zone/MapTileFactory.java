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
      if(baseType == TileType.VACUUM)
         return getVacuum();
      MapTile tile = new MapTile(baseType.iconIndex, fgColor.getRGB(), bgColor.getRGB(), baseType.name, 
                                 baseType.lowPassable, baseType.highPassable, baseType.transparent);
      if(baseType == TileType.NULL || baseType == TileType.CLEAR)
         tile.setDurability(Durability.UNBREAKABLE);
      if(baseType == TileType.RUBBLE)
         tile.setSlowsMovement(true);
      return tile;
   }
   
   // getting tile from a templateTile; note that probabilistic tiles should already be determined
   public static MapTile getTileFromTemplate(char c, TileType oob)
   {
      switch(c)
      {
         // border legal tiles
         case TEMPLATE_VACUUM       : return getVacuum();
         case TEMPLATE_CLEAR        : return getTile(TileType.CLEAR);
         case TEMPLATE_DOOR         : return getDoor();
         case TEMPLATE_WALL         : return getTile(TileType.HIGH_WALL);
         case TEMPLATE_OOB          : return getTile(oob);
         
         // misc tiles
         case TEMPLATE_BARREL             : return getBarrel();
         case TEMPLATE_WATER_BARREL       : return getWaterBarrel();
         case TEMPLATE_EXPLODING_BARREL   : return getExplodingBarrel();
         case TEMPLATE_CRATE              : return getCrate();
         case TEMPLATE_TABLE              : return getTile(TileType.LOW_WALL);
         case TEMPLATE_RUBBLE             : return getTile(TileType.RUBBLE);
         case TEMPLATE_WATER              : return getWater();
         case TEMPLATE_ACID               : return getAcid();
         case TEMPLATE_BUTTON             : return getButton();
         case TEMPLATE_WINDOW             : return getTile(TileType.WINDOW);
         case TEMPLATE_TERMINAL           : return getTerminal();
         
         // traversal tiles
         case TEMPLATE_EXIT         : return getTile(TileType.CLEAR);
         case TEMPLATE_DOWN_STAIRS  : return getTile(TileType.CLEAR);
         case TEMPLATE_UP_STAIRS    : return getTile(TileType.CLEAR);
         
         default        : System.out.println("Unrecognized tile type: " + ((char)c)); return null;
      }
   }

   
   public static Door getDoor()
   {
      Door door = new Door(DEFAULT_TILE_FG_COLOR.getRGB(), DEFAULT_TILE_BG_COLOR.getRGB(), "Door");
      return door;
   }
   
   public static Door getAutomaticDoor()
   {
      Door door = getDoor();
      door.setAutomatic(true);
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