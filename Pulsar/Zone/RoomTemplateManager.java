package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RoomTemplateManager implements ZoneConstants
{
   private TypeList[] typeList;
   private int tileHeight;
   private int tileWidth;
   
   public int getHeight(){return tileHeight;}
   public int getWidth(){return tileWidth;}
   
   
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
      return new RoomTemplate(typeList[t.ordinal()].random());
   }
   
   public void loadFromFile(String filename)
   {
      BufferedReader reader;
		try 
      {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
         boolean inTemplate = false;
         Vector<String> stringVect = new Vector<String>();
			while (line != null) 
         {
            // remove comments and trim
            if(line.contains("@"))
            {
               line = line.substring(0, line.indexOf('@'));
            }
            line = line.trim();
            
            // no text
            if(line.length() == 0)
            {
               // template has finished
               if(inTemplate)
               {
                  add(new RoomTemplate(stringVect));
                  inTemplate = false;
               }
            }
            // has text
				else
            {
               // new template
               if(!inTemplate)
               {
                  stringVect = new Vector<String>();
                  inTemplate = true;
               }
               stringVect.add(line);
            }
				// read next line
				line = reader.readLine();
			}
         
         // if the last template was on the last line, add it. Otherwise it's already added.
         if(inTemplate)
            add(new RoomTemplate(stringVect));

			reader.close();
		}
      catch (IOException e) 
      {
			e.printStackTrace();
		}
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
      str.add("#....");
      str.add("#....");
      str.add("#....");
      str.add("#....");
      str.add("#####");
      add(new RoomTemplate(str));
      
      str = new Vector<String>();
      str.add(".....");
      str.add(".....");
      str.add(".....");
      str.add(".....");
      str.add("#####");
      add(new RoomTemplate(str));
      
      str = new Vector<String>();
      str.add(".....");
      str.add(".....");
      str.add(".....");
      str.add(".....");
      str.add(".....");
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