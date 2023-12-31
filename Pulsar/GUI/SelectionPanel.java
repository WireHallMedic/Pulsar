package Pulsar.GUI;

import WidlerSuite.*;
import java.awt.event.*;


public abstract class SelectionPanel extends RogueTilePanel implements GUIConstants, KeyListener
{
   public static final int X_ORIGIN = 1;
   public static final int Y_ORIGIN = 1;
   public static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   public static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - 2;
   protected int selectionIndex = 0;
   protected String[] options = {"Unknown Option"};
   
   
   public SelectionPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      GUITools.setStandardBorder(this);
      setBackground(BG_COLOR);
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
                                    break;
         case KeyEvent.VK_DOWN :
         case KeyEvent.VK_NUMPAD2 : next(); 
                                    break;
         case KeyEvent.VK_ENTER :
         case KeyEvent.VK_SPACE :   doSelection(); 
                                    break;
      }
   }
   public void keyTyped(KeyEvent ke){}
   public void keyReleased(KeyEvent ke){}
   
   public abstract void doSelection();
   
   public abstract void update();
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(isVisible())
      {
         update();
         super.actionPerformed(ae);
      }
   }
}