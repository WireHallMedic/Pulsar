package Pulsar.Gear;

import Pulsar.GUI.*;

public class Credits extends GearObj implements GearConstants, GUIConstants
{
   private int amount;
   
   public int getAmount(){return amount;}
   
   public void setAmount(int amt){amount = amt;}
   
   public Credits(int amt)
   {
      super(CREDIT_ICON, "Credits");
      setColor(GOLD);
      amount = amt;
   }
   
   @Override
   public String getName()
   {
      return getSummary();
   }
   
   public void add(Credits that)
   {
      this.adjustAmount(that.getAmount());
      that.setAmount(0);
   }
   
   public void adjustAmount(int val)
   {
      amount += val;
   }
   
   public String getSummary()
   {
      return getAmount() + " " + super.getName();
   }
}