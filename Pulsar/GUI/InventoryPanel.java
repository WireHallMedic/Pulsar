package Pulsar.GUI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.awt.event.*;

public class InventoryPanel extends SelectionPanel
{
   private Player player;
   
   public void set()
   {
      player = GameEngine.getPlayer();
   }
   
   public void update()
   {
   
   }
   
   public void doSelection()
   {
   
   }
   
   @Override
   public void setVisible(boolean v)
   {
      if(v)
         set();
      super.setVisible(v);
   }
   
   @Override
   public void keyPressed(KeyEvent ke)
   {
      switch(ke.getKeyCode())
      {
         case KeyEvent.VK_I :
         case KeyEvent.VK_ESCAPE : InnerPanel.setActivePanel(MainGameBGPanel.class); 
                                   GameEngine.setGameMode(EngineConstants.GameMode.STANDARD);
                                   return;
      }
      super.keyPressed(ke);
   }
}