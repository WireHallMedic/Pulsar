/*
This is the non-map portion of the main game screen.
*/

package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainGameBGPanel extends PlayerPanel implements GUIConstants
{
   public MainGameBGPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
      setSize(50, 50);
      GUITools.setStandardBorder(this);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(isVisible())
         super.actionPerformed(ae);
   }
}