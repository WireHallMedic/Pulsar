package Pulsar.Actor;

import java.awt.*;

public class Corpse
{
   private int color;
	private Actor actor;


	public int getColor(){return color;}
	public Actor getActor(){return actor;}


	public void setColor(int c){color = c;}
	public void setActor(Actor a){actor = a;}
   
   public Corpse(Actor a)
   {
      actor = a;
      color = a.getSprite().getFGColor();
   }

}