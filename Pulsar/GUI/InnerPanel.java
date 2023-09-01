/*
This panel is the main display area, and manages the different panels used.
*/

package Pulsar.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InnerPanel extends JPanel implements ComponentListener
{
   private Vector<JPanel> panelList;
   
   public InnerPanel()
   {
      super();
      setBackground(Color.CYAN);
      
      panelList = new Vector<JPanel>();
      
      addComponentListener(this);
      setVisible(true);
   }
   
   public void componentHidden(ComponentEvent ce){}
   public void componentShown(ComponentEvent ce){}
   public void componentMoved(ComponentEvent ce){}
   public void componentResized(ComponentEvent ce){setPanelSizesAndLocations();}
   
   private void setPanelSizesAndLocations()
   {
      for(JPanel panel : panelList)
      {
         panel.setLocation(0, 0);
         panel.setSize(this.getWidth(), this.getHeight());
      }
   }
   
   private void addPanel(JPanel panel)
   {
      panel.setLocation(0, 0);
      panel.setSize(this.getWidth(), this.getHeight());
      panel.setVisible(false);
      panelList.add(panel);
      this.add(panel);
   }
   
   private void setVisiblePanel(JPanel panel)
   {
      for(JPanel listPanel : panelList)
      {
         if(listPanel == panel)
            listPanel.setVisible(true);
         else
            listPanel.setVisible(false);
      }
   }
}