package Pulsar.GUI;

import java.awt.*;
import WidlerSuite.*;
import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.awt.event.*;
import java.util.*;

public class VisualEffectFactory implements GUIConstants, ActionListener, WSFontConstants
{
   private static final double SHIELD_BREAK_DIAG_SPEED = .707 * SLOW_MOVE_SPEED;
   private static final double SHIELD_BREAK_ORTHO_SPEED = SLOW_MOVE_SPEED;
   private static final double[] SHIELD_FLICKER_X_OFFSET = {.2, .5, .8, .1, .5, .8, .2, .5, .8};
   private static final double[] SHIELD_FLICKER_Y_OFFSET = {.2, .1, .2, .5, .5, .5, .8, .9, .8};
   private static final double[] SHIELD_BREAK_X_SPEED = getShieldBreakXSpeeds();
   private static final double[] SHIELD_BREAK_Y_SPEED = getShieldBreakYSpeeds();
   private static TilePalette getPalette(){return GUIConstants.SQUARE_TILE_PALETTE;}
   private static double getSizeMultiplier(){return GameEngine.getMapPanel().getSizeMultiplier();}
   public static void add(UnboundTile ut){GameEngine.getMapPanel().add(ut);}
   public static void add(MovementScript ms){GameEngine.getMapPanel().add(ms);}
   private static Vector<DelayedVisualEffect> delayList = new Vector<DelayedVisualEffect>();

   
   public static void createSpray(Coord origin, Coord source, Color color){createSpray(origin, source, color, 0);}
   public static void createSpray(Coord origin, Coord source, Color color, int delay)
   {
      int reps = GameEngine.randomInt(8, 13);
      for(int i = 0; i < reps; i++)
         createSprayParticle(origin, source, color, delay);
   }
   
   public static void createSprayParticle(Coord origin, Coord source, Color color){createSprayParticle(origin, source, color, 0);}
   public static void createSprayParticle(Coord origin, Coord source, Color color, int delay)
   {
      double xSpeed = GUITools.getXSpeedTowards(source, origin);
      double ySpeed = GUITools.getYSpeedTowards(source, origin);
      xSpeed *= SPRAY_BASE_SPEED;
      ySpeed *= SPRAY_BASE_SPEED;
      xSpeed = GUITools.modifyByAmount(xSpeed, SPRAY_VARIABLE_SPEED);
      ySpeed = GUITools.modifyByAmount(ySpeed, SPRAY_VARIABLE_SPEED);
      UnboundTile ut = getPalette().getUnboundTile(SMALL_BULLET_TILE, color.getRGB(), getSizeMultiplier());
      ut.setLoc(origin);
      ut.setLifespan(SPRAY_DURATION);
      ut.setSpeed(xSpeed, ySpeed);
      if(delay > 0)
         addWithDelay(ut, null, delay);
      else
         add(ut);
   }
   
