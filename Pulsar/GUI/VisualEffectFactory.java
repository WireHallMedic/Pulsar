package Pulsar.GUI;

import java.awt.*;
import WidlerSuite.*;
import Pulsar.Engine.*;

public class VisualEffectFactory implements GUIConstants
{
   private static TilePalette getPalette(){return GUIConstants.SQUARE_TILE_PALETTE;}
   private static double getSizeMultiplier(){return GameEngine.getMapPanel().getSizeMultiplier();}
   private static void add(UnboundTile ut){GameEngine.getMapPanel().add(ut);}

   
   public static void createSpray(Coord origin, Coord source, Color color)
   {
      int reps = GameEngine.randomInt(7, 14);
      for(int i = 0; i < reps; i++)
         createSprayParticle(origin, source, color);
   }
   
   public static void createSprayParticle(Coord origin, Coord source, Color color)
   {
      double xSpeed = GUITools.getXSpeedTowards(source, origin);
      double ySpeed = GUITools.getYSpeedTowards(source, origin);
      xSpeed *= SPRAY_BASE_SPEED;
      ySpeed *= SPRAY_BASE_SPEED;
      xSpeed -= SPRAY_VARIABLE_SPEED;
      ySpeed -= SPRAY_VARIABLE_SPEED;
      xSpeed += GameEngine.random() * (2 * SPRAY_VARIABLE_SPEED);
      ySpeed += GameEngine.random() * (2 * SPRAY_VARIABLE_SPEED);
      UnboundTile ut = getPalette().getUnboundTile(250, color.getRGB(), getSizeMultiplier());
      ut.setLoc(origin);
      ut.setLifespan(SPRAY_DURATION);
      ut.setSpeed(xSpeed, ySpeed);
      add(ut);
   }
}
