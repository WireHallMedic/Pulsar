/*
This is the non-map portion of the main game screen.
*/

package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainGameBGPanel extends RogueTilePanel implements GUIConstants
{
   public MainGameBGPanel()
   {
      super(TERMINAL_WIDTH_TILES, TERMINAL_HEIGHT_TILES, RECT_TILE_PALETTE);
   }
}