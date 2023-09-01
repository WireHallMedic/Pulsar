/*
This panel is the main display area, and manages the different panels used.
*/

package Pulsar.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import WidlerSuite.*;

public class InnerPanel extends JPanel implements GUIConstants, ActionListener
{
   private Vector<RogueTilePanel> panelList;
   private javax.swing.Timer timer;
   private MainGameBGPanel mainGameBGPanel;
   private MainGameFGPanel mainGameFGPanel;
   
   private RogueTilePanel curPanel = null;
   
   public InnerPanel(javax.swing.Timer t)
   {
      super();
      setBackground(Color.BLACK);
      timer = t;
      timer.addActionListener(this);
      
      panelList = new Vector<RogueTilePanel>();
      mainGameFGPanel = new MainGameFGPanel();
      mainGameBGPanel = new MainGameBGPanel();
      addPanel(mainGameFGPanel);
      addPanel(mainGameBGPanel);
      
      setMainGamePanelActive();
      setVisible(true);
   }
   
   private void addPanel(RogueTilePanel panel)
   {
      panel.setLocation(0, 0);
      panel.setSize(this.getWidth(), this.getHeight());
      panelList.add(panel);
      timer.addActionListener(panel);
      this.add(panel);
   }
   
   public void setMainGamePanelActive(){setCurPanel(mainGameBGPanel);}
   
   private void setCurPanel(RogueTilePanel panel)
   {
      curPanel = panel;
      for(JPanel listPanel : panelList)
      {
         if(listPanel == panel)
            listPanel.setVisible(true);
         else
            listPanel.setVisible(false);
      }
      if(curPanel instanceof MainGameBGPanel)
         mainGameFGPanel.setVisible(true);
      this.repaint();
   }
   
   // called when parent (outerPanel) resizes
   public void arrange(double sizeMult)
   {
      int singleTileWidth = (int)(sizeMult * 8);
      int singleTileHeight = (int)(sizeMult * 16);
      int squareTileSize = singleTileHeight;
      
      for(RogueTilePanel panel : panelList)
      {
         if(panel instanceof MainGameFGPanel)
         {
            mainGameFGPanel.setLocation(MAP_X_INSET_TILES * singleTileWidth, singleTileHeight);
            mainGameFGPanel.setSize(squareTileSize * MAP_WIDTH_TILES, squareTileSize * MAP_HEIGHT_TILES);
         }
         else
         {
            panel.setLocation(0, 0);
            panel.setSize(this.getWidth(), this.getHeight());
         }
         panel.setSizeMultiplier(sizeMult);
      }
      this.repaint();
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      this.repaint();
   }
}