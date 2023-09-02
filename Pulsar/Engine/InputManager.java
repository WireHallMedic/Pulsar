package Pulsar.Engine;

import javax.swing.*;
import java.awt.event.*;
import WidlerSuite.*;

public class InputManager implements KeyListener
{
   public void keyTyped(KeyEvent ke){}
   public void keyReleased(KeyEvent ke){}
   
   public InputManager(JFrame parent)
   {
      parent.addKeyListener(this);
   }
   
   public void keyPressed(KeyEvent ke)
   {
      Coord playerLoc = GameEngine.getPlayer().getMapLoc();
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_NUMPAD1 : GameEngine.getPlayer().setAllLocs(playerLoc.x - 1, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD2 : GameEngine.getPlayer().setAllLocs(playerLoc.x, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD3 : GameEngine.getPlayer().setAllLocs(playerLoc.x + 1, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD4 : GameEngine.getPlayer().setAllLocs(playerLoc.x - 1, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD6 : GameEngine.getPlayer().setAllLocs(playerLoc.x + 1, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD7 : GameEngine.getPlayer().setAllLocs(playerLoc.x - 1, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD8 : GameEngine.getPlayer().setAllLocs(playerLoc.x, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD9 : GameEngine.getPlayer().setAllLocs(playerLoc.x + 1, playerLoc.y - 1); break;
      }
   }
}