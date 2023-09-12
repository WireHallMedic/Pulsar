package Pulsar.GUI;

import java.awt.*;
import WidlerSuite.*;

public interface GUIConstants
{
   // basic color palette
   public static final Color BLACK = new Color(0, 0, 0);
   public static final Color DARK_GREY = new Color(127, 127, 127);
   public static final Color LIGHT_GREY = new Color(195, 195, 195);
   public static final Color WHITE = new Color(255, 255, 255);
   public static final Color DARK_BLUE = new Color(63, 72, 204);
   public static final Color DARK_GREEN = new Color(34, 177, 76);
   public static final Color BROWN = new Color(185, 122, 87);
   public static final Color PURPLE = new Color(163, 73, 164);
   public static final Color BLUE = new Color(0, 162, 232);
   public static final Color LIGHT_GREEN = new Color(128, 255, 128);
   public static final Color BEIGE = new Color(239, 228, 176);
   public static final Color RED = new Color(237, 28, 36);
   public static final Color LIGHT_BLUE = new Color(153, 217, 234);
   public static final Color AMBER = new Color(255, 201, 14);
   public static final Color ORANGE = new Color(255, 92, 35);
   public static final Color PINK = new Color(255, 174, 201);

   public static final int FRAMES_PER_SECOND = 60;
   
   public static final int DEFAULT_WINDOW_WIDTH = 1150;
   public static final int DEFAULT_WINDOW_HEIGHT = 720;
   public static final int TERMINAL_WIDTH_TILES = 80;
   public static final int TERMINAL_HEIGHT_TILES = 24;
   public static final int TERMINAL_TILE_WIDTH_PIXELS = 8;
   public static final int TERMINAL_TILE_HEIGHT_PIXELS = 16;
   public static final int MAP_WIDTH_TILES = 17;
   public static final int MAP_HEIGHT_TILES = 17;
   public static final int MAP_X_INSET_TILES = ((TERMINAL_WIDTH_TILES - (2 * MAP_WIDTH_TILES)) / 2);
   
   public static final Color ALIEN_COLOR = PURPLE;
 //  public static final Color DULL_WHITE = new Color(220, 220, 220);
   public static final Color BG_COLOR = BLACK;
   public static final Color TERMINAL_FG_COLOR = LIGHT_BLUE;
   public static final Color DEFAULT_TILE_FG_COLOR = WHITE;
   public static final Color DEFAULT_TILE_BG_COLOR = DARK_GREY;
   public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
   public static final Color DEFAULT_MESSAGE_COLOR = LIGHT_BLUE;
   public static final Color PLAYER_COLOR = ORANGE;
   public static final Color SHIELD_COLOR = WHITE;
   public static final Color HEALTH_COLOR = RED;
   public static final Color DEFAULT_ACTOR_FG_COLOR = WHITE;
   public static final Color DEFAULT_ACTOR_BG_COLOR = DARK_GREY;
   public static final Color RETICULE_COLOR = AMBER;
   public static final Color INVALID_RETICULE_COLOR = RED;
   public static final Color HUMAN_BLOOD = Color.RED;
   public static final Color ROBOT_BLOOD = Color.GRAY;
   public static final Color ALIEN_BLOOD = ALIEN_COLOR;
   
   public static final double SLOW_MOVE_SPEED = .05;
   public static final double NORMAL_MOVE_SPEED = .2;
   public static final double FAST_MOVE_SPEED = .3;
   public static final int MELEE_ATTACK_HIT_DELAY = 9;
   public static final double SPRAY_BASE_SPEED = .3;
   public static final double SPRAY_VARIABLE_SPEED = .05;
   public static final int SPRAY_DURATION = 10;
   public static final double FLOAT_EFFECT_SPEED = SLOW_MOVE_SPEED;
   
   public static final char EXPLOSION_CHAR = 15;
   
   public static final String WINDOW_TITLE = "Pulsar: A Sci-Fi Roguelike";
   
   public static TilePalette RECT_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_8x16.png", 16, 16);
   public static TilePalette SQUARE_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_16x16.png", 16, 16);
   
   
}