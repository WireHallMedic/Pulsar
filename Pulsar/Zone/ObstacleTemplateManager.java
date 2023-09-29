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
   
   public ObstacleTemplateManager(String fileName)
   {
      this();
      loadFromFile(fileName);
   }
   
   public void clear()
   {
      list = new Vector<ObstacleTemplate>();
   }
   
   public void add(ObstacleTemplate t)
   {
      t.validateButtons();
      list.add(t);
   }
   
   public void add(Vector<String> strVect, Vector<String> buttonVect)
   {
      ObstacleTemplate template = new ObstacleTemplate(strVect);
      if(buttonVect.size() > 0)
      {
         ButtonTrigger trigger = new ButtonTrigger();
         for(String instruction : buttonVect)
            trigger.parse(instruction);
         for(int x = 0; x < ObstacleTemplate.REQUIRED_WIDTH; x++)
         for(int y = 0; y < ObstacleTemplate.REQUIRED_HEIGHT; y++)
         {
            if(template.getCell(x, y) == TEMPLATE_BUTTON)
            {
               trigger.setCallerLoc(x, y);
            }
         }
         if(trigger.getCallerLoc() == null)
            throw new java.lang.Error("Button instructions with no button.");
         template.setButtonTrigger(trigger);
      }
      template.validateButtons();
      add(template);
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
         Vector<String> buttonVect = new Vector<String>();
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
                  add(stringVect, buttonVect);
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
                  buttonVect = new Vector<String>();
                  inTemplate = true;
               }
               if(line.toUpperCase().contains("BUTTON"))
                  buttonVect.add(line);
               else
                  stringVect.add(line);
            }
				// read next line
				line = reader.readLine();
			}
         
         // if the last template was on the last line, add it. Otherwise it's already added.
         if(inTemplate)
            add(stringVect, buttonVect);

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
   
   public Vector<ObstacleTemplate> getAll()
   {
      Vector<ObstacleTemplate> bigList = new Vector<ObstacleTemplate>();
      for(ObstacleTemplate template : list)
         bigList.add(new ObstacleTemplate(template));
      return bigList;
   }
   
   
}