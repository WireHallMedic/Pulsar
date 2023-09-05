/*
   A class used by VisualEffectFactory to delay effects.
   Has to be a public class because static methods can't create instances of private classes.

*/
package Pulsar.GUI;

import WidlerSuite.*;

public class DelayedVisualEffect
{
	private UnboundTile unboundTile;
	private MovementScript movementScript;
	private int delay;


	public UnboundTile getUnboundTile(){return unboundTile;}
	public MovementScript getMovementScript(){return movementScript;}
	public int getDelay(){return delay;}


	public void setUnboundTile(UnboundTile u){unboundTile = u;}
	public void setMovementScript(MovementScript m){movementScript = m;}
	public void setDelay(int d){delay = d;}


   public DelayedVisualEffect(UnboundTile ut, MovementScript ms, int d)
   {
      unboundTile = ut;
      movementScript = ms;
      delay = d;
   }
   
   public void increment()
   {
      delay--;
   }
   
   public boolean shouldTrigger()
   {
      return delay <= 0;
   }
   
   public void trigger()
   {
      if(unboundTile != null)
         VisualEffectFactory.add(unboundTile);
      if(movementScript != null)
         VisualEffectFactory.add(movementScript);
   }
}
