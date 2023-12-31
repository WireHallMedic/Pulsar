package Pulsar.Zone;

import WidlerSuite.*;

public class SpawnPoint
{
	private Coord loc;
	private char room;
	private boolean boss;


	public Coord getLoc(){return new Coord(loc);}
	public char getRoom(){return room;}
	public boolean isBoss(){return boss;}


	public void setLoc(Coord l){setLoc(l.x, l.y);}
	public void setLoc(int x, int y){loc = new Coord(x, y);}
	public void setRoom(char r){room = r;}
	public void setBoss(boolean b){boss = b;}


   public SpawnPoint(int x, int y, char r)
   {
      loc = new Coord(x, y);
      room = r;
      boss = false;
   }
   
   public SpawnPoint(Coord l, char r)
   {
      this(l.x, l.y, r);
   }
   
   public void shift(Coord c)
   {
      loc.add(c);
   }
}