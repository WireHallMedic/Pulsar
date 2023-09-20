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


public class TitlePanel extends RogueTilePanel implements GUIConstants
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   
   private int selectionIndex = 0;
   private String[] options = {"Aliens", "Mercenaries", "Exit"};
   
   
   public TitlePanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      GUITools.setStandardBorder(this);
      setBackground(BG_COLOR);
      update();
   }
   
   public void next()
   {
      selectionIndex++;
      if(selectionIndex >= options.length)
         selectionIndex = 0;
   }
   
   public void previous()
   {
      selectionIndex--;
      if(selectionIndex < 0)
         selectionIndex = options.length - 1;
   }
   
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_UP :
         case KeyEvent.VK_NUMPAD8 : previous(); 
                                    update();
                                    break;
         case KeyEvent.VK_DOWN :
         case KeyEvent.VK_NUMPAD2 : next(); 
                                    update();
                                    break;
         case KeyEvent.VK_ENTER :
         case KeyEvent.VK_SPACE :   doSelection(); 
                                    break;
      }
   }
   
   private void doSelection()
   {
      if(options[selectionIndex].equals("Exit"))
      {
         System.exit(0);
      }
      if(options[selectionIndex].equals("Aliens"))
      {
         GameEngine engine = new GameEngine();
         ZoneMap map = ZoneMapFactory.getTestMap2();
         Zone zone = new Zone("Test Zone", -1, map);
         GameEngine.setCurZone(zone);
         Player p = ActorFactory.getPlayer();
         p.setAllLocs(2, 12);
         engine.setPlayer(p);
         ActorFactory.populateWithAliens(zone, p.getMapLoc());
         engine.begin();
         InnerPanel.setActivePanel(MainGameBGPanel.class); 
         GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
      }
      if(options[selectionIndex].equals("Mercenaries"))
      {
         GameEngine engine = new GameEngine();
         ZoneMap map = ZoneMapFactory.getTestMap2();
         Zone zone = new Zone("Test Zone", -1, map);
         GameEngine.setCurZone(zone);
         Player p = ActorFactory.getPlayer();
         p.setAllLocs(2, 12);
         engine.setPlayer(p);
         ActorFactory.populateWithMercenaries(zone, p.getMapLoc());
         engine.begin();
         InnerPanel.setActivePanel(MainGameBGPanel.class); 
         GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
      }
   }
   
   private void update()
   {
      int fgColor = TERMINAL_FG_COLOR.getRGB();
      int bgColor = BG_COLOR.getRGB();
      write(1, 1, "Title Panel", fgColor, bgColor, TERMINAL_WIDTH_TILES - 2, 1);
      
      for(int i = 0; i < options.length; i++)
      {
         if(selectionIndex == i)
            bgColor = ORANGE.getRGB();
         else
            bgColor = BG_COLOR.getRGB();
         write(1, 10 + i, options[i], fgColor, bgColor, options[i].length(), 1);
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(isVisible())
         super.actionPerformed(ae);
   }
}