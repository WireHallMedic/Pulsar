package Pulsar.DevTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import WidlerSuite.*;
import Pulsar.GUI.*;
import java.awt.datatransfer.*;

public class ToolRoomDesignerMapPanel extends RogueTilePanel
{
   public static final String[] buttonStrList = {"", "", "",
      "#", ".", "0", "/", "V", "",
      "", "", "",
      "Set all #", "Set all .", "Set all 0", "Set all V", "", "", 
      "", "", "",
      "Set block", "Set terminal", "Set straight", "Set elbow", "Set tee", "Set cross"};
   public static final char DEFAULT_CHAR = '.';
   public static final int MAX_MAP_RADIUS = 21;
   
   private int widthTiles = 13;
   private int heightTiles = 13;
   
   public ToolRoomDesignerMapPanel()
   {
      super(MAX_MAP_RADIUS, MAX_MAP_RADIUS, GUIConstants.SQUARE_TILE_PALETTE);
   }
   
   
   public int getXOrigin()
   {
      return (MAX_MAP_RADIUS - widthTiles) / 2;
   }
   
   public int getYOrigin()
   {
      return (MAX_MAP_RADIUS - heightTiles) / 2;
   }
   
   public void setAllTiles(char c)
   {
      for(int x = 0; x < widthTiles; x++)
      for(int y = 0; y < heightTiles; y++)
      {
         setIcon(x, y, c);
      }
   }
   
   public void setAllBorders(char c)
   {
      for(int x = 0; x < widthTiles; x++)
      {
         setIcon(x, 0, c);
         setIcon(x, heightTiles - 1, c);
      }
      for(int y = 0; y < heightTiles; y++)
      {
         setIcon(0, y, c);
         setIcon(widthTiles - 1, y, c);
      }
   }
   
   public void setTerminal()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
         setIcon(x, 0, '.');
   }
   
   public void setStraight()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
      {
         setIcon(x, 0, '.');
         setIcon(x, heightTiles - 1, '.');
      }
   }
   
   public void setElbow()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
         setIcon(x, 0, '.');
      for(int y = 0; y < heightTiles - 1; y++)
         setIcon(0, y, '.');
   }
   
   public void setTee()
   {
      setAllBorders('#');
      for(int x = 1; x < widthTiles - 1; x++)
      {
         setIcon(x, 0, '.');
         setIcon(x, heightTiles - 1, '.');
      }
      for(int y = 0; y < heightTiles; y++)
         setIcon(0, y, '.');
   }
   
   public void setCross()
   {
      setAllBorders('.');
   }
   
   public String getClipboardString()
   {
      String s = "";
      for(int y = 0; y < heightTiles; y++)
      {
         for(int x = 0; x < widthTiles; x++)
         {
            s += (char)getIcon(x, y);
         }
         s += "\n";
      }
      return s;
   }
}