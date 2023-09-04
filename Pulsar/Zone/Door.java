package Pulsar.Zone;

public class Door extends MapTile implements ToggleTile
{
   public static final boolean OPEN = true;
   public static final boolean CLOSED = false;
   
   private boolean open = false;
   
   
   public Door(int fg, int bg, String n)
   {
      super('x', fg, bg, n, false, false, false);
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
      return super.getName() + suffix;
   }
   
	public boolean isLowPassable(){return open;}
   
	public boolean isHighPassable(){return open;}
   
	public boolean isTransparent(){return open;}
   
   
   public void toggle()
   {
      open = !open;
   }
}