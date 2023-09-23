package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;

public class ToolRoomDesigner extends JFrame implements ActionListener, MouseListener
{
   public static final char[] buttonCharList = {'#', '.', '0', '/', 'V', '=', 
                                                '~', 'a', 'c', 'b', 'w', 'e', 
                                                '!', '?', 's', 'd', 'u', '<'};
   private JPanel layoutPanel;
   private RogueTilePanel mapPanel;
   private JPanel controlPanel;
   private JButton[][] buttonArray;
   private JLabel label;
   private JButton[] buttonList;
   
   
   private char activeChar;
   private int widthTiles = 13;
   private int heightTiles = 13;
   private int mouseLocX;
   private int mouseLocY;
   
   
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
      mapPanel.setBackground(Color.GRAY);
      mapPanel.setSizeMultiplier(2.0);
      mapPanel.setVisible(true);
      layoutPanel.add(mapPanel);
      
      controlPanel = new JPanel();
      controlPanel.setLayout(new GridLayout(12, 2));
      controlPanel.setVisible(true);
      populateControlPanel();
      layoutPanel.add(controlPanel);
      
      setButtonArray();
      mapPanel.addMouseListener(this);
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
   
   public void actionPerformed(ActionEvent ae)
   {
      if(ae.getSource() instanceof javax.swing.Timer)
      {
         mapPanel.actionPerformed(ae);
         updateMouseLoc(mapPanel.mouseColumn(), mapPanel.mouseRow());
         this.repaint();
      }
      for(int i = 0; i < buttonCharList.length; i++)
      {
         if(ae.getSource() == buttonList[i])
         {
            label.setText("" + buttonCharList[i]);
            activeChar = buttonCharList[i];
            break;
         }
      }
   }
   
   public void updateMouseLoc(int x, int y)
   {
      if(mouseLocX != x || mouseLocY != y)
      {
         mapPanel.setBGColor(mouseLocX, mouseLocY, Color.BLACK.getRGB());
         mapPanel.setBGColor(x, y, Color.BLUE.getRGB());
      }
      mouseLocX = x;
      mouseLocY = y;
   }
   
   public void mouseEntered(MouseEvent me){}
   public void mouseExited(MouseEvent me){}
   public void mouseReleased(MouseEvent me){}
   public void mousePressed(MouseEvent me){}
   public void mouseClicked(MouseEvent me)
   {
      mapPanel.setIcon(mouseLocX, mouseLocY, activeChar);
   }
   
   public void populateControlPanel()
   {
      controlPanel.add(new JLabel("Active char:"));
      label = new JLabel();
      controlPanel.add(label);
      buttonList = new JButton[buttonCharList.length];
      for(int i = 0; i < buttonCharList.length; i++)
      {
         buttonList[i] = new JButton("" + buttonCharList[i]);
         buttonList[i].addActionListener(this);
         controlPanel.add(buttonList[i]);
      }
   }
   
   public static void main(String[] args)
   {
      ToolRoomDesigner t = new ToolRoomDesigner();
   }
   
   
}