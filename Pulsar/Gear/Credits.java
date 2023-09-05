package Pulsar.Gear;

public class Credits extends GearObj implements GearConstants
{
   private int amount;
   
   public void getAmount(){return amount;}
   
   public int setAmount(int amt){amount = amt;}
   
   public Credits(int amt)
   {
      super(CREDIT_ICON, "Credits");
      amount = amt;
   }
   
   @Override
   public String getName()
   {
      return getAmount() + name;
   }
   
   public void add(Credits that)
   {
      this.adjustAmount(that.getAmount());
   }
   
   public void adjustAmount(int val)
   {
      amount += val;
   }
}