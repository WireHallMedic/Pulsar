package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import Pulsar.Zone.*;

public class ToolZoneViewer extends JFrame implements ActionListener, ZoneConstants
{
   private static final int MAP_WIDTH = (13 * 4) + 2;
   private static final int MAP_HEIGHT = (13 * 4) + 2;
   private static final int BUTTON_SLOTS = 20;
   
   private LayoutPanel layoutPanel;
   private RogueTilePanel mapPanel;
   private JPanel buttonPanel;
   private JButton fullRerollB;
   private JButton probRerollB;
   private JButton obstacleRerollB;
   
   private ZoneTemplate zoneTemplate;
   private ZoneMap zoneMap;
   
   
   public ToolZoneViewer()
   {
      super();
      setSize(1200, 1000);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle("Zone Viewer");
      
      layoutPanel = new LayoutPanel(this);
      layoutPanel.setVisible(true);
      this.add(layoutPanel);
      
      mapPanel = new RogueTilePanel(MAP_WIDTH, MAP_HEIGHT, GUIConstants.SQUARE_TILE_PALETTE);
      mapPanel.setBackground(Color.GRAY);
      mapPanel.setSizeMultiplier(1.0);
      mapPanel.setVisible(true);
      layoutPanel.add(mapPanel, .8, 1.0, 0.0, 0.0);
      
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(BUTTON_SLOTS, 1));
      buttonPanel.setVisible(true);
      buttonPanel.setBackground(Color.RED);
      layoutPanel.add(buttonPanel, .2, 1.0, .8, 0.0);
      
      fullRerollB = new JButton("Reroll all");
      fullRerollB.addActionListener(this);
      buttonPanel.add(fullRerollB);
      
      probRerollB = new JButton("Reroll Prob. Tiles");
      probRerollB.addActionListener(this);
      buttonPanel.add(probRerollB);
      
      obstacleRerollB = new JButton("Reroll Obstacles");
      obstacleRerollB.addActionListener(this);
      buttonPanel.add(obstacleRerollB);
      
      zoneTemplate = ZoneTemplate.getDemo("Room Templates.txt");
      zoneMap = ZoneMapFactory.buildFromTemplates(zoneTemplate.getTileMap(), TileType.HIGH_WALL);
      drawMap();
      
      for(int i = 0; i < BUTTON_SLOTS - 3; i++)
      {
         JPanel panel = new JPanel();
         buttonPanel.add(panel);
      }
      
      setVisible(true);
      layoutPanel.resizeComponents(true);
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      if(ae.getSource()== fullRerollB)
      {
         zoneTemplate = ZoneTemplate.getDemo("Room Templates.txt");
         zoneMap = ZoneMapFactory.buildFromTemplates(zoneTemplate.getTileMap(), TileType.HIGH_WALL);
         drawMap();
      }
      if(ae.getSource()== probRerollB)
      {
         drawMap();
      }
      if(ae.getSource()== obstacleRerollB)
      {
         drawMap();
      }
   }
   
   public void drawMap()
   {
      for(int x = 0; x < MAP_WIDTH; x++)
      for(int y = 0; y < MAP_HEIGHT; y++)
      {
         MapTile tile = zoneMap.getTile(x, y);
         mapPanel.setTile(x, y, tile.getIconIndex(), tile.getFGColor(), tile.getBGColor());
      }
      mapPanel.repaint();
   }
      
   public static void main(String[] args)
   {
      ToolZoneViewer zv = new ToolZoneViewer();
   }
   
   
}