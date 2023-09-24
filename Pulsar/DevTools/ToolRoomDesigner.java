package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import java.awt.datatransfer.*;

public class ToolRoomDesigner extends JFrame implements ActionListener, MouseListener, KeyListener
{
   public static final String[] buttonStrList = {"", "#", ".", "0", "/", "V", "", 
      "", "", "",
      "Set all #", "Set all .", "Set all 0", "Set all V", "", "", 
      "", "", "",
      "Set block", "Set terminal", "Set straight", "Set elbow", "Set tee", "Set cross"};
   public static final char DEFAULT_CHAR = '.';
   private JPanel layoutPanel;
   private RogueTilePanel mapPanel;
   private JPanel controlPanel;
   private JButton[][] buttonArray;
   private JLabel label;
   private JButton[] buttonList;
   private JButton clipboardButton;
   private int innerPanelIndex;
   private JPanel[] innerPanel;
   
   
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
      controlPanel.setLayout(new GridLayout(1, 3));
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
      
      setActiveChar('#');
      
      mapPanel.grabFocus();
      setVisible(true);
   }
   
   public void setButtonArray()
   {
      for(int x = 0; x < widthTiles; x++)
      for(int y = 0; y < heightTiles; y++)
      {
         mapPanel.setIcon(x, y, DEFAULT_CHAR);
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
      if(ae.getSource() == clipboardButton)
      {
			String str = getClipboardString();
			StringSelection ss = new StringSelection(str);
		   Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
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
            if(text.contains("Set block"))
               setAllTiles('#');
            if(text.contains("Set terminal"))
               setTerminal();
            if(text.contains("Set straight"))
               setStraight();
            if(text.contains("Set elbow"))
               setElbow();
            if(text.contains("Set tee"))
               setTee();
            if(text.contains("Set cross"))
               setCross();
            break;
         }
      }
   }
   
   private void setAllBorders(char c)
   {
      for(int x = 0; x < widthTiles; x++)
      {
         mapPanel.setIcon(x, 0, c);
         mapPanel.setIcon(x, heightTiles - 1, c);
      }
      for(int y = 0; y < heightTiles; y++)
      {
         mapPanel.setIcon(0, y, c);
         mapPanel.setIcon(widthTiles - 1, y, c);
      }
   }
   
   private void setTerminal()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
         mapPanel.setIcon(x, 0, '.');
   }
   
   private void setStraight()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
      {
         mapPanel.setIcon(x, 0, '.');
         mapPanel.setIcon(x, heightTiles - 1, '.');
      }
   }
   
   private void setElbow()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
         mapPanel.setIcon(x, 0, '.');
      for(int y = 0; y < heightTiles - 1; y++)
         mapPanel.setIcon(0, y, '.');
   }
   
   private void setTee()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
      {
         mapPanel.setIcon(x, 0, '.');
         mapPanel.setIcon(x, heightTiles - 1, '.');
      }
      for(int y = 0; y < heightTiles; y++)
         mapPanel.setIcon(0, y, '.');
   }
   
   private void setCross()
   {
      setAllBorders('.');
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
   
   public String getClipboardString()
   {
      String s = "";
      for(int y = 0; y < heightTiles; y++)
      {
         for(int x = 0; x < widthTiles; x++)
         {
            s += (char)mapPanel.getIcon(x, y);
         }
         s += "\n";
      }
      return s;
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
   
   public void addToControlPanel(Component c)
   {
      innerPanel[innerPanelIndex].add(c);
      innerPanelIndex++;
      if(innerPanelIndex >= innerPanel.length)
         innerPanelIndex = 0;
   }
   
   public void populateControlPanel()
   {
      innerPanel = new JPanel[3];
      for(int i = 0; i < innerPanel.length; i++)
      {
         innerPanel[i] = new JPanel();
         innerPanel[i].setLayout(new GridLayout(12, 1));
         controlPanel.add(innerPanel[i]);
      }
      addToControlPanel(new JLabel("Active char:"));
      label = new JLabel();
      addToControlPanel(label);
      buttonList = new JButton[buttonStrList.length];
      for(int i = 0; i < buttonStrList.length; i++)
      {
         buttonList[i] = new JButton(buttonStrList[i]);
         buttonList[i].addActionListener(this);
         buttonList[i].setFocusable(false);
         if(buttonStrList[i].equals(""))
            buttonList[i].setVisible(false);
         addToControlPanel(buttonList[i]);
      }
      
      // spacer buttons that do nothing
      for(int i = 0; i < 33 - (buttonStrList.length + 2); i++)
      {
         JButton button = new JButton();
         button.setFocusable(false);
         button.setVisible(false);
         addToControlPanel(button);
      }
      
      clipboardButton = new JButton("Copy to Clipboard");
      clipboardButton.setFocusable(false);
      clipboardButton.addActionListener(this);
      addToControlPanel(clipboardButton);
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