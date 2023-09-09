package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.util.*;
import WidlerSuite.*;

public class ActorMemory implements AIConstants
{
	private Actor self;
	private int memoryDuration;
	private Vector<MemoryObj> friendList;
	private Vector<MemoryObj> enemyList;


	public Actor getSelf(){return self;}
	public int getMemoryDuration(){return memoryDuration;}
	public Vector<MemoryObj> getFriendList(){return friendList;}
	public Vector<MemoryObj> getEnemyList(){return enemyList;}


	public void setSelf(Actor s){self = s;}
	public void setMemoryDuration(int m){memoryDuration = m;}
	public void setFriendList(Vector<MemoryObj> f){friendList = f;}
	public void setEnemyList(Vector<MemoryObj> e){enemyList = e;}

   public ActorMemory(Actor s)
   {
      self = s;
      memoryDuration = DEFAULT_MEMORY_DURATION;
      friendList = new Vector<MemoryObj>();
      enemyList = new Vector<MemoryObj>();
   }
   
   public void increment()
   {
      incrementList(friendList);
      incrementList(enemyList);
   }
   
   private void incrementList(Vector<MemoryObj> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         list.elementAt(i).increment();
         if(list.elementAt(i).isExpired())
         {
            list.removeElementAt(i);
            i--;
         }
      }
   }
   
   public Coord getNearestLastKnown()
   {
      int curDistance = 1000;
      Coord curLoc = null;
      for(MemoryObj memory : enemyList)
      {
         int prospectDistance = EngineTools.getDistanceTo(self.getMapLoc(), memory.lastKnownLocation);
         if(prospectDistance < curDistance)
         {
            curDistance = prospectDistance;
            curLoc = memory.lastKnownLocation;
         }
      }
      return curLoc;
   }
   
   private void alertFriends(Actor enemy)
   {
      for(MemoryObj friend : friendList)
      {
         if(self.canSee(friend.actor))
            friend.actor.getMemory().noteActor(enemy, false);
      }
   }
   
   public void noteActor(Actor actor){noteActor(actor, true);}
   public void noteActor(Actor actor, boolean tellFriends)
   {
      if(self.isHostile(actor))
      {
         noteMemoryList(actor, enemyList);
         if(tellFriends)
            alertFriends(actor);
      }
      if(self.isFriendly(actor))
      {
         noteMemoryList(actor, friendList);
      }
      // neutral actors are not remembered
   }
   
   private void noteMemoryList(Actor actor, Vector<MemoryObj> list)
   {
      boolean foundActor = false;
      for(MemoryObj memory : list)
      {
         if(memory.actor == actor)
         {
            memory.timeSinceSeen = 0;
            memory.lastKnownLocation = actor.getMapLoc();
            foundActor = true;
            break;
         }
      }
      if(!foundActor)
      {
         list.add(new MemoryObj(actor));
      }
   }
   
   // private class for seeing other actors
   private class MemoryObj
   {
      public Actor actor;
      public int timeSinceSeen;
      public Coord lastKnownLocation;
      
      public MemoryObj(Actor a)
      {
         actor = a;
         timeSinceSeen = 0;
         lastKnownLocation = actor.getMapLoc();
      }
      
      public void increment()
      {
         timeSinceSeen++;
      }
      
      public boolean isExpired()
      {
         return timeSinceSeen >= memoryDuration;
      }
   }
}