package Pulsar.Engine;

import javax.swing.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.Actor.*;
import Pulsar.AI.*;
import Pulsar.GUI.*;
import java.awt.*;

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
         case KeyEvent.VK_NUMPAD5 : target = new Coord(playerLoc.x, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD6 : target = new Coord(playerLoc.x + 1, playerLoc.y); break;
         case KeyEvent.VK_NUMPAD7 : target = new Coord(playerLoc.x - 1, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD8 : target = new Coord(playerLoc.x, playerLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD9 : target = new Coord(playerLoc.x + 1, playerLoc.y - 1); break;
         case KeyEvent.VK_I : debug('i'); break;
         case KeyEvent.VK_M : debug('m'); break;
         case KeyEvent.VK_S : debug('s'); break;
         case KeyEvent.VK_E : debug('e'); break;
         case KeyEvent.VK_C : debug('c'); break;
      }
      
      if(target != null)
      {
         player.getAI().setPendingTarget(target);
         player.getAI().setPendingAction(ACTION.CONTEXT_SENSITIVE);
      }
   }
   
   public void debug(char arg)
   {
      if(arg == 'e')
      {
         System.out.println(GameEngine.isActorAt(GameEngine.getPlayer().getMapLoc()));
         return;
      }
      if(arg == 'c')
      {
         Color c = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
         MessagePanel.addMessage("Random color!", c);
         return;
      }
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