package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;

public class RoomTemplateManager implements ZoneConstants
{
   private TypeList[] typeList;
   private int tileHeight;
   private int tileWidth;
   
   
   public RoomTemplateManager()
   {
      clear();
   }
   
   public void clear()
   {
      tileHeight = -1;
      tileWidth = -1;
      int len = RoomTemplate.RoomTemplateType.values().length;
      typeList = new TypeList[len];
      for(int i = 0; i < len; i++)
      {
         typeList[i] = new TypeList();
      }
   }
   
   public void add(RoomTemplate t)
   {
      typeList[t.getType().ordinal()].add(t);
      if(tileWidth == -1 || tileHeight == -1)
      {
         tileWidth = t.getWidth();
         tileHeight = t.getHeight();
      }
      else
      {
         if(tileWidth != t.getWidth() ||
            tileHeight != t.getHeight())
            throw new java.lang.Error("Added room does not match existing dimensions.");
      }
   }
   
   public RoomTemplate random(RoomTemplate.RoomTemplateType t)
   {
      return typeList[t.ordinal()].random();
   }
   
   public void loadDemos()
   {
      clear();
      Vector<String> str = new Vector<String>();
      str.add("#####");
      str.add("#####");
      str.add("#####");
      str.add("#####");
      str.add("#####");
      add(new RoomTemplate(str));
      
      str = new Vector<String>();
      str.add("#...#");
      str.add("#...#");
      str.add("#...#");
      str.add("#...#");
      str.add("#####");
      add(new RoomTemplate(str));
      
      str = new Vector<String>();
      str.add("#...#");
      str.add("#...#");
      str.add("#...#");
      str.add("#...#");
      str.add("#...#");
      add(new RoomTemplate(str));
      
      str = new Vector<String>();
      str.add("#...#");
      str.add("#....");
      str.add("#....");
      str.add("#....");
      str.add("#####");
      add(new RoomTemplate(str));
      
      str = new Vector<String>();
      str.add("#...#");
      str.add(".....");
      str.add(".....");
      str.add(".....");
      str.add("#####");
      add(new RoomTemplate(str));
      
      str = new Vector<String>();
      str.add("#...#");
      str.add(".....");
      str.add(".....");
      str.add(".....");
      str.add("#...#");
      add(new RoomTemplate(str));
   }
   
   
   
   private class TypeList
   {
      public Vector<RoomTemplate> list;
      
      public TypeList()
      {
         list = new Vector<RoomTemplate>();
      }
      
      public void add(RoomTemplate t)
      {
         list.add(t);
      }
      
      public RoomTemplate random()
      {
         return list.elementAt(GameEngine.randomInt(0, list.size()));
      }
   }
   
   
}