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
   private Durability durability;
   private OnDestructionEffect destructionEffect;


	public int getIconIndex(){return iconIndex;}
	public int getFGColor(){return fgColor;}
	public int getBGColor(){return bgColor;}
	public String getName(){return name;}
	public boolean isLowPassable(){return lowPassable;}
	public boolean isHighPassable(){return highPassable;}
	public boolean isTransparent(){return transparent;}
   public Durability getDurability(){return durability;}
   public OnDestructionEffect getOnDestructionEffect(){return destructionEffect;}


	public void setIconIndex(int i){iconIndex = i;}
	public void setFGColor(int f){fgColor = f;}
	public void setBGColor(int b){bgColor = b;}
	public void setName(String n){name = n;}
	public void setLowPassable(boolean l){lowPassable = l;}
	public void setHighPassable(boolean h){highPassable = h;}
	public void setTransparent(boolean t){transparent = t;}
   public void setDurability(Durability d){durability = d;}
   public void setOnDestructionEffect(OnDestructionEffect de){destructionEffect = de;}
   
   public MapTile(int i, int fg, int bg, String n, boolean lp, boolean hp, boolean t)
   {
      iconIndex = i;
      fgColor = fg;
      bgColor = bg;
      name = n;
      lowPassable = lp;
      highPassable = hp;
      transparent = t;
      durability = Durability.STANDARD;
      destructionEffect = null;
   }
   
   public MapTile(MapTile that)
   {
      this.iconIndex = that.iconIndex;
      this.fgColor = that.fgColor;
      this.bgColor = that.bgColor;
      this.name = that.name;
      this.lowPassable = that.lowPassable;
      this.highPassable = that.highPassable;
      this.transparent = that.transparent;
      this.durability = that.durability;
      this.destructionEffect = that.destructionEffect;
   }
   
   public boolean hasOnDestructionEffect()
   {
      return destructionEffect != null;
   }

}