/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;
import java.awt.event.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.util.*;

public class InfoPanel extends RogueTilePanel implements GUIConstants
{
   private static final int WIDTH_TILES = (TERMINAL_WIDTH_TILES - (MAP_HEIGHT_TILES * 2) - 4) / 2;
   private static final int HEIGHT_TILES = MAP_HEIGHT_TILES;
   private static final int X_ORIGIN = TERMINAL_WIDTH_TILES - WIDTH_TILES - 1;
   private static final int Y_ORIGIN = 1;
   
   public InfoPanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', TERMINAL_FG_COLOR, BG_COLOR);
      }
      write(X_ORIGIN + 5, Y_ORIGIN, "Info Panel", 10, 1);
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
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      showNearbyObjects();
      super.actionPerformed(ae);
   }
}