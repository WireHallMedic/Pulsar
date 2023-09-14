package Pulsar.Gear;

public class Gadget extends GearObj implements GearConstants
{
	private int maxUses;
	private int remainingUses;


	public int getMaxUses(){return maxUses;}
	public int getRemainingUses(){return remainingUses;}


	public void setMaxUses(int m){maxUses = m;}
	public void setRemainingUses(int r){remainingUses = r;}

   public Gadget()
   {
      super(GADGET_ICON, "Unknown Gadget");
      maxUses = DEFAULT_GADGET_USES;
      fullyCharge();
   }
   
   public String getSummary()
   {
      return getName() + " [" + getRemainingUses() + "]";
   }
   
   public void fullyCharge()
   {
      remainingUses = maxUses;
   }
}