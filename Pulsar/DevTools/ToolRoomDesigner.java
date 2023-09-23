package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;

public class ToolRoomDesigner extends JFrame implements ActionListener
{
   private JPanel layoutPanel;
   private RogueTilePanel mapPanel;
   private JPanel controlPanel;
   
   private int widthTiles = 13;
   private int heightTiles = 13;
   private JButton[][] buttonArray;
   
   
   public ToolRoomDesigner()
   {
      super();
      setSize(1000, 800);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle("Room Designer");
      
      buttonArray = null;
      
      layoutPanel = new JPanel();
      layoutPanel.setLayout(new GridLayout(1, 2));
      layoutPanel.setVisible(true);
      this.add(layoutPanel);
      
      mapPanel = new RogueTilePanel(widthTiles, heightTiles, GUIConstants.SQUARE_TILE_PALETTE);
      mapPanel.setSizeMultiplier(2.0);
      mapPanel.setVisible(true);
      layoutPanel.add(mapPanel);
      
      controlPanel = new JPanel();
      controlPanel.setVisible(true);
      layoutPanel.add(controlPanel);
      
      setButtonArray();
      javax.swing.Timer timer = new javax.swing.Timer(1000 / 30, null);
      timer.addActionListener(this);
      timer.start();
      
      setVisible(true);
   }
   
   public void setButtonArray()
   {
      for(int x = 0; x < widthTiles; x++)
      for(int y = 0; y < heightTiles; y++)
      {
         mapPanel.setIcon(x, y, '?');
      }
   }
   
   public static void main(String[] args)
   {
      ToolRoomDesigner t = new ToolRoomDesigner();
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      mapPanel.actionPerformed(ae);
      this.repaint();
   }
}