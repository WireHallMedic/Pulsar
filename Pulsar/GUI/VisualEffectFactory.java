package Pulsar.GUI;

import java.awt.*;
import WidlerSuite.*;
import Pulsar.Engine.*;

public class VisualEffectFactory implements GUIConstants
{
   private static TilePalette getPalette(){return GUIConstants.SQUARE_TILE_PALETTE;}
   private static double getSizeMultiplier(){return GameEngine.getMapPanel().getSizeMultiplier();}
   private static void add(UnboundTile ut){GameEngine.getMapPanel().add(ut);}
   private static void add(MovementScript ms){GameEngine.getMapPanel().add(ms);}

   
   public static void createSpray(Coord origin, Coord source, Color color)
   {
      int reps = GameEngine.randomInt(8, 13);
      for(int i = 0; i < reps; i++)
         createSprayParticle(origin, source, color);
   }
   
   public static void createSprayParticle(Coord origin, Coord source, Color color)
   {
      double xSpeed = GUITools.getXSpeedTowards(source, origin);
      double ySpeed = GUITools.getYSpeedTowards(source, origin);
      xSpeed *= SPRAY_BASE_SPEED;
      ySpeed *= SPRAY_BASE_SPEED;
      xSpeed = GUITools.modifyByAmount(xSpeed, SPRAY_VARIABLE_SPEED);
      ySpeed = GUITools.modifyByAmount(ySpeed, SPRAY_VARIABLE_SPEED);
      UnboundTile ut = getPalette().getUnboundTile(250, color.getRGB(), getSizeMultiplier());
      ut.setLoc(origin);
      ut.setLifespan(SPRAY_DURATION);
      ut.setSpeed(xSpeed, ySpeed);
      add(ut);
   }
   
   public static void createExplosion(Coord origin)
   {
      UnboundTile[] particleArray = new UnboundTile[16];
      for(int i = 0; i < particleArray.length; i++)
      {
         Color color = Color.RED;
         if(GameEngine.randomInt(0, 2) == 1)
            color = Color.ORANGE;
         particleArray[i] = getPalette().getUnboundTile('*', color.getRGB(), getSizeMultiplier());
         particleArray[i].setLoc(origin);
         particleArray[i].setLifespan(SPRAY_DURATION);
      }
      double diagonalSpeed = SPRAY_BASE_SPEED * .707;
      particleArray[0].setSpeed(GUITools.modifyByAmount(0.0, SPRAY_VARIABLE_SPEED), GUITools.modifyByAmount(SPRAY_BASE_SPEED, SPRAY_VARIABLE_SPEED));
      particleArray[1].setSpeed(GUITools.modifyByAmount(SPRAY_BASE_SPEED, SPRAY_VARIABLE_SPEED), GUITools.modifyByAmount(0.0, SPRAY_VARIABLE_SPEED));
      particleArray[2].setSpeed(GUITools.modifyByAmount(0.0, SPRAY_VARIABLE_SPEED), -(GUITools.modifyByAmount(SPRAY_BASE_SPEED, SPRAY_VARIABLE_SPEED)));
      particleArray[3].setSpeed(-(GUITools.modifyByAmount(SPRAY_BASE_SPEED, SPRAY_VARIABLE_SPEED)), GUITools.modifyByAmount(0.0, SPRAY_VARIABLE_SPEED));
      particleArray[4].setSpeed(GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED), GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED));
      particleArray[5].setSpeed(-(GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED)), GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED));
      particleArray[6].setSpeed(GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED), -(GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED)));
      particleArray[7].setSpeed(-(GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED)), -(GUITools.modifyByAmount(diagonalSpeed, SPRAY_VARIABLE_SPEED)));
      
      for(int i = 0; i < particleArray.length - 8; i++)
      {
         double xSpeed = -SPRAY_BASE_SPEED + (2 * GameEngine.random() * SPRAY_BASE_SPEED);
         double ySpeed = -SPRAY_BASE_SPEED + (2 * GameEngine.random() * SPRAY_BASE_SPEED);
         particleArray[8 + i].setSpeed(xSpeed, ySpeed);
      }
      
      for(int i = 0; i < particleArray.length; i++)
         add(particleArray[i]);
   }
   
   private class DelayedVisualEffect
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
            add(unboundTile);
         if(movementScript != null)
            add(movementScript);
      }
   }
}
