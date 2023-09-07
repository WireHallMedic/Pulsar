package Pulsar.Engine;

import javax.swing.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.Actor.*;
import Pulsar.AI.*;
import Pulsar.GUI.*;
import Pulsar.Gear.*;
import java.awt.*;
import java.awt.Toolkit.*;
import java.util.*;

public class InputManager implements KeyListener, AIConstants, EngineConstants
{
   public void keyTyped(KeyEvent ke){}
   public void keyReleased(KeyEvent ke){}
   private Toolkit toolkit;
   
   public InputManager(JFrame parent)
   {
      parent.addKeyListener(this);
      toolkit = Toolkit.getDefaultToolkit();
      if(!toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK))
      {
         toolkit.setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
         MessagePanel.addMessage("NumLock turned on");
      }
   }
   
   public void keyPressed(KeyEvent ke)
   {
      switch(GameEngine.getGameMode())
      {
         case STANDARD  :   standardModeKeyPressed(ke); break;
         case LOOK      :   lookModeKeyPressed(ke); break;
         case TARGETING :   targetingModeKeyPressed(ke); break;
      }
      InfoPanel.updateInfoPanel();
   }
   
   public void setPlayerAction(ActorAction action)
   {
      GameEngine.getPlayer().getAI().setPendingAction(action);
   }
   
   public void setPlayerTarget(Coord target)
   {
      GameEngine.getPlayer().getAI().setPendingTarget(target);
   }
   
   public void standardModeKeyPressed(KeyEvent ke)
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
         case KeyEvent.VK_L : GameEngine.setCursorLoc(playerLoc);
                              GameEngine.setGameMode(GameMode.LOOK); 
                              break;
         case KeyEvent.VK_F : GameEngine.setCursorLoc(playerLoc);
                              GameEngine.setGameMode(GameMode.TARGETING);
                              setPlayerAction(ActorAction.ATTACK);
                              break;
         case KeyEvent.VK_Z : debug('z'); break;
         case KeyEvent.VK_X : debug('x'); break;
         case KeyEvent.VK_D : debug('d'); break;
         case KeyEvent.VK_SPACE : debug(' '); break;
      }
      
      if(target != null && GameEngine.isAnimationLocked() == false)
      {
         setPlayerTarget(target);
         setPlayerAction(ActorAction.CONTEXT_SENSITIVE);
      }
   }
   
   public void targetingModeKeyPressed(KeyEvent ke)
   {
      Coord cursorLoc = GameEngine.getCursorLoc();
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_NUMPAD1 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD2 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD3 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD4 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y); break;
         case KeyEvent.VK_NUMPAD5 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y); break;
         case KeyEvent.VK_NUMPAD6 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y); break;
         case KeyEvent.VK_NUMPAD7 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD8 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD9 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y - 1); break;
         case KeyEvent.VK_ESCAPE  : GameEngine.setGameMode(GameMode.STANDARD); return;
         case KeyEvent.VK_F       :
         case KeyEvent.VK_ENTER   : GameEngine.setGameMode(GameMode.STANDARD); 
                                    setPlayerTarget(cursorLoc);
                                    GameEngine.setGameMode(GameMode.STANDARD);
                                    return;
      }
      GameEngine.setCursorLoc(cursorLoc);
   }
   
   public void lookModeKeyPressed(KeyEvent ke)
   {
      Coord cursorLoc = GameEngine.getCursorLoc();
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_NUMPAD1 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD2 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD3 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y + 1); break;
         case KeyEvent.VK_NUMPAD4 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y); break;
         case KeyEvent.VK_NUMPAD5 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y); break;
         case KeyEvent.VK_NUMPAD6 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y); break;
         case KeyEvent.VK_NUMPAD7 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD8 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y - 1); break;
         case KeyEvent.VK_NUMPAD9 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y - 1); break;
         case KeyEvent.VK_ESCAPE  : GameEngine.setGameMode(GameMode.STANDARD); return;
      }
      GameEngine.setCursorLoc(cursorLoc);
   }
   
   public void debug(char arg)
   {
      if(arg == ' ')
      {
         Vector<Actor> actorList = GameEngine.getActorList();
         Coord target = null;
         for(Actor a : actorList)
         {
            if(a != GameEngine.getPlayer())
            {
               target = a.getMapLoc();
               break;
            }
         }
         if(target != null)
            Combat.resolveAttack(GameEngine.getPlayer(), target);
         return;
      }
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
      if(arg == 'd')
      {
         GameEngine.getPlayer().applyDamage(4, GearConstants.DamageType.KINETIC);
         return;
      }
      // combat timing
      if(arg == 'z')
      {
         VisualEffectFactory.createExplosion(new Coord(1, 1));
         VisualEffectFactory.createExplosion(new Coord(2, 2), 10);
         VisualEffectFactory.createExplosion(new Coord(1, 2), 20);
         VisualEffectFactory.createExplosion(new Coord(2, 1), 30);
         return;
      }
      if(arg == 'x')
      {
         Actor player = GameEngine.getPlayer();
         MainGameFGPanel mapPanel = GameEngine.getMapPanel();
         Actor enemy = GameEngine.getActorList().elementAt(1);
         MovementScript msa = MovementScriptFactory.getShootScript(player, enemy.getMapLoc());
         MovementScript msd = MovementScriptFactory.getImpactScript(enemy, player.getMapLoc(), 0);
         mapPanel.addLocking(msa);
         mapPanel.addLocking(msd);
         VisualEffectFactory.createSpray(enemy.getMapLoc(), player.getMapLoc(), Color.RED);
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