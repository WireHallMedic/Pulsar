// a closet is a section of a zoneMap that is 4x4 or smaller, walled, and has exactly one door
// The location of the door is outside the room, as walls are not considered part of the cell

package Pulsar.Zone;

import WidlerSuite.*;

public class Closet
{
   private Coord origin;
	private Coord size;
	private Coord doorLoc;


	public Coord getOrigin(){return new Coord(origin);}
	public Coord getSize(){return new Coord(size);}
	public Coord getDoorLoc(){return new Coord(doorLoc);}


	public void setOrigin(Coord o){setOrigin(o.x, o.y);}
	public void setOrigin(int x, int y){origin = new Coord(x, y);}
	public void setSize(Coord s){setSize(s.x, s.y);}
	public void setSize(int x, int y){size = new Coord(x, y);}
	public void setDoorLoc(Coord d){setDoorLoc(d.x, d.y);}
	public void setDoorLoc(int x, int y){doorLoc = new Coord(x, y);}

   public Closet(Coord o, Coord s, Coord dl)
   {
      origin = new Coord(o);
      size = new Coord(s);
      doorLoc = new Coord(dl);
   }
}