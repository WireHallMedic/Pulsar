package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ObstacleTemplateManager implements ZoneConstants
{
   private Vector<ObstacleTemplate> list;
   private boolean validated;
   
   
   public ObstacleTemplateManager()
   {
      clear();
   }
   
   public void clear()
   {
      list = new Vector<ObstacleTemplate>();
   }
   
   public void add(ObstacleTemplate t)
   {
      list.add(t);
   }
   
   public void add(Vector<String> strVect)
   {
      add(new ObstacleTemplate(strVect));
   }
   
   public ObstacleTemplate random()
   {
      if(list.size() == 0)
         return generateEmpty();
      ObstacleTemplate template = new ObstacleTemplate(list.elementAt(GameEngine.randomInt(0, list.size())));
      int spins = GameEngine.randomInt(0, 4);
      for(int i = 0; i < spins; i++)
         template.rotate();
      if(GameEngine.randomBoolean())
         template.mirrorX();
      if(GameEngine.randomBoolean())
         template.mirrorY();
      return template;
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
                  add(stringVect);
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
            add(stringVect);

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
      str.add(".55..");
      str.add("5555.");
      str.add("5555.");
      str.add(".55..");
      str.add(".....");
      add(new ObstacleTemplate(str));
      
      str = new Vector<String>();
      str.add("#####");
      str.add("#...#");
      str.add("#.../");
      str.add("#...#");
      str.add("#####");
      add(new ObstacleTemplate(str));
   }
   
   // returns if random is called when list is empty
   private ObstacleTemplate generateEmpty()
   {
      Vector<String> strVect = new Vector<String>();
      for(int y = 0; y < ObstacleTemplate.REQUIRED_HEIGHT; y++)
      {
         String str = "";
         for(int x = 0; x < ObstacleTemplate.REQUIRED_WIDTH; x++)
            str += ".";
         strVect.add(str);
      }
      return new ObstacleTemplate(strVect);
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
      
      public int size()
      {
         return list.size();
      }
   }
   
   
}