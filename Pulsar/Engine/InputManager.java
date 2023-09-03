package Pulsar.Engine;

import javax.swing.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.Actor.*;
import Pulsar.AI.*;
import Pulsar.GUI.*;

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
         case KeyEvent.VK_NUMPAD1 : target = new Coord(playerLoc.x - 1, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD2 : target = new Coord(playerLoc.x, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD3 : target = new Coord(playerLoc.x + 1, playerLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD4 : target = new Coord(playerLoc.x - 1, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD6 : target = new Coord(playerLoc.x + 1, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD7 : target = new Coord(playerLoc.x - 1, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD8 : target = new Coord(playerLoc.x, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD9 : target = new Coord(playerLoc.x + 1, playerLoc.y - 1); break;
         case KeyEvent.VK_I : debug('i'); break;
         case KeyEvent.VK_M : debug('m'); break;
         case KeyEvent.VK_S : debug('s'); break;
      }
      
      if(target != null)
      {
         player.getAI().setPendingTarget(target);
         player.getAI().setPendingAction(ACTION.STEP);
      }
   }
   
   public void debug(char arg)
   {
      Actor player = GameEngine.getPlayer();
      MainGameFGPanel mapPanel = GameEngine.getMapPanel();
      Coord origin = new Coord(player.getMapLoc());
      origin.x = origin.x + 1;
      MovementScript ms = null;
      switch(arg)
      {
         case 'i' : ms = MovementScriptFactory.getImpactScript(player, origin, 0); break;
         case 'm' : ms = MovementScriptFactory.getMeleeScript(player, origin); break;
         case 's' : ms = MovementScriptFactory.getShootScript(player, origin); break;
      }
      mapPanel.addLocking(ms);
   }
}