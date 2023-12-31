package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import java.awt.datatransfer.*;
import javax.swing.border.*;

public class ToolRoomDesigner extends JFrame implements ActionListener, MouseListener, KeyListener
{
   public static final String[] buttonStrList = {"", "",
      "#", ".", "X", "/", "V", "",
      "", "", "",
      "Set all #", "Set all .", "Set all X", "Set all V", "", "", 
      "", "", "",
      "Set block", "Set terminal", "Set straight", "Set elbow", "Set tee", "Set cross"};
   public static final char DEFAULT_CHAR = '.';
   public static final int MAX_MAP_RADIUS = 21;
   private LayoutPanel layoutPanel;
   private ToolRoomDesignerMapPanel mapPanel;
   private JPanel controlPanel;
   private JButton[][] buttonArray;
   private JLabel cursorLabel;
   private JButton[] buttonList;
   private JButton clipboardButton;
   private JButton widthPlusB;
   private JButton heightPlusB;
   private JButton widthMinusB;
   private JButton heightMinusB;
   private JLabel widthLabel;
   private JLabel heightLabel;
   private JLabel locationLabel;
   private int innerPanelIndex;
   private JPanel[] innerPanel;
   private JButton setSizeButton;
   private javax.swing.Timer timer;
   
   
   private char activeChar;
   private int widthTiles = 13;
   private int heightTiles = 13;
   private int mouseLocX;
   private int mouseLocY;
   
   
   public ToolRoomDesigner()
   {
      super();
      setSize(1400, 800);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle("Room Designer");
      
      buttonArray = null;
      
      layoutPanel = new LayoutPanel(this);
      layoutPanel.setVisible(true);
      this.add(layoutPanel);
      
      mapPanel = new ToolRoomDesignerMapPanel();
      mapPanel.setBackground(Color.GRAY);
      mapPanel.setSizeMultiplier(2.0);
      mapPanel.setVisible(true);
      layoutPanel.add(mapPanel, .55, 1.0, 0.0, 0.0);
      
      controlPanel = new JPanel();
      controlPanel.setLayout(new GridLayout(1, 3));
      controlPanel.setVisible(true);
      populateControlPanel();
      controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      layoutPanel.add(controlPanel, .25, 1.0, .55, 0.0);
      
      addInstructionPanel();
      
      mapPanel.addMouseListener(this);
      this.addKeyListener(this);
      timer = new javax.swing.Timer(1000 / 30, null);
      timer.addActionListener(this);
      timer.start();
      
      setActiveChar('#');
      updateSizeLabels();
      
      setVisible(true);
      layoutPanel.resizeComponents(true);
   }
   
