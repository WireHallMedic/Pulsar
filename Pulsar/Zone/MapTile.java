package Pulsar.Zone;

public class MapTile implements ZoneConstants
{
   private int iconIndex;
	private int fgColor;
	private int bgColor;
	private String name;
	private boolean lowPassable;
	private boolean highPassable;
	private boolean transparent;


	public int getIconIndex(){return iconIndex;}
	public int getFgColor(){return fgColor;}
	public int getBgColor(){return bgColor;}
	public String getName(){return name;}
	public boolean isLowPassable(){return lowPassable;}
	public boolean isHighPassable(){return highPassable;}
	public boolean isTransparent(){return transparent;}


	public void setIconIndex(int i){iconIndex = i;}
	public void setFgColor(int f){fgColor = f;}
	public void setBgColor(int b){bgColor = b;}
	public void setName(String n){name = n;}
	public void setLowPassable(boolean l){lowPassable = l;}
	public void setHighPassable(boolean h){highPassable = h;}
	public void setTransparent(boolean t){transparent = t;}
   
   public MapTile(int i, int fg, int bg, String n, boolean lp, boolean hp, boolean t)
   {
      iconIndex = i;
      fgColor = fg;
      bgColor = bg;
      name = n;
      lowPassable = lp;
      highPassable = hp;
      transparent = t;
   }

}