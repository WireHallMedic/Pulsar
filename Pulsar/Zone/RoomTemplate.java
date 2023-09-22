package Pulsar.Zone;

import java.util.*;

public class RoomTemplate extends MapTemplate
{
   
   public enum RoomTemplateTile
   {
      DEFAULT_OOB ('0'),
      CLEAR       ('.'),
      WALL        ('#'),
      DOOR        ('/'),
      VACUUM      ('V');
      
      public char character;
      
      private RoomTemplateTile(char c)
      {
         character = c;
      }
   }
   
   public enum RoomTemplateType
   {
      BLOCK,
      TERMINAL,
      STRAIGHT,
      ELBOW,
      TEE,
      CROSS;
   }
   
   
	private RoomTemplateType type;
	private boolean passNorth;
	private boolean passSouth;
	private boolean passEast;
	private boolean passWest;


	public RoomTemplateType getType(){return type;}
	public boolean passNorth(){return passNorth;}
	public boolean passSouth(){return passSouth;}
	public boolean passEast(){return passEast;}
	public boolean passWest(){return passWest;}


	public void setPassNorth(boolean p){passNorth = p;}
	public void setPassSouth(boolean p){passSouth = p;}
	public void setPassEast(boolean p){passEast = p;}
	public void setPassWest(boolean p){passWest = p;}


   
   public RoomTemplate(Vector<String> input)
   {
      super(input);
   }
   
   @Override
   public void set(Vector<String> input)
   {
      super.set(input);
      setConnections();
      setType();
   }
   
   private void setConnections()
   {
      passNorth = false;
      passSouth = false;
      passEast = false;
      passWest = false;
      
      for(int x = 0; x < width; x++)
      {
         if(isPassCell(getCell(x, 0)))
            passNorth = true;
         if(isPassCell(getCell(x, height - 1)))
            passSouth = true;
      }
      for(int y = 0; y < height; y++)
      {
         if(isPassCell(getCell(0, y)))
            passWest = true;
         if(isPassCell(getCell(width - 1, y)))
            passEast = true;
      }
   }
   
   private boolean isPassCell(char c)
   {
      return c == '.' ||
             c == '/';
   }
   
   private void setType()
   {
      int totalConnections = 0;
      if(passNorth) totalConnections++;
      if(passSouth) totalConnections++;
      if(passEast) totalConnections++;
      if(passWest) totalConnections++;
      
      // no connections, it's a block
      if(totalConnections == 0)
         type = RoomTemplateType.BLOCK;
      // one connection, it's a terminal
      else if(totalConnections == 1)
         type = RoomTemplateType.TERMINAL;
      // two connections, could be either a straight or an elbow
      else if(totalConnections == 2)
      {
         // two connections, opposite pairs both match; it's a straight
         if((passNorth == passSouth) && (passEast == passWest))
            type = RoomTemplateType.STRAIGHT;
         // two connections, opposite pairs don't both match; it's an elbow
         else
            type = RoomTemplateType.ELBOW;
      }
      // three connections, it's a tee
      else if(totalConnections == 3)
         type = RoomTemplateType.TEE;
      // four connections, it's a cross
      else if(totalConnections == 4)
         type = RoomTemplateType.CROSS;
   }
}
