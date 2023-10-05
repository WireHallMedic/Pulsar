package Pulsar.GUI;

import WidlerSuite.*;
import java.awt.event.*;


public abstract class SelectionPanel extends RogueTilePanel implements GUIConstants
{
   
   private int selectionIndex = 0;
   private String[] options = {"Unknown Option"};
   
   
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