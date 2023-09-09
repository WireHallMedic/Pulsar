package Pulsar.AI;

import Pulsar.Actor.*;
import Pulsar.Engine.*;
import java.util.*;

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
   
   private void increment()
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
   
   private void noteActor(Actor actor)
   {
      if(self.isHostile(actor))
         noteMemoryList(actor, enemyList);
      if(self.isFriendly(actor))
         noteMemoryList(actor, friendList);
   }
   
   private void noteMemoryList(Actor actor, Vector<MemoryObj> list)
   {
      boolean foundActor = false;
      for(MemoryObj memory : list)
      {
         if(memory.actor == actor)
         {
            memory.lastSeen = 0;
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
      public int lastSeen;
      
      public MemoryObj(Actor a)
      {
         actor = a;
         lastSeen = 0;
      }
      
      public void increment()
      {
         lastSeen++;
      }
      
      public boolean isExpired()
      {
         return lastSeen >= memoryDuration;
      }
   }
}