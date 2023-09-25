package Pulsar.Zone;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;


public class RoomTemplateTest implements ZoneConstants
{


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() 
   {
   
   }
   
   public Vector<String> getBlockStrings()
   {
      String[] strArr = {"###",
                         "###",
                         "###"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
   }
   
   public Vector<String> getTerminalStrings()
   {
      String[] strArr = {"#.#",
                         "#.#",
                         "###"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
   }
   
   public Vector<String> getElbowStrings()
   {
      String[] strArr = {"#.#",
                         "#..",
                         "###"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
   }
   
   public Vector<String> getStraightStrings()
   {
      String[] strArr = {"#.#",
                         "#.#",
                         "#.#"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
   }
   
   public Vector<String> getTeeStrings()
   {
      String[] strArr = {"#.#",
                         "...",
                         "###"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
   }
   
   public Vector<String> getCrossStrings()
   {
      String[] strArr = {"#.#",
                         "...",
                         "#.#"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
   }
   
   public Vector<String> getBadStrings()
   {
      String[] strArr = {"#.Z",
                         "...",
                         "#.#"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
   }
   
   public RoomTemplate getButtonRoom()
   {
      String[] strArr = {"#...#",
                         "..!..",
                         ".....",
                         "#...#"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      RoomTemplate template = new RoomTemplate(v);
      ButtonTrigger trigger = new ButtonTrigger();
      trigger.parse("BUTTON_ACTION: FLOOD_WATER");
      trigger.parse("BUTTON_TARGET: [3, 3]");
      trigger.setCallerLoc(new Coord(2, 1));
      template.addButtonTrigger(trigger);
      return template;
   }
   
   public RoomTemplateManager getRoomTemplateManager()
   {
      RoomTemplateManager rtm = new RoomTemplateManager();
      rtm.add(new RoomTemplate(getBlockStrings()));
      rtm.add(new RoomTemplate(getTerminalStrings()));
      rtm.add(new RoomTemplate(getStraightStrings()));
      rtm.add(new RoomTemplate(getElbowStrings()));
      rtm.add(new RoomTemplate(getTeeStrings()));
      rtm.add(new RoomTemplate(getCrossStrings()));
      return rtm;
   }


   
   @Test public void testRoomTypesRecognized() 
   {
      RoomTemplate template = new RoomTemplate(getBlockStrings());
      assertEquals("Block room recognized as block", RoomTemplate.RoomTemplateType.BLOCK, template.getType());
      template = new RoomTemplate(getTerminalStrings());
      assertEquals("Terminal room recognized as terminal", RoomTemplate.RoomTemplateType.TERMINAL, template.getType());
      template.set(getElbowStrings());
      assertEquals("Elbow room recognized as elbow", RoomTemplate.RoomTemplateType.ELBOW, template.getType());
      template.set(getStraightStrings());
      assertEquals("Straight room recognized as straight", RoomTemplate.RoomTemplateType.STRAIGHT, template.getType());
      template.set(getTeeStrings());
      assertEquals("Tee room recognized as tee", RoomTemplate.RoomTemplateType.TEE, template.getType());
      template.set(getCrossStrings());
      assertEquals("Cross room recognized as cross", RoomTemplate.RoomTemplateType.CROSS, template.getType());
   }
   
   @Test public void testRotation()
   {
      RoomTemplate template = new RoomTemplate(getTerminalStrings());
      assertEquals("North is clear", true, template.passNorth());
      assertEquals("South is blocked", false, template.passSouth());
      assertEquals("East is blocked", false, template.passEast());
      assertEquals("West is blocked", false, template.passWest());
      
      template.rotate();
      assertEquals("North is blocked", false, template.passNorth());
      assertEquals("South is blocked", false, template.passSouth());
      assertEquals("East is clear", true, template.passEast());
      assertEquals("West is blocked", false, template.passWest());
      
      template.rotate();
      assertEquals("North is blocked", false, template.passNorth());
      assertEquals("South is clear", true, template.passSouth());
      assertEquals("East is blocked", false, template.passEast());
      assertEquals("West is blocked", false, template.passWest());
      
      template.rotate();
      assertEquals("North is blocked", false, template.passNorth());
      assertEquals("South is blocked", false, template.passSouth());
      assertEquals("East is blocked", false, template.passEast());
      assertEquals("West is clear", true, template.passWest());
   }
   
   @Test public void testPassArray()
   {
      RoomTemplate template = new RoomTemplate(getTerminalStrings());
      boolean[] passArray = template.getPassArray();
      template.rotate();
      assertFalse("Original does not match terminal rotated once", template.matchesPassArray(passArray));
      template.rotate();
      assertFalse("Original does not match terminal rotated twice", template.matchesPassArray(passArray));
      template.rotate();
      assertFalse("Original does not match terminal rotated three times", template.matchesPassArray(passArray));
      template.rotate();
      assertTrue("Original matches terminal rotated four times", template.matchesPassArray(passArray));
      
      template = new RoomTemplate(getStraightStrings());
      passArray = template.getPassArray();
      template.rotate();
      assertFalse("Original does not match straight rotated once", template.matchesPassArray(passArray));
      template.rotate();
      assertTrue("Original matches straight rotated twice", template.matchesPassArray(passArray));
      template.rotate();
      assertFalse("Original does not match straight rotated three times", template.matchesPassArray(passArray));
      template.rotate();
      assertTrue("Original matches terminal straight four times", template.matchesPassArray(passArray));
   }
   
   @Test public void testRotatesUntilMatches()
   {
      RoomTemplate template = new RoomTemplate(getTerminalStrings());
      boolean[] passArray = {false, false, false, false};
      passArray[WEST] = true;
      assertFalse("Original does not target", template.matchesPassArray(passArray));
      template.rotateUntilMatches(passArray);
      assertTrue("rotateUntilMatches causes to match", template.matchesPassArray(passArray));
      assertFalse("Rotated north is false", template.passNorth());
      assertFalse("Rotated south is false", template.passSouth());
      assertFalse("Rotated east is false", template.passEast());
      assertTrue("Rotated west is true", template.passWest());
      passArray[1] = true;
      // unmatchable conditions raise error
      assertThrows(java.lang.Error.class, () -> {template.rotateUntilMatches(passArray);});
   }
   
   @Test public void testMirroring()
   {
      RoomTemplate template = new RoomTemplate(getBlockStrings());
      assertTrue("Block can mirrorX", template.canMirrorX());
      assertTrue("Block can mirrorY", template.canMirrorY());
      template = new RoomTemplate(getTerminalStrings());
      assertTrue("Terminal can mirrorX", template.canMirrorX());
      assertFalse("Terminal cannot mirrorY", template.canMirrorY());
      template.set(getElbowStrings());
      assertFalse("Terminal cannot mirrorX", template.canMirrorX());
      assertFalse("Terminal cannot mirrorY", template.canMirrorY());
      template.set(getStraightStrings());
      assertTrue("Straight can mirrorX", template.canMirrorX());
      assertTrue("Straight can mirrorY", template.canMirrorY());
      template.set(getTeeStrings());
      assertTrue("Tee can mirrorX", template.canMirrorX());
      assertFalse("Tee cannot mirrorY", template.canMirrorY());
      template.set(getCrossStrings());
      assertTrue("Cross can mirrorX", template.canMirrorX());
      assertTrue("Cross can mirrorY", template.canMirrorY());
   }
   
   @Test public void testValidation()
   {
      assertThrows(java.lang.Error.class, () -> {new RoomTemplate(getBadStrings());});
      RoomTemplate template = getButtonRoom();
      assertTrue("RoomTemplate passes button validation without error.", template.validateButtons());
      template.rotate();
      assertTrue("RoomTemplate passes button validation without error after rotation.", template.validateButtons());
      template.mirrorY();
      assertTrue("RoomTemplate passes button validation without error after mirrorY.", template.validateButtons());
      template.mirrorX();
      assertTrue("RoomTemplate passes button validation without error after mirrorX.", template.validateButtons());
   }
   
   @Test public void testRoomTemplateManager()
   {
      RoomTemplateManager rtm = getRoomTemplateManager();
      assertEquals("RTM block is a block", RoomTemplate.RoomTemplateType.BLOCK, rtm.random(RoomTemplate.RoomTemplateType.BLOCK).getType());
      assertEquals("RTM terminal is a terminal", RoomTemplate.RoomTemplateType.TERMINAL, rtm.random(RoomTemplate.RoomTemplateType.TERMINAL).getType());
      assertEquals("RTM elbow is an elbow", RoomTemplate.RoomTemplateType.ELBOW, rtm.random(RoomTemplate.RoomTemplateType.ELBOW).getType());
      assertEquals("RTM straight is a straight", RoomTemplate.RoomTemplateType.STRAIGHT, rtm.random(RoomTemplate.RoomTemplateType.STRAIGHT).getType());
      assertEquals("RTM tee is a tee", RoomTemplate.RoomTemplateType.TEE, rtm.random(RoomTemplate.RoomTemplateType.TEE).getType());
      assertEquals("RTM cross is a cross", RoomTemplate.RoomTemplateType.CROSS, rtm.random(RoomTemplate.RoomTemplateType.CROSS).getType());
   }
   
}
