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
   
   public static final String WINDOW_TITLE = "Pulsar: A Sci-Fi Roguelike";
   
   public static TilePalette RECT_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_8x16.png", 16, 16);
   public static TilePalette SQUARE_TILE_PALETTE = new TilePalette("Pulsar/GUI/WSFont_16x16.png", 16, 16);
   
   
}