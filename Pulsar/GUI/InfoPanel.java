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

public class InfoPanel extends RogueTilePanel implements GUIConstants
{
   private static final int WIDTH_TILES = (TERMINAL_WIDTH_TILES - (MAP_HEIGHT_TILES * 2) - 4) / 2;
   private static final int HEIGHT_TILES = MAP_HEIGHT_TILES;
   private static final int X_ORIGIN = TERMINAL_WIDTH_TILES - WIDTH_TILES - 1;
   private static final int Y_ORIGIN = 1;
   public static boolean redrawF = true;
   
   public InfoPanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
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
      for(Actor a : GameEngine.getActorList())
      {
         if(a != GameEngine.getPlayer() && GameEngine.playerCanSee(a))
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
      Vector<Actor> actorsToShow = getActorsToShow();
      for(int i = 0; i < actorsToShow.size() && i < HEIGHT_TILES; i++)
      {
         Actor a = actorsToShow.elementAt(i);
         setTile(X_ORIGIN, Y_ORIGIN + i, a.getSprite().getIconIndex(), a.getSprite().getFGColor(), a.getSprite().getBGColor());
         write(X_ORIGIN + 2, Y_ORIGIN + i, a.getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 2, 1);
      }
   }
   
   // look mode
   public void showCursorLooking()
   {
      clearInfoPanel();
      write(X_ORIGIN, Y_ORIGIN, "Look mode (escape to exit)", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 2);
      
      MapTile tile = GameEngine.getZoneMap().getTile(GameEngine.getCursorLoc());
      if(GameEngine.playerCanSee(GameEngine.getCursorLoc()))
      {
         setTile(X_ORIGIN, Y_ORIGIN + 2, tile.getIconIndex(), tile.getFGColor(), tile.getBGColor());
         write(X_ORIGIN + 2, Y_ORIGIN + 2, tile.getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 2, 1);
         
         Actor actor = GameEngine.getActorAt(GameEngine.getCursorLoc());
         if(actor != null)
         {
            UnboundTile ut = actor.getSprite();
            setTile(X_ORIGIN, Y_ORIGIN + 4, ut.getIconIndex(), ut.getFGColor(), ut.getBGColor());
            write(X_ORIGIN + 2, Y_ORIGIN + 4, actor.getName(), TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES - 2, 1);
         }
      }
      else
      {
         write(X_ORIGIN, Y_ORIGIN + 2, "  Out of view.", TERMINAL_FG_COLOR.getRGB(), BG_COLOR.getRGB(), WIDTH_TILES, 1);
      }
      

   }
   
   // targeting mode
   public void showCursorTargeting()
   {
      clearInfoPanel();
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
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(redrawF)
      {
         redrawF = false;
         redraw();
      }
      super.actionPerformed(ae);
   }
}