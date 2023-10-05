/*
Displaying character attributes and gear
*/


package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import Pulsar.Actor.*;
import Pulsar.Gear.*;
import Pulsar.Engine.*;
import Pulsar.Zone.*;


public class TitlePanel extends SelectionPanel implements GUIConstants
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   public static final Color UPPER_GREY = new Color(196, 196, 196);
   public static final Color MID_GREY = new Color(128, 128, 128);
   public static final Color LOWER_GREY = new Color(64, 64, 64);
   public static final Color[] STAR_GRADIENT = WSTools.getGradient(MID_GREY, WHITE, 21);
   
   private int selectionIndex = 0;
   private String[] options = {"Testing", "Aliens", "Pirates", "Exit"};
   
   
   public TitlePanel()
   {
      super();
   }
   
   public void doSelection()
   {
      if(options[selectionIndex].equals("Exit"))
      {
         System.exit(0);
      }
      if(options[selectionIndex].equals("Testing"))
      {
         GameEngine engine = new GameEngine();
         ZoneMap map = ZoneMapFactory.getTestMap2();
         Zone zone = new Zone("Testing zone", -1, map);
         GameEngine.setCurZone(zone);
         Player p = ActorFactory.getPlayer();
         p.setAllLocs(4, 12);
         engine.setPlayer(p);
         Actor victim = ActorFactory.getVictim();
         victim.setAllLocs(20, 12);
         engine.add(victim);
         engine.begin();
         InnerPanel.setActivePanel(MainGameBGPanel.class); 
         GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
      }
      if(options[selectionIndex].equals("Aliens"))
      {
         GameEngine engine = new GameEngine();
         ZoneTemplateManager ztm = new ZoneTemplateManager();
         ZoneMap map = ZoneMapFactory.buildFromTemplates(ztm.random(), ZoneConstants.TileType.VACUUM);
         Zone zone = new Zone("Random Aliens", -1, map);
         GameEngine.setCurZone(zone);
         Player p = ActorFactory.getPlayer();
         int xStart = 0;
         int yStart = 0;
         boolean continueF = true;
         for(int x = 0; x < 20 && continueF; x++)
         for(int y = 5; y < map.getHeight() && continueF; y += 3)
         {
            if(map.getTile(x, y).isLowPassable())
            {
               xStart = x;
               yStart = y;
               continueF = false;
            }
         }
         p.setAllLocs(xStart, yStart);
         engine.setPlayer(p);
         ActorFactory.populateWithAliens(zone, p.getMapLoc());
         engine.begin();
         InnerPanel.setActivePanel(MainGameBGPanel.class); 
         GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
      }
      if(options[selectionIndex].equals("Pirates"))
      {
         GameEngine engine = new GameEngine();
         ZoneTemplateManager ztm = new ZoneTemplateManager();
         ZoneMap map = ZoneMapFactory.buildFromTemplates(ztm.random(), ZoneConstants.TileType.VACUUM);
         Zone zone = new Zone("Random Pirates", -1, map);
         GameEngine.setCurZone(zone);
         Player p = ActorFactory.getPlayer();
         p.setAllLocs(2, 12);
         engine.setPlayer(p);
         ActorFactory.populateWithPirates(zone, p.getMapLoc());
         engine.begin();
         InnerPanel.setActivePanel(MainGameBGPanel.class); 
         GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
      }
   }
   
   @Override
   public void update()
   {
      int fgColor = TERMINAL_FG_COLOR.getRGB();
      int bgColor = BG_COLOR.getRGB();
      int pulseIndex = animationManager.mediumPulse();
      for(int x = 0; x < TITLE_ARRAY[0].length(); x++)
      for(int y = 0; y < TITLE_ARRAY.length; y++)
      {
         switch(TITLE_ARRAY[y].charAt(x))
         {
            case '#' : setTile(x + X_ORIGIN, y + Y_ORIGIN, ' ', TERMINAL_FG_COLOR, TERMINAL_FG_COLOR); break;
            case '.' : setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', UPPER_GREY, BLACK); break;
            case ' ' : setTile(x + X_ORIGIN, y + Y_ORIGIN, ' ', WHITE, BLACK); break;
            case 'X' : setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', STAR_GRADIENT[pulseIndex], BLACK); break;
         }
      }
      for(int i = 0; i < options.length; i++)
      {
         if(selectionIndex == i)
            bgColor = ORANGE.getRGB();
         else
            bgColor = BG_COLOR.getRGB();
         write(5, 18 + i, options[i], fgColor, bgColor, options[i].length(), 1);
      }
   }
   
   public static final String[] TITLE_ARRAY = {
      "                                                                              ",
      "    ######             ##                    .                                ",
      "    ##    ##           ##                                      X              ",
      "    ##    ##           ##                                                     ",
      "    ######             ##    ##     ####  ##  ##                              ",
      "    ##      ##    ##   ##   ##    ##  ##  ####                                ",
      "    ##      ##    ##   ##    ##   ##  ##  ##                                  ",
      "    ##        ####     ##   ##      ####  ##                           .      ",
      "                                                                              ",
      "        .                                             .                       ",
      "                                                                              ",
      "                                                                              ",
      "                      .                  .                                    ",
      "                                                                              ",
      "                                                                              ",
      "                                                                  .           ",
      "                                                                              ",
      "                                                 .                            ",
      "                                .                                             ",
      "                                                                              ",
      "                                                                              ",
      "                                                                              "
   };
}