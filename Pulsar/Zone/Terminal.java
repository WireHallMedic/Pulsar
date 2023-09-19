package Pulsar.Zone;

import java.util.*;


public class Terminal extends MapTile implements ZoneConstants
{
	private Vector<String> pageList;


	public Vector<String> getPageList(){return pageList;}


	public void setPageList(Vector<String> p){pageList = p;}


   public Terminal(int fg, int bg)
   {
      super(TileType.TERMINAL.iconIndex, fg, bg, "Terminal", TileType.TERMINAL.lowPassable, TileType.TERMINAL.highPassable, TileType.TERMINAL.transparent);
      pageList = new Vector<String>();
   }
   
   public void addPage(String str)
   {
      pageList.add(str);
   }
}