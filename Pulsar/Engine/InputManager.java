package Pulsar.Engine;

import javax.swing.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.Actor.*;
import Pulsar.AI.*;
import Pulsar.GUI.*;
import Pulsar.Gear.*;
import Pulsar.*;
import java.awt.*;
import java.awt.Toolkit.*;
import java.util.*;

public class InputManager implements KeyListener, AIConstants, EngineConstants
{
   private Toolkit toolkit;
   private PulsarMain parent;
   private boolean shiftHeld;
   private boolean upHeld;
   private boolean downHeld;
   private boolean leftHeld;
   private boolean rightHeld;
   
   public InputManager(PulsarMain p)
   {
      parent = p;
      parent.addKeyListener(this);
      toolkit = Toolkit.getDefaultToolkit();
      if(!toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK))
      {
         toolkit.setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
         MessagePanel.addMessage("NumLock turned on");
      }
      shiftHeld = false;
      upHeld = false;
      downHeld = false;
      rightHeld = false;
      leftHeld = false;
   }
   
   public void keyTyped(KeyEvent ke){}
   
   public void keyReleased(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_SHIFT : shiftHeld = false; break;
         case KeyEvent.VK_UP    : upHeld = false; break;
         case KeyEvent.VK_DOWN  : downHeld = false; break;
         case KeyEvent.VK_LEFT  : leftHeld = false; break;
         case KeyEvent.VK_RIGHT : rightHeld = false; break;
      }
   }
   
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_SHIFT : shiftHeld = true; break;
         case KeyEvent.VK_UP    : upHeld = true; break;
         case KeyEvent.VK_DOWN  : downHeld = true; break;
         case KeyEvent.VK_LEFT  : leftHeld = true; break;
         case KeyEvent.VK_RIGHT : rightHeld = true; break;
      }
         
      switch(GameEngine.getGameMode())
      {
         case STANDARD    :   standardModeKeyPressed(ke); break;
         case LOOK        :   lookModeKeyPressed(ke); break;
         case TARGETING   :   targetingModeKeyPressed(ke); break;
         case OTHER_PANEL :   parent.keyPressed(ke); break;
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
   
   public Coord getDefaultTargetingLocation()
   {
      Actor lastTarget = GameEngine.getPlayer().getAI().getLastActorTargeted();
      if(lastTarget != null && lastTarget.isDead() == false)
         return lastTarget.getMapLoc();
      Actor closestActor = GameEngine.getClosestVisibleEnemy(GameEngine.getPlayer());
      if(closestActor != null)
         return closestActor.getMapLoc();
      return GameEngine.getPlayer().getMapLoc();
   }
   
   public void cancelAction()
   {
      GameEngine.getPlayer().getAI().clearPlan();
      GameEngine.setGameMode(GameMode.STANDARD);
      MessagePanel.addMessage("Action cancelled");
   }
   
   public void standardModeKeyPressed(KeyEvent ke)
   {
      Actor player = GameEngine.getPlayer();
      Coord playerLoc = player.getMapLoc();
      Coord target = null;
      if(player.isDead())
         return;
      
      if(shiftHeld)
      {
         if(upHeld && rightHeld)
            target = new Coord(playerLoc.x + 1, playerLoc.y - 1);
         if(upHeld && leftHeld)
            target = new Coord(playerLoc.x - 1, playerLoc.y - 1);
         if(downHeld && rightHeld)
            target = new Coord(playerLoc.x + 1, playerLoc.y + 1);
         if(downHeld && leftHeld)
            target = new Coord(playerLoc.x - 1, playerLoc.y + 1);
      }
      else
      {
         switch(ke.getKeyCode())
         {
            case KeyEvent.VK_NUMPAD1 : target = new Coord(playerLoc.x - 1, playerLoc.y + 1); break;
            case KeyEvent.VK_DOWN    :
            case KeyEvent.VK_NUMPAD2 : target = new Coord(playerLoc.x, playerLoc.y + 1); break;
            case KeyEvent.VK_NUMPAD3 : target = new Coord(playerLoc.x + 1, playerLoc.y + 1); break;
            case KeyEvent.VK_LEFT    :
            case KeyEvent.VK_NUMPAD4 : target = new Coord(playerLoc.x - 1, playerLoc.y); break;
            case KeyEvent.VK_NUMPAD5 : target = new Coord(playerLoc.x, playerLoc.y); break;
            case KeyEvent.VK_RIGHT   :
            case KeyEvent.VK_NUMPAD6 : target = new Coord(playerLoc.x + 1, playerLoc.y); break;
            case KeyEvent.VK_NUMPAD7 : target = new Coord(playerLoc.x - 1, playerLoc.y - 1); break;
            case KeyEvent.VK_UP      :
            case KeyEvent.VK_NUMPAD8 : target = new Coord(playerLoc.x, playerLoc.y - 1); break;
            case KeyEvent.VK_NUMPAD9 : target = new Coord(playerLoc.x + 1, playerLoc.y - 1); break;
            case KeyEvent.VK_L : GameEngine.setCursorLoc(playerLoc);
                                 GameEngine.setGameMode(GameMode.LOOK); 
                                 break;
            case KeyEvent.VK_A : attemptToShoot(); break;
            case KeyEvent.VK_S : setPlayerTarget(new Coord());
                                 setPlayerAction(ActorAction.SWITCH_WEAPONS);
                                 break;
            case KeyEvent.VK_U : setPlayerAction(ActorAction.USE);
                                 MessagePanel.addMessage("Choose a direction.");
                                 break;
            case KeyEvent.VK_C : InnerPanel.setActivePanel(CharacterPanel.class); 
                                 GameEngine.setGameMode(GameMode.OTHER_PANEL);
                                 break;
            case KeyEvent.VK_H : InnerPanel.setActivePanel(HelpPanel.class); 
                                 GameEngine.setGameMode(GameMode.OTHER_PANEL);
                                 break;
            case KeyEvent.VK_X : debug('x'); break;
            case KeyEvent.VK_D : debug('d'); break;
            case KeyEvent.VK_SPACE : debug(' '); break;
         }
      }
      
      // have a target, don't have an action, allowed to act
      if(target != null && player.getAI().getPendingAction() == null)// && GameEngine.isAnimationLocked() == false)
      {
         setPlayerTarget(target);
         setPlayerAction(ActorAction.CONTEXT_SENSITIVE);
      }
      // attempting to use something
      if(target != null && player.getAI().getPendingAction() == ActorAction.USE)// && GameEngine.isAnimationLocked() == false)
      {
         setPlayerTarget(target);
      }
   }
   
   public void targetingModeKeyPressed(KeyEvent ke)
   {
      Coord cursorLoc = GameEngine.getCursorLoc();
      if(shiftHeld)
      {
         if(upHeld && rightHeld)
            cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y - 1);
         if(upHeld && leftHeld)
            cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y - 1);
         if(downHeld && rightHeld)
            cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y + 1);
         if(downHeld && leftHeld)
            cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y + 1);
      }
      else
      {
         switch(ke.getKeyCode())
         {
            case KeyEvent.VK_NUMPAD1 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y + 1); break;
            case KeyEvent.VK_DOWN    :
            case KeyEvent.VK_NUMPAD2 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y + 1); break;
            case KeyEvent.VK_NUMPAD3 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y + 1); break;
            case KeyEvent.VK_LEFT    :
            case KeyEvent.VK_NUMPAD4 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y); break;
            case KeyEvent.VK_NUMPAD5 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y); break;
            case KeyEvent.VK_RIGHT   :
            case KeyEvent.VK_NUMPAD6 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y); break;
            case KeyEvent.VK_NUMPAD7 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y - 1); break;
            case KeyEvent.VK_UP      :
            case KeyEvent.VK_NUMPAD8 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y - 1); break;
            case KeyEvent.VK_NUMPAD9 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y - 1); break;
            case KeyEvent.VK_ESCAPE  : cancelAction(); return;
            case KeyEvent.VK_A       :
            case KeyEvent.VK_SPACE   :
            case KeyEvent.VK_ENTER   : setPlayerTarget(cursorLoc);
                                       GameEngine.setGameMode(GameMode.STANDARD);
                                       return;
         }  
      }
      GameEngine.setCursorLoc(cursorLoc);
   }
   
   public void lookModeKeyPressed(KeyEvent ke)
   {
      Coord cursorLoc = GameEngine.getCursorLoc();
      if(shiftHeld)
      {
         if(upHeld && rightHeld)
            cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y - 1);
         if(upHeld && leftHeld)
            cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y - 1);
         if(downHeld && rightHeld)
            cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y + 1);
         if(downHeld && leftHeld)
            cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y + 1);
      }
      else
      {
         switch(ke.getKeyCode())
         {
            case KeyEvent.VK_NUMPAD1 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y + 1); break;
            case KeyEvent.VK_DOWN    :
            case KeyEvent.VK_NUMPAD2 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y + 1); break;
            case KeyEvent.VK_NUMPAD3 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y + 1); break;
            case KeyEvent.VK_LEFT    :
            case KeyEvent.VK_NUMPAD4 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y); break;
            case KeyEvent.VK_NUMPAD5 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y); break;
            case KeyEvent.VK_RIGHT   :
            case KeyEvent.VK_NUMPAD6 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y); break;
            case KeyEvent.VK_NUMPAD7 : cursorLoc = new Coord(cursorLoc.x - 1, cursorLoc.y - 1); break;
            case KeyEvent.VK_UP      :
            case KeyEvent.VK_NUMPAD8 : cursorLoc = new Coord(cursorLoc.x, cursorLoc.y - 1); break;
            case KeyEvent.VK_NUMPAD9 : cursorLoc = new Coord(cursorLoc.x + 1, cursorLoc.y - 1); break;
            case KeyEvent.VK_ENTER   : 
            case KeyEvent.VK_SPACE   : 
            case KeyEvent.VK_ESCAPE  : GameEngine.setGameMode(GameMode.STANDARD); return;
         }
      }
      GameEngine.setCursorLoc(cursorLoc);
   }
   
   private void attemptToShoot()
   {
      if(!GameEngine.getPlayer().getWeapon().canFire())
      {
         MessagePanel.addMessage("Your weapon does not have enough energy!");
         Weapon w = GameEngine.getPlayer().getWeapon();
         return;
      }
      GameEngine.setCursorLoc(getDefaultTargetingLocation());
      GameEngine.setGameMode(GameMode.TARGETING);
      setPlayerAction(ActorAction.ATTACK);
                              
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
      if(arg == 'd')
      {
         GameEngine.getPlayer().applyDamage(4, GearConstants.DamageType.KINETIC, true);
         return;
      }
      // combat timing
      if(arg == 'x')
      {
         Actor player = GameEngine.getPlayer();
         Coord origin = player.getMapLoc();
         origin.x -= 1;
         Combat.doKnockback(player, origin, 1);
         player.discharge(1);
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