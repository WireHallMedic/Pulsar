package Pulsar.Zone;

public class Door extends MapTile implements ToggleTile
{
   public static final boolean OPEN = true;
   public static final boolean CLOSED = false;
   
   private boolean open;
   private boolean locked;
   private boolean automatic;
   
   public boolean isLocked(){return locked;}
   public boolean isAutomatic(){return automatic;}
   public boolean isOpen(){return open;}
   
   public void setLocked(boolean l){locked = l;}
   public void setAutomatic(boolean a){automatic = a;}
   
   
   public Door(int fg, int bg, String n)
   {
      super('x', fg, bg, n, false, false, false);
      open = false;
      locked = false;
      automatic = false;
   }

	public int getIconIndex()
   {
      if(open)
         return '/';
      return '|';
   }
   
	public String getName()
   {
      String suffix = " (Closed)";
      if(open)
         suffix = " (Open)";
      else if(locked)
         suffix = " (Locked)";
      return super.getName() + suffix;
   }
   
	public boolean isLowPassable(){return open;}
   
	public boolean isHighPassable(){return open;}
   
	public boolean isTransparent(){return open;}
   
   
   public void toggle()
   {
      if(!locked)
         open = !open;
   }
   
}