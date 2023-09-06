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
   private static boolean redrawF = false;
   
   private static Vector<String> messageList = getEmptyMessageList();
   private static Vector<Color> messageColorList = getEmptyMessageColorList();
   
   public static void updateMessagePanel(){redrawF = true;}
   
   public MessagePanel(int w, int h, TilePalette tp)
   {
      super(w, h, tp);
      clearMessagePanel();
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
      messageList.insertElementAt(message, 0);
      messageColorList.insertElementAt(messageColor, 0);
      messageList.removeElementAt(HEIGHT_TILES);
      messageColorList.removeElementAt(HEIGHT_TILES);
      redrawF = true;
   }
   
   @Override
   public void actionPerformed(ActionEvent ae)
   {
      if(redrawF)
      {
         redrawF = false;
         for(int i = 0; i < HEIGHT_TILES; i++)
         {
            String msg = messageList.elementAt(i);
            Color color = messageColorList.elementAt(i);
            if(msg.length() > WIDTH_TILES)
               msg = msg.substring(0, WIDTH_TILES);
            write(X_ORIGIN, Y_ORIGIN + i, msg, color.getRGB(), Color.BLACK.getRGB(), WIDTH_TILES, 1);
         }
      }
      super.actionPerformed(ae);
   }
   
   public static void clearMessagePanel()
   {
      messageList = getEmptyMessageList();
      messageColorList = getEmptyMessageColorList();
      redrawF = true;
   }
}