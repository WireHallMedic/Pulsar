package Pulsar.Gear;

import java.util.*;

public class Inventory implements GearConstants
{
	private int maxSize;
	private Vector<GearObj> list;
	private Credits credits;


	public int getMaxSize(){return maxSize;}
	public Vector<GearObj> getList(){return list;}
	public Credits getCredits(){return credits;}


	public void setMaxSize(int m){maxSize = m;}
	public void setList(Vector<GearObj> l){list = l;}
	public void setCredits(Credits c){credits = c;}

   public Inventory()
   {
      maxSize = DEFAULT_MAX_INVENTORY;
      credits = new Credits(0);
      list = new Vector<GearObj>();
   }
   
   public boolean isFull()
   {
      return size() >= maxSize;
   }
   
   public int size()
   {
      return list.size();
   }
   
   public void add(GearObj newGear)
   {
      if(newGear instanceof Credits)
      {
         Credits newCredits = (Credits)newGear;
         credits.add(newCredits);
      }
      else
         list.add(newGear);
   }
   
   public GearObj take(int index)
   {
      if(index >= list.size())
         return null;
      GearObj gear = list.elementAt(index);
      list.removeElementAt(index);
      return gear;
   }
}