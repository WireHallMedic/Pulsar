package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;

public class ZoneTemplate extends MapTemplate implements ZoneConstants
{
   
   public enum ZoneTemplateTile
   {
      DEFAULT_OOB    ('0'),
      ROOM           ('.'),
      WALL_BLOCK     ('#'),
      VACUUM_BLOCK   ('V'),
      INDETERMINATE  ('?');
      
      public char character;
      
      private ZoneTemplateTile(char c)
      {
         character = c;
      }
   }
   
   private boolean[][][] passArray;
   private RoomTemplate[][] roomTemplate;
   
   public ZoneTemplate(Vector<String> input){this(input, false);}
   public ZoneTemplate(Vector<String> input, boolean mostRestrictive)
   {
      super(input);
      setPassArray(mostRestrictive);
   }
   
   private void setPassArray(boolean mostRestrictive)
   {
      passArray = new boolean[width][height][4];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         boolean[] defaultArr = {false, false, false, false};
      }
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         setPassArrayCell(x, y, mostRestrictive);
      }
   }
   
   public void setPassArrayCell(int x, int y, boolean mostRestrictive)
   {
      char thisCell = getCell(x, y);
      char eastCell = '0';
      if(isInBounds(x + 1, y))
         eastCell = getCell(x + 1, y);
      char southCell = '0';
      if(isInBounds(x, y + 1))
         southCell = getCell(x, y + 1);
         
      if(thisCell == '.')
      {
         if(eastCell == '.' || eastCell == '?')
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(southCell == '.' || southCell == '?')
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
      else if(thisCell == '?')
      {
         boolean eastRoll = GameEngine.randomBoolean() && (!mostRestrictive);
         boolean northRoll = GameEngine.randomBoolean() && (!mostRestrictive);
         if(eastCell == '.' || (eastCell == '?' && eastRoll))
         {
            passArray[x][y][EAST] = true;
            passArray[x + 1][y][WEST] = true;
         }
         if(southCell == '.' || (southCell == '?' && northRoll))
         {
            passArray[x][y][SOUTH] = true;
            passArray[x][y + 1][NORTH] = true;
         }
      }
   }
   
   public void print()
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadDemos();
   }
}