package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ZoneTemplateManager implements ZoneConstants
{
   private Vector<ZoneTemplate> templateList;
   private RoomTemplateManager openTemplateManager;
   private RoomTemplateManager closedTemplateManager;
   private RoomTemplateManager corridorTemplateManager;
   private ObstacleTemplateManager obstacleTemplateManager;
   
   public ZoneTemplateManager()
   {
      templateList = new Vector<ZoneTemplate>();
      
      openTemplateManager = new RoomTemplateManager("Open Room Templates.txt");
      closedTemplateManager = new RoomTemplateManager("Closed Room Templates.txt");
      corridorTemplateManager = new RoomTemplateManager("Corridor Templates.txt");
      obstacleTemplateManager = new ObstacleTemplateManager("Obstacle Templates.txt");
      
      loadFromFile("Zone Templates.txt");
   }
   
   public void add(Vector<String> strVect)
   {
      templateList.add(new ZoneTemplate(strVect, openTemplateManager, closedTemplateManager, 
                                        corridorTemplateManager, obstacleTemplateManager));
   }
   
   public ZoneTemplate random()
   {
      if(templateList.size() > 0)
         return new ZoneTemplate(templateList.elementAt(GameEngine.randomInt(0, templateList.size())));
      return null;
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

   public static void main(String[] args)
   {
      ZoneTemplateManager ztm = new ZoneTemplateManager();
      ztm.loadFromFile("Zone Templates.txt");
      ztm.random().print();
   }
}