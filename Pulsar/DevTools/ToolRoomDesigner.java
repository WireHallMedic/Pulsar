package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;

public class ToolRoomDesigner extends JFrame implements ActionListener, MouseListener, KeyListener
{
   public static final String[] buttonStrList = {"#", ".", "0", "/", "V", 
      "Set all #", "Set all .", "Set all 0", "Set all V"};
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
      mapPanel.addKeyListener(this);
      this.addKeyListener(this);
      javax.swing.Timer timer = new javax.swing.Timer(1000 / 30, null);
      timer.addActionListener(this);
      timer.start();
      
      mapPanel.grabFocus();
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
   
   private void setActiveChar(String str){setActiveChar(str.charAt(0));}
   private void setActiveChar(char c)
   {
      activeChar = c;
      label.setText("" + activeChar);
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      if(ae.getSource() instanceof javax.swing.Timer)
      {
         mapPanel.actionPerformed(ae);
         updateMouseLoc(mapPanel.mouseColumn(), mapPanel.mouseRow());
         this.repaint();
      }
      for(int i = 0; i < buttonStrList.length; i++)
      {
         if(ae.getSource() == buttonList[i])
         {
            JButton button = (JButton)ae.getSource();
            String text = button.getText();
            if(text.length() == 1)
               setActiveChar(text);
            if(text.contains("Set all"))
               setAllTiles(text.charAt(text.length() - 1));
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
   public void mouseClicked(MouseEvent me){}
   public void mousePressed(MouseEvent me)
   {
      mapPanel.setIcon(mouseLocX, mouseLocY, activeChar);
   }
   
   public void keyPressed(KeyEvent ke){}
   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke)
   {
      setActiveChar(ke.getKeyChar());
   }
   
   public void populateControlPanel()
   {
      controlPanel.add(new JLabel("Active char:"));
      label = new JLabel();
      controlPanel.add(label);
      buttonList = new JButton[buttonStrList.length];
      for(int i = 0; i < buttonStrList.length; i++)
      {
         buttonList[i] = new JButton(buttonStrList[i]);
         buttonList[i].addActionListener(this);
         buttonList[i].setFocusable(false);
         controlPanel.add(buttonList[i]);
      }
      
      for(int i = 0; i < 22 - (buttonStrList.length + 2); i++)
      {
         controlPanel.add(new JButton());
      }
   }
   
   public void setAllTiles(char c)
   {
      for(int x = 0; x < widthTiles; x++)
      for(int y = 0; y < heightTiles; y++)
      {
         mapPanel.setIcon(x, y, c);
      }
   }
   
   public static void main(String[] args)
   {
      ToolRoomDesigner t = new ToolRoomDesigner();
   }
   
   
}