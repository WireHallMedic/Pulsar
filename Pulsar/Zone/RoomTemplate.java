package Pulsar.Zone;

import java.util.*;
import Pulsar.Engine.*;
import WidlerSuite.*;

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
   private Vector<ButtonTrigger> triggerList;
   
   public void addButtonTrigger(ButtonTrigger t){triggerList.add(t);}


	public RoomTemplateType getType(){return type;}
   public boolean[] getPassArray(){return passArray;}
   public boolean getPass(int dir){return passArray[dir];}
	public boolean passNorth(){return passArray[NORTH];}
	public boolean passSouth(){return passArray[SOUTH];}
	public boolean passEast(){return passArray[EAST];}
	public boolean passWest(){return passArray[WEST];}
   public Vector<ButtonTrigger> getTriggerList(){return triggerList;}


   public RoomTemplate(Vector<String> input)
   {
      super(input);
      setPassArray();
      setType();
      triggerList = new Vector<ButtonTrigger>();
      validate();
   }
   
   public RoomTemplate(RoomTemplate that)
   {
      super(that);
      triggerList = new Vector<ButtonTrigger>();
      for(ButtonTrigger trigger : that.getTriggerList())
         this.addButtonTrigger(new ButtonTrigger(trigger));
      setPassArray();
      setType();
      // copy constructor does not need to validate
   }
   
   // returns a copy, with probabilistic tiles resolved
   public RoomTemplate resolveProbTiles()
   {
      RoomTemplate newTemplate = new RoomTemplate(this);
      boolean roomRoll = GameEngine.randomBoolean();
      for(int x = 1; x < getWidth() - 1; x++)
      for(int y = 1; y < getHeight() - 1; y++)
      {
         switch(newTemplate.getCell(x, y))
         {
            // room probabilites
            case '1' :  if(roomRoll)         // wall
                           newTemplate.setCell(x, y, TEMPLATE_WALL);
                        else
                           newTemplate.setCell(x, y, TEMPLATE_CLEAR);
                        break;
            case '2' :  if(roomRoll)         // table
                           newTemplate.setCell(x, y, TEMPLATE_TABLE);
                        else
                           newTemplate.setCell(x, y, TEMPLATE_CLEAR);
                        break;
            case '3' :  newTemplate.setCell(x, y, TEMPLATE_CLEAR); // not in use
                        break;
            case '4' :  newTemplate.setCell(x, y, TEMPLATE_CLEAR); // not in use
                        break;
            
            // instance probabilities
            case '5' :  if(GameEngine.randomBoolean())         // random barrel
                        {
                           switch(GameEngine.randomInt(0, 4))
                           {
                              case 0 :
                              case 1 : newTemplate.setCell(x, y, TEMPLATE_EXPLODING_BARREL); break;
                              case 2 : newTemplate.setCell(x, y, TEMPLATE_BARREL); break;
                              case 3 : newTemplate.setCell(x, y, TEMPLATE_WATER_BARREL); break;
                           }
                        }
                        else
                           newTemplate.setCell(x, y, TEMPLATE_CLEAR);
                        break;
            case '6' :  if(GameEngine.randomBoolean())         // exploding barrel
                           newTemplate.setCell(x, y, TEMPLATE_EXPLODING_BARREL);
                        else
                           newTemplate.setCell(x, y, TEMPLATE_CLEAR);
                        break;
            case '7' :  if(GameEngine.randomBoolean())         // crate
                           newTemplate.setCell(x, y, TEMPLATE_CRATE);
                        else
                           newTemplate.setCell(x, y, TEMPLATE_CLEAR);
                        break;
            case '8' :  newTemplate.setCell(x, y, TEMPLATE_CLEAR); // not in use
                        break;
            case '9' :  newTemplate.setCell(x, y, TEMPLATE_CLEAR); // not in use
                        break;
            default  :  continue;
         }
      }
      return newTemplate;
   }
   
   // returns a copy of the roomTemplate with obstacles filled in
   public RoomTemplate resolveObstacles(ObstacleTemplateManager otm)
   {
      RoomTemplate newTemplate = new RoomTemplate(this);
      for(int x = 0; x < width - 4; x++)
      for(int y = 0; y < height - 4; y++)
      {
         if(newTemplate.getCell(x, y) == TEMPLATE_OBSTACLE)
         {
            ObstacleTemplate obstacle = otm.random();
            for(int xx = 0; xx < ObstacleTemplate.REQUIRED_WIDTH; xx++)
            for(int yy = 0; yy < ObstacleTemplate.REQUIRED_HEIGHT; yy++)
            {
               newTemplate.setCell(x + xx, y + yy, obstacle.getCell(xx, yy));
            }
            if(obstacle.hasButtonTrigger())
            {
               ButtonTrigger trigger = new ButtonTrigger(obstacle.getButtonTrigger());
               Coord oldLoc = new Coord(trigger.getCallerLoc());
               trigger.shift(x, y);
               addButtonTrigger(trigger);
            }
         }
      }
      return newTemplate;
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
      // TODO: apply to button triggers
   }
   
   @Override
   public void mirrorX()
   {
      super.mirrorX();
      setPassArray();
      // TODO: apply to button triggers
   }
   
   @Override
   public void mirrorY()
   {
      super.mirrorY();
      setPassArray();
      // TODO: apply to button triggers
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
   
   public boolean validate()
   {
      for(int x = 0; x < width; x++)
      {
         if(isInvalidBorderTile(getCell(x, 0)) ||
            isInvalidBorderTile(getCell(x, height - 1)))
            throw new java.lang.Error("RoomTemplate has invalid border tile.");
      }
      for(int y = 0; y < height; y++)
      {
         if(isInvalidBorderTile(getCell(0, y)) ||
            isInvalidBorderTile(getCell(width - 1, y)))
            throw new java.lang.Error("RoomTemplate has invalid border tile.");
      }
      return true;
   }
   
   public boolean validateButtons()
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(getCell(x, y) == TEMPLATE_BUTTON)
         {
            Coord c = new Coord(x, y);
            if(triggerList.size() == 0)
            {
               throw new java.lang.Error("RoomTemplate has button with no trigger.");
            }
            boolean foundF = false;
            for(ButtonTrigger trigger : triggerList)
            {
               
               if(trigger.getCallerLoc() == null)
               {
                  throw new java.lang.Error("RoomTemplate has ButtonTrigger with no callerLoc.");
               }
               if(trigger.getCallerLoc().equals(c))
               {
                  foundF = true;
                  break;
               }
            }
            if(!foundF)
            {
               throw new java.lang.Error("RoomTemplate has ButtonTrigger with bad callerLoc: expected " + c);
            }
         }
      }
      return true;
   }
   
   private boolean isInvalidBorderTile(char c)
   {
      switch(c)
      {
         case '0' : return false;
         case '.' : return false;
         case '#' : return false;
         case '/' : return false;
         case 'V' : return false;
         default  : return true;
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
