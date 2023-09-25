package Pulsar.Zone;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import WidlerSuite.*;


public class ZoneTemplateTest implements ZoneConstants
{


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() 
   {
   
   }
   
   public ZoneTemplate getTestZoneTemplate()
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadDemos();
      Vector<String> v = new Vector<String>();
      v.add("#...#");
      v.add(".???.");
      v.add(".???.");
      v.add(".???.");
      v.add("#...#");
      return new ZoneTemplate(v, rtm);
   }


   @Test public void testCompatibility() 
   {
      ZoneTemplate zt = getTestZoneTemplate();
      for(int x = 0; x < zt.getWidth() - 1; x++)
      for(int y = 0; y < zt.getHeight() - 1; y++)
      {
         Assert.assertEquals("Horizontally adjacent cells have matching pass values", true, zt.getPassArray(x, y)[EAST] == zt.getPassArray(x + 1, y)[WEST]);
         Assert.assertEquals("Vertically adjacent cells have matching pass values", true, zt.getPassArray(x, y)[SOUTH] == zt.getPassArray(x, y + 1)[NORTH]);
      }
      zt.setPassArray(true);
      zt.generateRooms();
      for(int x = 0; x < zt.getWidth() - 1; x++)
      for(int y = 0; y < zt.getHeight() - 1; y++)
      {
         Assert.assertEquals("Horizontally adjacent cells have matching pass values", true, zt.getPassArray(x, y)[EAST] == zt.getPassArray(x + 1, y)[WEST]);
         Assert.assertEquals("Vertically adjacent cells have matching pass values", true, zt.getPassArray(x, y)[SOUTH] == zt.getPassArray(x, y + 1)[NORTH]);
      }
   }


   @Test public void testValidation()
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.loadDemos();
      Vector<String> v = new Vector<String>();
      v.add(".???.");
      v.add(".???.");
      assertThrows(java.lang.Error.class, () -> {new ZoneTemplate(v, rtm);});
      Vector<String> v2 = new Vector<String>();
      v2.add("......");
      v2.add("..??..");
      v2.add(".????.");
      v2.add("..??..");
      v2.add("......");
      assertThrows(java.lang.Error.class, () -> {new ZoneTemplate(v2, rtm);});
      Vector<String> v3 = new Vector<String>();
      v3.add(".????.");
      v3.add("..??..");
      v3.add("......");
      assertThrows(java.lang.Error.class, () -> {new ZoneTemplate(v3, rtm);});
      
      ZoneTemplate zt = ZoneTemplate.getDemo("Room Templates.txt");
      zt.setObstacles();
      zt.process();
      assertTrue("Zone Template passes both validations", zt.validateButtons());
   }
   
   @Test public void test1000Zones()
   {
      for(int i = 0; i < 1000; i++)
      {
         ZoneTemplate zt = ZoneTemplate.getDemo("Room Templates.txt");
         zt.setObstacles();
         zt.process();
         assertTrue("Zone Template passes both validations", zt.validateButtons());
         assertTrue("Zone Template passes initial validation a second time", zt.validateInitial());
         zt.setObstacles();
         zt.process();
         assertTrue("Zone Template passes button validation a second time", zt.validateButtons());
      }
   }
   
}
