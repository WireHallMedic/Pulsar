package Pulsar.Gear;

public class Gadget extends GearObj implements GearConstants
{
   public Gadget()
   {
      super(GADGET_ICON, "Unknown Gadget");
   }
   
   public String getSummary()
   {
      return "";
   }
}