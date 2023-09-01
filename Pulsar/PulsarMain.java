package Pulsar;

import javax.swing.*;
import Pulsar.GUI.*;

public class PulsarMain extends JFrame implements GUIConstants
{
   public static final String VERSION_NUMBER = "0.0.1";
   public static javax.swing.Timer timer;
   private OuterPanel outerPanel;
   
   public PulsarMain()
   {
      super();
      setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setBackground(BG_COLOR);
      setTitle(WINDOW_TITLE + " v" + VERSION_NUMBER);
      timer = new javax.swing.Timer(1000 / FRAMES_PER_SECOND, null);
      setVisible(true);
      
      outerPanel = new OuterPanel(timer);
      this.add(outerPanel);
      
      outerPanel.arrange();
      
      timer.start();
   }
   
   public static void main(String[] args)
   {
      PulsarMain frame = new PulsarMain();
   }
}