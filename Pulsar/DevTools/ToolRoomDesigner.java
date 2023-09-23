package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import WidlerSuite.*;

public class ToolRoomDesigner extends JFrame
{
   private LayoutPanel layoutPanel;
   private JPanel mapPanel;
   private JPanel controlPanel;
   
   
   public ToolRoomDesigner()
   {
      super();
      setSize(1000, 800);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle("Room Designer");
      
      layoutPanel = new LayoutPanel(this);
      layoutPanel.setVisible(true);
      this.add(layoutPanel);
      
      mapPanel = new JPanel();
      mapPanel.setBackground(Color.BLUE);
      mapPanel.setVisible(true);
      layoutPanel.add(mapPanel, 1.0, .75, 0.0, 0.0);
      
      controlPanel = new JPanel();
      controlPanel.setBackground(Color.RED);
      controlPanel.setVisible(true);
      layoutPanel.add(controlPanel, 1.0, .25, 0.0, .75);
      
      setVisible(true);
      this.repaint();
   }
   
   public static void main(String[] args)
   {
      new ToolRoomDesigner();
   }
}