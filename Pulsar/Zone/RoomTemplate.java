package Pulsar.Zone;

import java.util.*;

public class RoomTemplate extends MapTemplate implements ZoneConstants
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
   private boolean[] passArray;


	public RoomTemplateType getType(){return type;}
   public boolean[] getPassArray(){return passArray;}
   public boolean getPass(int dir){return passArray[dir];}
	public boolean passNorth(){return passArray[NORTH];}
	public boolean passSouth(){return passArray[SOUTH];}
	public boolean passEast(){return passArray[EAST];}
	public boolean passWest(){return passArray[WEST];}


   public RoomTemplate(Vector<String> input)
   {
      super(input);
      setPassArray();
   }
   
   public RoomTemplate(RoomTemplate that)
   {
   
   }
   
   public boolean matchesPassArray(boolean[] target)
   {
      boolean[] current = getPassArray();
      for(int i = 0; i < current.length; i++)
      {
         if(current[i] != target[i])
            return false;
      }
      return true;
   }
   
   @Override
   public void set(Vector<String> input)
   {
      super.set(input);
      setPassArray();
      setType();
   }
   
   @Override
   public void rotate()
   {
      super.rotate();
      setPassArray();
   }
   
   @Override
   public void mirrorX()
   {
      super.mirrorX();
      setPassArray();
   }
   
   @Override
   public void mirrorY()
   {
      super.mirrorY();
      setPassArray();
   }
   
   public boolean canMirrorX()
   {
      return passArray[EAST] == passArray[WEST];
   }
   
   public boolean canMirrorY()
   {
      return passArray[NORTH] == passArray[SOUTH];
   }
   
   public void rotateUntilMatches(boolean[] target)
   {
      if(matchesPassArray(target))
         return;
      for(int i = 0; i < 3; i++)
      {
         rotate();
         System.out.println("Rotating");
         if(matchesPassArray(target))
            return;
      }
      throw new java.lang.Error("Room template cannot be rotated to match.");
   }
   
   
   private void setPassArray()
   {
      passArray = new boolean[4];
      
      for(int x = 0; x < width; x++)
      {
         if(isPassCell(getCell(x, 0)))
            passArray[NORTH] = true;
         if(isPassCell(getCell(x, height - 1)))
            passArray[SOUTH] = true;
      }
      for(int y = 0; y < height; y++)
      {
         if(isPassCell(getCell(0, y)))
            passArray[WEST] = true;
         if(isPassCell(getCell(width - 1, y)))
            passArray[EAST] = true;
      }
   }
   
   private boolean isPassCell(char c)
   {
      return c == '.' ||
             c == '/';
   }
   
   private void setType()
   {
      type = determineType(passArray);
   }
   
   public static RoomTemplateType determineType(boolean[] passArr)
   {  
      int totalConnections = 0;
      RoomTemplateType t = null;
      for(boolean dir : passArr)
         if(dir)
            totalConnections++;
      
      // no connections, it's a block
      if(totalConnections == 0)
         t = RoomTemplateType.BLOCK;
      // one connection, it's a terminal
      else if(totalConnections == 1)
         t = RoomTemplateType.TERMINAL;
      // two connections, could be either a straight or an elbow
      else if(totalConnections == 2)
      {
         // two connections, opposite pairs both match; it's a straight
         if((passArr[NORTH] == passArr[SOUTH]) && (passArr[EAST] == passArr[WEST]))
            t = RoomTemplateType.STRAIGHT;
         // two connections, opposite pairs don't both match; it's an elbow
         else
            t = RoomTemplateType.ELBOW;
      }
      // three connections, it's a tee
      else if(totalConnections == 3)
         t = RoomTemplateType.TEE;
      // four connections, it's a cross
      else if(totalConnections == 4)
         t = RoomTemplateType.CROSS;
      
      return t;
   }
   
   public void print()
   {
      for(int y = 0; y < height; y++)
      {
         for(int x = 0; x < width; x++)
         {
            System.out.print(getCell(x, y));
         }
         System.out.println("");
      }
   }
   
   public static void main(String[] args)
   {
      Vector<String> v = new Vector<String>();
      v.add("#..#");
      v.add("#..#");
      v.add("#..#");
      v.add("####");
      RoomTemplate t = new RoomTemplate(v);
      t.print();
      t.rotate();
      t.print();
   }
}
