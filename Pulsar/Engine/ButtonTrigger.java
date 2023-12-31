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
   private Coord callerLoc;            // used by template generation, where the trigger exists before the button
   private int callerRepetitions;      // same
   private boolean callerOneUseOnly;   // same


	public int getTriggerIndex(){return triggerIndex;}
	public ButtonAction getButtonAction(){return buttonAction;}
	public Vector<Coord> getTargetList(){return targetList;}
   public int getIntensity(){return intensity;}
   public Coord getCallerLoc(){return callerLoc;}
   public boolean hasCallerLoc(){return callerLoc != null;}
   public int getCallerReps(){return callerRepetitions;}
   public boolean getCallerOneUseOnly(){return callerOneUseOnly;}


	public void setTriggerIndex(int t){triggerIndex = t;}
	public void setButtonAction(ButtonAction b){buttonAction = b;}
	public void setTargetList(Vector<Coord> t){targetList = t;}
   public void setIntensity(int i){intensity = i;}
   public void setCallerLoc(int x, int y){callerLoc = new Coord(x, y);}
   public void setCallerLoc(Coord c){setCallerLoc(c.x, c.y);}
   public void setCallerReps(int r){callerRepetitions = r;}
   public void setCallerOneUseOnly(boolean ouo){callerOneUseOnly = ouo;}


   public ButtonTrigger(int ti, ButtonAction ba)
   {
      triggerIndex = ti;
      buttonAction = ba;
      targetList = new Vector<Coord>();
      intensity = 1;
      callerLoc = null;
      callerRepetitions = 1;
      callerOneUseOnly = false;
   }
   
   public ButtonTrigger()
   {
      this(-1, null);
   }
   
   public ButtonTrigger(ButtonTrigger that)
   {
      this.triggerIndex = that.triggerIndex;
      this.buttonAction = that.buttonAction;
      this.intensity = that.intensity;
      this.targetList = new Vector<Coord>();
      for(Coord c : that.targetList)
         this.targetList.add(new Coord(c));
      this.callerLoc = null;
      if(that.callerLoc != null)
         this.callerLoc = new Coord(that.callerLoc);
      this.callerRepetitions = that.callerRepetitions;
      this.callerOneUseOnly = that.callerOneUseOnly;
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
   
   public void shift(Coord c){shift(c.x, c.y);}
   public void shift(int x, int y)
   {
      for(Coord c : targetList)
      {
         c.x += x;
         c.y += y;
      }
      if(callerLoc != null)
      {
         callerLoc.x += x;
         callerLoc.y += y;
      }
   }
   
   public void shiftFromRotation(int w, int h)
   {
      for(Coord c : targetList)
      {
         int newX = (h - 1) - c.y;
         int newY = c.x;
         c.x = newX;
         c.y = newY;
      }
      if(callerLoc != null)
      {
         int newX = (h - 1) - callerLoc.y;
         int newY = callerLoc.x;
         callerLoc.x = newX;
         callerLoc.y = newY;
      }
   }
   
   public void shiftFromMirrorX(int w, int h)
   {
      for(Coord c : targetList)
      {
         c.x = (w - 1) - c.x;
      }
      if(callerLoc != null)
      {
         callerLoc.x = (w - 1) - callerLoc.x;
      }
   }
   
   public void shiftFromMirrorY(int w, int h)
   {
      for(Coord c : targetList)
      {
         c.y = (h - 1) - c.y;
      }
      if(callerLoc != null)
      {
         callerLoc.y = (h - 1) - callerLoc.y;
      }
   }
   
   public void parse(String s)
   {
      String instruction = s.split(":")[0].toUpperCase();
      String operand = s.split(":")[1].trim();
      if(instruction.contains("BUTTON_ACTION"))
         setButtonAction(ButtonAction.parse(operand));
      else if(instruction.contains("BUTTON_TARGET"))
         addTarget(EngineTools.parseCoord(operand));
      else if(instruction.contains("BUTTON_INTENSITY"))
         setIntensity(Integer.parseInt(operand));
      else if(instruction.contains("BUTTON_REPS"))
         setCallerReps(Integer.parseInt(operand));
      else if(instruction.contains("BUTTON_ONE_USE_ONLY"))
         setCallerOneUseOnly(Boolean.parseBoolean(operand));
      else
         throw new java.lang.Error("Unknown command when parsing ButtonTrigger: " + instruction);
   }
}