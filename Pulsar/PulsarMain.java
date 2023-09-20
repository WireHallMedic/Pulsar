package Pulsar;

import javax.swing.*;
import Pulsar.GUI.*;
import Pulsar.Engine.*;
import java.awt.event.*;

public class PulsarMain extends JFrame implements GUIConstants, WindowStateListener
{
   public static final String VERSION_NUMBER = "0.0.1";
   public static javax.swing.Timer timer;
   private OuterPanel outerPanel;
   private InputManager inputManager;
   
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
      
      inputManager = new InputManager(this);
      addWindowStateListener(this);
      
      VisualEffectFactory veFactory = new VisualEffectFactory();
      timer.addActionListener(veFactory);
      
      timer.start();
      outerPanel.arrange();
   }
   
   public void windowStateChanged(WindowEvent we)
   {
      outerPanel.arrange();
   }
   
   // received from InputManager when GameEngine.getGameMode() == OTHER_PANEL
   public void keyPressed(KeyEvent ke)
   {
      outerPanel.keyPressed(ke);
   }
   
   public static void main(String[] args)
   {
      PulsarMain frame = new PulsarMain();
      // now called on title page
      /*
      GameEngine engine = new GameEngine();
      engine.newGame();
      engine.begin();*/
      frame.outerPanel.arrange();
   }
}