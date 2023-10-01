package Pulsar.Gear;

import Pulsar.Actor.*;
import java.util.*;

public class Inventory implements GearConstants
{
   private Actor self;
	private int baseMaxSize;
	private Vector<GearObj> list;
	private Credits credits;


   public Actor getSelf(){return self;}
	public Vector<GearObj> getList(){return list;}
	public Credits getCredits(){return credits;}


   public void setSelf(Actor s){self = s;}
	public void setMaxSize(int m){baseMaxSize = m;}
	public void setList(Vector<GearObj> l){list = l;}
	public void setCredits(Credits c){credits = c;}

   public Inventory(Actor s)
   {
      self = s;
      baseMaxSize = DEFAULT_MAX_INVENTORY;
      credits = new Credits(0);
      list = new Vector<GearObj>();
   }
   
   public boolean isFull()
   {
      return size() >= getMaxSize();
   }
   
   public boolean isOverFull()
   {
      return size() > getMaxSize();
   }
   
	public int getMaxSize()
   {
      int armorMod = 0;
      if(self.hasArmor() && self.getArmor().hasInventorySpace())
         armorMod = self.getArmor().getInventorySpace();
      return baseMaxSize + armorMod;
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
   
   public String toString()
   {
      String str = credits.getName();
      for(int i = 0; i < list.size(); i++)
         str += ", " + list.elementAt(i).getName();
      return str;
   }
}