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
   private OuterPanel parent;
   private Vector<RogueTilePanel> panelList;
   private javax.swing.Timer timer;
   private MainGameBGPanel mainGameBGPanel;
   private MainGameFGPanel mainGameFGPanel;
   private CharacterPanel characterPanel;
   private static Class pendingPanelClass = null;
   
   private RogueTilePanel curPanel = null;
   
   public static void setActivePanel(Class p){pendingPanelClass = p;}
   
   public InnerPanel(javax.swing.Timer t, OuterPanel p)
   {
      super();
      parent = p;
      setBackground(Color.BLACK);
      timer = t;
      timer.addActionListener(this);
      setLayout(null);
      
      panelList = new Vector<RogueTilePanel>();
      mainGameFGPanel = new MainGameFGPanel();
      mainGameBGPanel = new MainGameBGPanel();
      characterPanel = new CharacterPanel();
      addPanel(mainGameFGPanel);
      addPanel(mainGameBGPanel);
      addPanel(characterPanel);
      
      setVisible(true);
      setCurPanel(mainGameBGPanel);
   }
   
   private void addPanel(RogueTilePanel panel)
   {
      panel.setLocation(0, 0);
      panel.setSize(this.getWidth(), this.getHeight());
      panelList.add(panel);
      timer.addActionListener(panel);
      panel.setVisible(false);
      this.add(panel);
   }
   
   public void setMainGamePanelActive(){setCurPanel(mainGameBGPanel);}
   
   private void setCurPanel(RogueTilePanel panel)
   {
      curPanel = panel;
      for(JPanel listPanel : panelList)
      {
         if(listPanel == curPanel)
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
      
      for(RogueTilePanel panel : panelList)
      {
         if(panel instanceof MainGameFGPanel)
         {
            mainGameFGPanel.setLocation(MAP_X_INSET_TILES * singleTileWidth, singleTileHeight);
            mainGameFGPanel.setSize(singleTileWidth *( MAP_WIDTH_TILES * 2), singleTileHeight * MAP_HEIGHT_TILES);
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
   
   private void activateByClass()
   {
      for(RogueTilePanel panel : panelList)
      {
         if(panel.getClass() == pendingPanelClass)
            setCurPanel(panel);
      }
      pendingPanelClass = null;
   }
   
   public void keyPressed(KeyEvent ke)
   {
      if(curPanel == characterPanel)
         characterPanel.keyPressed(ke);
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      if(pendingPanelClass != null)
         activateByClass();
      this.repaint();
   }
}