   private void setActiveChar(String str){setActiveChar(str.charAt(0));}
   private void setActiveChar(char c)
   {
      activeChar = c;
      cursorLabel.setText("Left Click: " + activeChar);
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
			String str = mapPanel.getClipboardString();
			StringSelection ss = new StringSelection(str);
		   Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
      }
      if(ae.getSource() == widthPlusB)
      {
         mapPanel.incrementWidth();
         updateSizeLabels();
      }
      if(ae.getSource() == widthMinusB)
      {
         mapPanel.decrementWidth();
         updateSizeLabels();
      }
      if(ae.getSource() == heightPlusB)
      {
         mapPanel.incrementHeight();
         updateSizeLabels();
      }
      if(ae.getSource() == heightMinusB)
      {
         mapPanel.decrementHeight();
         updateSizeLabels();
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
               mapPanel.setAllTiles(text.charAt(text.length() - 1));
            if(text.contains("Set block"))
               mapPanel.setAllTiles('#');
            if(text.contains("Set terminal"))
               mapPanel.setTerminal();
            if(text.contains("Set straight"))
               mapPanel.setStraight();
            if(text.contains("Set elbow"))
               mapPanel.setElbow();
            if(text.contains("Set tee"))
               mapPanel.setTee();
            if(text.contains("Set cross"))
               mapPanel.setCross();
            break;
         }
      }
   }
   
   private void addInstructionPanel()
   {
      JPanel instructionPanel = new JPanel();
      instructionPanel.setLayout(new GridLayout(30,1));
      instructionPanel.setVisible(true);
      instructionPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
      
      instructionPanel.add(new JLabel("V Vacuum"));
      instructionPanel.add(new JLabel(". Clear"));
      instructionPanel.add(new JLabel("# Wall"));
      instructionPanel.add(new JLabel("/ Door"));
      instructionPanel.add(new JLabel("X Out of Bounds"));
      instructionPanel.add(new JLabel("b Barrel"));
      instructionPanel.add(new JLabel("w Water Barrel"));
      instructionPanel.add(new JLabel("e Exploding Barrel"));
      instructionPanel.add(new JLabel("c Crate"));
      instructionPanel.add(new JLabel("= Table"));
      instructionPanel.add(new JLabel(", Rubble"));
      instructionPanel.add(new JLabel("~ Water"));
      instructionPanel.add(new JLabel("a Acid"));
      instructionPanel.add(new JLabel("! Button"));
      instructionPanel.add(new JLabel(": Window"));
      instructionPanel.add(new JLabel("T Terminal"));
      instructionPanel.add(new JLabel("> Exit"));
      instructionPanel.add(new JLabel("? Obstacle (5x5)"));
      instructionPanel.add(new JLabel("1 Wall (Room Prob)"));
      instructionPanel.add(new JLabel("2 Table (Room Prob)"));
      instructionPanel.add(new JLabel("3 Unused"));
      instructionPanel.add(new JLabel("4 Unused"));
      instructionPanel.add(new JLabel("5 Random Barrel (Instance Prob)"));
      instructionPanel.add(new JLabel("6 Exploding Barrel (Instance Prob)"));
      instructionPanel.add(new JLabel("7 Crate (Instance Prob)"));
      instructionPanel.add(new JLabel("8 Unused"));
      instructionPanel.add(new JLabel("9 Unused"));
      instructionPanel.add(new JLabel(""));
      instructionPanel.add(new JLabel(""));
      
      layoutPanel.add(instructionPanel, .2, 1.0, .8, 0.0);
   }
   
   public void updateSizeLabels()
   {
      widthLabel.setText("Width: " + mapPanel.getWidthTiles());
      heightLabel.setText("Height: " + mapPanel.getHeightTiles());
   }
   
   
   public void updateMouseLoc(int x, int y)
   {
      if(mouseLocX != x || mouseLocY != y)
      {
         if(mapPanel.isInActiveArea(mouseLocX, mouseLocY))
            mapPanel.setBGColor(mouseLocX, mouseLocY, Color.BLACK.getRGB());
         if(mapPanel.isInActiveArea(x, y))
         {
            mapPanel.setBGColor(x, y, Color.BLUE.getRGB());
            locationLabel.setText("Location: [" + x + ", " + y + "]");
         }
         else
         {
            locationLabel.setText("Location: []");
         }
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
      if(mapPanel.isInActiveArea(mouseLocX, mouseLocY))
      {
         if(me.getButton() == MouseEvent.BUTTON1)
            mapPanel.setIcon(mouseLocX, mouseLocY, activeChar);
         if(me.getButton() == MouseEvent.BUTTON3)
            setActiveChar((char)mapPanel.getIcon(mouseLocX, mouseLocY));
      }
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
      cursorLabel = new JLabel();
      addToControlPanel(cursorLabel);
      
      JPanel widthOuterPanel = new JPanel();
      widthOuterPanel.setLayout(new GridLayout(1, 2));
      JPanel widthInnerPanel = new JPanel();
      widthInnerPanel.setLayout(new GridLayout(2, 1));
      widthLabel = new JLabel("Width: " + widthTiles);
      widthOuterPanel.add(widthLabel);
      widthOuterPanel.add(widthInnerPanel);
      widthPlusB = new JButton("+");
      widthPlusB.addActionListener(this);
      widthPlusB.setFocusable(false);
      widthInnerPanel.add(widthPlusB);
      widthMinusB = new JButton("-");
      widthMinusB.addActionListener(this);
      widthMinusB.setFocusable(false);
      widthInnerPanel.add(widthMinusB);
      addToControlPanel(widthOuterPanel);
      
      JPanel heightOuterPanel = new JPanel();
      heightOuterPanel.setLayout(new GridLayout(1, 2));
      JPanel heightInnerPanel = new JPanel();
      heightInnerPanel.setLayout(new GridLayout(2, 1));
      heightLabel = new JLabel("Height: " + heightTiles);
      heightOuterPanel.add(heightLabel);
      heightOuterPanel.add(heightInnerPanel);
      heightPlusB = new JButton("+");
      heightPlusB.addActionListener(this);
      heightPlusB.setFocusable(false);
      heightInnerPanel.add(heightPlusB);
      heightMinusB = new JButton("-");
      heightMinusB.addActionListener(this);
      heightMinusB.setFocusable(false);
      heightInnerPanel.add(heightMinusB);
      addToControlPanel(heightOuterPanel);
      
      locationLabel = new JLabel("");
      addToControlPanel(locationLabel);
      
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
      
      int componentCount = 0;
      for(int i = 0; i < innerPanel.length; i++)
      {
         componentCount += innerPanel[i].getComponentCount();
      }
      // spacer buttons that do nothing
      for(int i = 0; i < 33 - componentCount; i++)
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
   
   public static void main(String[] args)
   {
      ToolRoomDesigner t = new ToolRoomDesigner();
   }
   
   
}