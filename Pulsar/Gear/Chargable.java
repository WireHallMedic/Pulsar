package Pulsar.Gear;

public interface Chargable
{
	public int getCurCharge();
	public int getMaxCharge();
	public int getChargeRate();


	public void setCurCharge(int c);
	public void setMaxCharge(int m);
	public void setChargeRate(int c);
   
   public void charge();
   public void fullyCharge();
}