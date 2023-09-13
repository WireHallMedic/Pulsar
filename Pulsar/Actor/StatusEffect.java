package Pulsar.Actor;

public class StatusEffect
{	
   private int remainingDuration;
	private boolean expires;
	private String name;
	private boolean stacks;


	public int getRemainingDuration(){return expires ? remainingDuration : 1;}
	public boolean getExpires(){return expires;}
	public String getName(){return name;}
	public boolean getStacks(){return stacks;}


	public void setRemainingDuration(int r){remainingDuration = r;}
	public void setExpires(boolean e){expires = e;}
	public void setName(String n){name = n;}
	public void setStacks(boolean s){stacks = s;}


   public StatusEffect()
   {
      this("Unknown Status Effect", 10);
   }
   
   public StatusEffect(String n, int dur)
   {
      name = n;
      remainingDuration = dur;
      expires = true;
      stacks = false;
      if(remainingDuration == -1)
         expires = false;
   }
   
   public boolean shouldCombine(StatusEffect that)
   {
      return this.name.equals(that.name) && !stacks;
   }
   
   public void increment()
   {
      if(expires)
         remainingDuration--;
   }
   
   public boolean isExpired()
   {
      return remainingDuration < 1 && expires;
   }
   
   public void combine(StatusEffect that)
   {
      remainingDuration = Math.max(this.remainingDuration, that.remainingDuration);
   }
   
   
}