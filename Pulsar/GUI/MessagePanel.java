/*
This is a compositional part of MainGameBGPanel
*/

package Pulsar.GUI;

import WidlerSuite.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class MessagePanel extends InfoPanel
{
   private static final int WIDTH_TILES = TERMINAL_WIDTH_TILES - 2;
   private static final int HEIGHT_TILES = TERMINAL_HEIGHT_TILES - MAP_HEIGHT_TILES - 3;
   private static final int X_ORIGIN = 1;
   private static final int Y_ORIGIN = TERMINAL_HEIGHT_TILES - HEIGHT_TILES - 1;
   
   private static Vector<String> messageList = getEmptyMessageList();
   private static Vector<Color> messageColorList = getEmptyMessageColorList();
   
   public MessagePanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      for(int x = 0; x < WIDTH_TILES; x++)
      for(int y = 0; y < HEIGHT_TILES; y++)
      {
         setTile(x + X_ORIGIN, y + Y_ORIGIN, '.', TERMINAL_FG_COLOR, BG_COLOR);
      }
      write(X_ORIGIN + ((WIDTH_TILES - 14) / 2), Y_ORIGIN, "Message Panel", 13, 1);
   }
   
   public static Vector<String> getEmptyMessageList()
   {
      Vector<String> newList = new Vector<String>();
      for(int i = 0; i < HEIGHT_TILES; i++)
         newList.add("");
      return newList;
   }
   
   public static Vector<Color> getEmptyMessageColorList()
   {
      Vector<Color> newList = new Vector<Color>();
      for(int i = 0; i < HEIGHT_TILES; i++)
         newList.add(DEFAULT_MESSAGE_COLOR);
      return newList;
   }
   
   public static void addMessage(String message){addMessage(message, DEFAULT_MESSAGE_COLOR);}
   public static void addMessage(String message, Color messageColor)
   {
      messageList.add(message);
      messageColorList.add(messageColor);
      messageList.removeElementAt(0);
      messageColorList.removeElementAt(0);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      for(int i = 0; i < HEIGHT_TILES; i++)
      {
         String msg = messageList.elementAt(i);
         Color color = messageColorList.elementAt(i);
         if(msg.length() > WIDTH_TILES)
            msg = msg.substring(0, WIDTH_TILES);
         write(X_ORIGIN, Y_ORIGIN + i, msg, color.getRGB(), Color.BLACK.getRGB(), WIDTH_TILES, 1);
      }
      super.actionPerformed(ae);
   }
}