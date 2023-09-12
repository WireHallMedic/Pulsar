package Pulsar.Actor;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class ActorTest 
{

   @Test public void actionSpeedTest() 
   {
      Assert.assertEquals("Slower than slow is slow", ActionSpeed.SLOW, ActionSpeed.SLOW.slower());
      Assert.assertEquals("Slower than standard is slow", ActionSpeed.SLOW, ActionSpeed.STANDARD.slower());
      Assert.assertEquals("Slower than fast is standard", ActionSpeed.STANDARD, ActionSpeed.FAST.slower());
      Assert.assertEquals("Slower than instantaneous is instantaneous", ActionSpeed.INSTANTANEOUS, ActionSpeed.INSTANTANEOUS.slower());
      
      Assert.assertEquals("Faster than slow is standard", ActionSpeed.STANDARD, ActionSpeed.SLOW.faster());
      Assert.assertEquals("Faster than standard is fast", ActionSpeed.FAST, ActionSpeed.STANDARD.faster());
      Assert.assertEquals("Faster than fast is fast", ActionSpeed.FAST, ActionSpeed.FAST.faster());
      Assert.assertEquals("Faster than instantaneous is instantaneous", ActionSpeed.INSTANTANEOUS, ActionSpeed.INSTANTANEOUS.faster());
   }
}
