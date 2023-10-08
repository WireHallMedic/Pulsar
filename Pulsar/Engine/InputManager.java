package Pulsar.Engine;

import javax.swing.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.Actor.*;
import Pulsar.AI.*;
import Pulsar.GUI.*;
import Pulsar.Gear.*;
import Pulsar.Zone.*;
import Pulsar.*;
import java.awt.*;
import java.awt.Toolkit.*;
import java.util.*;

public class InputManager implements KeyListener, AIConstants, EngineConstants, ActorConstants
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
      Player player = GameEngine.getPlayer();
      Coord playerLoc = player.getMapLoc();
      Coord target = null;
      if(player.isDead() || GameEngine.isAnimationLocked())
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
            case KeyEvent.VK_PERIOD  :
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
            case KeyEvent.VK_G : setPlayerAction(ActorAction.GRAB);
                                 break;
            case KeyEvent.VK_C : InnerPanel.setActivePanel(CharacterPanel.class); 
                                 GameEngine.setGameMode(GameMode.OTHER_PANEL);
                                 break;
            case KeyEvent.VK_I : InnerPanel.setActivePanel(InventoryPanel.class); 
                                 GameEngine.setGameMode(GameMode.OTHER_PANEL);
                                 break;
            case KeyEvent.VK_H : InnerPanel.setActivePanel(HelpPanel.class); 
                                 GameEngine.setGameMode(GameMode.OTHER_PANEL);
                                 break;
            case KeyEvent.VK_1 : setPlayerAction(ActorAction.GADGET_0);
                                 break;
            case KeyEvent.VK_2 : setPlayerAction(ActorAction.GADGET_1);
                                 break;
            case KeyEvent.VK_3 : setPlayerAction(ActorAction.GADGET_2);
                                 break;
            case KeyEvent.VK_4 : setPlayerAction(ActorAction.GADGET_3);
                                 break;
            case KeyEvent.VK_5 : setPlayerAction(ActorAction.GADGET_4);
                                 break;
            case KeyEvent.VK_X : debug('x'); break;
            case KeyEvent.VK_SPACE : debug(' '); break;
         }
      }
      
      ActorAction pendingAction = player.getAI().getPendingAction();
      
      // picking something up
      if(pendingAction == ActorAction.GRAB)
      {
         player.getAI().setPendingTarget(new Coord(playerLoc.x, playerLoc.y));
      }
      
      // check if trying to press already pressed button
      if(target != null && (pendingAction == ActorAction.USE || 
                            pendingAction == ActorAction.CONTEXT_SENSITIVE ||
                            pendingAction == null))
      {
         MapTile tile = GameEngine.getZoneMap().getTile(target);
         if(tile instanceof Pulsar.Zone.Button)
         {
            Pulsar.Zone.Button button = (Pulsar.Zone.Button)tile;
            if(button.isPressed())
            {
               MessagePanel.addMessage("That button has already been pressed.");
               player.getAI().clearPlan();
               pendingAction = null;
            }
         }
      }
      
      // using a gadget
      if(pendingAction != null && pendingAction.isGadgetAction())
      {
         Gadget gadget = player.getGadget(pendingAction);
         // no gadget in slot
         if(gadget == null)
         {
            MessagePanel.addMessage("You do not have a gadget in that slot");
            player.getAI().clearPlan();
         }
         else
         {
            // out of charges
            if(!gadget.canUse())
            {
               MessagePanel.addMessage(gadget.getName() + " is out of charges");
               player.getAI().clearPlan();
            }
            // no active use
            else if(gadget.isPassiveOnly())
            {
               MessagePanel.addMessage(gadget.getName() + " is always active");
               player.getAI().clearPlan();
            }
            // other
            else
            {
               // targets self
               if(gadget.getTargetsSelf())
                  setPlayerTarget(player.getMapLoc());
               // requires target
               else
               {
                  GameEngine.setCursorLoc(getDefaultTargetingLocation());
                  GameEngine.setGameMode(GameMode.TARGETING);
               }
            }
         }
      }
      if(target != null && GameEngine.getZoneMap().getTile(target) instanceof Terminal)
      {
         InnerPanel.setActivePanel(MapTerminalPanel.class); 
         GameEngine.setGameMode(GameMode.OTHER_PANEL);
         Terminal terminal = (Terminal)GameEngine.getZoneMap().getTile(target);
         MapTerminalPanel.setPageList(terminal.getPageList());
         target = null;
      }
      
      // have a target, don't have an action, allowed to act
      if(target != null && pendingAction == null)
      {
         setPlayerTarget(target);
         setPlayerAction(ActorAction.CONTEXT_SENSITIVE);
      }
      // attempting to use something
      if(target != null && pendingAction == ActorAction.USE)
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
            // any key that gets you here works to confirm
            case KeyEvent.VK_1       : 
            case KeyEvent.VK_2       : 
            case KeyEvent.VK_3       : 
            case KeyEvent.VK_4       : 
            case KeyEvent.VK_5       : 
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
      if(arg == 'x')
      {
         GameEngine.getPlayer().add(StatusEffectFactory.getEffect(StatusEffectType.FROZEN));
         GameEngine.getPlayer().add(StatusEffectFactory.getEffect(StatusEffectType.BLIND));
         return;
      }
      if(arg == ' ')
      {
         for(Actor a : GameEngine.getActorList())
            System.out.println(a + " charmed: " + a.isCharmed());
      }
   }
}