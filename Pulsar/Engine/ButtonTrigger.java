package Pulsar.Engine;

import Pulsar.Zone.*;
import WidlerSuite.*;
import java.util.*;

public class ButtonTrigger implements EngineConstants
{	
	private int triggerIndex;
	private ButtonAction buttonAction;
	private Vector<Coord> targetList;
   private int intensity;


	public int getTriggerIndex(){return triggerIndex;}
	public ButtonAction getButtonAction(){return buttonAction;}
	public Vector<Coord> getTargetList(){return targetList;}
   public int getIntensity(){return intensity;} 


	public void setTriggerIndex(int t){triggerIndex = t;}
	public void setButtonAction(ButtonAction b){buttonAction = b;}
	public void setTargetList(Vector<Coord> t){targetList = t;}
   public void setIntensity(int i){intensity = i;}


   public ButtonTrigger(int ti, ButtonAction ba)
   {
      triggerIndex = ti;
      buttonAction = ba;
      targetList = new Vector<Coord>();
      intensity = 1;
   }
   
   public void addTarget(Coord c)
   {
      targetList.add(new Coord(c));
   }
   
   public void removeTarget(Coord c)
   {
      for(int i = 0; i < targetList.size(); i++)
      {
         if(targetList.elementAt(i).equals(c))
         {
            targetList.removeElementAt(i);
            i--;
         }
      }
   }
}