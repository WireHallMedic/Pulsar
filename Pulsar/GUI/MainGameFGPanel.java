/*
This is the map portion of the main game screen.
*/

package Pulsar.GUI;

import WidlerSuite.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainGameFGPanel extends RogueTilePanel implements GUIConstants
{
   public MainGameFGPanel()
   {
      super(MAP_WIDTH_TILES, MAP_HEIGHT_TILES, SQUARE_TILE_PALETTE);
   }
}