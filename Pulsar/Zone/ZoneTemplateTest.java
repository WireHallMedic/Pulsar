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
      for(int x = 0; x < zt.getWidth() - 1; x++)
      for(int y = 0; y < zt.getHeight() - 1; y++)
      {
         Assert.assertEquals("Horizontally adjacent cells have matching pass values", true, zt.getPassArray(x, y)[EAST] == zt.getPassArray(x + 1, y)[WEST]);
         Assert.assertEquals("Vertically adjacent cells have matching pass values", true, zt.getPassArray(x, y)[SOUTH] == zt.getPassArray(x, y + 1)[NORTH]);
      }
   }


   // this should hit every zone template used
   @Test public void testValidation() 
   {
      ZoneTemplate zt = getTestZoneTemplate();
      Assert.assertTrue("No disconnected rooms", checkConnectivity(zt));
   }
   
}
