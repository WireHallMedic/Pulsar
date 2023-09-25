package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import Pulsar.Zone.*;

public class ToolZoneViewer extends JFrame implements ActionListener, ZoneConstants
{
   private static final int ROOM_WIDTH = 13;
   private static final int ROOM_HEIGHT = 13;
   private static final int MAP_WIDTH = ((ROOM_WIDTH - 1) * 4) + 3;
   private static final int MAP_HEIGHT = ((ROOM_HEIGHT - 1) * 4) + 3;
   private static final int BUTTON_SLOTS = 20;
   
   private LayoutPanel layoutPanel;
   private RogueTilePanel mapPanel;
   private JPanel buttonPanel;
   private JButton fullRerollB;
   private JButton probRerollB;
   private JButton obstacleRerollB;
   private JRadioButton vacuumButton;
   private JRadioButton wallButton;
   private ButtonGroup borderGroup;
   
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
      layoutPanel.add(buttonPanel, .2, 1.0, .8, 0.0);
      
      JPanel anonPanel = new JPanel();
      anonPanel.setLayout(new GridLayout(2, 1));
      buttonPanel.add(anonPanel);
      wallButton = new JRadioButton("Wall Border");
      wallButton.setSelected(true);
      wallButton.addActionListener(this);
      anonPanel.add(wallButton);
      vacuumButton = new JRadioButton("Vacuum Border");
      vacuumButton.setSelected(false);
      vacuumButton.addActionListener(this);
      anonPanel.add(vacuumButton);
      borderGroup = new ButtonGroup();
      borderGroup.add(wallButton);
      borderGroup.add(vacuumButton);
      
      // spacer
      buttonPanel.add(new JPanel());
      
      fullRerollB = new JButton("Reroll all");
      fullRerollB.addActionListener(this);
      buttonPanel.add(fullRerollB);
      
      obstacleRerollB = new JButton("Reroll Obstacles");
      obstacleRerollB.addActionListener(this);
      buttonPanel.add(obstacleRerollB);
      
      probRerollB = new JButton("Reroll Prob. Tiles");
      probRerollB.addActionListener(this);
      buttonPanel.add(probRerollB);
      
      int curComponents = buttonPanel.getComponentCount();
      for(int i = 0; i < BUTTON_SLOTS - curComponents; i++)
      {
         JPanel panel = new JPanel();
         buttonPanel.add(panel);
      }
      
      rerollAll();
      
      setVisible(true);
      layoutPanel.resizeComponents(true);
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      if(ae.getSource() == fullRerollB)
      {
         rerollAll();
      }
      if(ae.getSource() == probRerollB)
      {
         rerollProbs();
      }
      if(ae.getSource() == obstacleRerollB)
      {
         rerollObstacles();
      }
      if(ae.getSource() == wallButton || ae.getSource() == vacuumButton)
      {
         rerollBorder();
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
   
   public TileType getOOBType()
   {
      TileType type = TileType.HIGH_WALL;
      if(vacuumButton.isSelected())
         type = TileType.VACUUM;
      return type;
   }
   
   public void rerollAll()
   {
      zoneTemplate = ZoneTemplate.getDemo("Room Templates.txt");
      zoneTemplate.setObstacles();
      zoneTemplate.process();
      zoneMap = ZoneMapFactory.buildFromTemplates(zoneTemplate.getTileMap(), getOOBType());
      drawMap();
   }
   
   public void rerollBorder()
   {
      zoneMap = ZoneMapFactory.buildFromTemplates(zoneTemplate.getTileMap(), getOOBType());
      drawMap();
   }
   
   public void rerollProbs()
   {
      zoneTemplate.process();
      zoneMap = ZoneMapFactory.buildFromTemplates(zoneTemplate.getTileMap(), getOOBType());
      drawMap();
   }
   
   public void rerollObstacles()
   {
      zoneTemplate.setObstacles();
      zoneTemplate.process();
      zoneMap = ZoneMapFactory.buildFromTemplates(zoneTemplate.getTileMap(), getOOBType());
      drawMap();
   }
      
   public static void main(String[] args)
   {
      ToolZoneViewer zv = new ToolZoneViewer();
   }
   
   
}