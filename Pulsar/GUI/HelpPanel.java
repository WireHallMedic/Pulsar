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


public class HelpPanel extends RogueTilePanel implements GUIConstants, WSFontConstants
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   private String[][] pageArray;
   private int curPageIndex;
   
   
   public HelpPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      GUITools.setStandardBorder(this);
      setBackground(BG_COLOR);
      pageArray = getPages();
      update();
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(isVisible())
         super.actionPerformed(ae);
   }
      
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_ENTER  :
         case KeyEvent.VK_SPACE  :
         case KeyEvent.VK_ESCAPE :
         case KeyEvent.VK_H :       InnerPanel.setActivePanel(MainGameBGPanel.class); 
                                    GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
                                    break;
         case KeyEvent.VK_NUMPAD4 :
         case KeyEvent.VK_LEFT :    previousPage();
                                    break;
         case KeyEvent.VK_NUMPAD6 :
         case KeyEvent.VK_RIGHT :   nextPage();
                                    break;
      }
   }
   
   public void nextPage()
   {
      curPageIndex++;
      if(curPageIndex >= pageArray.length)
         curPageIndex = 0;
      update();
   }
   
   public void previousPage()
   {
      curPageIndex--;
      if(curPageIndex < 0)
         curPageIndex = pageArray.length - 1;
      update();
   }
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
      {
         curPageIndex = 0;
         update();
      }
      super.setVisible(v);
   }
   
   public void update()
   {
      clear();
      String[] pageText = pageArray[curPageIndex];
      for(int i = 0; i < pageText.length; i++)
         write(X_ORIGIN, Y_ORIGIN + i, pageText[i], WIDTH_TILES, 1);
   }
   
   public void clear()
   {
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(X_ORIGIN + x, Y_ORIGIN + y, ' ', TERMINAL_FG_COLOR, BG_COLOR);
      }
   }
   
   public String[][] getPages()
   {
      String[] pageOne = {
         GUITools.centerString("Key Bindings [^/$]", WIDTH_TILES),
         "Numpad   Movement",
         "Arrows   Movement; hold shift to allow diagonal input",
         "  A      Attack; press again to confirm target",
         "  U      Use; you will be prompted for a target",
         "  S      Switch weapons",
         " 1-5     Use gadget",
         "  L      Look mode",
         "  C      Open character panel",
         "  H      Open help panel (the one you're looking at)",
         "Escape   Cancel",
         "Enter    Confirm",
         "Space    Confirm",
         "",
         "",
         "",
         "",
         "",
         "",
         "",
         "",
         GUITools.centerString("Press Escape to exit", WIDTH_TILES)
      };
      String[] pageTwo = {
         GUITools.centerString("Terrain [^/$]", WIDTH_TILES),
         " " + (char)SMALL_BULLET_TILE + "  Clear. A normal, open tile.",
         " =  Low wall or similar. Blocks non-flying movement.",
         " #  Wall. Blocks movement and vision.",
         " :  Window or bars. Blocks movement, but not vision.",
         " ,  Rough terrain. Slows non-flying movement.",
         "| / Door, closed and open respectively.",
         " ~  Liquid. Slows non-flying movment, extinguishes fires.",
         " -  Ice. Walking on ice causes sliding.",
         " " + (char)CAPITAL_OMEGA_TILE + "  Terminal. Interact to read.",
         " ^  Fire.",
         (char)DOT_TILE + " " + (char)RING_TILE + " Button. Some buttons can be pressed repeatedly, some just once.",
         "",
         "",
         "",
         "",
         "",
         "",
         "",
         "",
         "",
         GUITools.centerString("Press Escape to exit", WIDTH_TILES)
      };
      String[] pageThree = {
         GUITools.centerString("Weapon Properties [^/$]", WIDTH_TILES),
         "Standard:  Can fire over low barriers.",
         "Blast:     Hits everything in a 3x3 area.",
         "Spray:     Fires in a cone. Hits low barriers, is blocked by high barriers and",
         "           enemies.",
         "Beam:      Fires a ray that hits low barriers and enemies. Only stopped by high",
         "           barriers or reaching max range (" + GearConstants.BEAM_MAX_RANGE + " meters).",
         "Heavy:     Can destroy walls. Be careful near bulkheads.",
         "Knockback: Pushes targets back.",
         "",
         "Kinetic:   Deals physical damage. Commonly deals knockback.",
         "Cryo:      Deals cold damage. Commonly freezes.",
         "Thermal:   Deals fire damage. Commonly deals ongoing damage.",
         "Electro:   Deals electric damage. Deals double damage to shields or enemies",
         "           standing in water (quad damage for both!).",
         "",
         "",
         "",
         "",
         "",
         "",
         GUITools.centerString("Press Escape to exit", WIDTH_TILES)
      };
      String[] pageFour = {
         GUITools.centerString("Speed [^/$]", WIDTH_TILES),
         "This game is entirely turn-based. You have as much time as you want to take",
         "  your turns. A standard length turn is \"1 second.\"",
         "",
         "Fast actions take half a standard turn (0.5 seconds).",
         "Slow actions take two standard turns (2.0 seconds).",
         "Instantenous actions don't take any time; if you take one, it'll still be",
         "  your turn.",
         "",
         "If your enemies seem to be taking quite a lot of turns, you're probably acting",
         "  slowly; check the character panel [c]. Heavy armor limits your maximum",
         "  speeds, as does movement through liquids or rough terrain.",
         "",
         "You have three speeds; movement, attacking, and interactions (such as opening",
         "  doors, pressing buttons, etc). If you wait on your turn ([5] or [.]), you'll",
         "  wait only as long as your fastest speed.",
         "",
         "A boost in speed is a massive advantage; be on the lookout for ways to",
         "  increase your speeds.",
         "",
         "",
         GUITools.centerString("Press Escape to exit", WIDTH_TILES)
      };
      String[][] pageArr = {pageOne, pageTwo, pageThree, pageFour};
      for(int i = 0; i < pageArr.length; i++)
      {
         String newStr = pageArr[i][0];
         newStr = newStr.replace("^", "" + (i + 1));
         newStr = newStr.replace("$", "" + pageArr.length);
         pageArr[i][0] = newStr;
      }
      return pageArr;
   }
}