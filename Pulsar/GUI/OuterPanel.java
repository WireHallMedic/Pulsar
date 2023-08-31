/*
This panel fills the frame, and exists manage the inner panel. Upon resizing, it centers the inner panel.
*/

package Pulsar.GUI;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class OuterPanel extends JPanel implements ComponentListener, GUIConstants
{
   private InnerPanel innerPanel;
   
   public OuterPanel()
   {
      setVisible(true);
      setLayout(null);
      setBackground(BG_COLOR);
      
      innerPanel = new InnerPanel();
      this.add(innerPanel);
      
      addComponentListener(this);
      center();
   }
   
   public void componentHidden(ComponentEvent ce){}
   public void componentShown(ComponentEvent ce){}
   public void componentMoved(ComponentEvent ce){}
   public void componentResized(ComponentEvent ce){center();}
   
   public void center()
   {
      double sizeMult = Math.min(getHeight() / (TERMINAL_TILE_HEIGHT_PIXELS * (double)TERMINAL_HEIGHT_TILES), 
                                 getWidth()  / (TERMINAL_TILE_WIDTH_PIXELS * (double)TERMINAL_WIDTH_TILES));
      int innerWidth = (int)(sizeMult * TERMINAL_TILE_WIDTH_PIXELS) * TERMINAL_WIDTH_TILES;
      int innerHeight = (int)(sizeMult * TERMINAL_TILE_HEIGHT_PIXELS) * TERMINAL_HEIGHT_TILES;
      
      innerPanel.setSize(innerWidth, innerHeight);
      innerPanel.setLocation((getWidth() - innerWidth) / 2, (getHeight() - innerHeight) / 2);
   }
}