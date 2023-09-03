package Pulsar.Engine;

import javax.swing.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.Actor.*;
import Pulsar.AI.*;

public class InputManager implements KeyListener, AIConstants
{
   public void keyTyped(KeyEvent ke){}
   public void keyReleased(KeyEvent ke){}
   
   public InputManager(JFrame parent)
   {
      parent.addKeyListener(this);
   }
   
   public void keyPressed(KeyEvent ke)
   {
      Actor player = GameEngine.getPlayer();
      Coord playerLoc = player.getMapLoc();
      Coord target = null;
      switch(ke.getKeyCode())
      {
        //  case KeyEvent.VK_NUMPAD1 : GameEngine.getPlayer().setAllLocs(playerLoc.x - 1, playerLoc.y + 1); break;
//          case KeyEvent.VK_NUMPAD2 : GameEngine.getPlayer().setAllLocs(playerLoc.x, playerLoc.y + 1); break;
//          case KeyEvent.VK_NUMPAD3 : GameEngine.getPlayer().setAllLocs(playerLoc.x + 1, playerLoc.y + 1); break;
//          case KeyEvent.VK_NUMPAD4 : GameEngine.getPlayer().setAllLocs(playerLoc.x - 1, playerLoc.y); break;
//          case KeyEvent.VK_NUMPAD6 : GameEngine.getPlayer().setAllLocs(playerLoc.x + 1, playerLoc.y); break;
//          case KeyEvent.VK_NUMPAD7 : GameEngine.getPlayer().setAllLocs(playerLoc.x - 1, playerLoc.y - 1); break;
//          case KeyEvent.VK_NUMPAD8 : GameEngine.getPlayer().setAllLocs(playerLoc.x, playerLoc.y - 1); break;
//          case KeyEvent.VK_NUMPAD9 : GameEngine.getPlayer().setAllLocs(playerLoc.x + 1, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD1 : target = new Coord(playerLoc.x - 1, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD2 : target = new Coord(playerLoc.x, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD3 : target = new Coord(playerLoc.x + 1, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD4 : target = new Coord(playerLoc.x - 1, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD6 : target = new Coord(playerLoc.x + 1, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD7 : target = new Coord(playerLoc.x - 1, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD8 : target = new Coord(playerLoc.x, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD9 : target = new Coord(playerLoc.x + 1, playerLoc.y - 1); break;
      }
      
      if(target != null)
      {
         player.getAI().setPendingTarget(target);
         player.getAI().setPendingAction(ACTION.STEP);
      }
   }
}