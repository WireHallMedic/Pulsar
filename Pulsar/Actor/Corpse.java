package Pulsar.Actor;

import java.awt.*;
import Pulsar.GUI.*;

public class Corpse implements GUIConstants
{
	private int color;
	private Actor actor;
	private char iconIndex;


	public int getColor(){return color;}
	public Actor getActor(){return actor;}
	public char getIconIndex(){return iconIndex;}


	public void setColor(int c){color = c;}
	public void setActor(Actor a){actor = a;}
	public void setIconIndex(char i){iconIndex = i;}

   
   public Corpse(Actor a)
   {
      actor = a;
      color = a.getSprite().getFGColor();
      iconIndex = CORPSE_CHAR;
   }
   
   public String getName()
   {
      return actor.getName() + " Corpse";
   }

}