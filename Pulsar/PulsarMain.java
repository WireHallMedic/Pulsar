package Pulsar;

import javax.swing.*;
import Pulsar.GUI.*;

public class PulsarMain extends JFrame implements GUIConstants
{
   public static final String VERSION_NUMBER = "0.0.1";
   private OuterPanel outerPanel;
   
   public PulsarMain()
   {
      super();
      setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle(WINDOW_TITLE + " v" + VERSION_NUMBER);
      
      outerPanel = new OuterPanel();
      this.add(outerPanel);
      outerPanel.center();
      
      setVisible(true);
   }
   
   public static void main(String[] args)
   {
      PulsarMain frame = new PulsarMain();
   }
}