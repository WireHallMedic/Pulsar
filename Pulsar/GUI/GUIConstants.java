package Pulsar.GUI;

import java.awt.*;
import WidlerSuite.*;

public interface GUIConstants
{
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
   
   public static final Color BG_COLOR = Color.BLACK;
   public static final Color TERMINAL_FG_COLOR = Color.CYAN;
   public static final Color DEFAULT_TILE_FG_COLOR = Color.WHITE;
   public static final Color DEFAULT_TILE_BG_COLOR = new Color(16, 16, 16);
   public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
   public static final Color DEFAULT_MESSAGE_COLOR = Color.CYAN;
   public static final Color PLAYER_COLOR = Color.ORANGE;
   public static final Color SHIELD_COLOR = Color.WHITE;
   public static final Color DEFAULT_ACTOR_FG_COLOR = new Color(220, 220, 220);
   public static final Color DEFAULT_ACTOR_BG_COLOR = Color.GRAY;
   
   public static final double SLOW_MOVE_SPEED = .05;
   public static final double NORMAL_MOVE_SPEED = .2;
   public static final double FAST_MOVE_SPEED = .3;
   public static final int MELEE_ATTACK_HIT_DELAY = 5;
   public static final double SPRAY_BASE_SPEED = .3;
   public static final double SPRAY_VARIABLE_SPEED = .05;
   public static final int SPRAY_DURATION = 10;
   
   public static final char EXPLOSION_CHAR = 15;
   
   public static final String WINDOW_TITLE = "Pulsar: A Sci-Fi Roguelike";
   
   public static TilePalette RECT_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_8x16.png", 16, 16);
   public static TilePalette SQUARE_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_16x16.png", 16, 16);
   
   
}