   public static void createExplosion(Coord origin){createExplosion(origin, 0);}
   public static void createExplosion(Coord origin, int delay)
   {
      UnboundTile[] particleArray = new UnboundTile[16];
      for(int i = 0; i < particleArray.length; i++)
      {
         Color color = Color.RED;
         if(GameEngine.randomInt(0, 2) == 1)
            color = Color.ORANGE;
         particleArray[i] = getPalette().getUnboundTile(EXPLOSION_CHAR, color.getRGB(), getSizeMultiplier());
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
      
      if(delay > 0)
      {
         addWithDelay(particleArray, delay);
      }
      else
      {
         for(int i = 0; i < particleArray.length; i++)
         {
            add(particleArray[i]);
         }
      }
   }
   
   public static void createShieldFlicker(Actor target)
   {
      int flickerDuration = 3;
      int reps = 3;
      for(int delay = 0; delay < reps; delay++)
      {
         for(int i = 0; i < SHIELD_FLICKER_X_OFFSET.length; i++)
         {
            double xOffset = SHIELD_FLICKER_X_OFFSET[i] - .6;
            double yOffset = SHIELD_FLICKER_Y_OFFSET[i] - .6;
            xOffset += GameEngine.random() * .2;
            yOffset += GameEngine.random() * .2;
            UnboundTile ut = getPalette().getUnboundTile(SMALL_BULLET_TILE, SHIELD_COLOR.getRGB(), getSizeMultiplier());
            ut.setXOffset(xOffset);
            ut.setYOffset(yOffset);
            ut.setLifespan(flickerDuration);
            ut.setAnchorTile(target.getSprite());
            if(delay == 0)
               add(ut);
            else
               addWithDelay(ut, null, delay * flickerDuration);
         }
      }
   }
   
   public static void createShieldBreak(Actor target)
   {
      for(int i = 0; i < SHIELD_FLICKER_X_OFFSET.length; i++)
      {
         if(i == 4)
            continue;
         double xOffset = SHIELD_FLICKER_X_OFFSET[i] - .6;
         double yOffset = SHIELD_FLICKER_Y_OFFSET[i] - .6;
         xOffset += GameEngine.random() * .2;
         yOffset += GameEngine.random() * .2;
         UnboundTile ut = getPalette().getUnboundTile(SMALL_BULLET_TILE, SHIELD_COLOR.getRGB(), getSizeMultiplier());
         ut.setXOffset(xOffset);
         ut.setYOffset(yOffset);
         double xSpeed = SHIELD_BREAK_X_SPEED[i] + (GameEngine.random() * SLOW_MOVE_SPEED * .5);
         double ySpeed = SHIELD_BREAK_Y_SPEED[i] + (GameEngine.random() * SLOW_MOVE_SPEED * .5);
         ut.setSpeed(SHIELD_BREAK_X_SPEED[i], SHIELD_BREAK_Y_SPEED[i]);
         ut.setLifespan(10);
         ut.setLoc(target.getMapLoc());
         add(ut);
      }
   }
   
   public static void addWithDelay(UnboundTile ut, MovementScript ms, int delay)
   {
      DelayedVisualEffect delayVE;
      delayVE = new DelayedVisualEffect(ut, ms, delay);
      delayList.add(delayVE);
   }
   
   public static void addWithDelay(UnboundTile[] utList, int delay)
   {
      for(UnboundTile ut : utList)
      {
         addWithDelay(ut, null, delay);
      }
   }
   
   private static double[] getShieldBreakXSpeeds()
   {
      double straight = 1.0 * SLOW_MOVE_SPEED;
      double diag = .707 * SLOW_MOVE_SPEED;
      double[] speedArr = new double[9];
      speedArr[0] = -diag;
      speedArr[1] = 0.0;
      speedArr[2] = diag;
      speedArr[3] = -straight;
      speedArr[4] = 0.0;
      speedArr[5] = straight;
      speedArr[6] = -diag;
      speedArr[7] = 0.0;
      speedArr[8] = diag;
      return speedArr;
   }
   
   private static double[] getShieldBreakYSpeeds()
   {
      double straight = 1.0 * SLOW_MOVE_SPEED;
      double diag = .707 * SLOW_MOVE_SPEED;
      double[] speedArr = new double[9];
      speedArr[0] = -diag;
      speedArr[1] = -straight;
      speedArr[2] = -diag;
      speedArr[3] = 0.0;
      speedArr[4] = 0.0;
      speedArr[5] = 0.0;
      speedArr[6] = diag;
      speedArr[7] = straight;
      speedArr[8] = diag;
      return speedArr;
   }
   
   // non-static stuff
   /////////////////////////////////////////////////////////////////////////////
   
   // hook this up to the timer for delays
   public void actionPerformed(ActionEvent ae)
   {
      for(int i = 0; i < delayList.size(); i++)
      {
         DelayedVisualEffect ve = delayList.elementAt(i);
         ve.increment();
         if(ve.shouldTrigger())
         {
            ve.trigger();
            delayList.remove(ve);
            i--;
         }
      }
   }
   

}
