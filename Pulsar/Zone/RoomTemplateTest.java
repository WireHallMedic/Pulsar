package Pulsar.Zone;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;


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
      String[] strArr = {"#.!",
                         "...",
                         "#.#"};
      Vector<String> v = new Vector<String>();
      for(int i = 0; i < strArr.length; i++)
         v.add(strArr[i]);
      return v;
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
      Assert.assertEquals("Block room recognized as block", RoomTemplate.RoomTemplateType.BLOCK, template.getType());
      template = new RoomTemplate(getTerminalStrings());
      Assert.assertEquals("Terminal room recognized as terminal", RoomTemplate.RoomTemplateType.TERMINAL, template.getType());
      template.set(getElbowStrings());
      Assert.assertEquals("Elbow room recognized as elbow", RoomTemplate.RoomTemplateType.ELBOW, template.getType());
      template.set(getStraightStrings());
      Assert.assertEquals("Straight room recognized as straight", RoomTemplate.RoomTemplateType.STRAIGHT, template.getType());
      template.set(getTeeStrings());
      Assert.assertEquals("Tee room recognized as tee", RoomTemplate.RoomTemplateType.TEE, template.getType());
      template.set(getCrossStrings());
      Assert.assertEquals("Cross room recognized as cross", RoomTemplate.RoomTemplateType.CROSS, template.getType());
   }
   
   @Test public void testRotation()
   {
      RoomTemplate template = new RoomTemplate(getTerminalStrings());
      Assert.assertEquals("North is clear", true, template.passNorth());
      Assert.assertEquals("South is blocked", false, template.passSouth());
      Assert.assertEquals("East is blocked", false, template.passEast());
      Assert.assertEquals("West is blocked", false, template.passWest());
      
      template.rotate();
      Assert.assertEquals("North is blocked", false, template.passNorth());
      Assert.assertEquals("South is blocked", false, template.passSouth());
      Assert.assertEquals("East is clear", true, template.passEast());
      Assert.assertEquals("West is blocked", false, template.passWest());
      
      template.rotate();
      Assert.assertEquals("North is blocked", false, template.passNorth());
      Assert.assertEquals("South is clear", true, template.passSouth());
      Assert.assertEquals("East is blocked", false, template.passEast());
      Assert.assertEquals("West is blocked", false, template.passWest());
      
      template.rotate();
      Assert.assertEquals("North is blocked", false, template.passNorth());
      Assert.assertEquals("South is blocked", false, template.passSouth());
      Assert.assertEquals("East is blocked", false, template.passEast());
      Assert.assertEquals("West is clear", true, template.passWest());
   }
   
   @Test public void testPassArray()
   {
      RoomTemplate template = new RoomTemplate(getTerminalStrings());
      boolean[] passArray = template.getPassArray();
      template.rotate();
      Assert.assertFalse("Original does not match terminal rotated once", template.matchesPassArray(passArray));
      template.rotate();
      Assert.assertFalse("Original does not match terminal rotated twice", template.matchesPassArray(passArray));
      template.rotate();
      Assert.assertFalse("Original does not match terminal rotated three times", template.matchesPassArray(passArray));
      template.rotate();
      Assert.assertTrue("Original matches terminal rotated four times", template.matchesPassArray(passArray));
      
      template = new RoomTemplate(getStraightStrings());
      passArray = template.getPassArray();
      template.rotate();
      Assert.assertFalse("Original does not match straight rotated once", template.matchesPassArray(passArray));
      template.rotate();
      Assert.assertTrue("Original matches straight rotated twice", template.matchesPassArray(passArray));
      template.rotate();
      Assert.assertFalse("Original does not match straight rotated three times", template.matchesPassArray(passArray));
      template.rotate();
      Assert.assertTrue("Original matches terminal straight four times", template.matchesPassArray(passArray));
   }
   
   @Test public void testRotatesUntilMatches()
   {
      RoomTemplate template = new RoomTemplate(getTerminalStrings());
      boolean[] passArray = {false, false, false, false};
      passArray[WEST] = true;
      Assert.assertFalse("Original does not target", template.matchesPassArray(passArray));
      template.rotateUntilMatches(passArray);
      Assert.assertTrue("rotateUntilMatches causes to match", template.matchesPassArray(passArray));
      Assert.assertFalse("Rotated north is false", template.passNorth());
      Assert.assertFalse("Rotated south is false", template.passSouth());
      Assert.assertFalse("Rotated east is false", template.passEast());
      Assert.assertTrue("Rotated west is true", template.passWest());
      passArray[1] = true;
      // unmatchable conditions raise error
      assertThrows(java.lang.Error.class, () -> {template.rotateUntilMatches(passArray);});
   }
   
   @Test public void testMirroring()
   {
      RoomTemplate template = new RoomTemplate(getBlockStrings());
      Assert.assertTrue("Block can mirrorX", template.canMirrorX());
      Assert.assertTrue("Block can mirrorY", template.canMirrorY());
      template = new RoomTemplate(getTerminalStrings());
      Assert.assertTrue("Terminal can mirrorX", template.canMirrorX());
      Assert.assertFalse("Terminal cannot mirrorY", template.canMirrorY());
      template.set(getElbowStrings());
      Assert.assertFalse("Terminal cannot mirrorX", template.canMirrorX());
      Assert.assertFalse("Terminal cannot mirrorY", template.canMirrorY());
      template.set(getStraightStrings());
      Assert.assertTrue("Straight can mirrorX", template.canMirrorX());
      Assert.assertTrue("Straight can mirrorY", template.canMirrorY());
      template.set(getTeeStrings());
      Assert.assertTrue("Tee can mirrorX", template.canMirrorX());
      Assert.assertFalse("Tee cannot mirrorY", template.canMirrorY());
      template.set(getCrossStrings());
      Assert.assertTrue("Cross can mirrorX", template.canMirrorX());
      Assert.assertTrue("Cross can mirrorY", template.canMirrorY());
   }
   
   @Test public void testValidation()
   {
      assertThrows(java.lang.Error.class, () -> {new RoomTemplate(getBadStrings());});
   }
   
   @Test public void testRoomTemplateManager()
   {
      RoomTemplateManager rtm = getRoomTemplateManager();
      Assert.assertEquals("RTM block is a block", RoomTemplate.RoomTemplateType.BLOCK, rtm.random(RoomTemplate.RoomTemplateType.BLOCK).getType());
      Assert.assertEquals("RTM terminal is a terminal", RoomTemplate.RoomTemplateType.TERMINAL, rtm.random(RoomTemplate.RoomTemplateType.TERMINAL).getType());
      Assert.assertEquals("RTM elbow is an elbow", RoomTemplate.RoomTemplateType.ELBOW, rtm.random(RoomTemplate.RoomTemplateType.ELBOW).getType());
      Assert.assertEquals("RTM straight is a straight", RoomTemplate.RoomTemplateType.STRAIGHT, rtm.random(RoomTemplate.RoomTemplateType.STRAIGHT).getType());
      Assert.assertEquals("RTM tee is a tee", RoomTemplate.RoomTemplateType.TEE, rtm.random(RoomTemplate.RoomTemplateType.TEE).getType());
      Assert.assertEquals("RTM cross is a cross", RoomTemplate.RoomTemplateType.CROSS, rtm.random(RoomTemplate.RoomTemplateType.CROSS).getType());
   }
   
}
