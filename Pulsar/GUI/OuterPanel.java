/*
This panel fills the frame, and exists manage the inner panel. Upon resizing, it centers the inner panel.
*/

package Pulsar.GUI;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class OuterPanel extends JPanel implements GUIConstants, ComponentListener
{
   private InnerPanel innerPanel;
   private static double sizeMult = 1.0;
   
   public OuterPanel(javax.swing.Timer timer)
   {
      setVisible(true);
      setLayout(null);
      setBackground(BG_COLOR);
      
      innerPanel = new InnerPanel(timer, this);
      this.add(innerPanel);
      
      addComponentListener(this);
      
      arrange();
   }
   
   public void componentHidden(ComponentEvent ce){}
   public void componentShown(ComponentEvent ce){}
   public void componentMoved(ComponentEvent ce){}
   public void componentResized(ComponentEvent ce){arrange();}
   
   public static double getSizeMultiplier(){return sizeMult;}
   
   public void arrange()
   {
      if(getWidth() == 0 || getHeight() == 0)
         return;
      
      double sizeMult = Math.min(getHeight() / (TERMINAL_TILE_HEIGHT_PIXELS * (double)TERMINAL_HEIGHT_TILES), 
                                 getWidth()  / (TERMINAL_TILE_WIDTH_PIXELS * (double)TERMINAL_WIDTH_TILES));
      int innerWidth = (int)(sizeMult * TERMINAL_TILE_WIDTH_PIXELS) * TERMINAL_WIDTH_TILES;
      int innerHeight = (int)(sizeMult * TERMINAL_TILE_HEIGHT_PIXELS) * TERMINAL_HEIGHT_TILES;
      
      innerPanel.setSize(innerWidth, innerHeight);
      innerPanel.setLocation((getWidth() - innerWidth) / 2, (getHeight() - innerHeight) / 2);
      innerPanel.arrange(sizeMult);
   }
   
   public void keyPressed(KeyEvent ke)
   {
      innerPanel.keyPressed(ke);
   }
}