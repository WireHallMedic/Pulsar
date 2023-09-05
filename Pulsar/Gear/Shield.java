package Pulsar.Gear;

public class Shield extends GearObj implements GearConstants
{
	private int curCharge;
	private int maxCharge;
	private int chargeDelay;
	private int chargeRate;
	private int timeSinceHit;


	public int getCurCharge(){return curCharge;}
	public int getMaxCharge(){return maxCharge;}
	public int getChargeDelay(){return chargeDelay;}
	public int getChargeRate(){return chargeRate;}
	public int getTimeSinceHit(){return timeSinceHit;}


	public void setCurCharge(int c){curCharge = c;}
	public void setMaxCharge(int m){maxCharge = m;}
	public void setChargeDelay(int c){chargeDelay = c;}
	public void setChargeRate(int c){chargeRate = c;}
	public void setTimeSinceHit(int t){timeSinceHit = t;}

   public Shield()
   {
      super(SHIELD_ICON, "Unknown Shield");
      curCharge = 10;
      maxCharge = 10;
      chargeDelay = 10;
      chargeRate = 1;
      timeSinceHit = 0;
   }
   
   public String getSummary()
   {
      return getCurCharge() + "/" + getMaxCharge() + ", Charge Delay: " + getChargeDelay() + ", Charge Rate: " + getChargeRate();
   }
   
   public void increment()
   {
      if(curCharge < maxCharge)
      {
         if(timeSinceHit >= chargeDelay)
         {
            curCharge = Math.min(maxCharge, curCharge + chargeRate);
         }
         else
         {
            timeSinceHit++;
         }
      }
   }
   
   public int applyDamage(int damage, DamageType damageType)
   {
      timeSinceHit = 0;
      if(damageType == DamageType.ELECTRO)
      {
         damage += Math.min(curCharge, damage);
      }
      int remainingDamage = 0;
      if(curCharge < damage)
      {
         remainingDamage = damage - curCharge;
         curCharge = 0;
      }
      else
      {
         curCharge = curCharge - damage;
      }
      return remainingDamage;
   }
   
   public static void test()
   {
      Shield shield = new Shield();
      int remainder = shield.applyDamage(6, DamageType.KINETIC);
      System.out.println("6 damage, kinetic");
      System.out.println("Remainder: " + (remainder == 0));
      System.out.println("curCharge: " + (shield.curCharge == 4));
      
      shield = new Shield();
      remainder = shield.applyDamage(12, DamageType.KINETIC);
      System.out.println("12 damage, kinetic");
      System.out.println("Remainder: " + (remainder == 2));
      System.out.println("curCharge: " + (shield.curCharge == 0));
      
      shield = new Shield();
      remainder = shield.applyDamage(3, DamageType.ELECTRO);
      System.out.println("3 damage, electro");
      System.out.println("Remainder: " + (remainder == 0));
      System.out.println("curCharge: " + (shield.curCharge == 4));
      
      shield = new Shield();
      remainder = shield.applyDamage(6, DamageType.ELECTRO);
      System.out.println("6 damage, electro");
      System.out.println("Remainder: " + (remainder == 2));
      System.out.println("curCharge: " + (shield.curCharge == 0));
      
      for(int i = 0; i < 5; i++)
         shield.increment();
      System.out.println("curCharge after 5 initiative counts: " + (shield.curCharge == 0) + " (" + shield.curCharge + ")");
      for(int i = 0; i < 5; i++)
         shield.increment();
      System.out.println("curCharge after 5 initiative counts: " + (shield.curCharge == 0) + " (" + shield.curCharge + ")");
      for(int i = 0; i < 5; i++)
         shield.increment();
      System.out.println("curCharge after 5 initiative counts: " + (shield.curCharge == 5) + " (" + shield.curCharge + ")");
      for(int i = 0; i < 5; i++)
         shield.increment();
      System.out.println("curCharge after 5 initiative counts: " + (shield.curCharge == 10) + " (" + shield.curCharge + ")");
      for(int i = 0; i < 5; i++)
         shield.increment();
      System.out.println("curCharge after 5 initiative counts: " + (shield.curCharge == 10) + " (" + shield.curCharge + ")");
   }
   
   public static void main(String[] args)
   {
      test();
   }
}