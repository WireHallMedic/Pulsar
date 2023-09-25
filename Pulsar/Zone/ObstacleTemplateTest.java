package Pulsar.Zone;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;;


public class ObstacleTemplateTest implements ZoneConstants
{


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() 
   {
   
   }
      
   public ObstacleTemplate getButtonObstacle()
   {
      String[] strArr = {"#...#",
                         "..!..",
                         ".....",
                         ".....",
                         "#...#"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      ObstacleTemplate template = new ObstacleTemplate(v);
      ButtonTrigger trigger = new ButtonTrigger();
      trigger.parse("BUTTON_ACTION: FLOOD_WATER");
      trigger.parse("BUTTON_TARGET: [3, 3]");
      trigger.setCallerLoc(new Coord(2, 1));
      template.setButtonTrigger(trigger);
      return template;
   }
      
   public ObstacleTemplate getBadObstacle()
   {
      String[] strArr = {"#...#",
                         ".....",
                         "#...#"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return new ObstacleTemplate(v);
   }
      
   @Test public void testValidation()
   {
      assertThrows(java.lang.Error.class, () -> {getBadObstacle();});
      ObstacleTemplate template = getButtonObstacle();
      assertTrue("ObstacleTemplate passes button validation without error.", template.validateButtons());
      template.rotate();
      assertTrue("ObstacleTemplate passes button validation without error after rotation.", template.validateButtons());
      template.mirrorY();
      assertTrue("ObstacleTemplate passes button validation without error after mirrorY.", template.validateButtons());
      template.mirrorX();
      assertTrue("ObstacleTemplate passes button validation without error after mirrorX.", template.validateButtons());
   }
   
   @Test public void testAllObstacles()
   {
      ObstacleTemplateManager otm = new ObstacleTemplateManager();
      otm.loadFromFile("Obstacle Templates.txt");
      Vector<ObstacleTemplate> bigList = otm.getAll();
      for(ObstacleTemplate template : bigList)
      {
         assertTrue("ObstacleTemplate passes validation without error.", template.validate());
         assertTrue("ObstacleTemplate passes button validation without error.", template.validateButtons());
         template.rotate();
         assertTrue("ObstacleTemplate passes button validation without error after rotation.", template.validateButtons());
         template.rotate();
         assertTrue("ObstacleTemplate passes button validation without error after rotation.", template.validateButtons());
         template.rotate();
         assertTrue("ObstacleTemplate passes button validation without error after rotation.", template.validateButtons());
         template.mirrorY();
         assertTrue("ObstacleTemplate passes button validation without error after mirrorY.", template.validateButtons());
         template.mirrorX();
         assertTrue("ObstacleTemplate passes button validation without error after mirrorX.", template.validateButtons());
      }
      System.out.println("Tested " + bigList.size() + " ObstacleTemplates.");
   }

   
}
