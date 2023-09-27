package Pulsar.GUI;

import java.awt.*;
import WidlerSuite.*;

public interface GUIConstants
{

   public static final int FRAMES_PER_SECOND = 30;
   
   public static final int DEFAULT_WINDOW_WIDTH = 1150;
   public static final int DEFAULT_WINDOW_HEIGHT = 720;
   public static final int TERMINAL_WIDTH_TILES = 80;
   public static final int TERMINAL_HEIGHT_TILES = 24;
   public static final int TERMINAL_TILE_WIDTH_PIXELS = 8;
   public static final int TERMINAL_TILE_HEIGHT_PIXELS = 16;
   public static final int MAP_WIDTH_TILES = 17;
   public static final int MAP_HEIGHT_TILES = 17;
   public static final int MAP_X_INSET_TILES = ((TERMINAL_WIDTH_TILES - (2 * MAP_WIDTH_TILES)) / 2);
   
   // basic color palette
   public static final Color BLACK = new Color(0, 0, 0);
   public static final Color DARK_GREY = new Color(63, 72, 79);
   public static final Color LIGHT_GREY = new Color(212, 224, 238);
   public static final Color WHITE = new Color(255, 255, 255);
   public static final Color DARK_BLUE = new Color(19, 47, 209);
   public static final Color DARK_GREEN = new Color(18, 127, 44);
   public static final Color BROWN = new Color(114, 73, 30);
   public static final Color PURPLE = new Color(104, 49, 192);
   public static final Color LIGHT_BLUE = new Color(0, 162, 232);
   public static final Color LIGHT_GREEN = new Color(80, 240, 58);
   public static final Color BEIGE = new Color(239, 228, 176);
   public static final Color RED = new Color(188, 10, 2);
   public static final Color CYAN = Color.CYAN;//new Color(56, 254, 219);
   public static final Color YELLOW = new Color(245, 245, 89);
   public static final Color ORANGE = new Color(239, 125, 13);
   public static final Color PINK = new Color(238, 83, 185);
   public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
   
   public static final Color ALIEN_COLOR = PURPLE;
   public static final Color BG_COLOR = BLACK;
   public static final Color TERMINAL_FG_COLOR = CYAN;
   public static final Color DEFAULT_TILE_FG_COLOR = WHITE;
   public static final Color DEFAULT_TILE_BG_COLOR = DARK_GREY;
   public static final Color DEFAULT_MESSAGE_COLOR = CYAN;
   public static final Color PLAYER_COLOR = ORANGE;
   public static final Color SHIELD_COLOR = WHITE;
   public static final Color HEALTH_COLOR = RED;
   public static final Color DEFAULT_ACTOR_FG_COLOR = WHITE;
   public static final Color DEFAULT_ACTOR_BG_COLOR = BLACK;
   public static final Color RETICULE_COLOR = YELLOW;
   public static final Color INVALID_RETICULE_COLOR = RED;
   public static final Color HUMAN_BLOOD = RED;
   public static final Color ROBOT_BLOOD = LIGHT_GREY;
   public static final Color ALIEN_BLOOD = ALIEN_COLOR;
   public static final Color HUMAN_FLESH = BEIGE;
   public static final Color ROBOT_FLESH = LIGHT_GREY;
   public static final Color ALIEN_FLESH = ALIEN_COLOR;
   public static final Color WARNING_COLOR = ORANGE;
   public static final Color FIRE_COLOR = Color.ORANGE;  // the regular one, not the custom orange
   public static final Color ASH_COLOR = LIGHT_GREY;
   public static final Color SENSOR_COLOR = LIGHT_GREEN;
   public static final Color MEMORY_COLOR = SENSOR_COLOR.darker().darker();
   public static final Color OUT_OF_SIGHT_COLOR = DARK_GREY.darker().darker().darker();
   
   // gradients
   public static final Color[] FIRE_COLOR_GRADIENT = WSTools.getGradient(YELLOW, RED, 21);
   public static final Color[] WATER_COLOR_GRADIENT = WSTools.getGradient(LIGHT_BLUE, DARK_BLUE, 21);
   public static final Color[] ACID_COLOR_GRADIENT = WSTools.getGradient(LIGHT_GREEN, LIGHT_GREEN.darker(), 21);
   
   public static final double SLOW_MOVE_SPEED = .1;
   public static final double NORMAL_MOVE_SPEED = .25;
   public static final double FAST_MOVE_SPEED = .333;
   public static final int MELEE_ATTACK_HIT_DELAY = 9;
   public static final double SPRAY_BASE_SPEED = .5;
   public static final double SPRAY_VARIABLE_SPEED = .1;
   public static final int SPRAY_DURATION = 10;
   public static final double FLOAT_EFFECT_SPEED = SLOW_MOVE_SPEED;
   
   public static final char EXPLOSION_CHAR = 15;
   public static final char CORPSE_CHAR = 'x';
   
   public static final String WINDOW_TITLE = "Pulsar: A Sci-Fi Roguelike";
   
  //  public static TilePalette RECT_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_8x16.png", 16, 16);
//    public static TilePalette SQUARE_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_16x16.png", 16, 16);
   public static TilePalette RECT_TILE_PALETTE = new TilePalette("WSFont_8x16.png", 16, 16);
   public static TilePalette SQUARE_TILE_PALETTE = new TilePalette("WSFont_16x16.png", 16, 16);
   
   
}