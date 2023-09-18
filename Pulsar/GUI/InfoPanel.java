/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;
import java.awt.event.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import Pulsar.Zone.*;
import java.util.*;

public class InfoPanel extends RogueTilePanel implements GUIConstants, EngineConstants
{
   private static final int WIDTH_TILES = (TERMINAL_WIDTH_TILES - (MAP_HEIGHT_TILES * 2) - 4) / 2;
   private static final int HEIGHT_TILES = MAP_HEIGHT_TILES;
   private static final int X_ORIGIN = TERMINAL_WIDTH_TILES - WIDTH_TILES - 1;
   private static final int Y_ORIGIN = 1;
   public static boolean redrawF = true;
   private int barLength;
   
   public InfoPanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      barLength = (WIDTH_TILES - 5) / 2;
      clearInfoPanel();
   }
   
   public static void updateInfoPanel()
   {
      redrawF = true;
   }
   
   public void clearInfoPanel()
   {
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, ' ', TERMINAL_FG_COLOR, BG_COLOR);
      }
   }
   
   public Vector<Actor> getActorsToShow()
   {
      Vector<Actor> actorsToShow = new Vector<Actor>();
      Vector<Actor> actorList = GameEngine.getActorList();
      for(int i = 0; i < actorList.size(); i++)
      {
         Actor a = actorList.elementAt(i);
         if(GameEngine.playerCanSee(a) && a != GameEngine.getPlayer() && !a.isDead())
         {
            actorsToShow.add(a);
         }
      }
      return actorsToShow;
   }
   
   // standard mode
   public void showNearbyObjects()
   {
      clearInfoPanel();
      // show actors
      Vector<Actor> actorsToShow = getActorsToShow();
      for(int i = 0; i < actorsToShow.size() && i < HEIGHT_TILES; i++)
      {
         int yInset = 2 * i;
         Actor a = actorsToShow.elementAt(i);
         setTile(X_ORIGIN, Y_ORIGIN + yInset, a.getSprite().getIconIndex(), a.getSprite().getFGColor(), a.getSprite().getBGColor());
         write(X_ORIGIN + 2, Y_ORIGIN + yInset, a.getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 2, 1);
         drawActorBars(a, 1, yInset + 1);
      }
      // show objects
   }
   
   // look mode
   public void showCursorLooking()
   {
      clearInfoPanel();
      write(X_ORIGIN, Y_ORIGIN, "Look mode", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 2);
      write(X_ORIGIN, Y_ORIGIN + 1, " (escape to exit)", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 2);
      describeTarget();
   }
   
   // targeting mode
   public void showCursorTargeting()
   {
      clearInfoPanel();
      write(X_ORIGIN, Y_ORIGIN, "Targeting mode", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 2);
      write(X_ORIGIN, Y_ORIGIN + 1, " (escape to exit)", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 2);
      describeTarget();
   }
   
   private void describeTarget()
   {
   
      MapTile tile = GameEngine.getZoneMap().getTile(GameEngine.getCursorLoc());
      if(GameEngine.playerCanSee(GameEngine.getCursorLoc()))
      {
         int dynamicY = 3;
         setTile(X_ORIGIN, Y_ORIGIN + dynamicY, tile.getIconIndex(), tile.getFGColor(), tile.getBGColor());
         write(X_ORIGIN + 2, Y_ORIGIN + dynamicY, tile.getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 2, 1);
         dynamicY++;
         
         Corpse corpse = GameEngine.getZoneMap().getCorpseAt(GameEngine.getCursorLoc());
         if(corpse != null)
         {
            setTile(X_ORIGIN, Y_ORIGIN + dynamicY, corpse.getIconIndex(), corpse.getColor(), tile.getBGColor());
            write(X_ORIGIN + 2, Y_ORIGIN + dynamicY, corpse.getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 2, 1);
            dynamicY++;
         }
         
         dynamicY++;
         Actor actor = GameEngine.getActorAt(GameEngine.getCursorLoc());
         if(actor != null)
         {
            UnboundTile ut = actor.getSprite();
            setTile(X_ORIGIN, Y_ORIGIN + dynamicY, ut.getIconIndex(), ut.getFGColor(), ut.getBGColor());
            write(X_ORIGIN + 2, Y_ORIGIN + dynamicY, actor.getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 2, 1);
            dynamicY++;
            drawActorBars(actor, 1, dynamicY);
            dynamicY++;
            write(X_ORIGIN, Y_ORIGIN + dynamicY, actor.getMood(GameEngine.getPlayer()), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
            dynamicY++;
            write(X_ORIGIN, Y_ORIGIN + dynamicY, actor.getWeapon().getName() + ": " + actor.getWeapon().getSummary(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 4);
            dynamicY++;
            for(int i = 0; i < 4; i++)
            {
               if(getIcon(X_ORIGIN, Y_ORIGIN + dynamicY) != ' ')
                  dynamicY++;
            }
            write(X_ORIGIN, Y_ORIGIN + dynamicY, actor.getInspectionString(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, HEIGHT_TILES - dynamicY);
         }
      }
      else
      {
         write(X_ORIGIN, Y_ORIGIN + 2, "  Out of view.", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      }
   }
   
   private void redraw()
   {
      switch(GameEngine.getGameMode())
      {
         case STANDARD  : showNearbyObjects(); break;
         case LOOK      : showCursorLooking(); break;
         case TARGETING : showCursorTargeting(); break;
      }
   }
   
   private void drawActorBars(Actor a, int xInset, int yInset)
   {
      String barBuffer = "";
      for(int i = 0; i < barLength; i++)
         barBuffer += " ";
      write(X_ORIGIN + xInset, Y_ORIGIN + yInset, "[" + barBuffer + "]", HEALTH_COLOR.getRGB(), BG_COLOR.getRGB(), barLength + 2, 1);
      write(X_ORIGIN + xInset + 2 + barLength, Y_ORIGIN + yInset, "[" + barBuffer + "]", SHIELD_COLOR.getRGB(), BG_COLOR.getRGB(), barLength + 2, 1);
      int[] healthBar = a.getHealthBar(barLength);
      int[] shieldBar = a.getShieldBar(barLength);
      for(int j = 0; j < barLength; j++)
      {
         setIcon(X_ORIGIN + 1 + xInset + j, Y_ORIGIN + yInset, healthBar[j]); 
         setIcon(X_ORIGIN + 3 + xInset + barLength + j, Y_ORIGIN + yInset, shieldBar[j]); 
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(redrawF || GameEngine.getGameMode() == GameMode.TARGETING || GameEngine.getGameMode() == GameMode.LOOK)
      {
         redrawF = false;
         redraw();
      }
      super.actionPerformed(ae);
   }
}